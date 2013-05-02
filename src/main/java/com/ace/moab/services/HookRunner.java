package com.ace.moab.services;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs a hook and records the result. Does some extra housekeeping so all hooks with a common
 * transition can be waited on together.
 */
class HookRunner implements Runnable {

	Lifecycle lifecycle;
	AtomicInteger counter;
	TransitionHook hook;
	Throwable outcome;
	Thread thread;

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
		if ((counter != null) && counter.decrementAndGet() == 0) {
			synchronized(counter) {
				counter.notifyAll();
			}
		}
	}
}
