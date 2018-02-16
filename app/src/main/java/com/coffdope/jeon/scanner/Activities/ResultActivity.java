package com.coffdope.jeon.scanner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.coffdope.jeon.scanner.R;
import com.coffdope.jeon.scanner.fragments.CameraControl;
import com.coffdope.jeon.scanner.func.Detector;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.*;

import java.io.File;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    Button saveButton, modifyButton;
    EditText imageName;
    ImageView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        saveButton = (Button)findViewById(R.id.save_button);
        modifyButton = (Button)findViewById(R.id.modify_button);
        resultView = (ImageView)findViewById(R.id.resultview);
        imageName = (EditText)findViewById(R.id.imageName);


        Intent imgintent = getIntent();
        File img = new File(imgintent.getData().getPath());
        MatOfPoint contour = MatOfPoint.fromNativeAddr(imgintent.getLongExtra(CameraControl.RESULT_CNT,0L));


        if(img.exists()&&null!=contour){
            Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());
            Bitmap transformed = transform(bmp,contour);

            resultView.setImageBitmap(transformed);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    Bitmap transform(Bitmap original,MatOfPoint contour){
        Bitmap bmp = original;
        Mat forTransform = new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmp,forTransform);
        Mat transformed = Detector.four_point_transform(contour,forTransform);

        return Detector.MTB(transformed);
    }
}
