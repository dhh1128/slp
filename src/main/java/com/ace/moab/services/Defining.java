package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Defining phase.
 */
public class Defining extends ServicePhase {
	LifecycleStateMachine lsm;

	public Defining(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			case Submit:
				lsm.setPhase(lsm.analyzing);
				break;
			default:
				rejectTransition(transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
