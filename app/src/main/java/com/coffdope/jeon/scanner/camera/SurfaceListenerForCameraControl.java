package com.coffdope.jeon.scanner.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

/**
 * Created by jeon on 18. 3. 7.
 */

public class SurfaceListenerForCameraControl implements TextureView.SurfaceTextureListener {

    private static SurfaceListenerForCameraControl surfaceListenerForCameraControl;
    private CameraControl cameraControl;

    public static SurfaceListenerForCameraControl getInstance(){
        if(null==surfaceListenerForCameraControl){
             surfaceListenerForCameraControl = new SurfaceListenerForCameraControl();
        }
        return surfaceListenerForCameraControl;
    }

    private SurfaceListenerForCameraControl() {
    }

    public void setCameraControl(CameraControl cameraControl) {
        this.cameraControl = cameraControl;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        cameraControl.openCamera();
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
