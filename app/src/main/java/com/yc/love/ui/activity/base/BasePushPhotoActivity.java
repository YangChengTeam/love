package com.yc.love.ui.activity.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.model.util.CropPhotoUtlis;
import com.yc.love.ui.view.CircleTransform;
import com.yc.love.ui.view.SelectPhotoDialog;
import com.yc.love.ui.view.imgs.ISCameraConfig;
import com.yc.love.ui.view.imgs.ISListConfig;
import com.yc.love.ui.view.imgs.ISNav;
import com.yc.love.ui.view.imgs.ImageLoader;
/*import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by mayn on 2019/5/7.
 */

public abstract class BasePushPhotoActivity extends BaseSameActivity {


    private ImageView putPhotoImageViewPhoto;
    private SelectPhotoDialog selectPhotoDialog;
    //    private static final String PICTURE_FILE = "temp.jpg";
//    private File mImageFile;
    private static final int TAKE_PICTURE_ALBUM = 666;
    private static final int TAKE_PICTURE_CAMERA = 667;
    private static final int CAMERA_REQUEST_CODE = 5241;
    //    private static final int REQUEST_CROP = 6569;
    private ISNav mInstance;

    public void showSelsctPhotoDialog(ImageView putPhotoImageViewPhoto) {
        this.putPhotoImageViewPhoto = putPhotoImageViewPhoto;
        initPhotoDialog();
        // 自定义图片加载器
        if (mInstance == null) {
            mInstance = ISNav.getInstance();
            mInstance.init(new ImageLoader() {
                @Override
                public void displayImage(Context context, String path, ImageView imageView) {
                    Glide.with(context).load(path).into(imageView);
                }
            });
        }
    }

    private void initPhotoDialog() {
        selectPhotoDialog = new SelectPhotoDialog() {
            @Override
            protected void clickAlbum() {
                openAlbum();
                selectPhotoDialog.dialogDismiss();
            }

            @Override
            protected void clickCamera() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                } else {
                    openCamera();
                }
            }
        };
        selectPhotoDialog.instanceDialog(this);
    }

    public void openAlbum() {
//        ImageSelectorUtils.openPhotoAndClip(this, TAKE_PICTURE_ALBUM);
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.WHITE)
                // 返回图标ResId
                .backResId(R.mipmap.icon_arr_lift_black)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.BLACK)
                // TitleBar背景色
                .titleBgColor(Color.WHITE)
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build();
        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, TAKE_PICTURE_ALBUM);
    }

    public static final String PICTURE_FILE = "temp.jpg";

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());// 开启
//            builder.detectFileUriExposure();
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PICTURE_FILE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE_CAMERA);

        /*ISCameraConfig config = new ISCameraConfig.Builder().needCrop(true)
                // 裁剪
                .cropSize(1, 1, 200, 200).build();
        ISNav.getInstance().toCameraActivity(this, config, TAKE_PICTURE_CAMERA);*/
        selectPhotoDialog.dialogDismiss();
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions,
                                           int[] grantResults) {
        Log.d("mylog", "onRequestPermissionsResult: permsRequestCode " + permsRequestCode);
        switch (permsRequestCode) {
            case CAMERA_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    openCamera();
                } else {
                    showToastShort("没有获取到拍照的权限");
                    Log.d("ssss", "没有权限操作这个请求");
                    selectPhotoDialog.dialogDismiss();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
//        Bitmap photo = null;
        File file = null;
        switch (requestCode) {
            case TAKE_PICTURE_ALBUM:

                ArrayList<String> images = data.getStringArrayListExtra("result");
                for (String path : images) {
//                    tvResult.append(path + "\n");
                    Log.d("mylog", "onActivityResult: path " + path);
                }

                /*//获取选择器返回的数据
                ArrayList<String> images = data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT);*/
                if (images == null || images.size() <= 0) {
                    return;
                }
                String image = images.get(0);
                if (TextUtils.isEmpty(image)) {
                    return;
                }
                file = new File(image);
                break;
            case TAKE_PICTURE_CAMERA:
             /*   String path = data.getStringExtra("result"); // 图片地址
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                file = new File(path);*/

                file = new File(Environment.getExternalStorageDirectory()
                        + "/" + PICTURE_FILE);

                break;
           /* case REQUEST_CROP:
                if (!mImageFile.exists()) {
                    Toast.makeText(this, "mImageFile REQUEST_CROP 图片不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                file = mImageFile;
                break;*/
            default:
                break;
        }
        if (file == null) {
            showToastShort("获取图片失败--file");
            return;
        }
        Luban.with(this)
                .load(file)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
//                .setTargetDir(this.getFileStreamPath("certificatess").getAbsolutePath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                        Log.d("mylog", "onStart: dddddddddddd");
                    }

                    @Override
                    public void onSuccess(File file) {
                        //        View 设置图片
                        Picasso.with(BasePushPhotoActivity.this).load(file).transform(new CircleTransform()).into(putPhotoImageViewPhoto);
                        Log.d("mylog", "onSuccess: file.getPath() " + file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("mylog", "onError: " + e.toString());
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }
}
