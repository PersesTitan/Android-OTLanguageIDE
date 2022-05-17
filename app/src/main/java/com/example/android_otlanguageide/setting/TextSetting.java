package com.example.android_otlanguageide.setting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.example.android_otlanguageide.R;
import com.example.android_otlanguageide.item.ItemPosition;

import java.util.ArrayList;
import java.util.List;

public class TextSetting {
    private final String print = "ㅅㅁㅅ";
    private final String println = "ㅆㅁㅆ";
    private final String scanner = "ㅅㅇㅅ";
    private final String blueColor = "#039BE5";

    public SpannableStringBuilder setTotalSpan(String total, List<ItemPosition> lists) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(total);
        for (ItemPosition list : lists) {
            spannable.setSpan(
                    new ForegroundColorSpan(Color.parseColor(blueColor)),
                    list.getStart(), list.getEnd(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } return spannable;
    }

    public List<ItemPosition> getList(String total) {
        List<ItemPosition> list = new ArrayList<>();
        addList(list, print, total);
        addList(list, println, total);
        addList(list, scanner, total);
        return list;
    }

    private void addList(List<ItemPosition> list, String value, String total) {
        int count = 0;
        int position;
        while ((position = total.indexOf(value, count)) != -1) {
            int end = position + value.length();
            list.add(new ItemPosition(position, end));
            count++;
        }
    }
}
