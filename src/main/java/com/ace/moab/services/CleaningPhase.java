package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Cleaning phase.
 */
public class CleaningPhase extends Phase {

	public boolean isStable() {
		return false;
	}
}
