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
		lc.requestTransition(Transition.Reject)

		then:
		lc.phase.name == "failed"

		when:
		lc.requestTransition(Transition.AutoMigrate)

		then:
		thrown(InvalidTransitionException)
	}

	class MyTransitionHook extends TransitionHook {
		final StringBuilder sb;
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

	def "pre hooks execute before all others and before returning from requestTransition"() {
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
		// This should guarantee that the Early hook completes. TO DO: add a method to lifecycle
		// so we can wait for all hooks, so we don't have to depend on this 99% but not 100% method.
		Thread.sleep(100);

		then:
		sb.toString() ==~ "hook[12] Submit Pre\nhook[12] Submit Pre\nhook3 Submit Early\n"
	}

    def "analyzing phase"() {
        given:
        Lifecycle lc = new Lifecycle()

        expect:
        lc.requestTransition(Transition.Submit).name == "analyzing"

        when:
        lc.requestTransition(Transition.RejectModify)

        then: "shouldn't be possible to reject modify request unless we were previously deployed"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.RejectResume)

        then: "shouldn't be possible to reject resume request unless we were previously deployed"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.Adopt)

        then: "shouldn't be possible to adopt unless we were onboarding"
        thrown(InvalidTransitionException)

        // Finish Deploying
        expect:
        lc.requestTransition(Transition.Accept).name == "deploying"
        lc.requestTransition(Transition.Deploy).name == "deployed"
        lc.requestTransition(Transition.Migrate).name == "analyzing"

        when:
        lc.requestTransition(Transition.Reject)

        then: "shouldn't be possible to do plain rejection unless we are on initial submit or adopt"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.Adopt)

        then: "shouldn't be possible to adopt unless we were onboarding"
        thrown(InvalidTransitionException)

        // Finish migrating
        expect:
        lc.requestTransition(Transition.Accept).name == "deploying"
        lc.requestTransition(Transition.Deploy).name == "deployed"
        lc.requestTransition(Transition.Pause).name == "suspended"
        lc.requestTransition(Transition.Resume).name == "analyzing"

        when:
        lc.requestTransition(Transition.Reject)

        then: "shouldn't be possible to do plain rejection unless we are on initial submit or adopt"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.Adopt)

        then: "shouldn't be possible to adopt unless we were onboarding"
        thrown(InvalidTransitionException)

        // Finish resuming
        expect:
        lc.requestTransition(Transition.Accept).name == "deploying"
        lc.requestTransition(Transition.Deploy).name == "deployed"

        when:
        lc = new Lifecycle()
        lc.requestTransition(Transition.Onboard)

        then:
        lc.phase.name == "analyzing"

        when:
        lc.requestTransition(Transition.RejectModify)

        then: "shouldn't be possible to reject modify request unless we were previously deployed"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.RejectResume)

        then: "shouldn't be possible to reject resume request unless we were previously deployed"
        thrown(InvalidTransitionException)

        when:
        lc.requestTransition(Transition.Accept)

        then: "shouldn't be possible to do ordinary accept->deploying while onboarding"
        thrown(InvalidTransitionException)

        // Finish onboarding
        expect:
        lc.requestTransition(Transition.Adopt).name == "deployed"

        when:
        lc = new Lifecycle()

        then:
        lc.requestTransition(Transition.Onboard)

        expect:
        lc.requestTransition(Transition.Reject).name == "failed"
    }

    def getBlockedVia(Phase previous) {
        Lifecycle lc = new Lifecycle()
        lc.requestTransition(Transition.Submit)
        lc.requestTransition(Transition.Accept)
        if (previous instanceof CleaningPhase) {
            lc.requestTransition(Transition.Terminate)
        }
        lc.requestTransition(Transition.Block)
        return lc
    }

    def "blocked phase"() {
        given:
        Lifecycle lc = getBlockedVia(DeployingPhase.Instance)

        when:
        lc.requestTransition(Transition.Abandon)

        then: "shouldn't be possible to abandon if we're not blocked via cleaning"
        thrown(InvalidTransitionException)

        expect:
        lc.requestTransition(Transition.Unblock).name == "deploying"
        lc.requestTransition(Transition.Terminate).name == "cleaning"
        lc.requestTransition(Transition.Clean).name == "terminated"

        when:
        lc = getBlockedVia(CleaningPhase.Instance)

        then:
        lc.requestTransition(Transition.Unblock).name == "cleaning"
        lc.requestTransition(Transition.Block).name == "blocked"
        lc.requestTransition(Transition.Abandon).name == "terminated"

        when:
        lc = getBlockedVia(CleaningPhase.Instance)

        then:
        lc.requestTransition(Transition.Terminate).name == "cleaning"
    }
}
