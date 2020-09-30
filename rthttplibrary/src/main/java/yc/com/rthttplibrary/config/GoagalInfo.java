package yc.com.rthttplibrary.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import yc.com.rthttplibrary.bean.ChannelInfo;
import yc.com.rthttplibrary.util.FileUtil;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.PathUtil;


/**
 * Created by zhangkai on 16/9/19.
 */
public class GoagalInfo {

    @JSONField(name = "pub_key")
    public String publicKey = "-----BEGIN PUBLIC KEY-----" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApcW/nMMsLOo8jg6PoY0F"
            + "YBOlFdST4sqRx8cMOYIbdXDDdjsxIMI2lN7skoielHELQB+LzaI8kURg2bHfLxOr"
            + "wGJAVKxEt7+GwNe1ZxEeSL7SxFCgvVYI088W/ElSXHiq57p9SFpVccwY+JmjyL/U"
            + "gX1OSoemNAKkys64NhEMm9C8Lrs/+N4nilzW2A6hSlgVtjbjMry8M6lSjB63xvQg"
            + "wu+u6GfWpx0/TM69gg5o0ytBxl6BEtcFXujeQoR6JY6MrPggLy4/FLIxxivHTX6s"
            + "Ae1W0/Go7bORbhELNfUP0OsYbDD3d4AF/E0rV9J1Nj/wXvTxn7mQQD9n8S+zrKP6"
            + "CJ4jirfEzlFxda1Wtk0Orxy+mMlT4WPaj/aYgHwZ/QeId00zoxwrJoCbxSjqhTjg"
            + "rVHdsoX5J+pspEENB9CHDu1AKGRwXn2525HqUVAwZoTL5q2Al0LoKA1NeEwEE8So"
            + "W9Mr/RdvQg9W674wc9hKNBZFJx6scei7Pq4JB62jOiEi7nCmIYNQUEMI1MtSsLJl"
            + "Un6E2pIu4pohlUm/vGghBrgZvT9fZhtRtBRCBCht+mrkG7IqhLVq3ZAcO9UQnGrW"
            + "bMQWqbDut1KV8Vh4B5tiaOoiswTZB5Nk4pMF2cCXWPDvNNjCRXkr0NYSK6vasZFp"
            + "WYe9pakq6ocysaPXT6+Gbi0CAwEAAQ=="
            + "-----END PUBLIC KEY-----";

    public ChannelInfo channelInfo = null;
    public PackageInfo packageInfo = null;

    public String uuid = "";
    public String channel = "default";

    public String configPath = "";

    public Object extra;

    private static GoagalInfo goagalInfo = new GoagalInfo();

    public static GoagalInfo get() {
        return goagalInfo;
    }

    public void init(Context context) {

        configPath = PathUtil.getConfigPath(context);

        String result1 = null;
        String result2 = null;
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/gamechannel.json");
            InputStream in1 = zf.getInputStream(ze1);
            result1 = FileUtil.readString(in1);
            LogUtil.msg("渠道->" + result1);

            ZipEntry ze2 = zf.getEntry("META-INF/rsa_public_key.pem");
            InputStream in2 = zf.getInputStream(ze2);
            result2 = FileUtil.readString(in2);
            LogUtil.msg("公钥->" + result2);
        } catch (Exception e) {
            LogUtil.msg("apk中gamechannel或rsa_public_key文件不存在", LogUtil.W);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }

        String name = gamechannelFilename;
        if (result1 != null) {
            FileUtil.writeInfoToFile(result1, configPath, name);
        } else {
            result1 = FileUtil.readInfoFromFile(configPath, name);
        }

        if (result1 != null) {
            channel = result1;
        }

        name = rasPublickeylFilename;
        if (result2 != null) {
            publicKey = getPublicKey(result2);
            FileUtil.writeInfoToFile(result2, configPath, name);
        } else {
            result2 = FileUtil.readInfoFromFile(configPath, name);
            if (result2 != null) {
                publicKey = getPublicKey(result2);
            }
        }

        channelInfo = getChannelInfo();
        uuid = getUid(context);
        packageInfo = getPackageInfo(context);

    }

    private String rasPublickeylFilename = "rsa_public_key.pem";
    private String gamechannelFilename = "gamechannel.json";

    public GoagalInfo setRasPublickeylFilename(String rasPublickeylFilename) {
        this.rasPublickeylFilename = rasPublickeylFilename;
        return this;
    }

    public GoagalInfo setGamechannelFilename(String gamechannelFilename) {
        this.gamechannelFilename = gamechannelFilename;
        return this;
    }

    private ChannelInfo getChannelInfo() {
        try {
            ChannelInfo channelInfo = JSON.parseObject(channel, ChannelInfo.class);
            return channelInfo;
        } catch (Exception e) {
            LogUtil.msg("渠道信息解析错误->" + e.getMessage());
        }
        return null;
    }

    private String getPublicKey(InputStream in) {
        String result = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.startsWith("----")) {
                    continue;
                }
                result += mLine;
            }
        } catch (Exception e) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
        }
        return result;
    }

    public String getPublicKey() {
        publicKey = getPublicKey(publicKey);
        return publicKey;
    }

    public String getPublicKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r", "")
                .replace("\n", "");
    }

    public String getUid(Context context) {
        String uid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (uid == null || uid.isEmpty() || uid.equals("02:00:00:00:00:00")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            uid = wInfo.getMacAddress();
        }

        if (uid == null || uid.isEmpty()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uid = telephonyManager.getDeviceId();
        }

        if (uid == null) {
            uid = "";
        }

        return uid;
    }


    public PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo;
        } catch (Exception e) {
        }
        return null;
    }
}
