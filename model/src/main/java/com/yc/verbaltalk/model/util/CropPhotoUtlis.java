package com.yc.verbaltalk.model.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Administrator on 2018/3/28.
 */

public class CropPhotoUtlis {

    public static void cropPhoto(Activity activity, Uri uri, File outFile, int REQUEST_CROP) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 设置裁剪
        intent.putExtra("aspectX", 1); // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 1000);// outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputY", 1000);
        //剪裁后过小时，拉伸
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
        activity.startActivityForResult(intent, REQUEST_CROP);
    