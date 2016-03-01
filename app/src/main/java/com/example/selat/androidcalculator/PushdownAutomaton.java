package com.example.selat.androidcalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

interface TransitionCallback {
    void call();
}



public class PushdownAutomaton {
    enum StackOperationType {PUSH, POP, NOP}
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

    public PushdownAutomaton(String startState) {
        this.transitions = new HashMap<>();
        this.startState = startState;
        this.curState = startState;
        this.stack = new Stack<>();
    }

    public void setCurrentState(String state) {
        this.curState = state;
    }

    public void addTransition(TransitCondition transitCondition, String to_state,
                              TransitionCallback callback) {
        transitions.put(transitCondition, new Transition(to_state, callback,
                StackOperationType.NOP, null));
    }

    public void addTransition(TransitCondition transitCondition, String to_state,
                              TransitionCallback callback,
                              StackOperationType stackOperationType,
                              String operand) {
        transitions.put(transitCondition, new Transition(to_state, callback,
                stackOperationType, operand));
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
            curState = transition.target_state;

            switch (transition.stackOperationType) {
                case NOP:
                    break;
                case PUSH:
                    stack.push(transition.stackOperand);
                    break;
                case POP:
                    stack.pop();
                    break;
            }

            transition.callback.call();

            return true;
        } else {
            return false;
        }
    }

    // NOTE: Here we assume that each state id is just one symbol long
    public void restart(String s) {
        curState = startState;
        stack.clear();
        for (int i = 0; i < s.length(); ++i) {
            transit(s.substring(i, i + 1));
        }
    }

    private class Transition {
        public Transition(String target_state, TransitionCallback callback,
                          StackOperationType stackOperationType, String stackOperand) {
            this.target_state = target_state;
            this.callback = callback;
            this.stackOperationType = stackOperationType;
            this.stackOperand = stackOperand;
        }
        public String target_state;
        public TransitionCallback callback;
        public StackOperationType stackOperationType;
        public String stackOperand;
    }

    protected String curState;
    protected String startState;
    protected Map<TransitCondition, Transition> transitions;
    protected Stack<String> stack;
}
