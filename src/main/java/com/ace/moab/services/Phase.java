package com.ace.moab.services;

/**
 * Implements the "state" pattern (see Gang of 4 book) to make evolution of service lifecycle
 * deterministic and straightforward.
 */
public abstract class Phase {

	protected Phase invalid(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		throw new InvalidTransitionException(lifecycle.getPhase().name, transition.toString());
	}

	public final String name = this.getClass().getSimpleName().toLowerCase().replace("phase", "");

	/*not public!*/ Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		return invalid(lifecycle, transition);
	}

	/**
	 * @return true if this phase is one that lasts a long time and generally exits due to user choices.
	 */
	abstract public boolean isStable();
}
