package com.example.android_otlanguageide.activity.variable;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Variable extends Setting implements Check {
    private final String text = ":([ㄱ-ㅎㅏ-ㅣ가-힣]|\\w)\\b";
    private final Pattern pattern = Pattern.compile(text);

    /**
     * 변수 :[변수명][공백] 을 변수 값으로 대체함
     * @param line 한줄을 받아옴
     */
    public String getVar(String line) {
        int count = 0;
        int begin;
        while((begin = line.indexOf(":", count)) != -1) {
            String copyLine = line.substring(begin);
            int end = copyLine.indexOf(" ");
            String key = copyLine.substring(1, end);
            if (set.contains(key)) {
                String value = ":"+key+" ";
                line = line.replaceFirst(value, checkValue(key));
            }
            count++;
        }
        return line;
    }

    /**
     * @param line 첫 번째 줄 받아오기
     * @return 변수가 존재시 값을 받아옴
     */
    @Override
    public boolean check(String line) {
        Matcher matcher = pattern.matcher(line);
        boolean bool = matcher.find();

        Log.d(TAG, "check: =======================================");
        Log.d(TAG, "check: " + line);
        Log.d(TAG, "check: " + bool);

        var lines = line.split(" ");
        bool = bool && new ArrayList<>(Arrays.asList(lines)).stream()
                .filter(v -> !v.isEmpty())
                .anyMatch(v -> v.startsWith(":"));
        Log.d(TAG, "line: " + line + " check: " + bool);
        return bool;
    }
}
