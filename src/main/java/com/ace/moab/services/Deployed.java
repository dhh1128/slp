package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deployed phase.
 */
public class Deployed extends ServicePhase {

	public Deployed(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			case Pause:
			case Damaged:
				lsm.setPhase(lsm.suspended);
				break;
			case Expired:
			case Terminate:
				lsm.setPhase(lsm.cleaning);
				break;
			case AutoMigrate:
			case ManualMigrate:
			case Modify:
				lsm.setPhase(lsm.analyzing);
			default:
				rejectTransition(transition);
		}
	}

	public boolean isStable() {
		return true;
	}
}
