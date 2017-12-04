package com.mu.zi.idcardcamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import com.mu.zi.idcardcamera.R;
import com.mu.zi.idcardcamera.fragment.UploadICPhotosFragment;
import com.mu.zi.idcardcamera.widget.CameraSurfaceView;
import com.mu.zi.idcardcamera.widget.CameraTopRectView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.mu.zi.idcardcamera.utils.Constants.BaseDir.PHOTODIR;


public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraSurfaceView mCameraSurfaceView;
    public static final int FRONT = 1;
    public static final int BACK = 2;
    public static final int SELF = 3;
    public static final String PHOTO_TYPE = "photo_type";
    public static final String PICTURE_NAME = "photo_name";
    private CameraTopRectView topView;
    private int type;


    public static void startTakePhotoActivity(UploadICPhotosFragment fragment, int type) {
        Intent intent = new Intent(fragment.getActivity(), TakePhotoActivity.class);
        intent.putExtra(PHOTO_TYPE, type);
        fragment.startActivityForResult(intent, type);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mu_t);


        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);

        findViewById(R.id.takePic).setOnClickListener(this);
        findViewById(R.id.iv_btn_close).setOnClickListener(this);
        findViewById(R.id.iv_btn_switch).setOnClickListener(this);

        findViewById(R.id.iv_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        topView = findViewById(R.id.rectOnCamera);
        type = getIntent().getIntExtra(PHOTO_TYPE, -1);
        if (type > 0) topView.setType(type);

    }


    //创建jpeg图片回调数据对象
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            topView.draw(new Canvas());
            BufferedOutputStream bos = null;
            Bitmap resultBitmap = null;
            Bitmap rotateBitmap = null;
            Bitmap sizeBitmap = null;
            if (data == null) return;
            try {
                // 获得图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                resultBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    String
                    long current = System.currentTimeMillis();
                    String fileName = current + ".JPEG";
                    String filePath = PHOTODIR + fileName;//照片保存路径

//                    //图片存储前旋转
                    Matrix m = new Matrix();
                    int height = resultBitmap.getHeight();
                    int width = resultBitmap.getWidth();
                    m.setRotate(90);
                    //旋转后的图片
                    rotateBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, width, height, m, true);
                    System.out.println("执行了吗+3");
                    File file = new File(filePath);
                    File dir = new File(PHOTODIR);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    sizeBitmap = Bitmap.createScaledBitmap(rotateBitmap,
                            topView.getViewWidth(), topView.getViewHeight(), true);
                    resultBitmap = Bitmap.createBitmap(sizeBitmap, topView.getRectLeft(),
                            topView.getRectTop(),
                            topView.getRectRight() - topView.getRectLeft(),
                            topView.getRectBottom() - topView.getRectTop());// 截取
                    resultBitmap = adjustPhotoRotation(resultBitmap, -90);

                    resultBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);//将图片压缩到流中

                    //先这个退出
                    Intent intent = new Intent();
                    intent.putExtra(PICTURE_NAME, fileName);
                    setResult(type, intent);
                    finish();
                } else {
                    Toast.makeText(TakePhotoActivity.this, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.flush();//输出
                        bos.close();//关闭
                    }
                    recycleBitmap(rotateBitmap);// 回收bitmap空间
                    recycleBitmap(resultBitmap);// 回收bitmap空间
                    recycleBitmap(sizeBitmap);// 回收bitmap空间
                    camera.stopPreview();// 关闭预览
                    camera.startPreview();// 开启预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };


    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }


    public void recycleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return;
        bitmap.recycle();
        bitmap = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePic:
                mCameraSurfaceView.takePicture(pictureCallback);
                break;

            case R.id.iv_btn_switch:
                mCameraSurfaceView.switchCamera();
                break;

            case R.id.iv_btn_close:
                finish();
                break;

        }
    }
}