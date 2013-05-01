package com.ace.moab.services;

/**
 * Implements the "state" pattern (see Gang of 4 book) to make evolution of service lifecycle
 * deterministic and straightforward.
 */
public abstract class Phase {

	protected void rejectTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		throw new InvalidTransitionException(lifecycle.getPhase().getName(), transition.toString());
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		rejectTransition(lifecycle, transition);
	}

	/**
	 * @return true if this phase is one that lasts a long time and generally exits due to user choices.
	 */
	abstract public boolean isStable();
}
