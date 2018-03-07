package com.coffdope.jeon.scanner.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by jeon on 18. 3. 7.
 */

public class CameraDeviceStateCallback extends CameraDevice.StateCallback {
    private CameraFragment cameraFragment;
    private static CameraDeviceStateCallback cameraDeviceStateCallback;
    
    private CameraDeviceStateCallback(){}
    
    public static CameraDeviceStateCallback getInstance(){
        if(null==cameraDeviceStateCallback){
            cameraDeviceStateCallback = new CameraDeviceStateCallback();
        }
        return cameraDeviceStateCallback;
    }
    
    public void setCameraFragment(CameraFragment cameraFragment){
        this.cameraFragment = cameraFragment;
    }

    @Override
    public void onClosed(@NonNull CameraDevice camera) {
        super.onClosed(camera);
    }

    @Override
    public void onOpened(@NonNull CameraDevice cameraDevice) {
        cameraFragment.mCameraDevice = cameraDevice;
        try {
            // TODO: 18. 3. 7 세션 콜백 넣어야됨 
            cameraFragment.mCameraDevice.createCaptureSession(cameraFragment.surfaces, cameraFragment.getCaptureSessionCallback(), cameraFragment.backgroundHandler);
        }catch(CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
        cameraDevice.close();
        cameraFragment.mCameraDevice=null;
    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int i) {
        cameraDevice.close();
        cameraFragment.mCameraDevice=null;
    }

}
