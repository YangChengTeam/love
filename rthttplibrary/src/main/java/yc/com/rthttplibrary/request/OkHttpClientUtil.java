package yc.com.rthttplibrary.request;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.converter.LogInterceptor;

/**
 * Created by suns  on 2020/7/24 11:57.
 */
public class OkHttpClientUtil {


    private static OkHttpClient defaultClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置读取超时时间
                .readTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置请求超时时间
                .writeTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS)//设置写入超时时间
                .addInterceptor(new LogInterceptor())//添加打印拦截器
                .retryOnConnectionFailure(true);//设置出现错误进行重新连接。
        return builder.build();
    }

    public static OkHttpClient setClient(OkHttpClient client) {
        if (client == null) {
            client = defaultClient();
        }
        return client;
    }


}
