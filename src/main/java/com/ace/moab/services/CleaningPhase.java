package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Cleaning phase.
 */
public class CleaningPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return false;
	}
}
