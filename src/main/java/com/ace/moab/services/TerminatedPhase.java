package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Terminated phase.
 */
public class TerminatedPhase extends Phase {

	public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Purge:
			case AutoPurge:
				return new DeletedPhase();
			default:
				return invalid(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}