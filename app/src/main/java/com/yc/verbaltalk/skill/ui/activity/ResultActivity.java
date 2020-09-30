package com.yc.verbaltalk.skill.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.music.player.lib.util.ToastUtils;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.view.ShareShowImgDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class ResultActivity extends BaseSameActivity {

    private Bitmap bitmapShow;
    private String mCreateTitle;
    private String mResImagePath;
    private Tencent tencent;
    private IWXAPI mMsgApi;
    private String mFilePath;

    private Bitmap fileBitmap;
    private ImageView mImageView;
    private boolean mIsRepeat = false;

    public static void startResultActivity(Context context, String resImagePath, String createTitle) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("resImagePath", resImagePath);
        intent.putExtra("createTitle", createTitle);
        context.startActivity(intent);
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mResImagePath = intent.getStringExtra("resImagePath");
        mCreateTitle = intent.getStringExtra("createTitle");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (TextUtils.isEmpty(mResImagePath)) {
            ToastUtils.showCenterToast("获取图片失败");
            finish();
            return;
        }
        tencent = Tencent.createInstance(ConstantKey.TENCENT_APP_ID, getApplicationContext());
        mMsgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        mMsgApi.registerApp(ConstantKey.WX_APP_ID);


        initViews();
    }

    private void initViews() {

        mBaseSameTvSub.setText("分享");
        mBaseSameTvSub.setOnClickListener(this);
        mBaseSameTvSub.setTextColor(getResources().getColor(R.color.red_crimson));
        mBaseSameTvSub.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_to_share, 0, 0, 0);
        mBaseSameTvSub.setOnClickListener(this);

        mImageView = findViewById(R.id.result_iv_img);
        ImageView shareLayoutImg = findViewById(R.id.result_iv_share_img);


        creatBitmapLoadImg();
        shareLayoutImg.setOnClickListener(this);
    }

    private void creatBitmapLoadImg() {  //网络图片转Bitmap 对象
        Log.d("mylog", "initViews: mResImagePath  " + mResImagePath);
        if (TextUtils.isEmpty(mResImagePath)) {
            return;
        }
        mLoadingDialog.showLoadingDialog();
        Glide.with(ResultActivity.this).asBitmap().load(mResImagePath).into(new SimpleTarget<Bitmap>() {


            @Override
            public void onLoadFailed(Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Log.d("mylog", "onLoadFailed: ");
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                fileBitmap = resource;
                mImageView.setImageBitmap(fileBitmap);
//                mFilePath = saveToSystemGallery(bitmap);

                Log.d("mylog", "onBitmapLoaded: fileBitmap " + fileBitmap);

                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                super.onLoadCleared(placeholder);
                Log.d("mylog", "onLoadCleared: ");
                mLoadingDialog.dismissLoadingDialog();
            }
        });
        /*Picasso.with(ResultActivity.this).load(mResImagePath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                fileBitmap = bitmap;
                mImageView.setImageBitmap(fileBitmap);
//                mFilePath = saveToSystemGallery(bitmap);

                Log.d("mylog", "onBitmapLoaded: fileBitmap " + fileBitmap);

                if (fileBitmap != null) {
                    mLoadingDialog.dismissLoadingDialog();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("mylog", "onBitmapFailed:  fileBitmap " + fileBitmap);
                if (fileBitmap == null) {
                    creatBitmapLoadImg();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("mylog", "onPrepareLoad: fileBitmap " + fileBitmap);
                Log.d("mylog", "onPrepareLoad: placeHolderDrawable " + placeHolderDrawable);

                *//*if (fileBitmap == null) {
                    creatBitmapLoadImg();
                }*//*

         *//* if (fileBitmap != null || mIsRepeat) {
                    mLoadingDialog.dismissLoadingDialog();
                } else {
                    mImageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsRepeat) {
                                return;
                            }
                            creatBitmapLoadImg();
                            mIsRepeat = true;
                        }
                    }, 200);
                }*//*
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_base_same_tv_sub:  //title sub  分享
                showShareDialog();
                break;
            case R.id.result_iv_share_img:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        if (isValidContext(ResultActivity.this)) {
            if (bitmapShow == null) {
                bitmapShow = BitmapFactory.decodeResource(getResources(), R.mipmap.share_fight_default);
            }
            ShareShowImgDialog shareShowImgDialog = new ShareShowImgDialog(ResultActivity.this, mResImagePath);
            shareShowImgDialog.show();
            shareShowImgDialog.setOnClickShareItemListent(new ShareShowImgDialog.OnClickShareItemListent() {
                @Override
                public void oClickShareItem(int postion) {
                    switch (postion) {
                        case 0: //QQ
                            mFilePath = saveToSystemGallery(fileBitmap);
                            if (TextUtils.isEmpty(mFilePath)) {
                                ToastUtils.showCenterToast("获取图片资源失败，请稍后再试");
                                return;
                            }
                            sharePhotoToQQ(ResultActivity.this, tencent, new QQShareIUiListener());
                            break;
                        case 1: //WX
                            mFilePath = saveToSystemGallery(fileBitmap);
                            if (TextUtils.isEmpty(mFilePath)) {
                                ToastUtils.showCenterToast("获取图片资源失败，请稍后再试");
                                return;
                            }
                            sharePhotoToWeChat();
                            break;
                        case 2: //SAVE
                            mFilePath = saveToSystemGallery(fileBitmap);
                            if (TextUtils.isEmpty(mFilePath)) {
                                ToastUtils.showCenterToast("获取图片资源失败，请稍后再试");
                                return;
                            }
                            saveToSystemGallery(fileBitmap, true);
                            break;
                    }
                }
            });
        }
    }

    private void sharePhotoToWeChat() {

//        第一步：判读图像文件是否存在
//        String path ="/storage/emulated/0/image/123.jpg";
        String path = mFilePath;
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(ResultActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        }

//        第二步：创建WXImageObject，
        WXImageObject imgObj = new WXImageObject();
//        设置文件的路径
        imgObj.setImagePath(path);
//        第三步：创建WXMediaMessage对象，并包装WXimageObjext对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
//        第四步：压缩图片
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap thumBitmap = bitmap.createScaledBitmap(bitmap, 120, 150, true);
//        释放图片占用的内存资源
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumBitmap, true);//压缩图
//        第五步：创建SendMessageTo.Req对象，发送数据
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        唯一标识
        req.transaction = buildTransaction("img");
//        发送的内容或者对象
        req.message = msg;
//        req.scene = send_friend.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mMsgApi.sendReq(req);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap, boolean recycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (recycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void sharePhotoToQQ(final Activity activity, final Tencent tencent, final IUiListener iUiListener) {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, mFilePath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tencent.shareToQQ(activity, params, iUiListener);
            }
        });
    }

    private class QQShareIUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            // 操作成功
//            showToastShort(PostManageActivity.this, "QQShare--操作成功");
            Log.d("mylog", "onComplete: QQShare--操作成功 ");
//            onQQShareSuccessListent.onQQShareSuccess();
        }

        @Override
        public void onError(UiError uiError) {
            // 分享异常
            Log.d("mylog", "onComplete: QQShare--分享异常 ");
            ToastUtils.showCenterToast("QQShare--分享异常" + uiError.errorCode + " " + uiError.errorDetail + " " + uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            // 取消分享
            Log.d("mylog", "onComplete: QQShare--取消分享 ");
//            showToastShort(PostManageActivity.this, "QQShare--取消分享");
        }
    }

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

    /**
     * Bitmap 转成 本地图片
     *
     * @param bmp Bitmap对象
     * @return
     */
    private String saveToSystemGallery(Bitmap bmp) {
        if (bmp == null) {
            creatBitmapLoadImg();
            return "";
        }
        if (!TextUtils.isEmpty(mFilePath)) {
            return mFilePath;
        }
        return saveToSystemGallery(bmp, false);
    }

    private String saveToSystemGallery(Bitmap bmp, boolean isShowToast) {

        Log.d("mylog", "saveToSystemGallery: bmp " + bmp);

        // 首先保存图片
//        File fileDir = new File(Environment.getExternalStorageDirectory(), SdPathConfig.SAVE_IMG_PATH);
        File fileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        final File file = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
        //图片保存成功，图片路径：
        if (isShowToast) {
            String filePath = file.getAbsolutePath();
            Log.d("mylog", "onClick: filePath  filePath---------- " + filePath);
            if (Build.VERSION.SDK_INT < 24) {
                Snackbar.make(mImageView, "图片已保存至相册", Snackbar.LENGTH_LONG)
                        .setAction("查看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                                        new CheckRequestPermissionListener() {
                                            @Override
                                            public void onPermissionOk(Permission permission) {

                                                Intent photoInten = new Intent();
                                                String path = file.getAbsolutePath();   //图片路径
                                                File file = new File(path);

                                                Log.d("mylog", "onPermissionOk: path " + path);
                                                Log.d("mylog", "onPermissionOk: file " + file);
                                                Log.d("mylog", "onPermissionOk: file.exists() " + file.exists());

                                                Uri uri;
                                                photoInten.setAction(Intent.ACTION_VIEW);
                                                if (Build.VERSION.SDK_INT >= 24) {
                                                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
                                                    photoInten.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                } else {
                                                    uri = Uri.fromFile(file);
                                                }
                                                photoInten.setDataAndType(uri, "image/*");
                                                startActivity(photoInten);
                                            }

                                            @Override
                                            public void onPermissionDenied(Permission permission) {
                                            }
                                        });


                            }
                        })
                        .show();
            } else {
//                showToastShort("图片已保存路径：" + file.getAbsolutePath());
                ToastUtils.showCenterToast("图片已保存至设备图库");
            }
        }
//        Toast.makeText(this,
//                "图片保存路径：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        return file.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fileBitmap != null) {
            fileBitmap = null;
        }
    }

    @Override
    protected String offerActivityTitle() {
        if (TextUtils.isEmpty(mCreateTitle)) {
            mCreateTitle = "合成成功";
        }
        return mCreateTitle;
    }
}
