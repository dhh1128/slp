/**
 * Author: Daniel Hardman
 */
package com.ace.moab.services;

import spock.lang.Specification;

public class PhaseSpec extends Specification {
	
	def "simple phase evolution"() {
		given:
		Lifecycle lc = new Lifecycle()

		expect:
		lc.phase.name == "defining"

		when:
		lc.requestTransition(Transition.Submit)
		lc.requestTransition(Transition.Rejected)

		then:
		lc.phase.name == "failed"

		when:
		lc.requestTransition(Transition.AutoMigrate)

		then:
		thrown(InvalidTransitionException)
	}

	class MyTransitionHook extends TransitionHook {
		StringBuilder sb;
		String name;
		MyTransitionHook(String name, Transition transition, HookPosition pos, StringBuilder sb) {
			super(transition, pos);
			this.name = name;
			this.sb = sb;
		}
		static Random theRandomizer = new Random();
		@Override
		void run(Lifecycle lifecycle) {
			Thread.sleep(5 + theRandomizer.nextInt(25));
			synchronized (sb) {
				sb.append(name);
				sb.append(' ');
				sb.append(transition.toString());
				sb.append(' ');
				sb.append(position.toString());
				sb.append('\n');
			}
		}
	}

	def "verify hooks"() {
		given:
		Lifecycle lc = new Lifecycle()
		List<TransitionHook> hooks = new ArrayList<TransitionHook>();
		StringBuilder sb = new StringBuilder();

		when:
		hooks.add(new MyTransitionHook("hook1", Transition.Submit, HookPosition.Pre, sb))
		hooks.add(new MyTransitionHook("hook2", Transition.Submit, HookPosition.Pre, sb))
		hooks.add(new MyTransitionHook("hook3", Transition.Submit, HookPosition.Early, sb))
		lc.hooks = hooks
		lc.requestTransition(Transition.Submit)

		then:
		lc.phase.name == "analyzing"
		// At this point, we may (but probably do not) have the "Early" hook finished --
		// but we should be able to *guarantee* that the two pre hooks executed, and
		// that they finished their work *before* the Early hook did.
		sb.toString() ==~ "hook[12] Submit Pre\nhook[12] Submit Pre\n*"

		when:
		// This should guarantee that the Early hook completes.
		Thread.sleep(100);

		then:
		sb.toString() ==~ "hook[12] Submit Pre\nhook[12] Submit Pre\nhook3 Submit Early\n"
	}
}
