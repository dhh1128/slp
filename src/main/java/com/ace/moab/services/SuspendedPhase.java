package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Suspended phase.
 */
public class SuspendedPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Resume:
				return new AnalyzingPhase(transition);
			case Terminate:
				return new CleaningPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
