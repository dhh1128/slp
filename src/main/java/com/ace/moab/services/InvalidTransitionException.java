package com.ace.moab.services;

/**
 * Documents an illegal request to change the phase of a service.
 */
public class InvalidTransitionException extends Exception {
	public InvalidTransitionException(String currentPhase, String transition) {
		super(String.format("Services in the %s phase cannot accept a \"%s\" signal.", currentPhase, transition));
	}
}
