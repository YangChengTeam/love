package com.yc.love.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.BecomeVipAdapter;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipItemNewViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipItemViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTagViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTailViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTitleViewHolder;
import com.yc.love.adaper.rv.holder.EndEmptyViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.IndexDoodsBean;
import com.yc.love.model.bean.OrdersInitBean;
import com.yc.love.model.bean.OthersJoinNum;
import com.yc.love.model.bean.event.EventBusWxPayResult;
import com.yc.love.model.bean.event.EventPayVipSuccess;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.OrderEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.PayActivity;
import com.yc.love.ui.view.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BecomeVipActivity extends PayActivity implements View.OnClickListener {
    private int[] imgResIds = {R.mipmap.become_vip_icon_06, R.mipmap.become_vip_icon_01, R.mipmap.become_vip_icon_02, R.mipmap.become_vip_icon_03,
            R.mipmap.become_vip_icon_04, R.mipmap.become_vip_icon_05};
    private String[] names = {"最少投入，恋爱成功率提高98%", "20W+话术免费搜索", "海量话术实战免费查看", "海量话术技巧免费阅读",
            "经典聊天开场白免费看", "多样表白话术免费查看"};
    private String[] subNames = {"花样幽默风趣语句，提高妹子好感度", "输入TA的话，快速查找撩心话术", "话术聊天实战案例，跟着套路学撩妹",
            "恋爱脱单的爱情秘籍，撩动TA心的话术技巧", "最全聊天开场白，跨出撩妹第一步",
            "高情商表白话术，提高表白成功率"};
    private String[] payName = {"购买VIP30天", "购买VIP90天", "购买全年VIP", "购买永久VIP"};
    private int[] payMoney = {38, 58, 198, 298};
    private String[] payDes = {"最低1.2元/天", "最低0.6元/天", "最低0.5元/天", "一次开通永久免费限时免费"};
    private RecyclerView mRecyclerView;
    private LinearLayout mLlTitleCon;
    private OrderEngin mOrderEngin;
    private List<BecomeVipBean> mDatas;
    private List<IndexDoodsBean> mIndexDoodsBeans;
    private int mNumber;
    private CacheWorker mCacheWorker;
    private boolean mIsCacheNumberExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_vip);
        mOrderEngin = new OrderEngin(this);
        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变

        mCacheWorker = new CacheWorker();
        initViews();

