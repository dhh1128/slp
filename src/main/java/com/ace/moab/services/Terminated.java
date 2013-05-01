package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Terminated phase.
 */
public class Terminated extends ServicePhase {

	public Terminated(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			case Purge:
			case AutoPurge:
				lsm.setPhase(lsm.deleted);
				break;
			default:
				rejectTransition(transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}