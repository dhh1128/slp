package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Cleaning phase.
 */
public class Cleaning extends ServicePhase {

	LifecycleStateMachine lsm;

	public Cleaning(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			default:
				rejectTransition(transition);
		}
	}

	public boolean isStable() {
		return false;
	}
}
