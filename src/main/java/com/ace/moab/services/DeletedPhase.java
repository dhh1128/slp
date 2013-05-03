package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deleted phase.
 */
public class DeletedPhase extends Phase {

    // This phase doesn't have any member variables, so there's no point
    // in creating a new object every time we want the phase. Just use
    // the singleton.
    static final DeletedPhase Instance = new DeletedPhase();

    public boolean isStable() {
		return true;
	}
}
