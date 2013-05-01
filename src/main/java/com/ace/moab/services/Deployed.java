package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deployed phase.
 */
public class Deployed extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Pause:
			case Damaged:
				lifecycle.setPhase(new Suspended());
				break;
			case Expired:
			case Terminate:
				lifecycle.setPhase(new Cleaning());
				break;
			case AutoMigrate:
			case ManualMigrate:
			case Modify:
				lifecycle.setPhase(new Analyzing());
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
