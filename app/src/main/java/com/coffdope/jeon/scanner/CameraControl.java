package com.coffdope.jeon.scanner;

/**
 * Created by jeon on 18. 1. 12.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.Surface;
import android.graphics.SurfaceTexture;
import android.widget.Toast;

import java.util.Collections;


public class CameraControl {
    private static final String TAG = "CameraControl";

    private static CameraControl cameraControl;
    private static AppCompatActivity activity;

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest mCaptureRequest;
    private CameraCharacteristics mCameraCharacteristics;
    private StreamConfigurationMap mStreamConfigurationMap;
    private CaptureRequest.Builder mPreviewRequestBuilder;

    private Size mCameraSize;
    private String mCameraID;

    private TextureView mPreview;
    private SurfaceTexture texture;
    private Surface surface;


    public static CameraControl getInstance(AppCompatActivity activity){
        if(null==cameraControl){
            cameraControl=new CameraControl(activity);
        }
        return cameraControl;
    }

    private CameraControl(AppCompatActivity activity){
        CameraControl.activity=activity;
    }

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
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                mCameraDevice.createCaptureSession(Collections.singletonList(surface),mCaptureSessionCallback,null);

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
        }
    };

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

    public boolean openCamera(){
        if(null==activity){
            return false;
        }else{
            mCameraManager=(CameraManager) activity.getSystemService(AppCompatActivity.CAMERA_SERVICE);


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

                mPreview = (TextureView) activity.findViewById(R.id.preview);
                texture = mPreview.getSurfaceTexture();
                surface = new Surface(texture);

                // TODO: 18. 1. 11 핸들러 처리 해야된다
                if(activity.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&mCameraID!=null) {
                    mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, null);
                }
                Toast.makeText(activity,"Camera Opened",Toast.LENGTH_LONG).show();

            }catch(CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }
        return true;
    }

    public void closeCamera(){
        if(null!=mCameraCaptureSession){
            mCameraCaptureSession.close();
            mCameraCaptureSession=null;
        }
        if(null!=mCameraDevice){
            mCameraDevice.close();
            mCameraDevice=null;
        }
    }
}
