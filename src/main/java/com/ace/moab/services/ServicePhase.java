package com.ace.moab.services;

/**
 * Implements the state pattern to make management of service state deterministic and straightforward.
 */
public abstract class ServicePhase {

	protected final LifecycleStateMachine lsm;

	protected ServicePhase(LifecycleStateMachine lsm) {
		this.lsm = lsm;
	}

	protected void rejectTransition(PhaseTransition transition) throws InvalidTransitionException {
		throw new InvalidTransitionException(lsm.getPhase().getName(), transition.toString());
	}

	public String getName() {
		return this.getClass().getName();
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		rejectTransition(transition);
	}

	/**
	 * @return true if this phase is one that lasts a long time and generally exits due to user choices.
	 */
	abstract public boolean isStable();
}
