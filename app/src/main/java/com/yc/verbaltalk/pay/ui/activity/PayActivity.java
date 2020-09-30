package com.yc.verbaltalk.pay.ui.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.verbaltalk.base.activity.BaseSlidingActivity;
import com.yc.verbaltalk.chat.bean.OrdersInitBean;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.pay.AuthResult;
import com.yc.verbaltalk.pay.PayResult;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;


/**
 * Created by sunshey on 2019/5/14.
 */

public abstract class PayActivity extends BaseSlidingActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private IWXAPI mMsgApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMsgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
//        mMsgApi.registerApp(ConstantKey.WX_APP_ID);
    }

    public void toWxPay(OrdersInitBean.ParamsBean paramsBean) {
        PayReq request = new PayReq();
        /*request.appId = "wxd930ea5d5a258f4f";
        request.partnerId = "1900000109";
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= "1101000000140429eb40476f8896f4c9";
        request.timeStamp= "1398746574";
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";*/
        ConstantKey.WX_APP_ID = paramsBean.appid;
        request.appId = paramsBean.appid;
        request.partnerId = paramsBean.mch_id;
        request.prepayId = paramsBean.prepay_id;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = paramsBean.nonce_str;
        request.timeStamp = paramsBean.timestamp;
        request.sign = paramsBean.sign;
        mMsgApi.registerApp(ConstantKey.WX_APP_ID);
        Log.d("mylog", "toWxPay: request.appId " + request.appId);
        Log.d("mylog", "toWxPay: request.partnerId " + request.partnerId);
        Log.d("mylog", "toWxPay: request.prepayId " + request.prepayId);
        Log.d("mylog", "toWxPay: request.packageValue " + request.packageValue);
        Log.d("mylog", "toWxPay: request.nonceStr " + request.nonceStr);
        Log.d("mylog", "toWxPay: request.timeStamp " + request.timeStamp);
        Log.d("mylog", "toWxPay: request.sign " + request.sign);

        mMsgApi.sendReq(request);
    }

    public void toZfbPay(final String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(PayActivity.this);

            Map<String, String> result = alipay.payV2(orderInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    Log.d("mylog", "handleMessage: resultStatus " + resultStatus);

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        onZfbPauResult(true, "支付成功");
//                        showAlert(PayActivity.this, "001 Payment success:" + payResult);
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        onZfbPauResult(false, "支付取消");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        onZfbPauResult(false, "支付失败");
//                        showAlert(PayActivity.this, "002 Payment failed:" + payResult);  //用户取消
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(PayActivity.this, "003 Authentication success:" + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(PayActivity.this, "004 Authentication failed:" + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    protected abstract void onZfbPauResult(boolean result, String des);

    private void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    private void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton("Confirm", null)
                .setOnDismissListener(onDismiss)
                .show();
    }

}
