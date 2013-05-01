package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Defining phase.
 */
public class DefiningPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Submit:
				return new AnalyzingPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
