package com.example.selat.androidcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CalculatorFiniteStateMachine calculator_fsm;
    private TextView text;

    private void addDigitButtonListener(int id, final String action) {
        Button button_digit = (Button)findViewById(id);
        button_digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingOperator == null) {
                    pendingArgument = null;
                }
                if (!calculator_fsm.transit(action)) {
                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }
                text.setText(calculator_fsm.getDisplayedText());
            }
        });
    }

    private void addBinaryOperator(int id, final BinaryOperator operator) {
        Button button_operator = (Button)findViewById(id);
        button_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingOperator == null) {
                    pendingOperator = operator;
                    pendingArgument = Double.parseDouble(calculator_fsm.getDisplayedText());
                    calculator_fsm.setDisplayedText("0");
                    calculator_fsm.restart();
                } else {
                    Double b = Double.parseDouble(calculator_fsm.getDisplayedText());
                    pendingArgument = pendingOperator.apply(pendingArgument, b);
                    pendingOperator = operator;
                    calculator_fsm.setDisplayedText(Double.toString(pendingArgument));
                    calculator_fsm.restart();
                    text.setText(calculator_fsm.getDisplayedText());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator_fsm = new CalculatorFiniteStateMachine();

        text = (TextView)findViewById(R.id.text_view);
        text.setText(calculator_fsm.getDisplayedText());

        addDigitButtonListener(R.id.button_digit_0, "0");
        addDigitButtonListener(R.id.button_digit_1, "1");
        addDigitButtonListener(R.id.button_digit_2, "2");
        addDigitButtonListener(R.id.button_digit_3, "3");
        addDigitButtonListener(R.id.button_digit_4, "4");
        addDigitButtonListener(R.id.button_digit_5, "5");
        addDigitButtonListener(R.id.button_digit_6, "6");
        addDigitButtonListener(R.id.button_digit_7, "7");
        addDigitButtonListener(R.id.button_digit_8, "8");
        addDigitButtonListener(R.id.button_digit_9, "9");
        Button button_dot = (Button)findViewById(R.id.button_dot);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calculator_fsm.transit(".")) {
                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }
                text.setText(calculator_fsm.getDisplayedText());
            }
        });
        Button buttonClear = (Button)findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculator_fsm.setDisplayedText("0");
                calculator_fsm.restart();
                pendingOperator = null;
                pendingArgument = null;
                text.setText(calculator_fsm.getDisplayedText());
            }
        });
        Button button_backspace = (Button)findViewById(R.id.button_backspace);
        button_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calculator_fsm.transit("backspace")) {
                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }
                text.setText(calculator_fsm.getDisplayedText());
            }
        });

        addBinaryOperator(R.id.button_plus, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a + b;
            }
        });
        addBinaryOperator(R.id.button_minus, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a - b;
            }
        });
        addBinaryOperator(R.id.button_mul, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a * b;
            }
        });
        addBinaryOperator(R.id.button_div, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a / b;
            }
        });

        Button buttonEquals = (Button)findViewById(R.id.button_equals);
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingOperator != null) {
                    Double b = Double.parseDouble(calculator_fsm.getDisplayedText());
                    Double res = pendingOperator.apply(pendingArgument, b);
                    pendingOperator = null;
                    calculator_fsm.setDisplayedText(res.toString());
                    text.setText(calculator_fsm.getDisplayedText());
                }
            }
        });
    }

    private BinaryOperator pendingOperator;
    private Double pendingArgument;
}
