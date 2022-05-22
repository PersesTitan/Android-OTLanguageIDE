package com.example.android_otlanguageide.activity.print;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.setting.Setting;

import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ScannerP extends Setting implements Check {
    private static final String SPECIFIED = "ㅅㅇㅅ";
    private final String patternText = "\\s(ㅅㅇㅅ)\\s";
    private final Pattern pattern = Pattern.compile(patternText);

    /**
     * ex) ㅇㅅㅇ 11:ㅅㅇㅅ
     * @param line 1줄 받아오기
     * @return 만약 ㅅㅇㅅ 을 포함하면
     */
    @Override
    public boolean check(String line) {
        return pattern.matcher(line).find();
    }

    /**
     * @param line 1줄 받아오기
     * @return 입력한 값 받아오기
     */
    public String start(String line) {
        scannerCheck = true;
        runOnUiThread(() -> {
            binding.input.setText(null);
            binding.inputLayout.setVisibility(View.VISIBLE);
        });

        while (scannerCheck) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        line = line.replaceFirst(SPECIFIED, binding.input.getText().toString());
        if (check(line)) return start(line);
        else {
            runOnUiThread(() -> binding.inputLayout.setVisibility(View.GONE));
            return line;
        }
    }
}