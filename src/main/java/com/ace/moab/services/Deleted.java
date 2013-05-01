package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Failed phase.
 */
public class Deleted extends Phase {

	public boolean isStable() {
		return true;
	}
}
