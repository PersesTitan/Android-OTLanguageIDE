package com.example.android_otlanguageide.activity;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Running extends Setting {
    protected boolean startCheck = false;
    StringBuilder stringBuilder;

    public void start() {
        //startCheck = 시작
        //stringBuilder = 출력 값 저장되있는 곳
        //입력 되있는 코드 읽기
        startCheck = true;
        stringBuilder = new StringBuilder();
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

        Log.d(TAG, "동작 끝남 : 출력");
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
