package com.coffdope.jeon.scanner.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.NonNull;

/**
 * Created by jeon on 18. 3. 7.
 */

public class CaptureCallback extends CameraCaptureSession.CaptureCallback {
    private static CaptureCallback captureCallback;

    private CaptureCallback(){}

    public static CaptureCallback getInstance(){
        if(null == captureCallback){
            captureCallback = new CaptureCallback();
        }
        return captureCallback;
    }

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
}
