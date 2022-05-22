package com.example.android_otlanguageide.activity.print;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.PrintWork;
import com.example.android_otlanguageide.setting.Setting;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Println extends Setting implements Check, PrintWork {
    private static final String SPECIFIED = "ㅆㅁㅆ";
    private final String patternText = "\\n\\s*ㅆㅁㅆ\\s|^\\s*ㅆㅁㅆ\\s";
    private final Pattern pattern = Pattern.compile(patternText);

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    /**
     * <p>줄바꿈이 들어가는 출력</p>
     * @param line 1줄 받아오기
     * @param id 출력될 값 가져오기
     */
    @Override
    public void start(String line, StringBuilder id) {
        /* --- ㅆㅁㅆ 제거 --- */
        int start;
        if (line.startsWith(SPECIFIED + " "))
            start = line.indexOf(SPECIFIED)+SPECIFIED.length()+1;
        else start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        line = line.substring(start);
        id.append(line).append("\n");
    }
}
