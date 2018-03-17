package com.coffdope.jeon.scanner.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import org.opencv.core.Point;
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

            ByteBuffer imageBuffer = img.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[imageBuffer.remaining()];
            imageBuffer.get(imageData);

            ArrayList<MatOfPoint> tmpContour = RectangleDetector.detectRectangleContour(
                    imageData, new org.opencv.core.Size(cameraFragment.mCameraSize.getWidth(), cameraFragment.mCameraSize.getHeight()));

            // FIXME: 18. 1. 28 서피스뷰 통제 필요

            if (tmpContour.size() != 0&&tmpContour.get(0).toArray().length>0) {
                cameraFragment.mContour = (ArrayList<MatOfPoint>) tmpContour.clone();
                Point[] points = cameraFragment.mContour.get(0).toArray();

                Canvas mCanvas = cameraFragment.overlayHolder.lockCanvas();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                float ratio = cameraFragment.mCameraSize.getHeight()/mCanvas.getWidth();
                adjustPoints(points, mCanvas.getWidth(),ratio);
                drawRectOnOverlayWithPoints(mCanvas,points);

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
    private void adjustPoints(Point[] points, int canvasWidth, float ratio){
        for(int i=0;i<4;++i){
            double tmpX = points[i].x;
            double tmpY = points[i].y;
            points[i].x = (canvasWidth - tmpY/ratio);
            points[i].y = tmpX/ratio;
        }
    }

    private void drawRectOnOverlayWithPoints(Canvas canvas, Point[] points){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.FILL);

        for(int i = 0; i<4; ++i){
            canvas.drawLine((float) points[i].x,(float)points[i].y,
                    (float)points[(i+1)%4].x,(float)points[(i+1)%4].y,paint);
        }
    }
}
