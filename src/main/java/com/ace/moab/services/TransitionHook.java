package com.ace.moab.services;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines the interface and behavior of any hook that will be invoked by state machine transitions.
 *
 * Basically this class is a fancy implementation of Runnable. It differs from the bare-bones interface
 * in that:
 *
 *   - It performs some extra housekeeping, and its run() method gets some extra args to facilitate that.
 *   - It knows which transition it is targeting, and which position the hook should occupy in the
 *     program flow around that transition.
 *   - It records whether invocation throws an exception, so success or failure can be analyzed.
 *   - It signals completion, so callers can take action when hooks complete.
 *   - Because it doesn't swallow an InterruptedException, it allows hooks to be stopped or killed
 *     by simply calling {@link Thread#interrupt()}. If this happens, the hook should return almost
 *     immediately.
 */
abstract public class TransitionHook {

	public final Transition transition;
	public final HookPosition position;
	public final int timeoutMillis;

	private static final int DEFAULT_HOOK_TIMEOUT_MILLIS = 15000;
	private static final int MAX_HOOK_TIMEOUT_MILLIS = 60000;

	public TransitionHook(Transition transition, HookPosition position) {
		this.transition = transition;
		this.position = position;
		timeoutMillis = DEFAULT_HOOK_TIMEOUT_MILLIS;
	}

	public TransitionHook(Transition transition, HookPosition position, int timeoutMillis) {
		this.transition = transition;
		this.position = position;
		this.timeoutMillis = timeoutMillis < 1 ? MAX_HOOK_TIMEOUT_MILLIS : Math.min(timeoutMillis, MAX_HOOK_TIMEOUT_MILLIS);
	}

	abstract public void run(Lifecycle lifecycle) throws Throwable;

}
