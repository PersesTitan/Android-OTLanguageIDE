package com.example.android_otlanguageide.activity.print;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_otlanguageide.MainActivity;
import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.TextSetting;

import java.util.Scanner;

public class ScannerP implements Check {
    public volatile boolean checkBool = false;
    protected final ActivityMainBinding binding;
    private final TextSetting textSetting = new TextSetting();
    private static final String SPECIFIED = "ㅅㅇㅅ";
    final ThreadItem threadItem = new ThreadItem();

    public ScannerP(ActivityMainBinding binding) {
        this.binding = binding;
    }

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
        checkBool = true;
        var editText = binding.input;
        editText.setText(null);
        editText.setVisibility(View.VISIBLE);
        editText.setOnEditorActionListener(new DoneOnEditorActionListener());

        threadItem.start();

        System.out.println("ScannerP.start");

        line = line.replaceFirst(SPECIFIED, textSetting.getText(binding.input));
        if (check(line)) return start(line);
        else return line;
    }

    private class ThreadItem extends Thread {
        @Override
        public void run() {
            synchronized(this){
                while (checkBool) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) { }
                }
            }
        }
    }

    private class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                var imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                checkBool = false;
                return true;
            }
            checkBool = true;
            return false;
        }
    }
}