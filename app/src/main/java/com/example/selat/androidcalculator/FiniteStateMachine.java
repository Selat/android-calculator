package com.example.selat.androidcalculator;

import java.util.Map;
import java.util.TreeMap;

interface TransitionCallback {
    void call();
}

public class FiniteStateMachine {
    public FiniteStateMachine(String start_state) {
        this.transitions = new TreeMap<>();
        this.cur_state = start_state;
    }

    public void addTransition(String from_state, String to_state, String action,
                              TransitionCallback callback) {
        if (transitions.containsKey(from_state)) {
            Map<String, Transition> transition = transitions.get(from_state);
            transition.put(action, new Transition(to_state, callback));
        } else {
            Map<String, Transition> transition = new TreeMap<>();
            transition.put(action, new Transition(to_state, callback));
            transitions.put(from_state, transition);
        }
    }

    public boolean transit(String action) {
        if (transitions.get(cur_state).containsKey(action)) {
            Transition transition = transitions.get(cur_state).get(action);
            transition.callback.call();
            cur_state = transition.target_state;
            return true;
        } else {
            return false;
        }
    }

    private class Transition {
        public Transition(String target_state, TransitionCallback callback) {
            this.target_state = target_state;
            this.callback = callback;
        }
        public String target_state;
        public TransitionCallback callback;
    }

    private String cur_state;
    private Map<String, Map<String, Transition>>transitions;
}
