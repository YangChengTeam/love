package com.yc.verbaltalk.base.engine;

import android.content.Context;
import android.text.TextUtils;


import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.UserInfo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import yc.com.rthttplibrary.bean.ResultInfo;


@SuppressWarnings("unchecked")
public class UserInfoEngine extends BaseEngine {

    public UserInfoEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<String>> sendCode(String mobile) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", mobile);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.ID_INFO_SMS, new TypeReference<ResultInfo<String>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);

        return request.sendCode(mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfo>> register(String code, String mobile, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put("code", code);
//        params.put("mobile", mobile);
//        params.put("password", password);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<UserInfo>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<ResultInfo<UserInfo>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
        return request.register(code, mobile, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfo>> login(String mobile, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", mobile);
//        params.put("password", password);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<UserInfo>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.PHONE_LOGIN_URL, new TypeReference<ResultInfo<UserInfo>>() {
//
//                }.getType(),
//                params,
//                true,
//                true, true);
        return request.login(mobile, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> addSuggestion(String userId, String content, String qq, String wechat) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("content", content);
        params.put("qq", qq);
        if (!TextUtils.isEmpty(wechat) && !"null".equals(wechat)) {
            params.put("wechat", wechat);
        }


        return request.addSuggestion(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<ResultInfo<String>> resetPassword(String code, String mobile, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put("code", code);
//        params.put("mobile", mobile);
//        params.put("new_password", password);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<ResultInfo<String>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);

        return request.resetPassword(code, mobile, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfo>> updateInfo(String user_id, String nick_name, String birthday, String sex, String face, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("birthday", birthday);
        params.put("sex", sex);
        params.put("nick_name", nick_name);
        if (!TextUtils.isEmpty(face)) {
            params.put("face", face);
        }
        if (!TextUtils.isEmpty(password))
            params.put("password", password);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<UserInfo>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<ResultInfo<UserInfo>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);

        return request.updateInfo(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfo>> userInfo(String userId) {
//        Map<String, String> params = new HashMap<>();
//        params.put("user_id", userId);
//        requestParams(params);
//        HttpCoreEngin<ResultInfo<UserInfo>> httpCoreEngin = HttpCoreEngin.get(mContext);
//        return httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<ResultInfo<UserInfo>>() {
//                }.getType(),
//                params,
//                true,
//                true, true);
        return request.userInfo(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



    public io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<UserInfo>> codeLogin(String mobile, String code) {


        return request.codeLogin(URLConfig.PHONE_REGISTER_URL, mobile, code).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param pwd    原密码
     * @param newPwd 新密码
     * @return
     */
    public io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<UserInfo>> modifyPwd(String pwd, String newPwd) {


        return request.modifyPwd(URLConfig.MODIFY_PWD_URL, UserInfoHelper.getUid(), pwd, newPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<String>> setPwd(String pwd) {

        return request.setPwd(URLConfig.SET_PWD_URL, UserInfoHelper.getUid(), pwd)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }


}
