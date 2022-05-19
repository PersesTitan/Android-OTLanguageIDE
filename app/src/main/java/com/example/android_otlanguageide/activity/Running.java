package com.example.android_otlanguageide.activity;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.print.Print;
import com.example.android_otlanguageide.activity.print.Println;
import com.example.android_otlanguageide.activity.variable.BooleanP;
import com.example.android_otlanguageide.activity.variable.CharacterP;
import com.example.android_otlanguageide.activity.variable.DoubleP;
import com.example.android_otlanguageide.activity.variable.FloatP;
import com.example.android_otlanguageide.activity.variable.IntegerP;
import com.example.android_otlanguageide.activity.variable.LongP;
import com.example.android_otlanguageide.activity.variable.StringP;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.TextSetting;

public class Running {
    protected final BooleanP booleanP = new BooleanP();
    protected final CharacterP characterP = new CharacterP();
    protected final DoubleP doubleP = new DoubleP();
    protected final FloatP floatP = new FloatP();
    protected final IntegerP integerP = new IntegerP();
    protected final LongP longP = new LongP();
    protected final StringP stringP = new StringP();

    protected final Print print = new Print();
    protected final Println println = new Println();

    private final TextSetting textSetting = new TextSetting();
    private boolean check = false;
    final ActivityMainBinding binding;
    StringBuilder stringBuilder;


    public Running(ActivityMainBinding binding) {
        this.binding = binding;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start() {
        check = true;
        stringBuilder = new StringBuilder();
        //입력 되있는 코드 읽기
        var lines = textSetting.getText(binding.content).split("\\n");
        for (String line : lines) {
            if (!check) break;
            if (print.check(line)) print.start(line, stringBuilder);
            else if (println.check(line)) println.start(line, stringBuilder);
        }

        binding.result.setText(stringBuilder.toString());
    }

    public void stop() {
        check = false;
    }
}
