package com.example.android_otlanguageide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.Setting;
import com.example.android_otlanguageide.setting.TextSetting;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

public class MainActivity extends AppCompatActivity {

    private final Setting setting = new Setting();
    private final TextSetting textSetting = new TextSetting();

    private final String CONTENT = "CONTENT";
    final String shared = "file";
    StringBuilder stringBuilder;
    ActivityMainBinding binding;
    String total;

    @Override
    @SuppressLint("NonConstantResourceId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setColorSpan();
//        binding.content.addTextChangedListener(textWatcher);

        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);
        binding.inputLayout.setVisibility(View.GONE);
        binding.content.setText(null);

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.play:
                    binding.stop.setVisibility(View.VISIBLE);
                    binding.play.setVisibility(View.GONE);
                    total = setting.getText(binding.content);
                    break;

                case R.id.stop:
                    binding.play.setVisibility(View.VISIBLE);
                    binding.stop.setVisibility(View.GONE);
                    binding.inputLayout.setVisibility(View.GONE);
                    break;

                case R.id.thisSave:
                    total = setting.getText(binding.content);
                    editor.putString(CONTENT, total);
                    if (editor.commit()) DynamicToast.makeSuccess(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    else DynamicToast.makeError(this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.thisLoad:
                    total = sharedPreferences.getString(CONTENT, null);
                    binding.content.setText(total);
                    DynamicToast.makeSuccess(this, "값을 불러왔습니다", Toast.LENGTH_SHORT);
                    break;

                case R.id.loadFile:
                    File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
                    System.out.println(extensionFilter(dir));
                    readFile();
                    break;

                case R.id.downloadFile:
                    EditText editText = new EditText(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMarginStart(200);
                    params.setMarginEnd(200);
                    editText.setLayoutParams(params);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("내보내기");
                    dialog.setMessage("파일 이름을 입력해주세요");
                    dialog.setView(editText);
                    dialog.setPositiveButton("확인", (dialogInterface, i) -> {
                        stringBuilder = new StringBuilder(setting.getText(editText));
                        stringBuilder.append(".otl");
                        createFile(stringBuilder.toString(), setting.getText(binding.content));
                    }).setNegativeButton("취소", (dialogInterface, i) -> { });
                    dialog.show();
                    break;

                default:
                    assert false;
                    break;
            }
        };

        binding.play.setOnClickListener(listener);
        binding.stop.setOnClickListener(listener);
        binding.thisSave.setOnClickListener(listener);
        binding.thisLoad.setOnClickListener(listener);
        binding.loadFile.setOnClickListener(listener);
        binding.downloadFile.setOnClickListener(listener);

    }

    private void readFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
        File[] files = dir.listFiles(pathname -> pathname.getName().toLowerCase(Locale.ROOT).endsWith(".otl"));
        System.out.println(files);
    }

    private List<String> extensionFilter(File folder) {
        List<String> result = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) result.addAll(extensionFilter(file));
                else {
                    if (".otl".contains(file.getName().substring(file.getName()
                            .lastIndexOf(".")).toLowerCase(Locale.ROOT))) {
                        result.add(file.toString());
                    }
                }
            }
        } return result;
    }

    /**
     * @param fileName 파일 이름
     * @param total 모든 파일 텍스트
     */
    private void createFile(String fileName, String total) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
            if (!dir.exists() && dir.mkdir()) {
                Toast.makeText(this, "파일이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                File file = new File(dir, fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(total.getBytes(StandardCharsets.UTF_8));
                } catch (IOException ignored) { }
                DynamicToast.makeSuccess(this, "파일이 생성되었습니다.", Toast.LENGTH_SHORT).show();
            } else DynamicToast.makeError(this, "파일 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        } else DynamicToast.makeWarning(this, "권한 허용을 해주세요.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 입력 받을 때마다 색상 변경
     */
    private void setColorSpan() {
        binding.content.addTextChangedListener(new TextWatcher() {
            final String print = "\\b(ㅅㅁㅅ|ㅆㅁㅆ|ㅅㅇㅅ)\\b";
            final String var = "\\b(ㅇㅈㅇ|ㅇㅉㅇ|ㅇㅂㅇ|ㅇㅁㅇ|ㅇㄱㅇ|ㅇㅅㅇ|ㅇㅆㅇ)\\b";
            final String bool = "\\b(ㅇㅇ|ㄴㄴ|ㄸ|ㄲ|\\^\\^|\\?ㅅ\\?)\\b";
            final int printColor = Color.parseColor("#006400"); // 검은색 초록색
            final int varColor = Color.parseColor("#9370DB"); //연보라색
            final int boolColor = Color.parseColor("#FF8C00"); //검은 주황
            final Pattern printPattern = Pattern.compile((print));
            final Pattern varPattern = Pattern.compile((var));
            final Pattern boolPattern = Pattern.compile((bool));
            final ColorScheme printScheme = new ColorScheme(printPattern, printColor);
            final ColorScheme varScheme = new ColorScheme(varPattern, varColor);
            final ColorScheme boolScheme = new ColorScheme(boolPattern, boolColor);
            final ColorScheme[] schemes = {printScheme, varScheme, boolScheme};

            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) {
                removeSpans(editable);
                for (ColorScheme scheme : schemes) {
                    for(Matcher m = scheme.pattern.matcher(editable); m.find();) {
                        editable.setSpan(new ForegroundColorSpan(scheme.color),
                                m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            void removeSpans(Editable e) {
                CharacterStyle[] spans = e.getSpans(0, e.length(),
                        (Class<? extends CharacterStyle>) ForegroundColorSpan.class);
                for (CharacterStyle span : spans) e.removeSpan(span);
            }

            @AllArgsConstructor
            class ColorScheme {
                final Pattern pattern;
                final int color;
            }
        });
    }
}