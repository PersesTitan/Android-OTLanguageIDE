package com.example.android_otlanguageide.setting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.print.ScannerP;
import com.example.android_otlanguageide.activity.variable.Variable;
import com.example.android_otlanguageide.activity.variable.VariableItem;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

public class Setting extends VariableItem {
    protected final ScannerP scannerP = new ScannerP();
    protected final Variable variable = new Variable();

    /**
     * @param SPECIFIED 타입 받아오기
     * @param line 한 줄값 받아오기
     * @return key, value 값을 반환함
     */
    public KeyValueItem setKeyValue(String SPECIFIED, String line) {
        int start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        int end = line.indexOf(":");
        String key = line.substring(start, end).trim();
        if (set.contains(key)) {
            return null;

        }
        String value = line.substring(end+1);
        value = scannerP.start(value);
        return new KeyValueItem(key, value);
    }

    /**
     * @param word key 값을 받아옴
     * @return 변수에 저장된 값을 반환함
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected String checkValue(@NotNull String word) {
        if (BM.containsKey(word)) return BM.get(word) ? "ㅇㅇ" : "ㄴㄴ";
        else if (CM.containsKey(word)) return Objects.requireNonNull(CM.get(word)).toString();
        else if (DM.containsKey(word)) return Objects.requireNonNull(DM.get(word)).toString();
        else if (FM.containsKey(word)) return Objects.requireNonNull(FM.get(word)).toString();
        else if (IM.containsKey(word)) return Objects.requireNonNull(IM.get(word)).toString();
        else if (LM.containsKey(word)) return Objects.requireNonNull(LM.get(word)).toString();
        else return SM.getOrDefault(word, "");
    }
}