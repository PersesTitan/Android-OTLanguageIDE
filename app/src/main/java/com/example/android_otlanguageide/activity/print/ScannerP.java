package com.example.android_otlanguageide.activity.print;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_otlanguageide.MainActivity;
import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.TextSetting;

import java.util.Scanner;

public class ScannerP implements Check {
    private final TextSetting textSetting = new TextSetting();
    protected final ActivityMainBinding binding = MainActivity.binding;
    private boolean checkBool = false;
    private static final String SPECIFIED = "ㅅㅇㅅ";

    /**
     * ex) ㅇㅅㅇ 11:ㅅㅇㅅ
     * @param line 1줄 받아오기
     * @return 만약 ㅅㅇㅅ 을 포함하면
     */
    @Override
    public boolean check(String line) {
        return line.contains(SPECIFIED);
    }

    /**
     * @param line 1줄 받아오기
     * @return 입력한 값 받아오기
     */
    public String start(String line) {
        //확인 버튼 클릭시
        var editText = binding.input;
        editText.setText(null);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(onEditorActionListener);

        ThreadItem threadItem = new ThreadItem();
        threadItem.start();

        synchronized (threadItem) {
            try {
                threadItem.wait();
            } catch (Exception ignored) { }
        }

        line = line.replaceFirst(SPECIFIED, textSetting.getText(editText));
        if (check(line)) return start(line);
        else return line;
    }

    private class ThreadItem extends Thread {
        @Override
        public void run() {
            synchronized(this){
                while (checkBool) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) { }
                }
            }
            notify();
        }
    }

    TextView.OnEditorActionListener onEditorActionListener = (textView, i, keyEvent) -> {
        boolean bool = keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
        bool = bool || i == EditorInfo.IME_ACTION_DONE;
        checkBool = bool;
        return bool;
    };
}