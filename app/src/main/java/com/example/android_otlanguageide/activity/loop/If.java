package com.example.android_otlanguageide.activity.loop;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class If implements Check {

    @Override
    public boolean check(String line) {
        return false;
    }
}
