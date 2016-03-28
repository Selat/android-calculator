package com.example.selat.androidcalculator;

public interface TextLineAccessor {
    boolean isClearable();
    void setIsClearable(boolean value);
    CharSequence getText();
    void updateSettings();
    void setText(CharSequence s);
    void append(CharSequence s);
    void evalAndUpdate();
}
