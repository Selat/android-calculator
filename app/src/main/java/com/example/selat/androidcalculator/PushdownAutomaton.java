package com.example.selat.androidcalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

interface TransitionCallback {
    void call();
}



public class PushdownAutomaton {
    public class TransitCondition {
        public TransitCondition(String state, String inputSignal, String stackTop) {
            this.state = state;
            this.inputSignal = inputSignal;
            this.stackTop = stackTop;
        }

        public String getState() {
            return state;
        }

        public String getInputSignal() {
            return inputSignal;
        }

        public String getStackTop() {
            return stackTop;
        }

        @Override
        // We don't add stackTop to a hashcode in order to be able to
        // specify "*" as a stack top for some transitions (i.e. "anything")
        public int hashCode() { return state.hashCode() ^
                inputSignal.hashCode(); }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TransitCondition)) return false;
            TransitCondition tc = (TransitCondition) o;
            return this.state.equals(tc.getState()) &&
                    this.inputSignal.equals(tc.getInputSignal()) &&
                    (this.stackTop.equals(tc.getStackTop()) ||
                            this.stackTop.equals("*") ||
                            tc.getStackTop().equals("*"));
        }


        private final String state;
        private final String inputSignal;
        private final String stackTop;
    }

    public PushdownAutomaton(String start_state) {
        this.transitions = new HashMap<>();
        this.curState = start_state;
        this.stack = new Stack<>();
    }

    public void addTransition(TransitCondition transitCondition, String to_state,
                              TransitionCallback callback) {
        transitions.put(transitCondition, new Transition(to_state, callback));
    }

    private String getStackTop() {
        if (stack.empty()) {
            return "*empty*";
        } else {
            return stack.peek();
        }
    }

    public boolean transit(String action) {
        TransitCondition tc = new TransitCondition(this.curState, action, getStackTop());
        if (transitions.containsKey(tc)) {
            Transition transition = transitions.get(tc);
            transition.callback.call();
            curState = transition.target_state;
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

    private String curState;
    private Map<TransitCondition, Transition> transitions;
    private Stack<String> stack;
}
