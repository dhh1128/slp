package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class AnalyzingPhase extends Phase {
	
	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Accepted:
				lifecycle.setPhase(new DeployingPhase());
				break;
			case Rejected:
			case Terminate:
				lifecycle.setPhase(new FailedPhase());
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
