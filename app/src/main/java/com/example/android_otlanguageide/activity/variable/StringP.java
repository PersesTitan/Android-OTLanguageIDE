package com.example.android_otlanguageide.activity.variable;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

public class StringP extends Setting implements Check, VariableWork {

    private static final String SPECIFIED = "ㅇㅁㅇ";

    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    /**
     * @param line 라인 값 받아 오기
     * @throws Exception 변수를 찾을 수 없을 시 에러 발생
     */
    @Override
    public void start(String line) throws Exception {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        String key = keyValue.getKey();
        String value = keyValue.getValue();
        SM.put(key, value);
        set.add(key);
    }
}