package com.example.android_otlanguageide;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_otlanguageide.activity.item.Check;
import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.setting.Setting;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.jetbrains.annotations.NotNull;

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

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements Check {

    @SuppressLint("StaticFieldLeak")
    public static ActivityMainBinding binding;
    public static StringBuilder totalStringBuilder = new StringBuilder();
    private SharedPreferences.Editor editor;

    private final String CONTENT = "CONTENT";
    private final String autoSave = "AutoSave";
    final String shared = "file";
    String total;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalStringBuilder.setLength(0);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        request();
        setColorSpan();
        binding.input.setOnEditorActionListener(new DoneOnEditorActionListener());
        var sharedPreferences = getSharedPreferences(shared, 0);
        editor = sharedPreferences.edit();
        binding.autoSave.setChecked(sharedPreferences.getBoolean(autoSave, false));
        if (binding.autoSave.isChecked()) binding.content.setText(sharedPreferences.getString(CONTENT, null));
        else binding.content.setText(null);

        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);
        binding.inputLayout.setVisibility(View.GONE);

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.play:
                    runOnUiThread(() -> {
                        totalStringBuilder.setLength(0);
                        binding.result.setText(null);
                        binding.stop.setVisibility(View.VISIBLE);
                        binding.play.setVisibility(View.GONE);
                        running.start();
                    });
                    break;

                case R.id.stop:
                    Setting.scannerCheck = false;
                    runOnUiThread(() -> {
                        binding.play.setVisibility(View.VISIBLE);
                        binding.stop.setVisibility(View.GONE);
                        binding.inputLayout.setVisibility(View.GONE);
                    });
                    break;

                case R.id.thisSave:
                    total = binding.content.getText().toString();
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
                        String text = editText.getText().toString();
                        if (text.isEmpty()) waringToast("파일이름을 입력해주세요.");
                        else createFile(editText.getText().toString()
                                + ".otl", binding.content.getText().toString());
                    }).setNegativeButton("취소", (dialogInterface, i) -> { });
                    dialog.show();
                    break;

                case R.id.clear:
                    binding.result.setText(null);
                    break;

                case R.id.done:
                    Setting.scannerCheck = false;
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
        binding.clear.setOnClickListener(listener);
        binding.done.setOnClickListener(listener);

        binding.autoSave.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean(autoSave, b);
            editor.apply();
        });
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
        totalStringBuilder.setLength(0);
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
     * print <br>
     * - ㅅㅁㅅ ㅆㅁㅆ 는 첫 부분일때만 허용 <br>
     * 단, 공백은 허용 <br>
     * - ㅅㅇㅅ 는 위치 상관 없음 <br>
     * var <br>
     * - ㅇㅈㅇ ㅇㅉㅇ ㅇㅂㅇ ㅇㅁㅇ ㅇㄱㅇ ㅇㅅㅇ ㅇㅆㅇ 는
     * 모두 처음 시작 할때만 <br>
     * 단, 공백은 허용 <br>
     * bool <br>
     * - ㅇㅇ ㄴㄴ ㄸ ㄲ ^^ ?ㅅ? 는 위치 상과 없음 </br>
     * 입력 받을 때마다 색상 변경
     */
    private void setColorSpan() {
        binding.content.addTextChangedListener(new TextWatcher() {
            private final String print = "\\n\\s*(ㅅㅁㅅ|ㅆㅁㅆ)\\b|^\\s*(ㅅㅁㅅ|ㅆㅁㅆ)\\s+|\\b(ㅅㅇㅅ)\\b";
            private final String var = "\\n\\s*(ㅇㅈㅇ|ㅇㅉㅇ|ㅇㅂㅇ|ㅇㅁㅇ|ㅇㄱㅇ|ㅇㅅㅇ|ㅇㅆㅇ)\\b|" +
                    "^\\s*(ㅇㅈㅇ|ㅇㅉㅇ|ㅇㅂㅇ|ㅇㅁㅇ|ㅇㄱㅇ|ㅇㅅㅇ|ㅇㅆㅇ)\\s+";
            private final String bool1 = "\\b(ㅇㅇ|ㄴㄴ)\\b";
            private final String bool2 = "\\b(ㄸ|ㄲ|\\^\\^|\\?ㅅ\\?)\\b";
            private final int printColor = Color.parseColor("#006400"); // 검은색 초록색
            private final int varColor = Color.parseColor("#9370DB"); //연보라색
            private final int boolColor1 = Color.parseColor("#FF8C00"); //검은 주황
            private final int boolColor2 = Color.parseColor("#00B8D4"); //파란색
            private final Pattern printPattern = Pattern.compile(print);
            private final Pattern varPattern = Pattern.compile(var);
            private final Pattern bool1Pattern = Pattern.compile(bool1);
            private final Pattern bool2Pattern = Pattern.compile(bool2);
            private final ColorScheme printScheme = new ColorScheme(printPattern, printColor);
            private final ColorScheme varScheme = new ColorScheme(varPattern, varColor);
            private final ColorScheme bool1Scheme = new ColorScheme(bool1Pattern, boolColor1);
            private final ColorScheme bool2Scheme = new ColorScheme(bool2Pattern, boolColor2);
            private final ColorScheme[] schemes = {printScheme, varScheme, bool1Scheme, bool2Scheme};

            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.autoSave.isChecked()) {
                    String total = binding.content.getText().toString();
                    editor.putString(CONTENT, total);
                    editor.apply();
                }
            }
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

    private static class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                var imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                Setting.scannerCheck = false;
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean check(String line) {
        return false;
    }
}