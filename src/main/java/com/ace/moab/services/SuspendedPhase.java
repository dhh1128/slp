package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Suspended phase.
 */
public class SuspendedPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final SuspendedPhase Instance = new SuspendedPhase();

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Resume:
				return new AnalyzingPhase(transition);
			case Terminate:
				return CleaningPhase.Instance;
			default:
                break;
		}
        return invalid(lifecycle, transition);
    }

	public boolean isStable() {
		return true;
	}
}
