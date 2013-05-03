package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deploying phase.
 */
public class DeployingPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final DeployingPhase Instance = new DeployingPhase();

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Block:
                return new BlockedPhase(this);
			case Terminate:
				return CleaningPhase.Instance;
			case Deploy:
				return DeployedPhase.Instance;
			default:
                break;
		}
        return invalid(lifecycle, transition);
    }

	public boolean isStable() {
		return false;
	}
}
