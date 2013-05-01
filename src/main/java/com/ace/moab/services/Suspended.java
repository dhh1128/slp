package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class Suspended extends ServicePhase {

	public Suspended(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			case Blocked:
			case Unblock:
				lsm.setPhase(lsm.deploying); //todo: fix
				break;
			case Terminate:
				lsm.setPhase(lsm.cleaning);
			case FinishedDeploying:
				lsm.setPhase(lsm.deployed);
			default:
				rejectTransition(transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}