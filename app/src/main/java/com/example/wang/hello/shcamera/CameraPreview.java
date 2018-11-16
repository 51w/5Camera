package com.example.wang.hello.shcamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "Hello";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private OnDataListener dataListener;

    private static int mOptVideoWidth = 1920;
    private static int mOptVideoHeight = 1080;

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "camera is not available");
        }
        return c;
    }

    public void setDataListener(OnDataListener dataListener)
    {
        this.dataListener = dataListener;
    }

    public CameraPreview(Context context)
    {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = getCameraInstance();
        try {
            mCamera.setPreviewCallback(mCameraPreviewCallback);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            getCameraOptimalVideoSize();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        Log.d(TAG, "surfaceDestroyed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "surfaceChanged [" + w + ", " + h + "]");

    }

    private void getCameraOptimalVideoSize() {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, getWidth(), getHeight());
            mOptVideoWidth = optimalSize.width;
            mOptVideoHeight = optimalSize.height;
            Log.d(TAG, "prepareVideoRecorder: optimalSize:" + mOptVideoWidth + ", " + mOptVideoHeight);
        } catch (Exception e) {
            Log.e(TAG, "getCameraOptimalVideoSize: ", e);
        }
    }

    private Camera.PreviewCallback mCameraPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            //Log.d(TAG, "onPreviewFrame: data.length=" + data.length);
            if (null != dataListener) {
                dataListener.onNV21(data, mOptVideoWidth, mOptVideoHeight);
            }
        }
    };

    public interface OnDataListener {
        void onNV21(byte[] data, final int width, final int height);
    }
}