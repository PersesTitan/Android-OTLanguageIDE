package com.example.android_otlanguageide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.android_otlanguageide.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.play.setVisibility(View.VISIBLE);
        binding.stop.setVisibility(View.GONE);

        binding.play.setOnClickListener(v -> {
            binding.stop.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.GONE);
        });

        binding.stop.setOnClickListener(v -> {
            binding.play.setVisibility(View.VISIBLE);
            binding.stop.setVisibility(View.GONE);
        });
    }
}