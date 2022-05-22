package com.example.android_otlanguageide.activity.variable;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DoubleP extends Setting implements Check, VariableWork {
    private static final String SPECIFIED = "ㅇㅆㅇ";
    private final String patternText = "\\n\\s*ㅇㅆㅇ\\s|^\\s*ㅇㄱㅇ\\s";
    private final Pattern pattern = Pattern.compile(patternText);

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void start(String line) {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        if(Setting.check(keyValue, errorMessage)) {
            String key = keyValue.getKey();
            String value = keyValue.getValue();
            DM.put(key, Double.valueOf(value));
            set.add(key);
        }
    }
}
