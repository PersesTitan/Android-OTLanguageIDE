package com.example.android_otlanguageide.activity.variable;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

public class DoubleP extends Setting implements Check, VariableWork {

    private static final String SPECIFIED = "ㅇㅆㅇ";

    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    @Override
    public void start(String line) throws Exception {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        String key = keyValue.getKey();
        String value = keyValue.getValue();
        DM.put(key, Double.valueOf(value));
        set.add(key);
    }
}
