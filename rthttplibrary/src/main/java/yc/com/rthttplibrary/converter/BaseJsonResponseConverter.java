package yc.com.rthttplibrary.converter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.exception.ApiException;
import yc.com.rthttplibrary.request.OKHttpUtil;
import yc.com.rthttplibrary.util.LogUtil;


import static okhttp3.internal.Util.UTF_8;

/**
 * Created by suns  on 2020/7/20 15:11.
 */
public class BaseJsonResponseConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = "BaseJsonResponseConvert";
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    BaseJsonResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String body;
        if ("application".equals(value.contentType().type())) {
            body = value.string();
        } else {
            body = OKHttpUtil.decodeBody(value.byteStream());
        }

        LogUtil.msg("服务器返回数据->" + body);

        //自定义解析实体类，只解析数据最外层，如code message
        ResultInfo re = gson.fromJson(body, ResultInfo.class);
        //关注的重点，自定义响应码中非1的情况，一律抛出ApiException异常。
        //这样，我们就成功的将该异常交给onError()去处理了。
        if (re.code != HttpConfig.STATUS_OK) {
            value.close();
//            Log.e(TAG, "convert: errCode:" + re.code + "  errMsg:" + re.message);
            throw new ApiException(re.code, re.message);
        }
        //如果是1（数据正常返回），我们正常解析
        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes());
        InputStreamReader reader = new InputStreamReader(bis, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
