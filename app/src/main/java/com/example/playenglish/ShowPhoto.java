package com.example.playenglish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.content.ContentValues.TAG;

public class ShowPhoto extends AppCompatActivity {

    private ImageView resultPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        resultPhoto = findViewById(R.id.Image_Show_Photo_ImageView);
        showPictureView();
        Log.d(TAG, "onCreate");
    }

    private void showPictureView() {
        String filePath = getIntent().getStringExtra(CameraActivity.FILE_PATH);
        if (!TextUtils.isEmpty(filePath)) {
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                Matrix matrix = new Matrix();
                matrix.setRotate(90);//旋转90度
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                resultPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
