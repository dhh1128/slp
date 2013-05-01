package com.ace.moab.services;

public class Lifecycle {

	public Lifecycle() {
		phase = new Defining();
	}

	private Phase phase;

	public Phase getPhase() {
		return phase;
	}

	void setPhase(Phase value) {
		phase = value;
	}

}
