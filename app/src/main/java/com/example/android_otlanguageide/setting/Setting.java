package com.example.android_otlanguageide.setting;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.MainActivity;
import com.example.android_otlanguageide.activity.Running;
import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.activity.item.KeyValueItem;
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
import com.example.android_otlanguageide.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Setting extends MainActivity implements Check {
    public static final ForegroundColorSpan errorColor = new ForegroundColorSpan(Color.RED);
    public static final int errorSpan = Spannable.SPAN_EXCLUSIVE_INCLUSIVE;
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
     */
    public KeyValueItem setKeyValue(String SPECIFIED, String line) {
        int start = line.indexOf(SPECIFIED) + SPECIFIED.length();
        int end = line.indexOf(":");
        String key = line.substring(start, end).trim();
        if (set.contains(key)) return null;
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
        else if (CM.containsKey(word)) return Objects.requireNonNull(CM.get(word)).toString();
        else if (DM.containsKey(word)) return Objects.requireNonNull(DM.get(word)).toString();
        else if (FM.containsKey(word)) return Objects.requireNonNull(FM.get(word)).toString();
        else if (IM.containsKey(word)) return Objects.requireNonNull(IM.get(word)).toString();
        else if (LM.containsKey(word)) return Objects.requireNonNull(LM.get(word)).toString();
        else return SM.getOrDefault(word, "");
    }

    /**
     * @param keyValue KeyValueItem 값을 받아옴 에러일때 null
     * @param message 출력될 에러 메세지
     * @return null 일때 false 반환
     */
    public static boolean check(KeyValueItem keyValue, String message) {
        if (keyValue == null) {
            totalStringBuilder.append(message);
            binding.play.setVisibility(View.VISIBLE);
            binding.stop.setVisibility(View.GONE);
            String total = totalStringBuilder.toString();
            int end = total.lastIndexOf(message);
            int start = end - message.length();
            var builder = new SpannableStringBuilder(message);
            builder.setSpan(errorColor, start, end, errorSpan);
            binding.result.setText(builder);
            return false;
        } return true;
    }
}