package yc.com.rthttplibrary.request;

import android.content.Context;
import android.text.TextUtils;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import yc.com.rthttplibrary.converter.BaseJsonConverterFactory;


/**
 * Created by suns  on 2020/7/18 09:52.
 */
public class RetrofitHttpRequest {


    private static Retrofit instance;
    private Context mContext;

    private RetrofitHttpRequest(Context context) {
        this.mContext = context;
    }

    public static Retrofit get(Context context) {
        synchronized (RetrofitHttpRequest.class) {
            if (instance == null) {
                synchronized (RetrofitHttpRequest.class) {
                    instance = new RetrofitHttpRequest(context).getRetrofit();
                }
            }
        }
        return instance;
    }


    public Retrofit getRetrofit() {

        return new Builder().build();
    }



    public static final class Builder {
        private static String url;
        private static OkHttpClient client;
        private static Converter.Factory convertFactory;
        private static CallAdapter.Factory adapterFactory;

        public Builder() {
        }

        public Builder(String url){
            Builder.url=url;
        }

        public Builder url(String url) {
            Builder.url = url;
            return this;
        }

        public Builder client(OkHttpClient client) {
            Builder.client = client;
            return this;
        }

        public Builder convert(Converter.Factory convertFactory) {
            Builder.convertFactory = convertFactory;
            return this;
        }

        public Builder adapter(CallAdapter.Factory adapterFactory) {
            Builder.adapterFactory = adapterFactory;
            return this;
        }

        public Retrofit build() {
            if (TextUtils.isEmpty(url)) {
                throw new RuntimeException("baseUrl 不能为空");
            }
            if (convertFactory == null) {
                convertFactory = BaseJsonConverterFactory.create();
            }
            if (adapterFactory == null) {
                adapterFactory = RxJava2CallAdapterFactory.create();
            }
            //创建Retrofit的实例
            return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(OkHttpClientUtil.setClient(client))
                    //设置网络请求的Url地址
                    .addConverterFactory(convertFactory) //设置数据解析器
                    .addCallAdapterFactory(adapterFactory)
                    .build();
        }

    }


}
