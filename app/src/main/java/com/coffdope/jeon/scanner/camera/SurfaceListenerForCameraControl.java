package com.coffdope.jeon.scanner.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

/**
 * Created by jeon on 18. 3. 7.
 */

public class SurfaceListenerForCameraControl implements TextureView.SurfaceTextureListener {

    private static SurfaceListenerForCameraControl surfaceListenerForCameraControl;
    private CameraFragment cameraFragment;

    public static SurfaceListenerForCameraControl getInstance(){
        if(null==surfaceListenerForCameraControl){
             surfaceListenerForCameraControl = new SurfaceListenerForCameraControl();
        }
        return surfaceListenerForCameraControl;
    }

    private SurfaceListenerForCameraControl() {
    }

    public void setCameraFragment(CameraFragment cameraFragment) {
        this.cameraFragment = cameraFragment;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        cameraFragment.setAndOpenCamera();
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
