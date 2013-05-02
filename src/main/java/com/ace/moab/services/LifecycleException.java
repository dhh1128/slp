package com.ace.moab.services;

/**
 * Any exception that happens while performing a transition.
 */
public class LifecycleException extends Exception {
	public LifecycleException(String message) {
		super(message);
	}
}
