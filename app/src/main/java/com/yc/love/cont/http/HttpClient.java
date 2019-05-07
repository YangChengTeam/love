package com.yc.love.cont.http;

import android.util.Log;

import com.yc.love.http.HttpUtils;
import com.yc.love.http.utils.BuildFactory;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.IdCorrelationSmsBean;
import com.yc.love.model.bean.JobBean;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mayn on 2019/4/24.
 */

public interface HttpClient {
    class Builder {
        public static HttpClient getTingServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_TING);
        }

        public static HttpClient getJobServer() {
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_JOB);
        }

        public static HttpClient getLoveServer() {
            Log.d("mylog", "getLoveServer: HttpUtils.API_LOVE " + HttpUtils.API_LOVE);
            return BuildFactory.getInstance().create(HttpClient.class, HttpUtils.API_LOVE);
        }
    }

    /**
     * 首页轮播图
     */
    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontpageBean> getFrontpage();


    @FormUrlEncoded
    @POST("comp/comp_get_identstate.jhtml")
    Flowable<JobBean> collectUrl(@Field("os_version") String name, @Field("imei") String imei,
                                 @Field("client_type") String client_type, @Field("timestamp") String timestamp,
                                 @Field("token") String token, @Field("sign") String sign);


    @GET("lg/collect/list/{page}/json")
    Flowable<IdCorrelationSmsBean> getCollectList(@Path("page") int page);

    @GET("user/code")
    Observable<IdCorrelationSmsBean> idCorrelationSms(@Path("mobile") String mobile, @Path("user_id") String user_id);


    @GET("user/code?mobile=15527240579&user_id=0")
    Observable<IdCorrelationSmsBean> idCorrelationSms();
   /* @GET("user/code?mobile={mobile}&user_id={user_id}&debug=qwe123")
    Observable<IdCorrelationSmsBean> idCorrelationSms();*/

}
