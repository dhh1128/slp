package com.ace.moab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Lifecycle {

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

	/**
	 * Ask lifecycle to transition to a new phase. This causes any hooks to fire.
	 *
	 * @param transition What event or occurrence is triggering this change?
	 * @throws InvalidTransitionException
	 */
	public void requestTransition(Transition transition) throws InvalidTransitionException {
		// What phase are we supposed to transition to? This call will throw an exception
		// if transition makes no sense.
		Phase next = phase.getNextPhase(this, transition);
		AtomicInteger preCount = new AtomicInteger(0);
		List<TransitionHook> pre = null;
		List<TransitionHook> early = null;
		List<TransitionHook> post = null;
		if (hooks != null) {
			for (TransitionHook hook: hooks) {
				if (hook.transition == transition) {
					switch (hook.position) {
						case Pre:
							if (pre == null) { pre = new ArrayList<TransitionHook>(); }
							preCount.incrementAndGet();
							pre.add(hook);
							break;
						case Early:
							if (early == null) { early = new ArrayList<TransitionHook>(); }
							early.add(hook);
							break;
						case Post:
							if (post == null) { post = new ArrayList<TransitionHook>(); }
							post.add(hook);
							break;
					}
				}
			}
			if (pre != null) {
				for (TransitionHook hook: pre) {
					getExecutor().submit(hook);
				}
			}
			if (early != null) {
				for (TransitionHook hook: early) {
					getExecutor().submit(hook);
				}
			}
		}
		phase = next;
		if (post != null) {
			for (TransitionHook hook: post) {
				getExecutor().submit(hook);
			}
		}
	}

	// We could use a Map<Transition, TransitionHook>, but I don't expect that we'll
	// have enough hooks on a given lifecycle to make the extra overhead worthwhile.
	// Iterating through 2-3 hooks is just as fast as hashing on Transition to get
	// a list of 1 item...
	private List<TransitionHook> hooks;

	public void registerHook(TransitionHook hook) {
		//todo: synchronize?
		if (hooks == null) {
			hooks = new ArrayList<TransitionHook>();
		}
		hooks.add(hook);
	}

	public void unregisterHook(TransitionHook hook) {
		//todo: synchronize?
		if (hooks != null) {
			hooks.remove(hook);
		}
	}

}
