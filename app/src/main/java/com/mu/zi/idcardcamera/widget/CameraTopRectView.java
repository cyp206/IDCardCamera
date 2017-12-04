package com.mu.zi.idcardcamera.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;


import com.mu.zi.idcardcamera.activity.TakePhotoActivity;

import java.io.IOException;
import java.io.InputStream;

import static com.mu.zi.idcardcamera.utils.DisplayUtil.dp2px;
import static com.mu.zi.idcardcamera.utils.DisplayUtil.sp2px;

/**
 * Created by y on 2017/11/28.
 */

public class CameraTopRectView extends View {

    private int panelWidth;
    private int panelHeght;

    private int viewWidth;
    private int viewHeight;

    public int rectWidth;
    public int rectHeght;

    private int rectTop;
    private int rectLeft;
    private int rectRight;
    private int rectBottom;

    private int lineLen;
    private static final int LINE_WIDTH = 8;


    private static final int ID_CARD_WIDTH = 250;
    private static final String TIPS = "PLEASE ALIGN THE FRONT FACE OF";
    private static final String TIPSSec = "THE DOCUMENT TO THE PHOTOGRAPHED AREA";

    private Paint linePaint;
    private Paint wordPaint;
    private Rect rect;
    private int baseline;
    private TextPaint textPaint;
    private Bitmap headBtimap;
    private Paint bitmapPaint;
    private Bitmap backRetBitmap;
    private Bitmap dogIdBitmap;
    private int type;

    public CameraTopRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Activity activity = (Activity) context;

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        panelWidth = wm.getDefaultDisplay().getWidth();//拿到屏幕的宽
        panelHeght = wm.getDefaultDisplay().getHeight();//拿到屏幕的高

        //高度不需要dp转换px,不然整体相机会向上移动一小节
//        viewHeight = panelHeght - (int) DisplayUtil.dp2px(activity,TOP_BAR_HEIGHT + BOTTOM_BTN_HEIGHT);

        viewHeight = panelHeght;
        //viewHeight,界面的高,viewWidth,界面的宽
        viewWidth = panelWidth;

        rectWidth = dp2px(getContext(), ID_CARD_WIDTH);
        rectHeght = (int) (rectWidth * 85.6 / 54);
        // 相对于此view

        rectTop = (viewHeight - rectHeght) / 2;
        rectLeft = (viewWidth - rectWidth) / 2;
        rectBottom = rectTop + rectHeght;
        rectRight = rectLeft + rectWidth;

//        lineLen = panelWidth / 8;
        lineLen = dp2px(getContext(), 19);
        wordPaint = new Paint();
        wordPaint.setAntiAlias(true);
        wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wordPaint.setStrokeWidth(3);
        wordPaint.setTextSize(35);

        type = 1;
        rect = new Rect(rectLeft, rectTop - 80, rectRight, rectTop - 10);
        wordPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        wordPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(rect, wordPaint);

        //画蒙层
        wordPaint.setColor(0xa0000000);
        rect = new Rect(0, viewHeight / 2 + rectHeght / 2, viewWidth, viewHeight);
        canvas.drawRect(rect, wordPaint);

        rect = new Rect(0, 0, viewWidth, viewHeight / 2 - rectHeght / 2);
        canvas.drawRect(rect, wordPaint);

        rect = new Rect(0, viewHeight / 2 - rectHeght / 2, (viewWidth - rectWidth) / 2, viewHeight / 2 + rectHeght / 2);
        canvas.drawRect(rect, wordPaint);

        rect = new Rect(viewWidth - (viewWidth - rectWidth) / 2, viewHeight / 2 - rectHeght / 2, viewWidth, viewHeight / 2 + rectHeght / 2);
        canvas.drawRect(rect, wordPaint);
        drawLines(canvas);


        if (type == TakePhotoActivity.FRONT) {
            //id卡正面
            drawCardFront(canvas);
        }
        if (type == TakePhotoActivity.BACK) {
            //id卡 背面
            drawCardBack(canvas);
        }
        if (type == TakePhotoActivity.SELF) {
            //狗 和id 卡
            drawDogAndCard(canvas);
        }


