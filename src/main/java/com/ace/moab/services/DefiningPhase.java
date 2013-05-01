package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Defining phase.
 */
public class DefiningPhase extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Submit:
				lifecycle.setPhase(new AnalyzingPhase());
				break;
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
