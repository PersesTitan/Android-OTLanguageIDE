package com.example.android_otlanguageide.activity.print;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.PrintWork;
import com.example.android_otlanguageide.setting.Setting;

import java.util.List;

public class Println extends Setting implements Check, PrintWork {
    private static final String SPECIFIED = "ㅆㅁㅆ";

    @Override
    public boolean check(String line) {
        return line.trim().startsWith(SPECIFIED);
    }

    /**
     * @param line 1줄 받아오기
     * @param id 출력될 값 가져오기
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start(String line, StringBuilder id) {
        /* -- ㅆㅁㅆ 제거 -- */
        int start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        line = line.substring(start);
        if (variable.check(line)) {
            List<String> lists = variable.getVar(line);
            for (String list : lists)
                line = line.replaceFirst(list, checkValue(list));
        } id.append(line).append("\n");
    }
}
