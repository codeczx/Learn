package io.github.drawdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.drawdemo.databinding.ActivityTestPathBinding;

public class TestPathActivity extends AppCompatActivity {

    private ActivityTestPathBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test_path);
    }
}
