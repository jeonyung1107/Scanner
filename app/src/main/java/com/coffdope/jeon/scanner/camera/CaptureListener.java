package com.coffdope.jeon.scanner.camera;

import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by jeon on 18. 3. 7.
 */

public class CaptureListener implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "CaptureListener";
    private static CaptureListener captureListener;
    private CameraFragment cameraFragment;

    public static CaptureListener getInstance(){
        if(null == captureListener){
            captureListener = new CaptureListener();
        }
        return captureListener;
    }

    public void setCameraFragment(CameraFragment cameraFragment) {
        this.cameraFragment = cameraFragment;
    }

    private CaptureListener(){}

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        cameraFragment.backgroundHandler.post(new ImageSaver(imageReader.acquireLatestImage(),cameraFragment.mFile));
    }

    private static class ImageSaver implements Runnable{
        private final Image mImage;
        private final File mFile;

        ImageSaver(Image image,File file){
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(mFile);
                fos.write(bytes);
            }catch (IOException e){
                Log.e(TAG,e.getMessage());
            }finally {
                mImage.close();
                if(null!=fos){
                    try{
                        fos.close();
                    }catch (IOException e){
                        Log.e(TAG,e.getMessage());
                    }
                }
            }
        }
    }
}
