package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Failed phase.
 */
public class Deleted extends ServicePhase {
	LifecycleStateMachine lsm;

	public Deleted(LifecycleStateMachine lsm) {
		super(lsm);
	}

	public boolean isStable() {
		return true;
	}
}
