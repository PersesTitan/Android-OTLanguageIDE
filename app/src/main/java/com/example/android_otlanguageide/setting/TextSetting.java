package com.example.android_otlanguageide.setting;

import android.content.ClipData;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.example.android_otlanguageide.item.ItemPosition;
import com.example.android_otlanguageide.list.ListInf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TextSetting implements ListInf {
    Set<List<ItemPosition>> sets = new HashSet<>();

    List<ItemPosition> listPrint = new ArrayList<>();
    List<ItemPosition> listBool = new ArrayList<>();
    List<ItemPosition> listVarType = new ArrayList<>();

    public TextSetting() {
        sets.add(listPrint);
        sets.add(listVarType);
        sets.add(listBool);
    }

    public ItemPosition chek(int position1, int position2) {
        int start;
        int end;
        for (List<ItemPosition> list : sets) {
            for (ItemPosition itemPosition : list) {
                start = itemPosition.getStart();
                end = itemPosition.getEnd();
                if (start <= position1 && end >= position1) return itemPosition;
                if (start <= position2 && end >= position2) return itemPosition;
            }
        } return null;
    }

    public void setTotalSpan(Editable editable, String total) {
        editable.setSpan(new ForegroundColorSpan(Color.BLACK),
                0, total.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        getListPrint(total);
        getListVarType(total);
        getBoolSign(total);

//        for (List<ItemPosition> set : sets) {
//            for (ItemPosition list : set)
//                editable.setSpan(new ForegroundColorSpan(getColor(set)),
//                        list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        }

        for (ItemPosition list : listPrint)
            editable.setSpan(new ForegroundColorSpan(getColor(listPrint)),
                    list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        for (ItemPosition list : listBool)
            editable.setSpan(new ForegroundColorSpan(getColor(listBool)),
                    list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        for (ItemPosition list : listVarType)
            editable.setSpan(new ForegroundColorSpan(getColor(listVarType)),
                    list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    //파일 전체 읽고 측정
    public SpannableStringBuilder setTotalSpan(String total) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(total);
        getListPrint(total);
        getListVarType(total);
        getBoolSign(total);

        for (List<ItemPosition> set : sets) {
            for (ItemPosition list : set)
                spannable.setSpan(new ForegroundColorSpan(getColor(set)),
                        list.getStart(), list.getEnd(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannable;
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

    /**
     * @param list 무슨 리스트인지 판단함
     * @return 색상을 반환
     */
    //색상을 정하는 메소드
    private int getColor(List<ItemPosition> list) {
        if (list.equals(listPrint)) return Color.parseColor(printColor);
        else if (list.equals(listVarType)) return Color.parseColor(varTypeColor);
        else if (list.equals(listBool)) return Color.parseColor(boolColor);
        else return Color.BLACK;
    }
}
