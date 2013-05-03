package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Terminated phase.
 */
public class TerminatedPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final TerminatedPhase Instance = new TerminatedPhase();

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Purge:
			case AutoPurge:
				return DeletedPhase.Instance;
			default:
                break;
		}
        return invalid(lifecycle, transition);
    }

	public boolean isStable() {
		return true;
	}
}
