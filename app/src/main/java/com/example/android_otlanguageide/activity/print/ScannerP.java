package com.example.android_otlanguageide.activity.print;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_otlanguageide.activity.item.Check;

import java.util.Scanner;

public class ScannerP implements Check {

    private static final String SPECIFIED = "ㅅㅇㅅ";
    private final EditText editText;
    private boolean checkBool = false;

    public ScannerP(EditText editText) {
        this.editText = editText;
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
     * @param line 라인 받아 오기
     * @return scanner 가 존재하지 않을때까지 받아오기
     */
    public String start(String line) {
        //확인 버튼 클릭시
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        var onEditorActionListener = new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean bool = keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                bool = bool || i == EditorInfo.IME_ACTION_DONE;
                checkBool = bool;
                return bool;
            }
        };
        editText.setOnEditorActionListener(onEditorActionListener);


        Scanner scanner = new Scanner(System.in);
        if (check(line)) {
            line = line.replaceFirst(SPECIFIED, scanner.next());
            return start(line);
        } else return line;
    }



}