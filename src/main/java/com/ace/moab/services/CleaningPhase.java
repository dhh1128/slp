package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Cleaning phase.
 */
public class CleaningPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final CleaningPhase Instance = new CleaningPhase();

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
        switch (transition) {
            case Block:
                return new BlockedPhase(this);
            case Clean:
                return TerminatedPhase.Instance;
            default:
                break;
        }
        return invalid(lifecycle, transition);
    }

    public boolean isStable() {
		return false;
	}
}
