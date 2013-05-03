package com.ace.moab.services;

/**
 * Encapsulates all phase-transition logic that applies during the Blocked phase.
 */
public class BlockedPhase extends Phase {

    public final Phase previous;

    public BlockedPhase(Phase previous) {
        this.previous = previous;
    }

    public Phase getNextPhase(Lifecycle lifecycle, Transition transition) throws InvalidTransitionException {
        switch (transition) {
            case Unblock:
                return previous;
            case Terminate:
                return CleaningPhase.Instance;
            case Abandon:
                if (previous instanceof CleaningPhase) {
                    return TerminatedPhase.Instance;
                } else {
                    throw new InvalidTransitionException(String.format(
                            "Services in the %s phase can only accept a \"%s\" transition if they were previously cleaning, not \"%s\".",
                            name, transition.toString(), previous.name));
                }
            default:
                break;
        }
        return invalid(lifecycle, transition);
    }

    public boolean isStable() {
        return true;
    }
}
