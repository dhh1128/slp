package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Failed phase.
 */
public class FailedPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final FailedPhase Instance = new FailedPhase();

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Purge:
			case AutoPurge:
				return DeletedPhase.Instance;
            case Tweak:
                return DefiningPhase.Instance;
            case Submit:
                return new AnalyzingPhase(transition);
			default:
                break;
		}
        return invalid(lifecycle, transition);
    }

	public boolean isStable() {
		return true;
	}
}
