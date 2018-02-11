package com.coffdope.jeon.scanner.fragments;

/**
 * Created by jeon on 18. 1. 12.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.Surface;
import android.graphics.SurfaceTexture;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.media.ImageReader;

import com.coffdope.jeon.scanner.Activities.ResultActivity;
import com.coffdope.jeon.scanner.R;
import com.coffdope.jeon.scanner.func.Detector;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CameraControl extends Fragment
implements TextureView.SurfaceTextureListener {
    private static final String TAG = "CameraControl";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int RESULT_REQUEST = 1;

    public static final String RESULT_IMG = "resultImg";

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

    private static HandlerThread handlerThread;
    private static Handler backgroundHandler;

    private SurfaceView overlay;
    private SurfaceHolder overlayHolder;

    private ImageReader cntImageReader;
    private ImageReader resultImageReader;

    private ArrayList<MatOfPoint> mContour = new ArrayList<MatOfPoint>();
    private Mat matForTranmsform;

    private File mFile;

    public static CameraControl newInstance(){
        return new CameraControl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cameracontrol,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreview = (TextureView)view.findViewById(R.id.preview);
        overlay = (SurfaceView)view.findViewById(R.id.overlay);
        overlay.setZOrderOnTop(true);

        overlayHolder = overlay.getHolder();
        overlayHolder.setFormat(PixelFormat.TRANSPARENT);

        FloatingActionButton captureButton = (FloatingActionButton)view.findViewById(R.id.fab);

        captureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: 18. 2. 11 임시파일 생성
                captureStillPicture();

                Intent resultIntent = new Intent(getContext(), ResultActivity.class);
                resultIntent.setData(Uri.fromFile(mFile));
                getActivity().startActivityForResult(resultIntent,RESULT_REQUEST);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFile = new File(getActivity().getExternalFilesDir(null),"tmp.jpg");
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        if(mPreview.isAvailable()){
            openCamera();
        }else{
            mPreview.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
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

                ArrayList<MatOfPoint> tmpCnt= Detector.detectPage(data,new org.opencv.core.Size(mCameraSize.getWidth(),mCameraSize.getHeight()));

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

    final ImageReader.OnImageAvailableListener captureListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader imageReader) {
            backgroundHandler.post(new ImageSaver(imageReader.acquireLatestImage(),mFile));
        }
    };

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        openCamera();
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

    public boolean openCamera(){
        if(null==getActivity()){
            return false;
        }else{
            mCameraManager=(CameraManager) getActivity().getSystemService(AppCompatActivity.CAMERA_SERVICE);

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
                texture = mPreview.getSurfaceTexture();
                texture.setDefaultBufferSize(mCameraSize.getWidth(),mCameraSize.getHeight());
                surface = new Surface(texture);

                cntImageReader = ImageReader.newInstance(mCameraSize.getWidth(),mCameraSize.getHeight(), ImageFormat.YUV_420_888,2);
                cntImageReader.setOnImageAvailableListener(onImageAvailableListener,backgroundHandler);
                surface2= cntImageReader.getSurface();

                resultImageReader = ImageReader.newInstance(mCameraSize.getWidth(),mCameraSize.getHeight(),
                        ImageFormat.JPEG,2);
                resultImageReader.setOnImageAvailableListener(captureListener,backgroundHandler);

                surfaces = new ArrayList<>(2);
                surfaces.add(surface);
                surfaces.add(surface2);
                surfaces.add(resultImageReader.getSurface());

                if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&mCameraID!=null) {
                    mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallback, backgroundHandler);
                }
                Toast.makeText(getContext(),"Camera Opened",Toast.LENGTH_LONG).show();

            }catch(CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }
        return true;
    }

    public void closeCamera(){
        if(null!= cntImageReader){
            cntImageReader.close();
            cntImageReader =null;
        }
        if(null!=mCameraCaptureSession){
            mCameraCaptureSession.close();
            mCameraCaptureSession=null;
        }
        if(null!=mCameraDevice){
            mCameraDevice.close();
            mCameraDevice=null;
        }
        Toast.makeText(getContext(),"Camera Closed",Toast.LENGTH_LONG).show();
    }

    private void startBackgroundThread(){
        handlerThread = new HandlerThread("background");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    private void stopBackgroundThread(){
        handlerThread.quitSafely();
        try{
            handlerThread.join();
            handlerThread = null;
            backgroundHandler = null;
        }catch (InterruptedException e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void captureStillPicture(){
        try{
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(resultImageReader.getSurface());

            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);


            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.abortCaptures();
            mCameraCaptureSession.capture(captureBuilder.build(),null,null);

        }catch (CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
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
                if(!mFile.exists()){
                    mFile.createNewFile();
                }
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
