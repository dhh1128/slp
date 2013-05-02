package com.ace.moab.services;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A TransitionHook allows callbacks to execute as services transition from one phase
 * of their lifecycle to another. Hooks are possible on any transition.
 *
 * This implementation is like a fancy implementation of java's Runnable interface.
 * It differs from the bare-bones interface in that:
 *
 *   - It works with {@link HookRunner} to performs some extra housekeeping, and its
 *     run() method gets some extra args to facilitate that.
 *   - It knows which transition it is targeting, and when the hook should run relative to that
 *     transition.
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
