package com.example.android_otlanguageide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.example.android_otlanguageide.item.ItemPosition;
import com.example.android_otlanguageide.setting.Setting;
import com.example.android_otlanguageide.setting.TextSetting;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        binding.content.addTextChangedListener(textWatcher);

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

//                    File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
//                    Uri uri = Uri.parse(dir.toString());
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setDataAndType(uri, "*/*");
//                    startActivity(Intent.createChooser(intent, "Open"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("==================");
        assert data != null;
        Uri uri = data.getData();
        String path = uri.getPath();
        System.out.println(path);
        System.out.println(uri);
    }

    private void readFile() {
        File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
        File[] files = dir.listFiles(pathname -> pathname.getName().toLowerCase(Locale.ROOT).endsWith(".otl"));
    }

    private void createFile(String fileName, String total) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory(), "OTLanguage");
            if (!dir.exists()) Toast.makeText(this, dir.mkdir()?"파일이 생성되었습니다.":"파일 생성에 실패하였습니다." , Toast.LENGTH_SHORT).show();
            File file = new File(dir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(total.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ignored) { }
            DynamicToast.makeSuccess(this, "파일이 생성되었습니다.", Toast.LENGTH_SHORT).show();
        } else DynamicToast.makeWarning(this, "권한 허용을 해주세요.", Toast.LENGTH_SHORT).show();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            String total = setting.getText(binding.content);
            textSetting.setTotalSpan(editable, total);
        }
    };

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
}