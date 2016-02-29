package com.example.selat.androidcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CalculatorFiniteStateMachine calculator_fsm;
    private TextView text;

    private void addDigitButtonListener(int id, final String action) {
        Button button_digit = (Button)findViewById(id);
        button_digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculator_fsm.transit(action);
                text.setText(calculator_fsm.getDisplayed_text());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculator_fsm = new CalculatorFiniteStateMachine();

        text = (TextView)findViewById(R.id.text_view);

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
    }
}
