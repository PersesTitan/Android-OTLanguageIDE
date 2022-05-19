package com.example.android_otlanguageide.activity.print;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.PrintWork;

public class Print implements Check, PrintWork {

    private static final String SPECIFIED = "ㅅㅁㅅ";


    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    @Override
    public void start(String line) {

    }
}
