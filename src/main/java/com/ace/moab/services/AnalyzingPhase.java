package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class AnalyzingPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Accepted:
				return new DeployingPhase();
			case Rejected:
			case Terminate:
				return new FailedPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	@Override
	public boolean isStable() {
		return false;
	}
}
