package com.example.android_otlanguageide.activity.print;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.work.PrintWork;
import com.example.android_otlanguageide.setting.Setting;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Print extends Setting implements Check, PrintWork {
    private static final String SPECIFIED = "ㅅㅁㅅ";
    private final String patternText = "\\n\\s*ㅅㅁㅅ\\s|^\\s*ㅅㅁㅅ\\s";
    private final Pattern pattern = Pattern.compile(patternText);

    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    @Override
    public void start(String line, StringBuilder id) {
        /* --ㅅㅁㅅ 제거-- */
        int start;
        if (line.startsWith(SPECIFIED + " "))
            start = line.indexOf(SPECIFIED)+SPECIFIED.length()+1;
        else start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        line = line.substring(start).trim();
        id.append(line);
    }
}
