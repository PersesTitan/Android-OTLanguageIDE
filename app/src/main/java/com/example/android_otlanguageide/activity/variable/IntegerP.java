package com.example.android_otlanguageide.activity.variable;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IntegerP extends Setting implements Check, VariableWork {

    private static final String SPECIFIED = "ㅇㅈㅇ";

    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    @Override
    public void start(String line) throws Exception {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        String key = keyValue.getKey();
        String value = keyValue.getValue();
        IM.put(key, Integer.valueOf(value));
        set.add(key);
    }
}
