package yc.com.rthttplibrary.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Converter;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.exception.ApiException;
import yc.com.rthttplibrary.request.OKHttpUtil;
import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by suns  on 2020/7/27 11:03.
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;


    FastJsonResponseBodyConverter(Type type) {
        this.mType = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convert(@NonNull ResponseBody value) throws IOException {

        try {
            String body;
            if ("application".equals(value.contentType().type())) {
                body = value.string();
            } else {
                body = OKHttpUtil.decodeBody(value.byteStream());
            }

            LogUtil.msg("服务器返回数据->" + body);


            T resultInfo;
            if (mType != null) {
                resultInfo = JSON.parseObject(body, mType);
            } else {
                resultInfo = JSON.parseObject(body, new TypeReference<T>() {
                }); //范型已被擦除 --！
            }

            if (resultInfo instanceof ResultInfo) {
                //自定义解析实体类，只解析数据最外层，如code message
                ResultInfo re = (ResultInfo) resultInfo;
                //关注的重点，自定义响应码中非1的情况，一律抛出ApiException异常。
                //这样，我们就成功的将该异常交给onError()去处理了。
//                if (re.code != HttpConfig.STATUS_OK) {
//                    value.close();
////            Log.e(TAG, "convert: errCode:" + re.code + "  errMsg:" + re.message);
//                    throw new ApiException(re.code, re.message);
//                }
            }
            return resultInfo;

        } finally {
            value.close();
        }
    }
}