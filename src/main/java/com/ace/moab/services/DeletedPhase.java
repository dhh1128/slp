package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Deleted phase.
 */
public class DeletedPhase extends Phase {

	public boolean isStable() {
		return true;
	}
}
