package com.example.selat.androidcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Stack;

public class MainActivity extends Activity
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

        String precision = SP.getString("precision_list", "5");
        Integer val = Integer.parseInt(precision);
        if (val != null) {
            df.setMaximumFractionDigits(val);
        }
        String s = df.format(evalExpression(text.getText().toString()));
        clearData();
        setText(s.substring(0, 1));
        for (int i = 1; i < s.length(); ++i) {
            append(s.substring(i, i + 1));
        }
    }

    @Override
    public CharSequence getText() {
        return text.getText();
    }

    @Override
    public void clearData() {
        isClearable = true;
        text.setText("0");
        lexem_ids.push(text.length());
    }

    @Override
    public void popBack() {
        if (lexem_ids.size() == 1) {
            clearData();
        } else {
            int l = lexem_ids.pop();
            text.setText(text.getText().subSequence(0, l));
        }
    }

    @Override
    public void append(CharSequence s) {
        lexem_ids.push(text.length());
        text.append(s);
    }

    @Override
    public void setText(CharSequence t) {
        lexem_ids.clear();
        lexem_ids.push(1);
        text.setText(t);
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
                    setText(name);
                    isClearable = false;
                } else {
                    append(name);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lexem_ids = new Stack<Integer>();

        DecimalFormatSymbols decimalSeparator = new DecimalFormatSymbols();
        decimalSeparator.setDecimalSeparator('.');
        df = new DecimalFormat("#.##########", decimalSeparator);
        text = (TextView)findViewById(R.id.text_view);
        clearData();

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

    static Stack<Integer> lexem_ids;
    private TextView text;
    DecimalFormat df;
    private boolean isClearable;
}
