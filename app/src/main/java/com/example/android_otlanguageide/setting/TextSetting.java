package com.example.android_otlanguageide.setting;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.example.android_otlanguageide.item.ItemPosition;
import com.example.android_otlanguageide.list.ListInf;

import java.util.ArrayList;
import java.util.List;

public class TextSetting implements ListInf {
    private final List<ItemPosition> listPrint = new ArrayList<>();
    private final List<ItemPosition> listBool = new ArrayList<>();
    private final List<ItemPosition> listVarType = new ArrayList<>();

    private final ForegroundColorSpan colorPrint
            = new ForegroundColorSpan(Color.parseColor(printColor));
    private final ForegroundColorSpan colorBool
            = new ForegroundColorSpan(Color.parseColor(boolColor));
    private final ForegroundColorSpan colorVar
            = new ForegroundColorSpan(Color.parseColor(varColor));

    public void setTotalSpan(Editable editable, String total) {
        editable.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, total.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        getListPrint(total);
        getListVarType(total);
        getBoolSign(total);

        for (ItemPosition list : listPrint)
            editable.setSpan(colorPrint, list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        for (ItemPosition list : listBool)
            editable.setSpan(colorBool, list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        for (ItemPosition list : listVarType)
            editable.setSpan(colorVar, list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private void getBoolSign(String total) {
        listBool.clear();
        addList(listBool, boolTrue, total);
        addList(listBool, boolFalse, total);
        addList(listBool, boolOr, total);
        addList(listBool, boolAnd, total);
        addList(listBool, singFor, total);
        addList(listBool, singIf, total);
    }

    /**
     * @param total 모든 문장
     * @return 저장된 값 리턴
     */
    private void getListPrint(String total) {
        listPrint.clear();
        addList(listPrint, print, total);
        addList(listPrint, println, total);
        addList(listPrint, scanner, total);
    }

    private void getListVarType(String total) {
        listVarType.clear();
        addList(listVarType, integerP, total);
        addList(listVarType, longP, total);
        addList(listVarType, booleanP, total);
        addList(listVarType, stringP, total);
        addList(listVarType, charP, total);
        addList(listVarType, floatP, total);
        addList(listVarType, doubleP, total);
    }

    /**
     * @param list 저장할 리스트
     * @param value 색이 칠해질 값
     * @param total 모든 문장
     */
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
