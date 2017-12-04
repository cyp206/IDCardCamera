package com.mu.zi.idcardcamera.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by avazu on 2017/7/21.
 */

public class UpLoadPicItem {

    public String strPicName;
    private Bitmap bmp;
    public String strPicPath;

    public UpLoadPicItem(String strPicName, String strPicPath) {
        this.strPicName = strPicName;
        this.strPicPath = strPicPath;
    }

    public Bitmap getBmp() {
        return BitmapFactory.decodeFile(strPicPath);
    }

    public String getFilePath() {
        return strPicPath;
    }
}
