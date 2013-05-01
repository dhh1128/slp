/**
 * Author: Daniel Hardman
 */
package com.ace.moab.services;

import spock.lang.Specification;

public class PhaseSpec extends Specification {
	
	def "simple phase evolution"() {
		given:
		LifecycleStateMachine machine = new LifecycleStateMachine();

		when:
			machine.phase.requestTransition(PhaseTransition.Submit)
			machine.phase.requestTransition(PhaseTransition.Rejected)
		then:
			machine.phase.name == "Failed"
	}
}
