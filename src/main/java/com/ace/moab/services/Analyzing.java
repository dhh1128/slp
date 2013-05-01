package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class Analyzing extends Phase {
	
	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Accepted:
				lifecycle.setPhase(new Deploying());
				break;
			case Rejected:
			case Terminate:
				lifecycle.setPhase(new Failed());
				break;
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	@Override
	public boolean isStable() {
		return false;
	}
}
