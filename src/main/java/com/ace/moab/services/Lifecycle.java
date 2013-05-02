package com.ace.moab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Lifecycle {

	static final int MAX_PRE_HOOK_TIMEOUT_MILLIS = 60000;

	public Lifecycle() {
		phase = new DefiningPhase();
	}

	private Phase phase;

	public Phase getPhase() {
		return phase;
	}

	/*not public!*/ void setPhase(Phase value) {
		phase = value;
	}

	private static ExecutorService _executor;
	private static ThreadGroup _threadGroup;
	private static ThreadFactory _threadFactory;

	private static ExecutorService getExecutor() {
		// This pattern of checking lazy init status, synchronizing, and then checking again is
		// an optimization. In the 99% case where we've already inited, we can avoid a lock.
		// The only ugliness is that in the 1% case, we repeat our test for null.
		if (_executor == null) {
			synchronized (Lifecycle.class) {
				if (_executor == null) {
					_threadGroup = new ThreadGroup("Lifecycle hook");
					_threadGroup.setDaemon(true);
					_threadGroup.setMaxPriority(Thread.NORM_PRIORITY - 1);
					_threadFactory = new ThreadFactory() {
						@Override
						public Thread newThread(Runnable r) {
							return new Thread(_threadGroup, r);
						}
					};
					_executor = Executors.newCachedThreadPool(_threadFactory);
				}
			}
		}
		return _executor;
	}

	private ReentrantLock transitionLock = new ReentrantLock();

	public static long MAX_SECS_TO_WAIT_ON_TRANSITION_LOCK = 30;
	/**
	 * Ask lifecycle to transition to a new phase. This causes any hooks to fire; however, only "pre" hooks
	 * have finished (successfully) by the time this function returns.
	 *
	 * @param transition What event or occurrence is triggering this change?
	 * @throws InvalidTransitionException
	 */
	public void requestTransition(Transition transition) throws LifecycleException {
		try {
			if (!transitionLock.tryLock(MAX_SECS_TO_WAIT_ON_TRANSITION_LOCK, TimeUnit.SECONDS)) {
				throw new LifecycleException(String.format("Unable to acquire transition lock within %d seconds.", MAX_SECS_TO_WAIT_ON_TRANSITION_LOCK));
			}
		} catch (InterruptedException ex) {
			LifecycleException lex = new LifecycleException("Interrupted before transition lock could be acquired.");
			lex.initCause(ex);
			throw lex;
		}
		try {
			// Figure out what phase are we supposed to transition to. This call will
			// throw an exception if transition makes no sense.
			Phase next = phase.getNextPhase(this, transition);

			// Simplest case = no hooks. Transition is immediate.
			List<TransitionHook> activeHooks = hooks;
			if (activeHooks == null) {
				phase = next;
				// Complex case = hooks. Process will incur some overhead.
			} else {
				// We need to track how many pre hooks haven't yet finished.
				AtomicInteger preCount = new AtomicInteger(0);
				List<HookRunner> pre = null;
				List<HookRunner> early = null;
				List<HookRunner> post = null;
				for (TransitionHook hook : hooks) {
					if (hook.transition == transition) {
						switch (hook.position) {
							case Pre:
								if (pre == null) {
									pre = new ArrayList<HookRunner>();
								}
								pre.add(new HookRunner(this, preCount, hook));
								break;
							case Early:
								if (early == null) {
									early = new ArrayList<HookRunner>();
								}
								early.add(new HookRunner(this, null, hook));
								break;
							case Post:
								if (post == null) {
									post = new ArrayList<HookRunner>();
								}
								post.add(new HookRunner(this, null, hook));
								break;
						}
					}
				}
				if (pre != null) {
					for (HookRunner runner : pre) {
						getExecutor().submit(runner);
					}
					// Wait until all pre hooks exit or until we lose patience.
					long startTime = System.currentTimeMillis();
					long elapsed = 0;
					for (; elapsed < MAX_PRE_HOOK_TIMEOUT_MILLIS;) {
						synchronized (preCount) {
							try {
								// Wait up to a second for all hooks to complete.
								preCount.wait(1000);
							} catch (InterruptedException e) {
								// Force elapsed time to look enormous, so all threads are killed.
								startTime = 0;
							}
							// See if we now have 0 hooks running.
							if (preCount.get() == 0) {
								break;
							} else {
								elapsed = System.currentTimeMillis() - startTime;
								// Interrupt any threads that have run longer than they should.
								for (HookRunner runner : pre) {
									if (runner.thread.isAlive() && runner.hook.timeoutMillis > elapsed) {
										runner.thread.interrupt();
									}
								}
							}
						}
					}
					// If we timed out...
					if (elapsed > MAX_PRE_HOOK_TIMEOUT_MILLIS) {
						// Interrupt any remaining threads.
						for (HookRunner runner : pre) {
							if (runner.thread.isAlive()) {
								runner.thread.interrupt();
							}
						}
					}
					// Now see if all pre threads ran cleanly.
					Throwable cause = null;
					StringBuilder sb = null;
					for (HookRunner runner: pre) {
						if (runner.outcome != null) {
							// Pick the first exception from a hook as the cause of the one we're about to throw.
							// However, prefer something other an in InterruptedException, if there is one.
							if (cause == null) {
								cause = runner.outcome;
							} else if (!(runner.outcome instanceof InterruptedException)) {
								if (sb == null) {
									sb = new StringBuilder();
								}
								sb.append(' ');
								sb.append(runner.outcome.toString());
							}
						}
					}
					if (cause != null) {
						LifecycleException lex = new LifecycleException(String.format("One or more hooks failed.%s", sb.toString()));
						if (cause != null) {
							lex.initCause(cause);
						}
						throw lex;
					}
				}
				if (early != null) {
					for (HookRunner runner : early) {
						getExecutor().submit(runner);
					}
				}
				phase = next;
				if (post != null) {
					for (HookRunner runner : post) {
						getExecutor().submit(runner);
					}
				}
			}
		} finally {
			transitionLock.unlock();
		}
	}

	private List<TransitionHook> hooks;

	/**
	 * Global hooks are ones that we want to run with every lifecycle (as opposed to ones that we want to run
	 * only on this lifecycle instance).
	 */
	public List<TransitionHook> getHooks() {
		return hooks;
	}

	public void setHooks(List<TransitionHook> value) {
		hooks = value;
	}

}
