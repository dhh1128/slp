package com.ace.moab.services;

public class LifecycleStateMachine {

	public final ServicePhase analyzing;
	public final ServicePhase cleaning;
	public final ServicePhase failed;
	public final ServicePhase terminated;
	public final ServicePhase suspended;
	public final ServicePhase deployed;
	public final ServicePhase deploying;
	public final ServicePhase deleted;
	public final ServicePhase defining;

	public LifecycleStateMachine() {
		defining = new Defining(this);
		deleted = new Deleted(this);
		analyzing = new Analyzing(this);
		cleaning = new Cleaning(this);
		failed = new Failed(this);
		terminated = new Terminated(this);
		suspended = new Suspended(this);
		deployed = new Deployed(this);
		deploying = new Deploying(this);
		currentPhase = failed;
	}

	private ServicePhase currentPhase;

	public ServicePhase getPhase() {
		return currentPhase;
	}

	void setPhase(ServicePhase newState) {
		currentPhase = newState;
	}

}
