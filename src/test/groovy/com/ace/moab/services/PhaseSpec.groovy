/**
 * Author: Daniel Hardman
 */
package com.ace.moab.services;

import spock.lang.Specification;

public class PhaseSpec extends Specification {
	
	def "simple phase evolution"() {
		given:
		Lifecycle lc = new Lifecycle();

		expect:
		lc.phase.name == "defining"

		when:
			lc.requestTransition(Transition.Submit)
			lc.requestTransition(Transition.Rejected)
		then:
			lc.phase.name == "failed"
	}
}
