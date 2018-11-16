package com.example.wang.hello.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.wang.hello.R;
import com.example.wang.hello.cpp.EHuman;
import com.example.wang.hello.shcamera.CameraPreview;
import com.example.wang.hello.view.ColorHintView;

import java.util.List;

public class VideoFragment extends Fragment {
    private static final String TAG = "Hello";

    private CameraPreview mCameraPreview;
    private ColorHintView mHintView;
    private Button mCaptureBtn;
    private View mRoot;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "frag onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "frag onCreateView");
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "frag onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        mCaptureBtn = view.findViewById(R.id.capture_btn);
        mCaptureBtn.setOnClickListener(mOnClickListener);

        mHintView = view.findViewById(R.id.color_hint_view);
        mRoot = view;

        initCameraPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 销毁预览");
        EHuman.setOnResultListener(null);
        mCameraPreview.setDataListener(null);
        mCameraPreview = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 回到前台");
        EHuman.setOnResultListener(mEHOnResultListener);
        if (null == mCameraPreview) {
            initCameraPreview();
        }
    }

    private void initCameraPreview() {
        mCameraPreview = new CameraPreview(getContext());
        mCameraPreview.setDataListener(mOnCameraDataListener);
        FrameLayout preview = mRoot.findViewById(R.id.camera_preview);

        preview.addView(mCameraPreview);
    }

    private CameraPreview.OnDataListener mOnCameraDataListener = new CameraPreview.OnDataListener() {
        @Override
        public void onNV21(byte[] data, int width, int height) {
            //Log.d(TAG, "onNV21: " + data.length + "   " + width + "  -  " + height);
            EHuman.detectHuman(data, width, height);
        }
    };

    private EHuman.OnResultListener mEHOnResultListener = new EHuman.OnResultListener() {
        @Override
        public void onResult(final List<Rect> resArray, final int picWidth, final int picHeight) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (resArray.isEmpty()) {
                        mHintView.setShowRect(false);
                    } else {
                        mHintView.setFaceRect(resArray, picWidth, picHeight);
                    }
                }
            });
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.capture_btn:

                    mCaptureBtn.setText("录像");

                    break;
            }
        }
    };
}