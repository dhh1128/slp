package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deployed phase.
 */
public class DeployedPhase extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Pause:
			case Damaged:
				lifecycle.setPhase(new SuspendedPhase());
				break;
			case Expired:
			case Terminate:
				lifecycle.setPhase(new CleaningPhase());
				break;
			case AutoMigrate:
			case ManualMigrate:
			case Modify:
				lifecycle.setPhase(new AnalyzingPhase());
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
