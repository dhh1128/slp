package com.ace.moab.services;

/**
 * Documents an illegal request to change the phase of a service.
 */
public class InvalidTransitionException extends LifecycleException {

	public InvalidTransitionException(Phase phase, Transition transition) {
		super(String.format("Services in the %s phase cannot accept a \"%s\" signal.", phase.name, transition.toString()));
	}

    public InvalidTransitionException(String msg) {
        super(msg);
    }
}
