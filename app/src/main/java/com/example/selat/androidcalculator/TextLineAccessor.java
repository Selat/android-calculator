package com.example.selat.androidcalculator;

public interface TextLineAccessor {
    boolean isClearable();
    void setIsClearable(boolean value);
    CharSequence getText();
    void updateSettings();
    void popBack();
    void setText(CharSequence t);
    void clearData();
    void append(CharSequence s);
    void evalAndUpdate();
}
