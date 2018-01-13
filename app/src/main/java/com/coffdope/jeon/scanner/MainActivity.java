package com.coffdope.jeon.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;

import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements TextureView.SurfaceTextureListener {

    private static final String TAG = "Scanner_Main";
    private static final int PERMISSION_REQUEST_CODE = 100;

    private CameraControl mCameracontrol;
    private TextureView mPreview, mOverlay;
    private SurfaceTexture texture;
    private Surface surface;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }

    // TODO: 18. 1. 11 need to handle permission 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreview = (TextureView) findViewById(R.id.preview);
        mOverlay = (TextureView) findViewById(R.id.overlay);

        mPreview.setSurfaceTextureListener(this);

        PackageManager pm = getPackageManager();
        if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
        }

        mCameracontrol = CameraControl.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPreview.isAvailable()){
            mCameracontrol.openCamera();
        }else{
            mPreview.setSurfaceTextureListener(this);
        }
    }

    @Override
    protected void onPause() {
        mCameracontrol.closeCamera();
        Toast.makeText(this,"Camera Closed",Toast.LENGTH_LONG).show();
        super.onPause();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if(null!=mCameracontrol) {
            mCameracontrol.openCamera();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
