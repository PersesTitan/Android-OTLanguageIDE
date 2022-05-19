package com.example.android_otlanguageide.setting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.KeyValueItem;
import com.example.android_otlanguageide.activity.print.ScannerP;
import com.example.android_otlanguageide.activity.variable.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

public class Setting {
    protected final ScannerP scannerP = new ScannerP();
    protected final Variable variable = new Variable();

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

    /**
     * @param word key 값을 받아옴
     * @return 변수에 저장된 값을 반환함
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected String checkValue(@NotNull String word) {
        if (BM.containsKey(word)) return BM.get(word) ? "ㅇㅇ" : "ㄴㄴ";
        else if (CM.containsKey(word)) return CM.get(word).toString();
        else if (DM.containsKey(word)) return DM.get(word).toString();
        else if (FM.containsKey(word)) return FM.get(word).toString();
        else if (IM.containsKey(word)) return IM.get(word).toString();
        else if (LM.containsKey(word)) return LM.get(word).toString();
        else return SM.getOrDefault(word, "");
    }
}