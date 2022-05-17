package com.example.android_otlanguageide;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimerTask;

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
                    File fileName = getFileStreamPath("Android");
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(fileName.getPath());
                    intent.setDataAndType(uri, "TEXT/*");
                    startActivity(Intent.createChooser(intent,"Open"));
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
                    dialog.setTitle("파일 이름을 입력해주세요");
                    dialog.setView(editText);
                    dialog.setPositiveButton("확인", (dialogInterface, i) -> {
                        stringBuilder = new StringBuilder(setting.getText(editText));
                        stringBuilder.append(".otl");
//                        File file = new File(stringBuilder.toString());
//                        System.out.println(file.getPath());
//                        if (!file.exists()) {
//                            try (FileOutputStream fos = new FileOutputStream(stringBuilder.toString())) {
//                                fos.write(setting.getText(binding.content).getBytes(StandardCharsets.UTF_8));
//                                fos.flush();
//                            } catch (IOException ignored) { }
//                        }
                        try (FileOutputStream fOut = new FileOutputStream(stringBuilder.toString())) {
                            OutputStreamWriter osw = new OutputStreamWriter(fOut);
                            osw.write(setting.getText(binding.content));
                            osw.flush();
                        } catch (IOException ignored) { }
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

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!setting.getText(binding.content).isEmpty()) tread();
        }
    };

    private void tread() {
        runOnUiThread(() -> {
            binding.content.removeTextChangedListener(textWatcher);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    int position = binding.content.getSelectionStart();
                    String total = setting.getText(binding.content);
                    List<ItemPosition> list = textSetting.getList(total);
                    binding.content.setText(textSetting.setTotalSpan(total, list));
                    binding.content.addTextChangedListener(textWatcher);
                    binding.content.setSelection(position);
                } catch (InterruptedException e) {
                    binding.content.addTextChangedListener(textWatcher);
                }
            }).start();
        });
    }
}