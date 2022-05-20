package com.example.android_otlanguageide.activity.thread.inputWork;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class InputWork extends Setting {
    public synchronized void startA() {
        Log.d(TAG, "startA: start");


        Log.d(TAG, "startA: finish");
    }

    public synchronized void startB() {
        Log.d(TAG, "startB: start");

        Log.d(TAG, "startB: finish");
    }
}
