package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Analyzing phase.
 */
public class Analyzing extends ServicePhase {
	
    public Analyzing(LifecycleStateMachine lsm) {
        super(lsm);
    }

	public void requestTransition(PhaseTransition transition) throws InvalidTransitionException {
		switch (transition) {
			case Accepted:
				lsm.setPhase(lsm.deploying);
				break;
			case Rejected:
			case Terminate:
				lsm.setPhase(lsm.failed);
				break;
			default:
				rejectTransition(transition);
		}
	}

	@Override
	public boolean isStable() {
		return false;
	}
}