        //文字
        int save = canvas.save();
        canvas.rotate(90, getWidth() / 2, getHeight() / 2);
        canvas.drawText(TIPS, (getWidth() / 2), getHeight() / 2 - (getWidth() / 2) + dp2px(getContext(), 17), getTextPaint());
        canvas.drawText(TIPSSec, (getWidth() / 2), getHeight() / 2 - (getWidth() / 2) + dp2px(getContext(), 17 + 17), getTextPaint());
    }


    private final int DogIdWidth = dp2px(getContext(), 234);
    private final int DogIdHeight = dp2px(getContext(), 318);
    private final int DogIdMarginLeft = 8;
    private final int DogIdMarginTop = 30;

    //狗 和 id card
    private void drawDogAndCard(Canvas canvas) {
        int left = getRectLeft() + dp2px(getContext(), DogIdMarginLeft);
        int top = getRectTop() + dp2px(getContext(), DogIdMarginTop);
        int right = left + DogIdWidth;
        int bottom = top + DogIdHeight;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(getDogIdBitmap(), null, rect, getBitmapPaint());


    }


    //    画线
    private void drawLines(Canvas canvas) {
        int padding = LINE_WIDTH / 2;
        // left  top x
        canvas.drawLine(rectLeft - padding, rectTop, rectLeft + lineLen, rectTop, getLinePaint());
        //right top x
        canvas.drawLine(rectRight - lineLen, rectTop, rectRight + padding, rectTop, getLinePaint());
        //left  top y
        canvas.drawLine(rectLeft, rectTop, rectLeft, rectTop + lineLen, getLinePaint());
        //right top  y
        canvas.drawLine(rectRight, rectTop, rectRight, rectTop + lineLen, getLinePaint());
        //left bottom x
        canvas.drawLine(rectLeft - padding, rectBottom, rectLeft + lineLen, rectBottom, getLinePaint());
        //right bottom x
        canvas.drawLine(rectRight - lineLen, rectBottom, rectRight + padding, rectBottom, getLinePaint());
        //left bottom y
        canvas.drawLine(rectLeft, rectBottom - lineLen, rectLeft, rectBottom, getLinePaint());
        //right bottom y
        canvas.drawLine(rectRight, rectBottom - lineLen, rectRight, rectBottom, getLinePaint());
    }


    private final int HeadWidth = dp2px(getContext(), 103);
    private final int HeadHeight = dp2px(getContext(), 82);
    private final int HeadMarginLeft = 76;
    private final int HeadMarginTop = 21;

    private void drawCardFront(Canvas canvas) {
        int left = getRectLeft() + dp2px(getContext(), HeadMarginLeft);
        int top = getRectTop() + dp2px(getContext(), HeadMarginTop);
        int right = left + HeadWidth;
        int bottom = top + HeadHeight;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(getHeadBitmap(), null, rect, getBitmapPaint());
    }


    public int getRectLeft() {
        return rectLeft;
    }

    public int getRectTop() {
        return rectTop;
    }

    public int getRectRight() {
        return rectRight;
    }

    public int getRectBottom() {
        return rectBottom;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }


    public TextPaint getTextPaint() {
        if (textPaint == null) {
            textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setStrokeWidth(3);
            textPaint.setTextSize(sp2px(getContext(), 15));
            textPaint.setColor(Color.WHITE);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        return textPaint;
    }


    public Paint getBitmapPaint() {
        if (bitmapPaint == null) {
            bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        return bitmapPaint;

    }


    public Bitmap getHeadBitmap() {
        if (headBtimap == null) {
            try {
                InputStream is = getContext().getResources().getAssets().open("icon_head_portrait.png");
                headBtimap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return headBtimap;
    }

    public static final String cornerColor = "#FD9827";

    public Paint getLinePaint() {
        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.parseColor(cornerColor));
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(LINE_WIDTH);// 设置线宽
            linePaint.setAlpha(255);
        }
        return linePaint;
    }


    private final int BackRecWidth = dp2px(getContext(), 114);
    private final int BackRecHeight = dp2px(getContext(), 104);
    private final int BackRecMarginLeft = 70;
    private final int BackRecMarginTop = 30;

    private void drawCardBack(Canvas canvas) {
        int left = getRectLeft() + dp2px(getContext(), BackRecMarginLeft);
        int top = getRectTop() + dp2px(getContext(), BackRecMarginTop);
        int right = left + BackRecWidth;
        int bottom = top + BackRecHeight;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(getBackRetBitmap(), null, rect, getBitmapPaint());
    }

    public Bitmap getBackRetBitmap() {
        if (backRetBitmap == null) {
            try {
                InputStream is = getContext().getResources().getAssets().open("bitmap_finger_layout.png");
                backRetBitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return backRetBitmap;
    }

    public Bitmap getDogIdBitmap() {
        if (dogIdBitmap == null) {
            try {
                InputStream is = getContext().getResources().getAssets().open("bitmap_people_dog.png");
                dogIdBitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dogIdBitmap;
    }


    //记得主动去回收bitmap
    public void onDestroy() {
        recycleBitmap(dogIdBitmap);
        recycleBitmap(backRetBitmap);
        recycleBitmap(headBtimap);


    }


    public void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;

        }
    }

    public void setType(int type) {
        this.type = type;
        invalidate();
    }


    public int getType() {
        return type;
    }
}