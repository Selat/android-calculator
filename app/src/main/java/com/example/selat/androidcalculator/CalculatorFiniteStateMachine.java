package com.example.selat.androidcalculator;

public class CalculatorFiniteStateMachine extends PushdownAutomaton {
    private static final String TAG = "AndroidCalculator";
    private void addDigitInput(final String digit) {
        addTransition(new TransitCondition("integer_part", digit, "*"), "integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText += digit;
            }
        }, StackOperationType.PUSH, "integer_part");
        addTransition(new TransitCondition("real_part", digit, "*"), "real_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText += digit;
            }
        }, StackOperationType.PUSH, "real_part");
    }
    private void addZeroDigitInput(final String digit) {
        addTransition(new TransitCondition("zero_integer_part", digit, "*"), "integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = digit;
            }
        }, StackOperationType.PUSH, "zero_integer_part");
    }
    public CalculatorFiniteStateMachine() {
        super("zero_integer_part");
        this.displayedText = "0";

        addZeroDigitInput("1");
        addZeroDigitInput("2");
        addZeroDigitInput("3");
        addZeroDigitInput("4");
        addZeroDigitInput("5");
        addZeroDigitInput("6");
        addZeroDigitInput("7");
        addZeroDigitInput("8");
        addZeroDigitInput("9");
        addDigitInput("0");
        addDigitInput("1");
        addDigitInput("2");
        addDigitInput("3");
        addDigitInput("4");
        addDigitInput("5");
        addDigitInput("6");
        addDigitInput("7");
        addDigitInput("8");
        addDigitInput("9");

        addTransition(new TransitCondition("zero_integer_part", ".", "*"), "real_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText += ".";
            }
        }, StackOperationType.PUSH, "zero_integer_part");
        addTransition(new TransitCondition("integer_part", ".", "*"), "real_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText += ".";
            }
        }, StackOperationType.PUSH, "integer_part");

        // We don't want to pop something out of an empty stack
        addTransition(new TransitCondition("zero_integer_part", "backspace", "*empty*"), "zero_integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = "0";
            }
        });
        addTransition(new TransitCondition("integer_part", "backspace", "zero_integer_part"), "zero_integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = "0";
            }
        }, StackOperationType.POP, null);
        addTransition(new TransitCondition("integer_part", "backspace", "integer_part"), "integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = displayedText.substring(0, displayedText.length() - 1);
            }
        }, StackOperationType.POP, null);
        addTransition(new TransitCondition("real_part", "backspace", "integer_part"), "integer_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = displayedText.substring(0, displayedText.length() - 1);
            }
        }, StackOperationType.POP, null);
        addTransition(new TransitCondition("real_part", "backspace", "real_part"), "real_part", new TransitionCallback() {
            @Override
            public void call() {
                displayedText = displayedText.substring(0, displayedText.length() - 1);
            }
        }, StackOperationType.POP, null);
    }

    public String getDisplayedText() {
        return this.displayedText;
    }

    private String displayedText;
}
