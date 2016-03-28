package com.example.selat.androidcalculator;

/**
 * Created by selat on 3/28/16.
 */
public interface TextLineAccessor {
    boolean isClearable();
    void setIsClearable(boolean value);
    CharSequence getText();
    void setText(CharSequence s);
    void append(CharSequence s);
    void evalAndUpdate();
}
