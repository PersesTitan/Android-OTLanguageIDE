package com.example.android_otlanguageide.activity.item;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.Running;
import com.example.android_otlanguageide.activity.print.Print;
import com.example.android_otlanguageide.activity.print.Println;
import com.example.android_otlanguageide.activity.print.ScannerP;
import com.example.android_otlanguageide.activity.variable.BooleanP;
import com.example.android_otlanguageide.activity.variable.CharacterP;
import com.example.android_otlanguageide.activity.variable.DoubleP;
import com.example.android_otlanguageide.activity.variable.FloatP;
import com.example.android_otlanguageide.activity.variable.IntegerP;
import com.example.android_otlanguageide.activity.variable.LongP;
import com.example.android_otlanguageide.activity.variable.StringP;
import com.example.android_otlanguageide.activity.variable.Variable;
import com.example.android_otlanguageide.setting.TextSetting;

@RequiresApi(api = Build.VERSION_CODES.N)
public interface Check {
    boolean check(String line);

    BooleanP booleanP = new BooleanP();
    CharacterP characterP = new CharacterP();
    DoubleP doubleP = new DoubleP();
    FloatP floatP = new FloatP();
    IntegerP integerP = new IntegerP();
    LongP longP = new LongP();
    StringP stringP = new StringP();

    Running running = new Running();
    TextSetting textSetting = new TextSetting();
    Print print = new Print();
    Println println = new Println();
    ScannerP scannerP = new ScannerP();
    Variable variable = new Variable();

}
