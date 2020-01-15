package com.yc.verbaltalk.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.CourseInfo;
import com.yc.verbaltalk.model.bean.OrdersInitBean;
import com.yc.verbaltalk.model.bean.event.EventBusWxPayResult;
import com.yc.verbaltalk.model.bean.event.EventPayVipSuccess;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.model.engin.OrderEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.SizeUtils;
import com.yc.verbaltalk.ui.activity.base.PayActivity;
import com.yc.verbaltalk.ui.frament.VipPaywayFragment;
import com.yc.verbaltalk.ui.view.CommonWebView;
import com.yc.verbaltalk.ui.view.LoadDialog;
import com.yc.verbaltalk.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import rx.Subscriber;

/**
 * Created by suns  on 2019/11/18 12:31.
 */
public class ChatCourseDetailActivity extends PayActivity implements View.OnClickListener {


    private TextView tvOriginPrice;
    private TextView tvCourseBuyPrice;
    private CommonWebView courseWebView;
    private LinearLayout llBottom;
    private int id;
    private TextView tvCourseDesc;
    private TextView tvCourseTitle;
    private ImageView ivBack;
    private ImageView ivCourseDetail;
    private OrderEngine mOrderEngine;
    private LinearLayout llWx;
    private LinearLayout llBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_course_detail);
        invadeStatusBar();
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
        initView();
    }


    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", -1);
        }

        mOrderEngine = new OrderEngine(this);

        ivBack = findViewById(R.id.activity_base_same_iv_back);
        TextView tvTitle = findViewById(R.id.activity_base_same_tv_title);
        tvOriginPrice = findViewById(R.id.tv_course_origin_price);
        tvCourseBuyPrice = findViewById(R.id.tv_course_buy_price);
        tvCourseDesc = findViewById(R.id.tv_course_desc);
        tvCourseTitle = findViewById(R.id.tv_course_title);

        courseWebView = findViewById(R.id.course_webView);
        llBottom= findViewById(R.id.ll_bottom);
        llBuy = findViewById(R.id.ll_tutor_buy);
        ivCourseDetail = findViewById(R.id.iv_course_detail);
        llWx = findViewById(R.id.ll_tutor_wx);

        tvOriginPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvTitle.setText("人气课程");
        getData();
        initListener();

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llBuy.setOnClickListener(this);
        llWx.setOnClickListener(this);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llBottom.getLayoutParams();
        LinearLayout.LayoutParams webLayoutParams = (LinearLayout.LayoutParams) courseWebView.getLayoutParams();

        int bottom = 0;
        int webBottomHeight;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this);
            webBottomHeight = ScreenUtil.dip2px(this, 50) + StatusBarUtil.getNavigationBarHeight(this);
        } else {
            webBottomHeight = ScreenUtil.dip2px(this, 50);
        }

        layoutParams.bottomMargin = bottom;
        llBottom.setLayoutParams(layoutParams);

        webLayoutParams.bottomMargin = webBottomHeight;
        courseWebView.setLayoutParams(webLayoutParams);
    }

    private void getData() {
        if (mLoadingDialog == null) mLoadingDialog = new LoadDialog(this);
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.getChatCourseDetailInfo(id + "").subscribe(new Subscriber<ResultInfo<CourseInfo>>() {
            @Override
            public void onCompleted() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<CourseInfo> courseInfoResultInfo) {
                if (courseInfoResultInfo != null && courseInfoResultInfo.code == HttpConfig.STATUS_OK && courseInfoResultInfo.data != null) {
                    initData(courseInfoResultInfo.data);
                }
            }
        });
    }


    private void initWebView(String data) {
        WebSettings settings = courseWebView.getSettings();

        courseWebView.addJavascriptInterface(new AndroidJavaScript(), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局


        courseWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);


        courseWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                courseWebView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())");
            }
        });

    }

    private CourseInfo courseInfo;

    private void initData(CourseInfo data) {
        initWebView(data.getContent());
        courseInfo = data;
        tvCourseBuyPrice.setText("¥" + data.getM_price());
        tvOriginPrice.setText("¥" + data.getPrice());
        tvCourseTitle.setText(data.getTitle());
        tvCourseDesc.setText(data.getContact_info());
        Glide.with(this).load(data.getImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).error(R.mipmap.course_pic_big)).into(ivCourseDetail);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_same_iv_back:
                finish();
                break;
            case R.id.ll_tutor_buy:
                VipPaywayFragment vipPaywayFragment = new VipPaywayFragment();
                vipPaywayFragment.show(getSupportFragmentManager(), "");
                vipPaywayFragment.setOnPayWaySelectListener(payway -> {
                    if (courseInfo != null)
                        nextOrders(payway, courseInfo);
                });

                break;
            case R.id.ll_tutor_wx:
                showToWxServiceDialog("lesson", null);
                break;
        }
    }


    private void nextOrders(final String payWayName, CourseInfo indexDoodsBean) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        if (indexDoodsBean == null) return;
        int id = YcSingle.getInstance().id;
        String name = YcSingle.getInstance().name;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(id));
        if (!TextUtils.isEmpty(name)) {
            params.put("user_name", name);
        }

        params.put("title", indexDoodsBean.getTitle()); //订单标题，会员购买，商品购买等
        if (!TextUtils.isEmpty(indexDoodsBean.getM_price()))
            params.put("money", indexDoodsBean.getM_price());

        params.put("pay_way_name", payWayName);

        JsonObject jsonObject = new JsonObject();
        int goodId = indexDoodsBean.getGoods_id();
        jsonObject.addProperty("goods_id", goodId);
        jsonObject.addProperty("num", 1);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);
        params.put("goods_list", jsonArray.toString());

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadDialog(this);
        }
        mLoadingDialog.showLoadingDialog();
        mOrderEngine.initOrders(params, "orders/init").subscribe(new MySubscriber<AResultInfo<OrdersInitBean>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<OrdersInitBean> ordersInitBeanAResultInfo) {
                OrdersInitBean ordersInitBean = ordersInitBeanAResultInfo.data;
                OrdersInitBean.ParamsBean paramsBean = ordersInitBean.params;

                if (payWayName.equals("alipay")) {

                    toZfbPay(paramsBean.info);
                } else {
                    toWxPay(paramsBean);
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusWxPayResult event) {
        switch (event.code) {
            case 0://支付成功
                //  微信支付成功
                showPaySuccessDialog(true, event.mess);
                break;
            case -1://错误
                showPaySuccessDialog(false, event.mess);
                break;
            case -2://用户取消
                showPaySuccessDialog(false, event.mess);
                break;
            default:
                break;
        }
    }


    //  支付宝支付成功
    @Override
    protected void onZfbPauResult(boolean result, String des) {
        showPaySuccessDialog(result, des);
    }

    private void showPaySuccessDialog(final boolean result, String des) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("提示");
        if (result) {
            MobclickAgent.onEvent(ChatCourseDetailActivity.this, ConstantKey.UM_PAY_SUCCESS_ID);
            EventBus.getDefault().post(new EventPayVipSuccess());
        }
        alertDialog.setMessage(des);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", (dialog, which) -> {
            if (result) {
                showToWxServiceDialog(null);
            }
        });
        alertDialog.show();


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    private class AndroidJavaScript {


        @JavascriptInterface
        public void resize(Float height) {
            //            Log.e("TAG", "resize: " + height);
            runOnUiThread(() -> {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(ChatCourseDetailActivity.this, 10f), (int) (height * getResources().getDisplayMetrics().density));

                //                layoutParams.rightMargin = SizeUtils.dp2px(LoveCaseDetailActivity.this, 10f);
                courseWebView.setLayoutParams(layoutParams);
            });
        }
    }
}
