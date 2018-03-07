package com.coffdope.jeon.scanner.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.coffdope.jeon.scanner.camera.CameraFragment.backgroundHandler;

/**
 * Created by jeon on 18. 3. 7.
 */

public class CaptureSessionCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CaptureSessionCallback";
    private static CaptureSessionCallback captureSessionCallback;
    private CameraFragment cameraFragment;

    private CaptureSessionCallback(){}

    public static CaptureSessionCallback getInstance(){
        if(null==captureSessionCallback){
            captureSessionCallback = new CaptureSessionCallback();
        }
        return captureSessionCallback;
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

            cameraFragment.mCameraCaptureSession.setRepeatingRequest(cameraFragment.mPreviewRequestBuilder.build(), cameraFragment.getCaptureCallback(), backgroundHandler);
        }catch (CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

    }
}
