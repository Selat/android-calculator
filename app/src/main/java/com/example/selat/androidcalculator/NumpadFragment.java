package com.example.selat.androidcalculator;

import android.app.Fragment;
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
                    mTextLineAccessor.setText(mTextLineAccessor.getText() + action);
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
                    mTextLineAccessor.setText(mTextLineAccessor.getText().subSequence(0,
                            mTextLineAccessor.getText().length() - 1) + op_name);
                } else {
                    mTextLineAccessor.append(op_name);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.small_calculator, container, false);
        mTextLineAccessor = (TextLineAccessor)getActivity();
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
        Button button_dot = (Button)view.findViewById(R.id.button_dot);
        button_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextLineAccessor.isClearable()) {
                    mTextLineAccessor.setText("0.");
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
                mTextLineAccessor.setText("0");
                mTextLineAccessor.setIsClearable(true);
            }
        });
        Button buttonSettings = (Button)view.findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                mTextLineAccessor.updateSettings();
            }
        });
        Button button_backspace = (Button)view.findViewById(R.id.button_backspace);
        button_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextLineAccessor.getText().length() > 1) {
                    mTextLineAccessor.setText(mTextLineAccessor.getText().subSequence(0,
                            mTextLineAccessor.getText().length() - 1));
                } else {
                    mTextLineAccessor.setText("0");
                    mTextLineAccessor.setIsClearable(true);
                }
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
