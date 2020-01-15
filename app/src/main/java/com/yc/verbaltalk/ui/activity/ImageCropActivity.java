package com.yc.verbaltalk.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;
import com.yc.verbaltalk.utils.HeadImageUtils;

import java.io.File;

public class ImageCropActivity extends BaseSameActivity {


    private DisplayImageOptions options;
    private String fileName = "temp_crop.jpg";
    protected ImageLoader imageLoader;
    private CropImageView mCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        initViews();
        initData();

    }

    private void initViews() {
        mCropImageView = findViewById(R.id.cropImageView);
        ImageButton cancelBtn = findViewById(R.id.crop_cancel_btn);
        ImageButton confirmBtn = findViewById(R.id.crop_confirm_btn);

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        mBaseSameTvSub.setText("确定");
        mBaseSameTvSub.setOnClickListener(this);
    }

    private void initData() {
        //清除历史路径数据
        HeadImageUtils.imgResultPath = null;


        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(50))
                .build();

        if (HeadImageUtils.imgPath != null && HeadImageUtils.imgPath.length() > 0) {
            fileName = HeadImageUtils.imgPath.substring(HeadImageUtils.imgPath.lastIndexOf("/") + 1, HeadImageUtils.imgPath.length());

            //加载本地图片，需要在图片前面加上前缀 "file:///"
            imageLoader.displayImage("file:///" + HeadImageUtils.imgPath, mCropImageView, options);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.getInt("xcrop") > 0 && bundle.getInt("ycrop") > 0) {
                mCropImageView.setCustomRatio(bundle.getInt("xcrop"), bundle.getInt("ycrop"));
            } else {
                //设置自由裁剪
                mCropImageView.setCropMode(CropImageView.CropMode.FREE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.crop_cancel_btn:
                finish();
                break;
            case R.id.crop_confirm_btn:
//                loadDialog = new ProgressDialog(ImageCropActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
//                loadDialog.setMessage("图片裁剪中...");
                if (isValidContext(ImageCropActivity.this)) {
                    mLoadingDialog.showLoadingDialog();
                }
                mCropImageView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
                break;
            case R.id.activity_base_same_tv_sub:
//                loadDialog = new ProgressDialog(ImageCropActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
//                loadDialog.setMessage("图片裁剪中...");
                if (isValidContext(ImageCropActivity.this)) {
                    mLoadingDialog.showLoadingDialog();
                }
                mCropImageView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
                break;
        }
    }

    private Uri createSaveUri() {
        return Uri.fromFile(new File(getExternalCacheDir(), "cropped"));
    }

    private CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            if (cropped != null) {
                HeadImageUtils.cropBitmap = cropped;
                //HeadImageUtils.cutPhoto = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), cropped, null,null));

                /*String urlString = CapturePhotoUtils.insertImage(context.getContentResolver(), cropped, fileName, null);
                if (urlString != null && urlString.length() > 0) {
                    Toast.makeText(context, "3333", Toast.LENGTH_SHORT).show();
                    HeadImageUtils.cutPhoto = Uri.parse(urlString);
                }*/
            }
        }

        @Override
        public void onError() {
            if (isValidContext(ImageCropActivity.this) && mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismissLoadingDialog();
            }
            finish();
        }
    };

    private SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {

            /*if (outputUri != null) {
                //Log.e("outputUri is not null","outputUri is not null---");

                try {
                    HeadImageUtils.cropBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), outputUri);
                    Toast.makeText(context, "not null"+ HeadImageUtils.cropBitmap.getWidth(), Toast.LENGTH_SHORT).show();
                    cancelBtn.setImageBitmap(HeadImageUtils.cropBitmap);
                    HeadImageUtils.cutPhoto = outputUri;
                    Toast.makeText(context, HeadImageUtils.cropBitmap +"", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("outputUri null-----"," outputUri null-----");
                Toast.makeText(context, "uri null", Toast.LENGTH_SHORT).show();
            }*/

            /*if (!TextUtils.isEmpty(outputUri.getAuthority())) {
                Cursor cursor = getContentResolver().query(outputUri,
                        new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (null == cursor) {
                    Toast.makeText(context, "图片没找到", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();
                HeadImageUtils.imgResultPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                Toast.makeText(context, "4444444", Toast.LENGTH_SHORT).show();
            } else {
                //HeadImageUtils.imgResultPath = outputUri.getPath();
                Toast.makeText(context, "555555", Toast.LENGTH_SHORT).show();
            }*/

            if (isValidContext(ImageCropActivity.this) && mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismissLoadingDialog();
            }

            Intent intent = new Intent();
            setResult(HeadImageUtils.FREE_CUT, intent);
            finish();
        }

        @Override
        public void onError() {
            if (isValidContext(ImageCropActivity.this) && mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismissLoadingDialog();
            }
            finish();
        }
    };

    private boolean isValidContext(Context ctx) {
        Activity activity = (Activity) ctx;

        if (Build.VERSION.SDK_INT > 17) {
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (activity == null || activity.isFinishing()) {
                return false;
            } else {
                return true;
            }
        }
    }


    @Override
    protected String offerActivityTitle() {
        return "裁剪图片";
    }
}
