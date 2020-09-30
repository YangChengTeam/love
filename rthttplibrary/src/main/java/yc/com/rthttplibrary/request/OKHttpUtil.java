package yc.com.rthttplibrary.request;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import yc.com.rthttplibrary.bean.UpFileInfo;
import yc.com.rthttplibrary.config.GoagalInfo;
import yc.com.rthttplibrary.util.Encrypt;
import yc.com.rthttplibrary.util.EncryptUtil;
import yc.com.rthttplibrary.util.LogUtil;

public final class OKHttpUtil {


    //< 设置请求参数MultipartBody.Builder
    public static MultipartBody.Builder setBuilder(UpFileInfo upFileInfo, Map<String, String> params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        addDefaultParams(params);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addFormDataPart(key, value);
        }
        if (upFileInfo.file != null) {
            builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                            ("multipart/form-data"),
                    upFileInfo.file));
        } else if (upFileInfo.buffer != null) {
            builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                            ("multipart/form-data"),
                    upFileInfo.buffer));
        }
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        return builder;
    }


    //<  加密正文
    public static byte[] encodeParams(Map<String, String> params, boolean isrsa) {
        if (params == null) {
            params = new HashMap<>();
        }
        addDefaultParams(params);
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        if (isrsa) {
            LogUtil.msg("当前公钥->" + GoagalInfo.get().getPublicKey());
            jsonStr = EncryptUtil.rsa(GoagalInfo.get().getPublicKey(), jsonStr);
        }
        return EncryptUtil.compress(jsonStr);
    }


    //< 设置请求参数FormBody.Builder
    public static FormBody.Builder setBuilder(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        addDefaultParams(params);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key, value);
        }
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        return builder;
    }

    ///< 解密返回值
    public static String decodeBody(InputStream in) {
        return Encrypt.decode(EncryptUtil.unzip(in));
    }


    //设置默认参数
    private static void addDefaultParams(Map<String, String> params) {
        if (defaultParams != null) {
            params.putAll(defaultParams);
        }
    }


    public static void setDefaultParams(Map<String, String> params) {
        OKHttpUtil.defaultParams = params;
    }

    private static Map<String, String> defaultParams;

}
