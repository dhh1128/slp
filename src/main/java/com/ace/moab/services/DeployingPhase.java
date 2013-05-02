package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deploying phase.
 */
public class DeployingPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Block:
                return new BlockedPhase();
			case Terminate:
				return new CleaningPhase();
			case Complete:
				return new DeployedPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return false;
	}
}
