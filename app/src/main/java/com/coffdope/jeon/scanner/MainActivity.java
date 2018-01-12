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

import org.opencv.android.OpenCVLoader;

import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements TextureView.SurfaceTextureListener {

    private static final String TAG = "Scanner_Main";
    private static final int PERMISSION_REQUEST_CODE = 100;

    TextureView mPreview, mOverlay;

    CameraManager mCameraManager;
    CameraDevice mCameraDevice;
    CameraCaptureSession mCameraCaptureSession;
    CameraCharacteristics mCameraCharacteristics;
    CaptureRequest mCaptureRequest;
    StreamConfigurationMap mStreamConfigurationMap;
    Size mCameraSize;
    CaptureRequest.Builder mPreviewRequestBuilder;
    SurfaceTexture texture;
    Surface surface;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPreview.isAvailable()){

        }else{
            mPreview.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        String mCameraID=null;



        final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                super.onCaptureStarted(session, request, timestamp, frameNumber);
            }

            @Override
            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                super.onCaptureProgressed(session, request, partialResult);
            }

            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
            }
        };

        final CameraCaptureSession.StateCallback mCaptureSessionCallback = new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                mCameraCaptureSession = cameraCaptureSession;
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                try {
                    mCameraCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, null);
                }catch (CameraAccessException e){
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

            }
        };

        final CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback(){
            @Override
            public void onClosed(@NonNull CameraDevice camera) {
                super.onClosed(camera);
            }

            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                mCameraDevice = cameraDevice;
                try {
                    mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    mPreviewRequestBuilder.addTarget(surface);

                    mCameraDevice.createCaptureSession(Collections.singletonList(surface), mCaptureSessionCallback, null);
                }catch(CameraAccessException e){
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                cameraDevice.close();
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {
                cameraDevice.close();
                finish();
            }
        };

        try {
            for (String cameraID : mCameraManager.getCameraIdList()) {
                mCameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraID);
                if(mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)==
                        CameraCharacteristics.LENS_FACING_BACK){
                    mStreamConfigurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    Size[] sizes = mStreamConfigurationMap.getOutputSizes(SurfaceTexture.class);
                    mCameraID=cameraID;

                    mCameraSize = sizes[0];
                    for(Size size: sizes){
                        if (size.getWidth()>mCameraSize.getWidth()){
                            mCameraSize=size;
                        }
                    }

                }
            }
            // TODO: 18. 1. 11 핸들러 처리 해야된다
            if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED&&mCameraID!=null) {
                mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, null);
            }else{
                finish();
            }

            texture = mPreview.getSurfaceTexture();
            texture.setDefaultBufferSize(mCameraSize.getWidth(),mCameraSize.getHeight());
            surface = new Surface(texture);


        }catch(CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if(null!=mCameraCaptureSession){
            mCameraCaptureSession.close();
            mCameraCaptureSession=null;
        }
        if(null!=mCameraDevice){
            mCameraDevice.close();
            mCameraDevice=null;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
