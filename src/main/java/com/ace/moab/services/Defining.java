package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Defining phase.
 */
public class Defining extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Submit:
				lifecycle.setPhase(new Analyzing());
				break;
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
