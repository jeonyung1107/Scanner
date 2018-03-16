package com.coffdope.jeon.scanner.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;
import android.util.Log;


/**
 * Created by jeon on 18. 3. 7.
 */

public class CaptureSessionStateCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CaptureSessionStateCallback";
    private static CaptureSessionStateCallback captureSessionStateCallback;
    private CameraFragment cameraFragment;

    private CaptureSessionStateCallback(){}

    public static CaptureSessionStateCallback getInstance(){
        if(null== captureSessionStateCallback){
            captureSessionStateCallback = new CaptureSessionStateCallback();
        }
        return captureSessionStateCallback;
    }

    public void setCameraFragment(CameraFragment cameraFragment){
        this.cameraFragment = cameraFragment;
    }


    @Override
    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
        cameraFragment.mCameraCaptureSession = cameraCaptureSession;

        try {
            cameraFragment.mPreviewRequestBuilder = cameraFragment.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            cameraFragment.mPreviewRequestBuilder.addTarget(cameraFragment.surface);
            cameraFragment.mPreviewRequestBuilder.addTarget(cameraFragment.surface2);
            cameraFragment.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            cameraFragment.mCameraCaptureSession.setRepeatingRequest(cameraFragment.mPreviewRequestBuilder.build(), cameraFragment.getCaptureCallback(), cameraFragment.backgroundHandler);
        }catch (CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

    }
}
