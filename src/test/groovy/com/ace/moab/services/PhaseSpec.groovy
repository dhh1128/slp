/**
 * Author: Daniel Hardman
 */
package com.ace.moab.services;

import spock.lang.Specification;

public class PhaseSpec extends Specification {
	
	def "simple phase evolution"() {
		given:
		Lifecycle lc = new Lifecycle();

		when:
			lc.phase.requestTransition(lc, Transition.Submit)
			lc.phase.requestTransition(lc, Transition.Rejected)
		then:
			lc.phase.name == "Failed"
	}
}
