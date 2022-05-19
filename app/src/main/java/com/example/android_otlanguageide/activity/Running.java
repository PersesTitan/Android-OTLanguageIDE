package com.example.android_otlanguageide.activity;

import android.widget.EditText;
import android.widget.TextView;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.Setting;
import com.example.android_otlanguageide.setting.TextSetting;

public class Running {
    private final TextSetting textSetting = new TextSetting();
    private boolean check = false;
    final ActivityMainBinding binding;
    StringBuilder stringBuilder;

    public Running(ActivityMainBinding binding) {
        this.binding = binding;
    }

    public void start() {
        check = true;
         stringBuilder = new StringBuilder();
         //입력 되있는 코드 읽기
         var lines = textSetting.getText(binding.content).split("\\n");
         for (String line : lines) {
             if (!check) break;

         }
    }

    public void stop() {
        check = false;
    }
}
