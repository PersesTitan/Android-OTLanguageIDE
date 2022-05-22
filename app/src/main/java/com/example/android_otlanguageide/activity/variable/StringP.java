package com.example.android_otlanguageide.activity.variable;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class StringP extends Setting implements Check, VariableWork {
    private static final String SPECIFIED = "ㅇㅁㅇ";
    private final String patternText = "\\n\\s*ㅇㅁㅇ\\s|^\\s*ㅇㅁㅇ\\s";
    private final Pattern pattern = Pattern.compile(patternText);

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    /**
     * @param line 라인 값 받아 오기
     * @throws Exception 변수를 찾을 수 없을 시 에러 발생
     */
    @Override
    public void start(String line) {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        if (Setting.check(keyValue, errorMessage)) {
            String key = keyValue.getKey();
            String value = keyValue.getValue();
            SM.put(key, value);
            set.add(key);
        }
    }
}