package com.example.selat.androidcalculator;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NumpadFragment extends Fragment {

    private void addDigitButtonListener(int id, final String action) {
        Button button_digit = (Button)view.findViewById(id);
        button_digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextLineAccessor.isClearable()) {
                    mTextLineAccessor.setText(action);
                } else {
                    mTextLineAccessor.append(action);
                }
                mTextLineAccessor.setIsClearable(false);
            }
        });
    }


    private static boolean finishesWithOperator(CharSequence s) {
        return s.charAt(s.length() - 1) == '*' ||
                s.charAt(s.length() - 1) == '/' ||
                s.charAt(s.length() - 1) == '+' ||
                s.charAt(s.length() - 1) == '-';
    }

    private void addBinaryOperator(int id, final BinaryOperator operator, final String op_name) {
        Button button_operator = (Button)view.findViewById(id);
        button_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextLineAccessor.setIsClearable(false);
                if (finishesWithOperator(mTextLineAccessor.getText())) {
                    mTextLineAccessor.popBack();
                }
                mTextLineAccessor.append(op_name);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.small_calculator, container, false);
        mTextLineAccessor = (TextLineAccessor)getActivity();
        Button button_0 = (Button)view.findViewById(R.id.button_digit_0);
        button_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = mTextLineAccessor.getText();
                int len = text.length();
                if (mTextLineAccessor.isClearable()) {
                    mTextLineAccessor.clearData();
                } else {
                    int i = len - 1;
                    while (i >= 0 && text.charAt(i) == '0') {
                        --i;
                    }
                    if (i != -1 && (i == len - 1 || Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
                        mTextLineAccessor.append("0");
                    }
                }
                mTextLineAccessor.setIsClearable(false);
            }
        });
        addDigitButtonListener(R.id.button_digit_1, "1");
        addDigitButtonListener(R.id.button_digit_2, "2");
        addDigitButtonListener(R.id.button_digit_3, "3");
        addDigitButtonListener(R.id.button_digit_4, "4");
        addDigitButtonListener(R.id.button_digit_5, "5");
        addDigitButtonListener(R.id.button_digit_6, "6");
        addDigitButtonListener(R.id.button_digit_7, "7");
        addDigitButtonListener(R.id.button_digit_8, "8");
        addDigitButtonListener(R.id.button_digit_9, "9");
        Button button_dot = (Button)view.findViewById(R.id.button_dot);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextLineAccessor.isClearable()) {
                    mTextLineAccessor.clearData();
                    mTextLineAccessor.append(".");
                    mTextLineAccessor.setIsClearable(false);
                } else if (Character.isDigit(mTextLineAccessor.getText().charAt(
                        mTextLineAccessor.getText().length() - 1))) {
                    CharSequence s = mTextLineAccessor.getText();
                    int i = s.length() - 1;
                    while (i >= 0 && Character.isDigit(s.charAt(i))) --i;
                    if (i < 0 || s.charAt(i) != '.') {
                        mTextLineAccessor.append(".");
                    }
                }
            }
        });
        Button buttonClear = (Button)view.findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextLineAccessor.clearData();
            }
        });
        Button buttonSettings = (Button)view.findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextLineAccessor.updateSettings();
            }
        });
        Button buttonHelp = (Button)view.findViewById(R.id.button_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Selat/android-calculator"));
                intent.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
                getActivity().startActivity(intent);
            }
        });
        Button button_backspace = (Button)view.findViewById(R.id.button_backspace);
        button_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextLineAccessor.popBack();
            }
        });

        addBinaryOperator(R.id.button_plus, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a + b;
            }
        }, "+");
        addBinaryOperator(R.id.button_minus, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a - b;
            }
        }, "-");
        addBinaryOperator(R.id.button_mul, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a * b;
            }
        }, "*");
        addBinaryOperator(R.id.button_div, new BinaryOperator() {
            @Override
            public Double apply(Double a, Double b) {
                return a / b;
            }
        }, "/");

        Button buttonEquals = (Button)view.findViewById(R.id.button_equals);
        buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextLineAccessor.evalAndUpdate();
                mTextLineAccessor.setIsClearable(true);
            }
        });
        return view;
    }

    private View view;
    private TextLineAccessor mTextLineAccessor;
}
