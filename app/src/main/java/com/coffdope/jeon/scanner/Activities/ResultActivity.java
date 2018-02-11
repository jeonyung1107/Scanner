package com.coffdope.jeon.scanner.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.coffdope.jeon.scanner.R;
import com.coffdope.jeon.scanner.fragments.CameraControl;

import java.io.File;

public class ResultActivity extends AppCompatActivity {
    Button saveButton, modifyButton;
    ImageView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        saveButton = (Button)findViewById(R.id.save_button);
        modifyButton = (Button)findViewById(R.id.modify_button);
        resultView = (ImageView)findViewById(R.id.resultview);



        Intent imgintent = getIntent();
        File img = new File(imgintent.getData().getPath());

        if(img.exists()){
            Bitmap bmp = BitmapFactory.decodeFile(img.getAbsolutePath());
            resultView.setImageBitmap(bmp);
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
}
