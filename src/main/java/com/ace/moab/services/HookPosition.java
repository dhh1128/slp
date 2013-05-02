package com.ace.moab.services;

/**
 * Describes possible semantics for a hook -- does it run before or after a transition?
 */
public enum HookPosition {
	/**
	 * Hook is triggered as soon as transition is requested. It must finish successfully before transition is approved;
	 * if it fails, no transition happens.
	 */
	Pre,
	/**
	 * Hook is triggered asynchronously as soon as transition is requested, but its failure does not prevent transition.
	 */
	Early,
	/**
	 * Hook runs after transition has happened. Failure of the hook does not invalidate or undo transition.
	 */
	Post,
}
