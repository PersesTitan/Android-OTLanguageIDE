package com.example.android_otlanguageide.setting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.android_otlanguageide.activity.item.VarType;

import org.jetbrains.annotations.NotNull;

@RequiresApi(api = Build.VERSION_CODES.N)
public class VarCheck {

    public boolean check(@NotNull String line,@NotNull VarType varType) {
        if (varType.equals(VarType.Boolean)) return isBoolean(line);
        else if (varType.equals(VarType.Character)) return isCharacter(line);
        else if (varType.equals(VarType.Double)) return isDouble(line);
        else if (varType.equals(VarType.Float)) return isFloat(line);
        else if (varType.equals(VarType.Integer)) return isInteger(line);
        else if (varType.equals(VarType.Long)) return isLong(line);
        else return varType.equals(VarType.String);
    }

    private boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isBoolean(String text) {
        if (text.isEmpty()) return false;
        return text.equals("false") || text.equals("true");
    }

    private boolean isCharacter(String text) {
        try {
            return text.length() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFloat(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLong(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
