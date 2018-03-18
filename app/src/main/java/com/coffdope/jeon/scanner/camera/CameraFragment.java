package com.coffdope.jeon.scanner.camera;

/**
 * Created by jeon on 18. 1. 12.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.params.StreamConfigurationMap;
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

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.io.File;
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

    private CameraDeviceStateCallback cameraDeviceStateCallback;
    private CaptureSessionStateCallback captureSessionStateCallback;
    private CaptureCallback captureCallback;
    private OnImageAvailableListener onImageAvailableListener;
    private CaptureListener captureListener;

    private ImageReader contourImageReader;
    private ImageReader resultImageReader;

    HandlerThread handlerThread;
    Handler backgroundHandler;
    HandlerThread pageDetectThread;
    Handler pageDetectHandler;

    SurfaceView overlay;
    SurfaceHolder overlayHolder;

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


    ArrayList<MatOfPoint> mContour = new ArrayList<MatOfPoint>();
    File mFile;

    public CaptureSessionStateCallback getCaptureSessionStateCallback() {
        return captureSessionStateCallback;
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

        captureSessionStateCallback = CaptureSessionStateCallback.getInstance();
        captureSessionStateCallback.setCameraFragment(this);

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
                MatOfPoint resultContour = new MatOfPoint(mContour.get(0).toArray());

                if(null!=resultContour&&resultContour.toArray().length>0) {
                    captureStillPicture();
                    pageDetectThread.quit();

                    double[] resultContourPointsInDouble = new double[8];
                    Point[] resultContourPointArray = resultContour.toArray();
                    for(int i=0; i<4;++i){
                        resultContourPointsInDouble[i*2] = resultContourPointArray[i].x;
                        resultContourPointsInDouble[i*2 + 1] = resultContourPointArray[i].y;
                    }

                    Intent resultIntent = new Intent(getContext(), ResultActivity.class);
                    resultIntent.setData(Uri.fromFile(mFile));
                    resultIntent.putExtra(RESULT_CNT, resultContourPointsInDouble);
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
            setAndOpenCamera();
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

    public boolean setAndOpenCamera(){

        CameraManager cameraManager;

        if(null==getActivity()){
            return false;
        }else{
            cameraManager =(CameraManager) getActivity().getSystemService(AppCompatActivity.CAMERA_SERVICE);

            try {
                setCameraSize(cameraManager);
                setTargetSurfaces();

                checkPermissionOpenCamera(cameraManager);

                Toast.makeText(getContext(),"Camera Opened",Toast.LENGTH_LONG).show();

            }catch(CameraAccessException e){
                Log.e(TAG,e.getMessage());
            }
        }
        return true;
    }

    private void checkPermissionOpenCamera(CameraManager cameraManager) throws CameraAccessException{
        if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&mCameraID!=null) {
            cameraManager.openCamera(mCameraID, cameraDeviceStateCallback, backgroundHandler);
        }
    }

    private void setTargetSurfaces(){
        texture = mPreview.getSurfaceTexture();
        texture.setDefaultBufferSize(mCameraSize.getWidth(),mCameraSize.getHeight());
        surface = new Surface(texture);

        contourImageReader = ImageReader.newInstance(mCameraSize.getWidth(),mCameraSize.getHeight(), ImageFormat.YUV_420_888,2);
        // FIXME: 18. 2. 23 이부분 스레드 조정해야 될것 같다
        contourImageReader.setOnImageAvailableListener(onImageAvailableListener,pageDetectHandler);
        surface2= contourImageReader.getSurface();

        resultImageReader = ImageReader.newInstance(mCameraSize.getWidth(),mCameraSize.getHeight(),
                ImageFormat.JPEG,2);
        resultImageReader.setOnImageAvailableListener(captureListener,backgroundHandler);

        surfaces = new ArrayList<>(2);
        surfaces.add(surface);
        surfaces.add(surface2);
        surfaces.add(resultImageReader.getSurface());

    }

    private void setCameraSize(CameraManager cameraManager)throws CameraAccessException{

        for (String cameraID : cameraManager.getCameraIdList()) {
            mCameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
            if(mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)==
                    CameraCharacteristics.LENS_FACING_BACK){

                mStreamConfigurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                Size[] sizes = mStreamConfigurationMap.getOutputSizes(SurfaceTexture.class);
                mCameraSize = sizes[1];

                mCameraID=cameraID;
            }
        }
    }

    public void closeCamera(){
        if(null!= contourImageReader){
            contourImageReader.close();
            contourImageReader =null;
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

        pageDetectThread = new HandlerThread("pageDetect");
        pageDetectThread.start();
        pageDetectHandler = new Handler(pageDetectThread.getLooper());
    }

    private void stopBackgroundThread(){
        handlerThread.quitSafely();
        pageDetectThread.quit();
        try{
            handlerThread.join();
            handlerThread = null;
            backgroundHandler = null;

            pageDetectThread.join();
            pageDetectThread = null;
            pageDetectHandler = null;

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
