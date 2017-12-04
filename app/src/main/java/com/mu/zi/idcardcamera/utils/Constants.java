package com.mu.zi.idcardcamera.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Avazu Holding on 2017/11/27.
 */

public class Constants {
    public static final int OPEN_NETWORK = 1000;

    public static class BaseUrl {
        public static String BaseAmazonUrl = "https://www.amazon.com/?&_encoding=UTF8&tag=opg0a-20&linkCode=ur2&linkId=21a09f96c49ad98289b9ef440ec82684&camp=1789&creative=9325";
        public static String AmazonUrlEntrance = "https://www.amazon.com/?&_encoding=UTF8&tag=opg0a-20&linkCode=ur2&linkId=21a09f96c49ad98289b9ef440ec82684&camp=1789&creative=9325";
        public static String AmazonSearchUrl = BaseAmazonUrl + "/s/ref=nb_sb_noss?field-keywords=";
        public static String BaseTaoBaoiUrl = "https://s.taobao.com";
        public static String TaoBaoSearchUrl = BaseTaoBaoiUrl + "/search?q=";
        public static String TaoBaoEntrance = "https://m.taobao.com/#index";


    }

    public static class BaseDir {
        public static final String APPNAME = "sphereby";
        public static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + APPNAME + File.separator;
        public static final String PHOTODIR = DIR + File.separator + "DIM" + File.separator;
    }
}
