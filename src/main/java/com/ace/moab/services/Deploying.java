package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deploying phase.
 */
public class Deploying extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Blocked:
			case Unblock:
				//lifecycle.setPhase(new deploying); //todo: fix
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
		return false;
	}
}
