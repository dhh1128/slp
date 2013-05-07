package com.ace.moab.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Sample app to experiment with lifecycles.
 */
public class App {

    static Random theRandomizer = new Random();

    static class MyTransitionHook extends TransitionHook {
        String name;
        MyTransitionHook(String name, Transition transition, HookPosition pos) {
            super(transition, pos);
            this.name = name;
        }
        @Override
        public void run(Lifecycle lifecycle) {
            try {
                Thread.sleep(300 + theRandomizer.nextInt(300));
            } catch (InterruptedException ex) {
            }
            StringBuilder sb = new StringBuilder();
            synchronized (sb) {
                sb.append("finished ");
                sb.append(name);
                sb.append(' ');
                sb.append(transition.toString());
                sb.append(' ');
                sb.append(position.toString());
                sb.append('\n');
            }
            synchronized (System.out) {
                System.out.print(sb.toString());
            }
        }
    }

    private static Lifecycle getSampleLifecycle() {
        Lifecycle lc = new Lifecycle();
        List<TransitionHook> hooks = new ArrayList<TransitionHook>();
        for (Transition t: Transition.values()) {
            hooks.add(new MyTransitionHook(String.format("pre %s #1", t.toString()), t, HookPosition.Pre));
            hooks.add(new MyTransitionHook(String.format("pre %s #2", t.toString()), t, HookPosition.Pre));
            hooks.add(new MyTransitionHook(String.format("early %s #1", t.toString()), t, HookPosition.Early));
            hooks.add(new MyTransitionHook(String.format("post %s #1", t.toString()), t, HookPosition.Post));
        }
        lc.setHooks(hooks);
        return lc;
    }

    public static void main(String[] args) {
        Lifecycle lc = getSampleLifecycle();
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.printf("Currently in \"%s\" phase.\n", lc.getPhase().name);
            System.out.printf("  Request transition by name, or \"reset\" to start over: ");
            String cmd = stdin.nextLine().trim().toLowerCase();
            if (cmd.equals("reset")) {
                lc = getSampleLifecycle();
            } else {
                boolean found = false;
                for (Transition t: Transition.values()) {
                    if (t.toString().toLowerCase().equals(cmd)) {
                        try {
                            found = true;
                            lc.requestTransition(t);
                            System.out.printf("  Request for \"%s\" transition accepted.\n", t.toString());
                        } catch (LifecycleException ex) {
                            System.out.printf(ex.toString());
                        }
                        break;
                    }
                }
                if (!found) {
                    System.out.printf("  \"%s\" is not recognized.\n", cmd);
                }
            }
        }
    }
}
