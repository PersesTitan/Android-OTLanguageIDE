package com.example.android_otlanguageide.activity;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.setting.Setting;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Running extends Setting {
    protected boolean startCheck = false;

    public void start() {
        startCheck = true;
        clearVar();
        var lines = binding.content.getText().toString().split("\\n");
        runOnUiThread(() -> {
            binding.stop.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.GONE);
        });
        //동작을 실해아는 쓰레드
        new Thread(() -> {
            for (String line : lines) {
                if (binding.stop.getVisibility() == View.GONE) break;
                if (scannerP.check(line)) {
                    line = scannerP.start(line);
                    while (scannerCheck) {
//                        if (binding.stop.getVisibility() != View.GONE) break loop;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {}
                    }
                }
                work(line);
            }

            runOnUiThread(() -> {
                binding.result.setText(totalStringBuilder.toString());
                if (binding.stop.getVisibility() == View.VISIBLE) {
                    binding.play.setVisibility(View.VISIBLE);
                    binding.stop.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private static final class InputThread extends Thread implements Runnable {
        @Override
        public void run() {
            // 입력이 끝나거나 stop 이 보일때만 동작함
            while (scannerCheck && binding.stop.getVisibility() == View.VISIBLE) {
                try {
                    sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void work(String line) {
        if (line != null && !line.isEmpty()) {
            if (variable.check(line)) line = variable.getVar(line);

            if (print.check(line)) print.start(line, totalStringBuilder);
            else if (println.check(line)) println.start(line, totalStringBuilder);
            else if (booleanP.check(line)) booleanP.start(line);
            else if (characterP.check(line)) characterP.start(line);
            else if (doubleP.check(line)) doubleP.start(line);
            else if (floatP.check(line)) floatP.start(line);
            else if (integerP.check(line)) integerP.start(line);
            else if (longP.check(line)) longP.start(line);
            else if (stringP.check(line)) stringP.start(line);
        }

    }
}
