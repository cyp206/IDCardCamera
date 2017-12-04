package com.mu.zi.idcardcamera.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mu.zi.idcardcamera.R;
import com.mu.zi.idcardcamera.activity.TakePhotoActivity;
import com.mu.zi.idcardcamera.bean.UpLoadPicItem;
import com.mu.zi.idcardcamera.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Avazu Holding on 2017/11/28.
 * 申请贷款信息第二步
 */

public class UploadICPhotosFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = UploadICPhotosFragment.class.getSimpleName();

    @BindView(R.id.iv_front_id_card)
    ImageView ivFrontIdCard;
    @BindView(R.id.iv_front_take_photo)
    ImageView ivFrontTakePhoto;
    @BindView(R.id.iv_back_id_card)
    ImageView ivBackIdCard;
    @BindView(R.id.iv_back_take_photo)
    ImageView ivBackTakePhoto;
    @BindView(R.id.iv_yourself_id_card)
    ImageView ivYourselfIdCard;
    @BindView(R.id.iv_yourself_take_photo)
    ImageView ivYourselfTakePhoto;
    private Unbinder bind;

    public static UploadICPhotosFragment newInstance() {
        UploadICPhotosFragment fragment = new UploadICPhotosFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        ivBackTakePhoto.setOnClickListener(this);
        ivFrontTakePhoto.setOnClickListener(this);
        ivYourselfTakePhoto.setOnClickListener(this);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ic_photo_upload;
    }


    @Override
    public void onDestroyView() {
        if (bind != null) bind.unbind();
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back_take_photo:
                TakePhotoActivity.startTakePhotoActivity(this, TakePhotoActivity.BACK);

                break;
            case R.id.iv_front_take_photo:
                TakePhotoActivity.startTakePhotoActivity(this, TakePhotoActivity.FRONT);


                break;
            case R.id.iv_yourself_take_photo:
                TakePhotoActivity.startTakePhotoActivity(this, TakePhotoActivity.SELF);


                break;

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != resultCode) return;
        String photoName = data.getStringExtra(TakePhotoActivity.PICTURE_NAME);
        if (photoName == null || "".equals(photoName)) return;
        Log.i("snow", "requestCode:" + requestCode + "__photoName_" + photoName);
        String filePath = Constants.BaseDir.PHOTODIR + photoName;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap == null) return;
        if (resultCode == TakePhotoActivity.BACK) {
            map.put(TakePhotoActivity.BACK + "", new UpLoadPicItem(photoName, filePath));
            ivBackIdCard.setImageBitmap(bitmap);
        }
        if (resultCode == TakePhotoActivity.FRONT) {
            ivFrontIdCard.setImageBitmap(bitmap);
            map.put(TakePhotoActivity.FRONT + "", new UpLoadPicItem(photoName, filePath));

        }
        if (resultCode == TakePhotoActivity.SELF) {
            ivYourselfIdCard.setImageBitmap(bitmap);
            map.put(TakePhotoActivity.SELF + "", new UpLoadPicItem(photoName, filePath));

        }


    }

    HashMap<String, UpLoadPicItem> map = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void submit() {
        if (map.size() < 3) {
            Toast.makeText(getContext(), getContext().getString(R.string.basic_information_enter_required_info), Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<String> strings = new ArrayList<>();
        strings.add(map.get(TakePhotoActivity.FRONT + "").getFilePath());
        strings.add(map.get(TakePhotoActivity.BACK + "").getFilePath());
        strings.add(map.get(TakePhotoActivity.SELF + "").getFilePath());

        List<UpLoadPicItem> upLoadPicItems = new ArrayList<>();

        for (HashMap.Entry<String, UpLoadPicItem> entry : map.entrySet()) {
            upLoadPicItems.add(entry.getValue());
        }


        // TempleUtils.httpUploadPicsThread(getContext(), uploadIdCard(), null,upLoadPicItems , null);

//        HttpHelper.obtain().postFile(uploadIdCard(), strings, new HttpCallback<Object>() {
//            @Override
//            public void onSuccess(Object testResult) {
////                if (testResult.body != null) {
////                    EventBus.getDefault().post(new EventBusUtils.EventGoods(testResult.body));
////                }
//                FinishStateModel finishStateModel = new FinishStateModel();
//                finishStateModel.index = 2;
//                finishStateModel.isFinish = true;
//                EventBus.getDefault().post(finishStateModel);
//
//            }
//
//            @Override
//            public void onFailed(String string) {
//                Log.i("hbin", "进行图片上传:onFailed" + string);
//
//            }
//        });
    }


}
