package com.example.android_otlanguageide.activity.variable;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableItem {
    public ActivityMainBinding binding;

    public void setBinding(ActivityMainBinding binding) {
        this.binding = binding;
    }

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
}
