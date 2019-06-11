package com.yc.love.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.love.R;
import com.yc.love.model.constant.ConstantKey;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mWxApi;
//    private CustomProgressDialog customProgressDialog;

    private String requestWorkShareUrl;
    private boolean isH5 = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        mWxApi = WXAPIFactory.createWXAPI(this, ConstantKey.WX_APP_ID, false);
        mWxApi.registerApp(ConstantKey.WX_APP_ID);
        mWxApi.handleIntent(getIntent(), this);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp resp) { //在这个方法中处理微信传回的数据
        //形参resp 有下面两个个属性比较重要
        //1.resp.errCode
        //2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
        switch (resp.errCode) { //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) { //登录成功后resp的返回类型为 SendAuth.Resp
                } else {
                    String transaction = resp.transaction;
//            Toast.makeText(WXEntryActivity.this, transaction, Toast.LENGTH_SHORT).show();
                    Log.d("mylog", "onResp: transaction " + transaction);
                    if (transaction.contains("workShare")) {
                        String[] split = transaction.split("##");
                        String shareTaskid = split[1];
                        String shareJobShareCode = split[2];

                        if (TextUtils.isEmpty(shareJobShareCode)) {
                            return;
                        }
                        //正确返回
                    } else {
                        //正确返回
//                        Toast.makeText(WXEntryActivity.this, "微信分享--成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                Toast.makeText(WXEntryActivity.this, "微信:用户取消", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                Toast.makeText(WXEntryActivity.this, "微信:认证被否决", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                Toast.makeText(WXEntryActivity.this, "微信:发送失败", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                Toast.makeText(WXEntryActivity.this, "微信:不支持错误", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                Toast.makeText(WXEntryActivity.this, "微信:一般错误", Toast.LENGTH_SHORT).show();
                break;
            default:
                //其他不可名状的情况
                break;
        }
        finish();
    }






    @Override
    public void onReq(BaseReq req) {
        //......这里是用来处理接收的请求,暂不做讨论
    }



}
