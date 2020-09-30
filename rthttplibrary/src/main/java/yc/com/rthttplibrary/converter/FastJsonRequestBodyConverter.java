package yc.com.rthttplibrary.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by suns  on 2020/7/27 11:03.
 */
public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");


    FastJsonRequestBodyConverter() {
    }

    @Override
    public RequestBody convert(@NonNull T value) {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}
