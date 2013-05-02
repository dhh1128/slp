package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class AnalyzingPhase extends Phase {

    /**
     * Which transition caused us to enter the Analyzing phase? This is important because
     * it influences where we go after analyzing.
     */
    public final Transition fromTransition;

    public AnalyzingPhase(Transition fromTransition) {
        this.fromTransition = fromTransition;
    }

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Accepted:
                if (transition == Transition.OnBoard) {
                    return new DeployedPhase();
                } else {
				    return new DeployingPhase();
                }
			case Rejected:
			case Terminate:
                if (fromTransition == Transition.Submit) {
				    return new FailedPhase();
                } else {
                    return new CleaningPhase();
                }
            case CantModify:
                return new DeployedPhase();
            case CantResume:
                return new SuspendedPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	@Override
	public boolean isStable() {
		return false;
	}
}
