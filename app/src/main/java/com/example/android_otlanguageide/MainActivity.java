package com.example.android_otlanguageide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android_otlanguageide.activity.Running;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.TextSetting;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

public class MainActivity extends AppCompatActivity {

    public static ActivityMainBinding binding;
    private final TextSetting textSetting = new TextSetting();
    private final String CONTENT = "CONTENT";
    final String shared = "file";
    StringBuilder totalStringBuilder;
    StringBuilder stringBuilder;
    String total;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        var running = new Running(binding);

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setColorSpan();
        request();

        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);
        binding.input.setVisibility(View.GONE);
        binding.content.setText(null);

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.play:
                    binding.stop.setVisibility(View.VISIBLE);
                    binding.play.setVisibility(View.GONE);
                    total = textSetting.getText(binding.content);
                    running.start();
                    break;

                case R.id.stop:
                    running.stop();
                    stop();
                    break;

                case R.id.thisSave:
                    total = textSetting.getText(binding.content);
                    editor.putString(CONTENT, total);
                    if (editor.commit()) okToast("저장되었습니다");
                    else errorToast("오류가 발생하였습니다.");
                    break;

                case R.id.thisLoad:
                    total = sharedPreferences.getString(CONTENT, null);
                    binding.content.setText(total);
                    okToast("값을 불러왔습니다");
                    break;

                case R.id.loadFile:
                    File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
                    List<String> listFiles = extensionFilter(dir);
                    readFiles(listFiles);
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
                        String text = textSetting.getText(editText);
                        if (text.isEmpty()) waringToast("파일이름을 입력해주세요.");
                        else {
                            stringBuilder = new StringBuilder(textSetting.getText(editText));
                            stringBuilder.append(".otl");
                            createFile(stringBuilder.toString(), textSetting.getText(binding.content));
                        }
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

    private void stop() {
        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);
        binding.input.setVisibility(View.GONE);
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


    private void readFiles(List<String> list) {
        totalStringBuilder = new StringBuilder();
        var size = list.size();
        String[] items = new String[size];
        for (int i = 0; i < list.size(); i++) {
            var pos = list.get(i).lastIndexOf("/") + 1;
            items[i] = list.get(i).substring(pos);
        }
        var builder = new AlertDialog.Builder(this);
        builder.setTitle("파일을 선택해주세요.");
        builder.setItems(items, (dialogInterface, i) -> {
            String path = list.get(i);
            if (!new File(path).canRead()) errorToast("파일을 읽을 수 없습니다.");
            if (!path.toLowerCase(Locale.ROOT).endsWith(".otl"))
                errorToast("확장자가 일치하지 않습니다.");
            else {
                File file = new File(path);
                try (var br = new BufferedReader(new FileReader(file))){
                    String line;
                    while ((line = br.readLine()) != null)
                        totalStringBuilder.append(line).append("\n");
                    binding.content.setText(totalStringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.create().show();
    }

    /**
     * @param fileName 파일 이름
     * @param total 모든 파일 텍스트
     */
    private void createFile(String fileName, String total) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
            if (!dir.exists()) {
                if (dir.mkdir()) okToast("파일이 생성되었습니다.");
                else errorToast("파일 생성에 실패하였습니다.");
            }
            File file = new File(dir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(total.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) { }
        } else waringToast("권한 허용을 해주세요.");
    }

    /**
     * 입력 받을 때마다 색상 변경
     */
    private void setColorSpan() {
        binding.content.addTextChangedListener(new TextWatcher() {
            final String print = "\\n\\s*(ㅅㅁㅅ|ㅆㅁㅆ|ㅅㅇㅅ)\\b|^\\s*(ㅅㅁㅅ|ㅆㅁㅆ|ㅅㅇㅅ)\\s+";
            final String var = "\\b(ㅇㅈㅇ|ㅇㅉㅇ|ㅇㅂㅇ|ㅇㅁㅇ|ㅇㄱㅇ|ㅇㅅㅇ|ㅇㅆㅇ)\\b";
            final String bool1 = "\\b(ㅇㅇ|ㄴㄴ)\\b";
            final String bool2 = "\\b(ㄸ|ㄲ|\\^\\^|\\?ㅅ\\?)\\b";
            final int printColor = Color.parseColor("#006400"); // 검은색 초록색
            final int varColor = Color.parseColor("#9370DB"); //연보라색
            final int boolColor1 = Color.parseColor("#FF8C00"); //검은 주황
            final int boolColor2 = Color.parseColor("#00B8D4"); //파란색
            final Pattern printPattern = Pattern.compile((print));
            final Pattern varPattern = Pattern.compile((var));
            final Pattern bool1Pattern = Pattern.compile((bool1));
            final Pattern bool2Pattern = Pattern.compile((bool2));
            final ColorScheme printScheme = new ColorScheme(printPattern, printColor);
            final ColorScheme varScheme = new ColorScheme(varPattern, varColor);
            final ColorScheme bool1Scheme = new ColorScheme(bool1Pattern, boolColor1);
            final ColorScheme bool2Scheme = new ColorScheme(bool2Pattern, boolColor2);
            final ColorScheme[] schemes = {printScheme, varScheme, bool1Scheme, bool2Scheme};

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

    private void errorToast(String message) {
        DynamicToast.makeError(this, message, Toast.LENGTH_SHORT).show();
    }

    private void okToast(String message) {
        DynamicToast.makeSuccess(this, message, Toast.LENGTH_SHORT).show();
    }

    private void waringToast(String message) {
        DynamicToast.makeWarning(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void request() {
        var input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        binding.scrollView1.setOnTouchListener((view, motionEvent) -> {
            binding.content.requestFocus();
            input.showSoftInput(binding.content, 0);
            return false;
        });

        binding.scrollView2.setOnTouchListener((view, motionEvent) -> {
            binding.content.requestFocus();
            input.showSoftInput(binding.content, 0);
            return false;
        });
    }
}