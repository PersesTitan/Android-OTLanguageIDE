package com.example.android_otlanguageide.setting;

import android.widget.EditText;
import android.widget.TextView;

public class Setting {
    public String getText(TextView textView) {
        try {
            return textView.getText().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getText(EditText editText) {
        try {
            return editText.getText().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
