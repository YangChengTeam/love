package com.yc.love.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.love.R;
import com.yc.love.model.bean.event.EventBusWxPayResult;
import com.yc.love.model.constant.ConstantKey;

import org.greenrobot.eventbus.EventBus;



/**
 * Created by Administrator on 2018/7/4.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    public WXPayEntryActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_entry);
        this.api = WXAPIFactory.createWXAPI(this, ConstantKey.WX_APP_ID);
        this.api.handleIntent(this.getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        this.api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    public void onResp(BaseResp resp) {
        Log.d("mylog", "onPayFinish, errCode = " + resp.errCode + " transaction " + resp.transaction);
        /*if (resp.getType() == 5) {
            Builder builder = new Builder(this);
            builder.setTitle("2131165188");
            builder.setMessage(String.valueOf(resp.errCode));
            builder.show();
        }*/
        switch (resp.errCode) {
            case 0://支付成功
                break;
            case -1://错误
                break;
            case -2://用户取消
                break;
            default:
                break;
        }
        EventBus.getDefault().post(new EventBusWxPayResult(resp.errCode));
        finish();
    }
}

