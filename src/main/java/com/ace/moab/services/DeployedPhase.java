package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deployed phase.
 */
public class DeployedPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Pause:
			case Damaged:
				return new SuspendedPhase();
			case Expired:
			case Terminate:
				return new CleaningPhase();
			case AutoMigrate:
			case ManualMigrate:
			case Modify:
				return new AnalyzingPhase(transition);
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