//        netIsVipData();
        netJoinNum();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, ConstantKey.UM_VIPPURCHASE_ID);
    }

    private void netJoinNum() {
        OthersJoinNum othersJoinNum = (OthersJoinNum) mCacheWorker.getCache(this, "pay_vip_Others_join_num");
        Log.d("mylog", "netJoinNum: othersJoinNum " + othersJoinNum);
        if (othersJoinNum != null) {
            mNumber = othersJoinNum.number;
            if (mNumber > 0) {
                mIsCacheNumberExist = true;
                netData();
            }
        }
        mOrderEngin.othersJoinNum("Others/join_num").subscribe(new MySubscriber<AResultInfo<OthersJoinNum>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<OthersJoinNum> othersJoinNumAResultInfo) {
                OthersJoinNum othersJoinNum = othersJoinNumAResultInfo.data;
                mNumber = othersJoinNum.number;
                mCacheWorker.setCache("pay_vip_Others_join_num", othersJoinNum);
                if (!mIsCacheNumberExist) {
                    netData();
                }
            }

            @Override
            protected void onNetError(Throwable e) {
                netData();
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netIsVipData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        mOrderEngin.userInfo(String.valueOf(id), "user/info").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
                int is_vip = idCorrelationLoginBean.is_vip;
                Log.d("mylog", "onNetNext: is_vip " + is_vip);
                if (is_vip > 0) {
                    showIsVipTrueDialog();
                    return;
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

    private void showIsVipTrueDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("您已经是VIP用户，不需要重复购买");
        DialogInterface.OnClickListener listent = null;
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "", listent);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });
        alertDialog.show();
    }

    private void netData() {
        LoadDialog loadDialog = null;
        mDatas = (List<BecomeVipBean>) mCacheWorker.getCache(BecomeVipActivity.this, "pay_vip_goods_index");
        if (mDatas != null && mDatas.size() != 0) {
            initRecyclerData();
        } else {
            loadDialog = new LoadDialog(this);
            loadDialog.showLoadingDialog();
        }
//        LoadDialog loadDialog = new LoadDialog(this);
//        loadDialog.showLoadingDialog();
        mOrderEngin.indexDoods("goods/index").subscribe(new MySubscriber<AResultInfo<List<IndexDoodsBean>>>(loadDialog) {

            @Override
            protected void onNetNext(AResultInfo<List<IndexDoodsBean>> listAResultInfo) {
                mIndexDoodsBeans = listAResultInfo.data;

                mDatas = new ArrayList<>();
                mDatas.add(new BecomeVipBean(1, "升级VIP解锁全部聊天话术及20W+条话术免费搜索"));
                if (mIndexDoodsBeans == null) {
                    mIndexDoodsBeans = new ArrayList<>();
                }
                mDatas.add(new BecomeVipBean(3, mIndexDoodsBeans));
                mDatas.add(new BecomeVipBean(4, "vip标识"));
               /* for (int i = 0; i < imgResIds.length; i++) {
                    mDatas.add(new BecomeVipBean(2, names[i], subNames[i], imgResIds[i]));
                }*/
                mDatas.add(new BecomeVipBean(2, names[0], subNames[0], imgResIds[0]));
                mDatas.add(new BecomeVipBean(5, "底部空白"));

               /* List<IndexDoodsBean> mIndexDoodsBe=new ArrayList<>();
                for (int i = 0; i < mIndexDoodsBeans.size(); i++) {
                    if(i<=2){
                        mIndexDoodsBe.add(mIndexDoodsBeans.get(i));
                    }
                }
                mDatas.add(new BecomeVipBean(3, mIndexDoodsBe));*/
                mCacheWorker.setCache("pay_vip_goods_index", mDatas);
                initRecyclerData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    protected void initViews() {
        ImageView ivToWx = findViewById(R.id.become_vip_iv_to_wx);
        ivToWx.setOnClickListener(this);

        initTitleView();
        initRecyclerView();
    }

    private void initTitleView() {
        mLlTitleCon = findViewById(R.id.become_vip_ll_title_con);
        View viewBar = findViewById(R.id.activity_base_same_view_bar);
        RelativeLayout rlTitleCon = findViewById(R.id.activity_base_same_rl_title_con);
        ImageView ivBack = findViewById(R.id.activity_base_same_iv_back);
        TextView tvTitle = findViewById(R.id.activity_base_same_tv_title);
        viewBar.setBackgroundColor(Color.TRANSPARENT);
        rlTitleCon.setBackgroundColor(Color.TRANSPARENT);
        ivBack.setOnClickListener(this);
        ivBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_arr_lift_white));
        tvTitle.setTextColor(Color.WHITE);
//        tvTitle.setText("开通VIP");
        tvTitle.setText("");
        setStateBarHeight(viewBar, 25);

    }

    public void initRecyclerView() {
        mRecyclerView = findViewById(R.id.become_vip_rv);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(gridLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initRecyclerData() {
        BecomeVipAdapter becomeVipAdapter = new BecomeVipAdapter(mDatas, mRecyclerView, mLlTitleCon) {
            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new BecomeVipTitleViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getEndHolder(ViewGroup parent) {
                return new EndEmptyViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getVipTagHolder(ViewGroup parent) {
                return new BecomeVipTagViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
//                return new BecomeVipItemViewHolder(BecomeVipActivity.this, null, parent);
                return new BecomeVipItemNewViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getTailViewHolder(ViewGroup parent) {
                BecomeVipTailViewHolder becomeVipTailViewHolder = new BecomeVipTailViewHolder(BecomeVipActivity.this, null, parent, mNumber);
                becomeVipTailViewHolder.setOnClickTailListener(new BecomeVipTailViewHolder.OnClickTailListener() {
                    @Override
                    public void onClickTailNext(int payType, int selectPosition) {
                        if (mIndexDoodsBeans == null || mIndexDoodsBeans.size() < selectPosition + 1) {
                            showToastShort("数据错误 002001");
                            return;
                        }
                        Log.d("mylog", "onClickTailNext: payType " + payType + " selectPosition " + selectPosition);
                        IndexDoodsBean indexDoodsBean = mIndexDoodsBeans.get(selectPosition);
                        nextOrders(payType, indexDoodsBean);
                    }
                });
                return becomeVipTailViewHolder;
            }
        };
        mRecyclerView.setAdapter(becomeVipAdapter);
    }

    private void nextOrders(final int payType, IndexDoodsBean indexDoodsBean) { // PAY_TYPE_ZFB=0   PAY_TYPE_WX=1;
        int id = YcSingle.getInstance().id;
        String name = YcSingle.getInstance().name;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        String payWayName;
        if (payType == 0) {
            MobclickAgent.onEvent(this, ConstantKey.UM_PAY_ORDERS_ALIPAY);
            payWayName = "alipay";
        } else {
            MobclickAgent.onEvent(this, ConstantKey.UM_PAY_ORDERS_WXPAY);
            payWayName = "wxpay";
        }
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(id));
        if (!TextUtils.isEmpty(name)) {
            params.put("user_name", name);
        }
//        params.put("app_id", String.valueOf(indexDoodsBean.app_id));
        params.put("title", "会员购买"); //订单标题，会员购买，商品购买等
        params.put("price_total", String.valueOf(indexDoodsBean.m_price));
        params.put("money", String.valueOf(indexDoodsBean.m_price));
//        params.put("imeil","2");
        params.put("pay_way_name", payWayName);

        JsonObject jsonObject = new JsonObject();
        int goodId = indexDoodsBean.id;
        jsonObject.addProperty("goods_id", goodId);
        jsonObject.addProperty("num", 1);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject);
        params.put("goods_list", jsonArray.toString());

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadDialog(this);
        }
        mLoadingDialog.showLoadingDialog();
        mOrderEngin.initOrders(params, "orders/init").subscribe(new MySubscriber<AResultInfo<OrdersInitBean>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<OrdersInitBean> ordersInitBeanAResultInfo) {
                OrdersInitBean ordersInitBean = ordersInitBeanAResultInfo.data;
                OrdersInitBean.ParamsBean paramsBean = ordersInitBean.params;
                Log.d("mylog", "onNetNext: payType == 0  Zfb   payType " + payType);
                if (payType == 0) {
//                    String info="alipay_sdk=alipay-sdk-php-20180705&app_id=2019051564672294&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A0.01%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%5Cu5145%5Cu503c%22%2C%22out_trade_no%22%3A%22201905161657594587%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Flove.bshu.com%2Fnotify%2Falipay%2Fdefault&sign_type=RSA2&timestamp=2019-05-16+16%3A57%3A59&version=1.0&sign=BRj%2FY6Bk319dZwNoHwWbYIKYZFJahg1TRgvhFf7ubJzFKZEIESnattbFnaGJ6wq6%2BmauaKZcGv83ianrZfw0R%2BMQ9OmbTPXjKYGZUMzdPNDV3NygmVMgM68vs6oeHyQOxsbx16L4ltGi%2BdEjPDsLWqlw8E1INukZMxV4EDbFl8ZlyzKYerY9YZR1dRtxscFXgG7npmyPp3mO%2BA%2BywZABb5sANxqBShG%2FgeGbE%2BG1hpkZUE4KYGV7rCC80dcBjODWPgj%2FKQtFUXnx5NzCfWIeUMcyc8UaeK%2FsxqyrMJmsFPQgCBYGR5HH1llIfQ8NJuitwhDnJTKMhqCgh03UG9j%2B%2BQ%3D%3D";
//                    toZfbPay(info);
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
                showPaySuccessDialog(true, "支付成功");
                break;
            case -1://错误
                showPaySuccessDialog(false, "支付失败");
                break;
            case -2://用户取消
                showPaySuccessDialog(false, "支付取消");
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
            MobclickAgent.onEvent(BecomeVipActivity.this, ConstantKey.UM_PAY_SUCCESS_ID);
            EventBus.getDefault().post(new EventPayVipSuccess());
        }
        alertDialog.setMessage(des);
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (result) {
                    finish();
                }
            }
        });
        alertDialog.show();

    /*    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                setResult(RESULT_CODE_PAY_XIAO_FENG_CLASS);
                finish();
            }
        });*/
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_same_iv_back:
                finish();
                break;
            case R.id.become_vip_iv_to_wx:
                showToWxServiceDialog();
                break;
        }
    }
}
