package com.example.playenglish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CameraActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {

    private SurfaceView surfaceview;
    private Camera camera;
    private Button takePhoto;
    private SurfaceHolder surfaceHolder;
    public static final String FILE_PATH = "filePath";//注意内存卡必须要有这个文件夹

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initView();
    }

    private void initView() {
        surfaceview = findViewById(R.id.camera_surfaceView);
        takePhoto = findViewById(R.id.CAP_Camera_Button);
        surfaceview.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        surfaceHolder = surfaceview.getHolder();
        surfaceHolder.addCallback(this);//SurfaceHolder.Callback在底层的Surface状态发生变化的时候通知View
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == camera) {
            camera = getCustomCamera();
            if (surfaceHolder != null) {
                previceCamera(camera, surfaceHolder);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();//释放
            camera = null;
        }
    }

    private void previceCamera(Camera camera, SurfaceHolder surfaceHolder) {
        Camera.Parameters parameters = camera.getParameters();//因素
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100); // 设置照片质量
        parameters.setPreviewSize(1024,768);
        parameters.setPictureSize(1024,768);
        camera.setParameters(parameters);// 设置相机参数
        try {
            camera.setPreviewDisplay(surfaceHolder);//把摄像头与SurfaceHolder进行绑定
            camera.setDisplayOrientation(90);//旋转90度
            camera.startPreview();//相机预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        previceCamera(camera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        previceCamera(camera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != camera) {
            camera.setPreviewCallback(null);//停止预览
            camera.stopPreview();//释放相机资源
            camera.release();//释放
            camera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.READ_Camera_Button:
                if (null != camera) {
                    camera.autoFocus(null);
                }
                break;
            case R.id.CAP_Camera_Button:
                startTakePhoto();
                break;
        }
    }

    private void startTakePhoto() {
//        cameraSetSomething();
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {//聚焦
                if (success) {//拍照
                    camera.takePicture(null, null, new Camera.PictureCallback() {//快门，原料
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            dealWithCameraData(data);
                        }
                    });
                }
            }
        });
    }


    private void dealWithCameraData(byte[] data) {
        FileOutputStream fileOutputStream = null;
        String tempStr = "/sdcard/ldm/";//图片临时保存位置
        String fileName = tempStr + System.currentTimeMillis() + ".jpg";
        Log.d(TAG, "dealWithCameraData: "+fileName);
        File tempFile = new File(fileName);
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(data);//保存图片数据
            fileOutputStream.close();
            Intent intent = new Intent(CameraActivity.this, ShowPhoto.class);
            intent.putExtra(FILE_PATH, fileName);
            startActivity(intent);
            Log.d(TAG, "Intent");
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Camera getCustomCamera() {
        if (null == camera) {
            camera = Camera.open();
        }
        return camera;
    }
}
