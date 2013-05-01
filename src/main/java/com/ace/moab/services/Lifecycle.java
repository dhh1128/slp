package com.ace.moab.services;

public class Lifecycle {

	public Lifecycle() {
		phase = new DefiningPhase();
	}

	private Phase phase;

	public Phase getPhase() {
		return phase;
	}

	/*not public!*/ void setPhase(Phase value) {
		phase = value;
	}

	public void requestTransition(Transition transition) throws InvalidTransitionException {
		phase.requestTransition(this, transition);
	}

}
