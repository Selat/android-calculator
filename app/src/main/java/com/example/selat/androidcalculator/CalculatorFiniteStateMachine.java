package com.example.selat.androidcalculator;

public class CalculatorFiniteStateMachine extends FiniteStateMachine {
    private static final String TAG = "AndroidCalculator";
    private void addDigitInput(final String digit) {
        addTransition("integer_part", "integer_part", digit, new TransitionCallback() {
            @Override
            public void call() {
                displayed_text += digit;
            }
        });
    }
    public CalculatorFiniteStateMachine() {
        super("integer_part");
        this.displayed_text = new String();
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
    }

    public String getDisplayed_text() {
        return this.displayed_text;
    }

    private String displayed_text;
}
