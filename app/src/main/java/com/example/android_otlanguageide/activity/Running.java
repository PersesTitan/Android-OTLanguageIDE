package com.example.android_otlanguageide.activity;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.MainActivity;
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
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.TextSetting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Running {
    ActivityMainBinding binding;
    protected final BooleanP booleanP = new BooleanP();
    protected final CharacterP characterP = new CharacterP();
    protected final DoubleP doubleP = new DoubleP();
    protected final FloatP floatP = new FloatP();
    protected final IntegerP integerP = new IntegerP();
    protected final LongP longP = new LongP();
    protected final StringP stringP = new StringP();

    protected final Print print = new Print();
    protected final Println println = new Println();
    protected ScannerP scannerP;

    private final MainActivity mainActivity = new MainActivity();
    private final TextSetting textSetting = new TextSetting();
    private boolean check = false;
    StringBuilder stringBuilder;

    public Running(ActivityMainBinding binding) {
        this.binding = binding;
        scannerP = new ScannerP(binding);
    }

    public void start() {
        check = true;
        stringBuilder = new StringBuilder();
        //입력 되있는 코드 읽기
        var lines = textSetting.getText(binding.content).split("\\n");
        for (String line : lines) {
            //play 가 보일때 동작 중지
            if (binding.play.getVisibility() == View.VISIBLE) break;
            if (scannerP.check(line)) {
                ThreadStart threadStart = new ThreadStart(line);
                threadStart.start();
                synchronized(threadStart){
                    try{
                        threadStart.wait();
                    }catch(InterruptedException ignored){ }
                    Log.d(TAG, "루프 끝남");
                    work(line, stringBuilder);
                }
            } else work(line, stringBuilder);
        }

        binding.result.setText(stringBuilder.toString());
    }

    private void work(String line, StringBuilder stringBuilder) {
        if (print.check(line)) print.start(line, stringBuilder);
        else if (println.check(line)) println.start(line, stringBuilder);
    }

    public class ThreadStart extends Thread{
        String line;
        public ThreadStart(String line) {
            this.line = line;
        }

        @Override
        public void run(){
            synchronized(this){
                line = scannerP.start(line);
                if (scannerP.checkBool) {
                    while (scannerP.checkBool) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) { }
                    }
                }
                notify();
            }
        }
    }
}
