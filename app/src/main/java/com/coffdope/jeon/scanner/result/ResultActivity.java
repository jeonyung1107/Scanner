package com.coffdope.jeon.scanner.result;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.coffdope.jeon.scanner.R;
import com.coffdope.jeon.scanner.camera.CameraFragment;
import com.coffdope.jeon.scanner.imgProcess.PerspectiveTransformer;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button saveButton, modifyButton;
    EditText imageName;
    ImageView resultView;
    Bitmap transformed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        saveButton = (Button)findViewById(R.id.save_button);
        modifyButton = (Button)findViewById(R.id.modify_button);
        resultView = (ImageView)findViewById(R.id.resultview);
        imageName = (EditText)findViewById(R.id.imageName);

        String timeStamp = new SimpleDateFormat("YYMMDD").format(new Date());
        imageName.setText(timeStamp);

        Intent imgintent = getIntent();
        File img = new File(imgintent.getData().getPath());
        double[] resultContourInDouble = imgintent.getDoubleArrayExtra(CameraFragment.RESULT_CNT);
        Point[] resultContourPoints = new Point[4];

        for(int i = 0; i<4; ++i){
            resultContourPoints[i] = new Point();
            resultContourPoints[i].x = resultContourInDouble[i*2];
            resultContourPoints[i].y = resultContourInDouble[i*2 + 1];
        }

        MatOfPoint contour = new MatOfPoint(resultContourPoints);


            if (img.exists() && null != contour && contour.toArray().length > 0) {
                Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());
                transformed = PerspectiveTransformer.bitmapPerspectiveTransform(bmp, contour);

                resultView.setImageBitmap(transformed);
            } else {
                Toast.makeText(this, "No Contour", Toast.LENGTH_LONG).show();
                finish();
            }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
                }
                String fileName = imageName.getText().toString();
                saveResult(fileName,transformed);
                Toast.makeText(view.getContext(),fileName+" Saved",Toast.LENGTH_LONG).show();
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void saveResult(String name,Bitmap result){
        String fileName = name + ".jpg";
        File img = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),fileName);
        try {
            FileOutputStream fos = new FileOutputStream(img);
            result.compress(Bitmap.CompressFormat.JPEG,100,fos);

        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }

    }
}
