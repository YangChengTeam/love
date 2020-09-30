package com.yc.verbaltalk.base.engine;

import android.content.Context;
import android.util.Log;

import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.base.httpinterface.HttpRequestInterface;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;

/**
 * Created by sunshey on 2019/5/17.
 */

public abstract class UploadPhotoEngin {

    private static String mImageType = "multipart/form-data";

    public UploadPhotoEngin(Context context, File file) {
//        OkHttpClient okHttpClient = new OkHttpClient();

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

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image", "images", fileBody);

        RetrofitHttpRequest.get(context).create(HttpRequestInterface.class)
                .uploadPhoto(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        onSuccess(responseBody);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailure(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public abstract void onSuccess(ResponseBody body);

    public abstract void onFailure(Throwable e);
}
