package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Blocked phase.
 */
public class BlockedPhase extends Phase {

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
        switch (transition) {
            case Unblock:
                return new DeployingPhase();
            case Terminate:
                return new CleaningPhase();
            default:
                return invalid(lifecycle, transition);
        }
    }

    public boolean isStable() {
        return true;
    }
}
