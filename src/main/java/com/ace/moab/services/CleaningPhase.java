package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Cleaning phase.
 */
public class CleaningPhase extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return false;
	}
}
