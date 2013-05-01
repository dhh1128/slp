package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Terminated phase.
 */
public class TerminatedPhase extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Purge:
			case AutoPurge:
				lifecycle.setPhase(new DeletedPhase());
				break;
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}