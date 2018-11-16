package com.example.wang.hello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wang.hello.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, VideoFragment.newInstance()).commit();
    }
}