package com.coffdope.jeon.scanner.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import android.util.Size;

import com.coffdope.jeon.scanner.imgProcess.RectangleDetector;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by jeon on 18. 3. 7.
 */

public class OnImageAvailableListener implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "OnImageAvailableListener";
    private static OnImageAvailableListener onImageAvailableListener;
    private CameraFragment cameraFragment;

    public static OnImageAvailableListener getInstance(){
        if(null == onImageAvailableListener){
            onImageAvailableListener = new OnImageAvailableListener();
        }
        return onImageAvailableListener;
    }

    public void setCameraFragment(CameraFragment cameraFragment){
        this.cameraFragment = cameraFragment;
    }

    private OnImageAvailableListener(){}

    @Override
    public void onImageAvailable(ImageReader imageReader) {

        Image img = imageReader.acquireLatestImage();
        try {
            if (null == img) {
                throw new NullPointerException("null img");
            }

            ByteBuffer buffer = img.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            ArrayList<MatOfPoint> tmpCnt = RectangleDetector.detectPage(data, new org.opencv.core.Size(cameraFragment.mCameraSize.getWidth(), cameraFragment.mCameraSize.getHeight()));

            // FIXME: 18. 1. 28 서피스뷰 통제 필요

            if (tmpCnt.size() != 0) {
                cameraFragment.mContour = (ArrayList<MatOfPoint>) tmpCnt.clone();

                Canvas mCanvas = cameraFragment.overlayHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                Bitmap cntBitmap = drawCntOnOnverlay(cameraFragment.mContour, cameraFragment.mCameraSize, mCanvas);
                mCanvas.drawBitmap(cntBitmap, 0, 0, null);

                cameraFragment.overlayHolder.unlockCanvasAndPost(mCanvas);
            }


        } catch (NullPointerException ne) {
            Log.e(TAG, ne.getMessage());
        } finally {
            if (null != img) {
                img.close();
            }
        }
    }

    private Bitmap drawCntOnOnverlay(ArrayList<MatOfPoint> mContour, Size mCameraSize, Canvas mCanvas){
        Mat mat = new Mat(mCameraSize.getHeight(), mCameraSize.getWidth(), CvType.CV_8UC4);
        Mat mat_rot = new Mat(mCameraSize.getWidth(),mCameraSize.getHeight(), CvType.CV_8UC4);
        Mat mat_resize = new Mat(mCanvas.getHeight(), mCanvas.getWidth(), CvType.CV_8UC4);
        Bitmap cntBitmap = Bitmap.createBitmap(mCanvas.getWidth(), mCanvas.getHeight(), Bitmap.Config.ARGB_8888);

        Imgproc.drawContours(mat, mContour, -1, new Scalar(255, 0, 0), 5);
        Core.rotate(mat,mat_rot,Core.ROTATE_90_CLOCKWISE);
        Imgproc.resize(mat_rot, mat_resize, new org.opencv.core.Size(mat_resize.width(), mat_resize.height()));
        Utils.matToBitmap(mat_resize,cntBitmap);

        return cntBitmap;
    }
}
