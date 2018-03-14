package com.coffdope.jeon.scanner.camera;

/**
 * Created by jeon on 18. 1. 12.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraAccessException;
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
import android.util.SparseIntArray;
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

import com.coffdope.jeon.scanner.result.ResultActivity;
import com.coffdope.jeon.scanner.R;

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

public class CameraFragment extends Fragment {
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    private static final String TAG = "CameraFragment";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int RESULT_REQUEST = 1;
    private static final String NO_CONTOUR = "null contour";

    static{
        ORIENTATION.append(Surface.ROTATION_0,90);
        ORIENTATION.append(Surface.ROTATION_90,0);
        ORIENTATION.append(Surface.ROTATION_180,270);
        ORIENTATION.append(Surface.ROTATION_270,180);
    }

    public static final String RESULT_IMG = "resultImg";
    public static final String RESULT_CNT = "contour";

    CameraManager mCameraManager;
    CameraDevice mCameraDevice;
    CameraCaptureSession mCameraCaptureSession;
    CaptureRequest mCaptureRequest;
    CameraCharacteristics mCameraCharacteristics;
    StreamConfigurationMap mStreamConfigurationMap;
    CaptureRequest.Builder mPreviewRequestBuilder;

    Size mCameraSize;
    String mCameraID;

    TextureView mPreview;
    SurfaceTexture texture;
    Surface surface,surface2;
    List<Surface> surfaces;

    private CameraDeviceStateCallback cameraDeviceStateCallback;
    private CaptureSessionCallback captureSessionCallback;
    private CaptureCallback captureCallback;
    private OnImageAvailableListener onImageAvailableListener;
    private CaptureListener captureListener;

    static HandlerThread handlerThread;
    static Handler backgroundHandler;

    SurfaceView overlay;
    SurfaceHolder overlayHolder;

    private ImageReader cntImageReader;
    private ImageReader resultImageReader;

    ArrayList<MatOfPoint> mContour = new ArrayList<MatOfPoint>();
    File mFile;

    public CaptureSessionCallback getCaptureSessionCallback() {
        return captureSessionCallback;
    }

    public CaptureCallback getCaptureCallback() {
        return captureCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cameracontrol,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViews(view,savedInstanceState);
        setCallbacks();

        PackageManager pm = getActivity().getPackageManager();
        if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
        }
    }

    private void setCallbacks(){
        cameraDeviceStateCallback = CameraDeviceStateCallback.getInstance();
        cameraDeviceStateCallback.setCameraFragment(this);

        captureSessionCallback = CaptureSessionCallback.getInstance();
        captureSessionCallback.setCameraFragment(this);

        captureCallback = CaptureCallback.getInstance();

        onImageAvailableListener = OnImageAvailableListener.getInstance();
        onImageAvailableListener.setCameraFragment(this);

        captureListener = CaptureListener.getInstance();
        captureListener.setCameraFragment(this);
    }

    private void setViews(@NonNull View view, @NonNull Bundle savedInstanceState){

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

                if(null!=mContour&&mContour.size()>0) {
                    captureStillPicture();

                    Intent resultIntent = new Intent(getContext(), ResultActivity.class);
                    resultIntent.setData(Uri.fromFile(mFile));
                    resultIntent.putExtra(RESULT_CNT, mContour.get(0).getNativeObjAddr());
                    getActivity().startActivityForResult(resultIntent, RESULT_REQUEST);
                }else{
                    Toast.makeText(getContext(),NO_CONTOUR,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFile = new File(getActivity().getExternalFilesDir(null),"tmp.jpg");
    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceListenerForCameraControl surfaceListenerForCameraControl =
                SurfaceListenerForCameraControl.getInstance();
        surfaceListenerForCameraControl.setCameraFragment(this);

        startBackgroundThread();

        if(mPreview.isAvailable()){
            openCamera();
        }else{
            mPreview.setSurfaceTextureListener(surfaceListenerForCameraControl);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /*this method set and open the camera
    * it check whether the device has camera which face back
    * and it set the target surfaces*/
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
                // FIXME: 18. 2. 23 이부분 스레드 조정해야 될것 같다
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
                    mCameraManager.openCamera(mCameraID, cameraDeviceStateCallback, backgroundHandler);
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

            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,getOrientation(rotation));

            mCameraCaptureSession.stopRepeating();
            mCameraCaptureSession.abortCaptures();
            mCameraCaptureSession.capture(captureBuilder.build(),null,null);

        }catch (CameraAccessException e){
            Log.e(TAG,e.getMessage());
        }
    }

    private int getOrientation(int rotation){
        return ORIENTATION.get(rotation);
    }

}
