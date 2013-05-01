package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class Suspended extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Blocked:
			case Unblock:
				lifecycle.setPhase(new Deploying()); //todo: fix
				break;
			case Terminate:
				lifecycle.setPhase(new Cleaning());
			case FinishedDeploying:
				lifecycle.setPhase(new Deployed());
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}