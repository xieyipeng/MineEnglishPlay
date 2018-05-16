package com.example.playenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * 两个bug
 * 按下拍照按钮时图像拉伸
 * 从sd卡获取图片时图片显示不完整
 */
public class MainActivity extends AppCompatActivity {


    Button capButton;
    Button checkButton;
    Button otherButton;
    ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        in_click();

    }

    private void init() {
        capButton = findViewById(R.id.CAP_Main_Button);
        checkButton = findViewById(R.id.CHECK_Main_Button);
        searchImageView = findViewById(R.id.Search_Main_ImageView);
        otherButton=findViewById(R.id.translation_Main_Button);
    }

    private void in_click() {
        capButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCamera = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(toCamera);
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCheck = new Intent(MainActivity.this, CheckActivity.class);
                startActivity(toCheck);
            }
        });
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRead = new Intent(MainActivity.this, TranslationActivity.class);
                startActivity(toRead);
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCamera = new Intent(MainActivity.this, CheckActivity.class);
                startActivity(toCamera);
            }
        });
    }
}
