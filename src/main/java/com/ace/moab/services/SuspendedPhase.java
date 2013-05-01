package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Suspended phase.
 */
public class SuspendedPhase extends Phase {

	public void requestTransition(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
		switch (transition) {
			case Blocked:
			case Unblock:
				lifecycle.setPhase(new DeployingPhase()); //todo: fix
				break;
			case Terminate:
				lifecycle.setPhase(new CleaningPhase());
			case FinishedDeploying:
				lifecycle.setPhase(new DeployedPhase());
			default:
				rejectTransition(lifecycle, transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}