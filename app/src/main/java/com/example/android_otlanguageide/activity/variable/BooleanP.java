package com.example.android_otlanguageide.activity.variable;

import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.item.VariableWork;
import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class BooleanP extends Setting implements Check, VariableWork {
    private static final String SPECIFIED = "ㅇㅂㅇ";

    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    @Override
    public void start(String line) {
        KeyValueItem keyValue = setKeyValue(SPECIFIED, line);
        if (Setting.check(keyValue, errorMessage)) {
            String key = keyValue.getKey();
            String value = keyValue.getValue();
            value = value.replace("ㅇㅇ", "true");
            value = value.replace("ㄴㄴ", "false");
            value = value.replace(" ", "");
            BM.put(key, change(value));
            set.add(key);
        }
    }

    /**
     * @param line boolean 식을 받은 뒤 값을 계산하는 식입니다.
     * @return bool 계산 후 반환하는 값
     */
    private boolean change(String line) {
        if (line.equals("true") || line.equals("false")) return Boolean.parseBoolean(line);
        else {
            String[] sign = line.split("false|true");
            String[] boolS = line.split("[ㄲㄸ]");
            assert sign.length+1 == boolS.length;
            boolean bool = Boolean.parseBoolean(boolS[0]);
            for (int i = 0; i<sign.length; i++) {
                if (sign[i].equals("ㄲ")) bool = bool && Boolean.parseBoolean(sign[i+1]);
                else bool = bool || Boolean.parseBoolean(sign[i+1]);
            } return bool;
        }
    }
}