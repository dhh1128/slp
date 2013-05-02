package com.ace.moab.services;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs a {@link TransitionHook} and records the result. Does some extra housekeeping so all hooks with a common
 * transition can be waited on together.
 */
class HookRunner implements Runnable {

    /** Which {@link Lifecycle} is associated with this HookRunner? */
	Lifecycle lifecycle;
    /**
     * How many HookRunners are part of this group of hooks, and thus share the
     * same semantics? May be null.
     */
	AtomicInteger counter;
    /**
     * Which callback will we run?
     */
	TransitionHook hook;
    /**
     * What exception (if any) was thrown by the hook?
     */
	Throwable outcome;
    /**
     * What thread is running the hook?
     */
	Thread thread;

    /**
     * Create an object capable of running a hook in a thread pool.
     * @param lifecycle see {@link #lifecycle}
     * @param counter see {@link #counter}
     * @param hook see {@link #hook}
     */
	HookRunner(Lifecycle lifecycle, AtomicInteger counter, TransitionHook hook) {
		this.lifecycle = lifecycle;
		this.counter = counter;
		this.hook = hook;
		this.outcome = null;
		if (counter != null) {
			counter.incrementAndGet();
		}
	}

	@Override
	public void run() {
		thread = Thread.currentThread();
		outcome = null;
		try {
			hook.run(lifecycle);
		} catch (Throwable e) {
			outcome = e;
		}
        // If we are running this hook as part of a group that someone
        // is waiting on, and if this is the last hook in the group
        // to exit, notify listeners that the group of hooks is now done.
		if ((counter != null) && counter.decrementAndGet() == 0) {
			synchronized(counter) {
				counter.notifyAll();
			}
		}
	}
}
