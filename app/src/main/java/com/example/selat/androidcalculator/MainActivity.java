package com.example.selat.androidcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity
implements TextLineAccessor {

    @Override
    public boolean isClearable() {
        return isClearable;
    }

    @Override
    public void setIsClearable(boolean value) {
        isClearable = value;
    }

    @Override
    public void evalAndUpdate() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String precision = SP.getString("precision_list", "NA");
        Integer val = Integer.parseInt(precision);
        if (val != null) {
            df.setMaximumFractionDigits(val);
        }
        text.setText(df.format(evalExpression(text.getText().toString())));
    }

    @Override
    public CharSequence getText() {
        return text.getText();
    }

    @Override
    public void setText(CharSequence s) {
        text.setText(s);
    }

    @Override
    public void append(CharSequence s) {
        text.append(s);
    }

    @Override
    public void updateSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void addTextButton(int id, final String name) {
        Button button_digit = (Button)findViewById(id);
        button_digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClearable) {
                    text.setText(name);
                    isClearable = false;
                } else {
                    text.append(name);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isClearable = true;

        DecimalFormatSymbols decimalSeparator = new DecimalFormatSymbols();
        decimalSeparator.setDecimalSeparator('.');
        df = new DecimalFormat("#.##########", decimalSeparator);

        //calculator_fsm = new CalculatorFiniteStateMachine();

        text = (TextView)findViewById(R.id.text_view);
        text.setText("0");

        LinearLayout mainlayout = (LinearLayout)findViewById(R.id.mainlayout);
        if (mainlayout.getTag().equals("landscape")) {
            addTextButton(R.id.button_sin, "sin(");
            addTextButton(R.id.button_cos, "cos(");
            addTextButton(R.id.button_tan, "tan(");
            addTextButton(R.id.button_abs, "abs(");

            addTextButton(R.id.button_leftp, "(");
            addTextButton(R.id.button_pi, "π");
            addTextButton(R.id.button_e, "e");
            addTextButton(R.id.button_inv, "⁻¹");

            addTextButton(R.id.button_rightp, ")");
            addTextButton(R.id.button_ln, "ln(");
            addTextButton(R.id.button_sqrt, "sqrt(");
            addTextButton(R.id.button_xtoy, "^");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("text", text.getText());
        outState.putBoolean("isClearable", isClearable);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        if (inState.getCharSequence("text") != null) {
            text.setText(inState.getCharSequence("text"));
        }
        if (inState.get("isClearable") != null) {
            isClearable = inState.getBoolean("isClearable");
        }
    }

    static {
        System.loadLibrary("expression-parser-jni");
    }
    public native double evalExpression(String s);


    private TextView text;
    DecimalFormat df;
    private boolean isClearable;
}
