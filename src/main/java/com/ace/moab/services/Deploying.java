package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deploying phase.
 */
public class Deploying extends ServicePhase {
	LifecycleStateMachine lsm;

	public Deploying(LifecycleStateMachine lsm) {
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
		return false;
	}
}
