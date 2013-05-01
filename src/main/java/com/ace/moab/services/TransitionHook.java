package com.ace.moab.services;

/**
 * Defines the interface and behavior of any hook that will be invoked by state machine transitions.
 *
 * Basically this class is a fancy implementation of Runnable. It differs from the bare-bones interface
 * in that:
 *
 *   - It knows which transition it is targeting, and which position the hook should occupy in the
 *     program flow around that transition.
 *   - It records whether invocation throws an exception, so success or failure can be analyzed.
 *   - It signals completion, so callers can take action when hooks complete.
 *   - Because it doesn't swallow an InterruptedException, it allows hooks to be stopped or killed
 *     by simply calling {@link Thread#interrupt()}. If this happens, the hook should return almost
 *     immediately, with #problem set to an InterruptedException.
 */
abstract public class TransitionHook implements Runnable {

	// Eventually, this may allow the hook to walk back to its corresponding Service object to get context...
	public final Lifecycle lifecycle;

	public final Transition transition;
	public final HookPosition position;
	private Throwable problem;

	public TransitionHook(Lifecycle lifecycle, Transition transition, HookPosition position) {
		this.lifecycle = lifecycle;
		this.transition = transition;
		this.position = position;
		this.problem = null;
	}

	abstract protected void doRun() throws Throwable;

	@Override
	final public void run() {
		try {
			this.problem = null;
			doRun();
		} catch (Throwable e) {
			this.problem = e;
		}
		synchronized(this) {
			this.notifyAll();
		}
	}
}
