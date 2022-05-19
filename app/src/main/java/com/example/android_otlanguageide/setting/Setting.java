package com.example.android_otlanguageide.setting;

import android.widget.EditText;

import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.print.ScannerP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Setting {
    EditText editText;
    ScannerP scannerP;

    /*===========================================*/
    //n번째 위치와 라인의 값을 저장하는 곳
    //변수 이름 저장하는 장소
    public static final Map<Integer, String> idLine = new HashMap<>();
    public static final Set<String> set = new HashSet<>();
    public static final Map<String, Integer> IM = new HashMap<>();
    public static final Map<String, Long> LM = new HashMap<>();
    public static final Map<String, Boolean> BM = new HashMap<>();
    public static final Map<String, String> SM = new HashMap<>();
    public static final Map<String, Character> CM = new HashMap<>();
    public static final Map<String, Float> FM = new HashMap<>();
    public static final Map<String, Double> DM = new HashMap<>();

    public Setting(EditText editText) {
        this.editText = editText;
        scannerP = new ScannerP(editText);
    }

    /**
     * @param SPECIFIED 타입 받아오기
     * @param line 한 줄값 받아오기
     * @return key, value 값을 반환함
     * @throws Exception 변수가 이미 존재하면 에러 반환함
     */
    public KeyValueItem setKeyValue(String SPECIFIED, String line) throws Exception {
        int start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        int end = line.indexOf(":");
        String key = line.substring(start, end).trim();
        if (set.contains(key)) throw new Exception("이미 존재하는 변수 이름 입니다.");
        String value = line.substring(end+1);
        value = scannerP.start(value);
        return new KeyValueItem(key, value);
    }

}
