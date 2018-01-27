package com.coffdope.jeon.scanner;

/**
 * Created by jeon on 18. 1. 12.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PorterDuff;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.Surface;
import android.graphics.SurfaceTexture;
import android.widget.Toast;
import android.media.ImageReader;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
    private Surface surface,surface2;
    private List<Surface> surfaces;

    private Handler handler;

    private static HandlerThread handlerThread;
    private static Handler backgroundHandler;

    private SurfaceView overlay;
    private SurfaceHolder overlayHolder;

    private ImageReader imageReader;

    private ArrayList<MatOfPoint> mContour = new ArrayList<MatOfPoint>();
    private Mat matForTranmsform;



    public static CameraControl getInstance(AppCompatActivity activity){
        if(null==cameraControl){
            cameraControl=new CameraControl(activity);
        }else{
            CameraControl.activity=activity;
        }
        return cameraControl;
    }

    public void cameraStop(){
        closeCamera();
        CameraControl.activity=null;
        CameraControl.handlerThread.quitSafely();
        CameraControl.cameraControl=null;
    }

    private CameraControl(AppCompatActivity activity){
        CameraControl.activity=activity;
        handlerThread = new HandlerThread("background");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
        overlayHolder = ((SurfaceView)activity.findViewById(R.id.overlay)).getHolder();
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
                mCameraDevice.createCaptureSession(surfaces,mCaptureSessionCallback,backgroundHandler);
            }catch(CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice=null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            mCameraDevice=null;
            if(null!=activity){
                activity.finish();
            }
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
                mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(surface);
                mPreviewRequestBuilder.addTarget(surface2);
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                mCameraCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, backgroundHandler);
            }catch (CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

        }
    };

    final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader imageReader) {
            Image img = imageReader.acquireLatestImage();
            try{
                if(null==img) { throw new NullPointerException("null img"); }

                ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                ArrayList<MatOfPoint> tmpCnt=Detector.detectPage(data,new org.opencv.core.Size(mCameraSize.getWidth(),mCameraSize.getHeight()));

                // FIXME: 18. 1. 28 서피스뷰 통제 필요
                synchronized (overlayHolder){
                    Canvas mCanvas = overlayHolder.lockCanvas();
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    if(tmpCnt.size()!=0){
                        mContour=(ArrayList<MatOfPoint>) tmpCnt.clone();

                        Bitmap cntBitmap = drawCntOnOnverlay(mContour, mCameraSize, mCanvas);
                        mCanvas.drawBitmap(cntBitmap, 0, 0, null);
                    }

                    overlayHolder.unlockCanvasAndPost(mCanvas);
                }

            }catch (NullPointerException ne){
                Log.e(TAG,ne.getMessage());
            }finally {
                if(null!=img){
                    img.close();
                }
            }
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
                        mCameraSize = sizes[1];

                        mCameraID=cameraID;
                    }
                }

                /*set target surfaces*/
                mPreview = (TextureView) activity.findViewById(R.id.preview);
                texture = mPreview.getSurfaceTexture();
                texture.setDefaultBufferSize(mCameraSize.getWidth(),mCameraSize.getHeight());
                surface = new Surface(texture);

                imageReader = ImageReader.newInstance(mCameraSize.getWidth(),mCameraSize.getHeight(), ImageFormat.YUV_420_888,2);
                imageReader.setOnImageAvailableListener(onImageAvailableListener,backgroundHandler);
                surface2=imageReader.getSurface();

                surfaces = new ArrayList<>(2);
                surfaces.add(surface);
                surfaces.add(surface2);

                // TODO: 18. 1. 11 핸들러 처리 해야된다
                if(activity.checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&mCameraID!=null) {
                    mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, backgroundHandler);
                }
                Toast.makeText(activity,"Camera Opened",Toast.LENGTH_LONG).show();

            }catch(CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }
        return true;
    }

    public void closeCamera(){
        if(null!=imageReader){
            imageReader.close();
            imageReader=null;
        }
        if(null!=mCameraCaptureSession){
            mCameraCaptureSession.close();
            mCameraCaptureSession=null;
        }
        if(null!=mCameraDevice){
            mCameraDevice.close();
            mCameraDevice=null;
        }
    }

    public interface captureInterface{
        void capture();
    }

    private class detectWorker implements Runnable {
        @Override
        public void run() {
            Bitmap bitmap = mPreview.getBitmap();
            ArrayList<MatOfPoint> ctr;
            Mat overlayMat=new Mat(mCameraSize.getHeight(),mCameraSize.getWidth(),CvType.CV_8UC4);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int size = bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            bitmap.copyPixelsToBuffer(byteBuffer);
            byte[] byteArray = byteBuffer.array();

            ctr=Detector.detectPage(byteArray,new org.opencv.core.Size(bitmap.getWidth(),bitmap.getHeight()));

            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }
    private Bitmap drawCntOnOnverlay(ArrayList<MatOfPoint> mContour, Size mCameraSize, Canvas mCanvas){
        Mat mat = new Mat(mCameraSize.getHeight(), mCameraSize.getWidth(),CvType.CV_8UC4);
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
