package com.yc.verbaltalk.model.engin;

import android.util.Log;

import com.yc.verbaltalk.model.domain.URLConfig;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mayn on 2019/5/17.
 */

public class UploadPhotoEngin {

    private static String mImageType = "multipart/form-data";

    public UploadPhotoEngin(File file, Callback responseCallback) {
        OkHttpClient okHttpClient = new OkHttpClient();

        /*MultipartBody.Builder builder1 = new MultipartBody.Builder();//构建者模式
        builder1.setType(MultipartBody.FORM);//传输类型
        Log.d("mylog", "UploadPhotoEngin: file.getPath " + file.getPath());
        boolean b = file.renameTo(new File("type.png"));
        Log.d("mylog", "UploadPhotoEngin: b " + b);
        Log.d("mylog", "UploadPhotoEngin: file.getPath " + file.getPath());
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        *//*builder1.addFormDataPart("image", "image", RequestBody.create(MediaType.parse
                ("multipart/form-data"), file));*//*
        builder1.addFormDataPart("image", "image", RequestBody.create(MEDIA_TYPE_MARKDOWN, file));*/

        Log.d("securityhttp", "UploadPhotoEngin: request url " + URLConfig.uploadPhotoUrl);

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("image", "images", fileBody)
                .addFormDataPart("imagetype", mImageType)
                .build();

        //表单数据参数填入
        final Request request = new Request.Builder()
                .url(URLConfig.uploadPhotoUrl)
//                .post(builder1.build())
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(responseCallback);
    }
}
