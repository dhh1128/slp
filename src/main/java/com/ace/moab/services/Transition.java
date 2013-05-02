package com.ace.moab.services;

/**
 * Enumerates all the signals that a Service can respond to, which may cause a transition to a new phase of
 * the service's lifecycle.
 */
public enum Transition {
	Submit,
	Accepted,
	Rejected,
	Purge,
	AutoPurge,
	Terminate,
	Unblock,
	Block,
    Complete,
    OnBoard,
	CantModify,
	CantResume,
	AutoMigrate,
	ManualMigrate,
	Modify,
	Expired,
	Pause,
	Damaged,
    Tweak,
	Resume,
}
