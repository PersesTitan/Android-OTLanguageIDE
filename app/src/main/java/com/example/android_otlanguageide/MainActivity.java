package com.example.android_otlanguageide;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String total;

    @Override
    @SuppressLint("NonConstantResourceId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String shared = "file";
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);
        binding.inputLayout.setVisibility(View.GONE);

        View.OnClickListener listener = view -> {
            switch (view.getId()) {
                case R.id.play:
                    binding.stop.setVisibility(View.VISIBLE);
                    binding.play.setVisibility(View.GONE);
                    total = binding.content.getText().toString();
                    break;

                case R.id.stop:
                    binding.play.setVisibility(View.VISIBLE);
                    binding.stop.setVisibility(View.GONE);
                    binding.inputLayout.setVisibility(View.GONE);
                    break;

                case R.id.save:
                    if (editor.commit()) DynamicToast.makeSuccess(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    else DynamicToast.makeError(this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.download:

                    break;

                case R.id.upload:

                    break;

                default:
                    assert false;
                    break;
            }
        };

        binding.play.setOnClickListener(listener);
        binding.stop.setOnClickListener(listener);
        binding.save.setOnClickListener(listener);
        binding.upload.setOnClickListener(listener);
        binding.download.setOnClickListener(listener);
    }
}