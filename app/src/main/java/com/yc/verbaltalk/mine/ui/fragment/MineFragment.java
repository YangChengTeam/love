package com.yc.verbaltalk.mine.ui.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kk.utils.ToastUtil;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.MySubscriber;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.chat.bean.event.EventPayVipSuccess;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.PackageUtils;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.mine.ui.activity.CollectActivity;
import com.yc.verbaltalk.mine.ui.activity.FeedbackActivity;
import com.yc.verbaltalk.mine.ui.activity.IdCorrelationSlidingActivity;
import com.yc.verbaltalk.mine.ui.activity.PrivacyStatementActivity;
import com.yc.verbaltalk.mine.ui.activity.SettingActivity;
import com.yc.verbaltalk.mine.ui.activity.UserInfoActivity;
import com.yc.verbaltalk.mine.ui.activity.UsingHelpActivity;
import com.yc.verbaltalk.mine.ui.fragment.ShareAppFragment;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by mayn on 2019/4/23.
 */

public class MineFragment extends BaseMainFragment implements View.OnClickListener {
    private TextView mTvName;
    private TextView mTvBtnInfo;
    private ImageView mIvIcon;
    private LoveEngine mLoveEngin;
    private TextView mTvVipState;
    private LoadDialog loadDialog = null;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_mine;
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
    protected void initViews() {


        mLoveEngin = new LoveEngine(mMainActivity);
        View viewBar = rootView.findViewById(R.id.main_t5_view_bar);
        mTvBtnInfo = rootView.findViewById(R.id.main_t5_tv_btn_info);
        mIvIcon = rootView.findViewById(R.id.main_t5_iv_icon);
        mTvName = rootView.findViewById(R.id.main_t5_tv_name);
        mTvVipState = rootView.findViewById(R.id.main_t5_tv_vip_state);
        ImageView ivVip = rootView.findViewById(R.id.main_t5_ll_iv_vip);
        LinearLayout llTitle = rootView.findViewById(R.id.main_t5_ll_title);
        LinearLayout llItem01 = rootView.findViewById(R.id.main_t5_ll_item_01);
        LinearLayout llItem02 = rootView.findViewById(R.id.main_t5_ll_item_02);
        LinearLayout llItem03 = rootView.findViewById(R.id.main_t5_ll_item_03);
        LinearLayout llItem04 = rootView.findViewById(R.id.main_t5_ll_item_04);
        LinearLayout llItem05 = rootView.findViewById(R.id.main_t5_ll_item_05);
        LinearLayout shareLl = rootView.findViewById(R.id.main_t5_ll_item_share);
        LinearLayout settingItem03 = rootView.findViewById(R.id.setting_ll_item_03);

        mTvBtnInfo.setOnClickListener(this);
        llTitle.setOnClickListener(this);
        ivVip.setOnClickListener(this);
        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        llItem04.setOnClickListener(this);
        llItem05.setOnClickListener(this);
        shareLl.setOnClickListener(this);
        settingItem03.setOnClickListener(this);
        mMainActivity.setStateBarHeight(viewBar, 14);

    }

    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_PERSONAL_ID);
        initData();
    }


    private void initData() {
        CommonInfoHelper.getO(mMainActivity, "main_t5_user_info", new TypeReference<IdCorrelationLoginBean>() {
        }.getType(), (CommonInfoHelper.onParseListener<IdCorrelationLoginBean>) idCorrelationLoginBean -> {
            Log.d("mylog", "netIsVipData: idCorrelationLoginBean " + idCorrelationLoginBean);
            Log.d("securityhttp", "netIsVipData: idCorrelationLoginBean " + idCorrelationLoginBean);
            if (idCorrelationLoginBean != null) {
                fillData(idCorrelationLoginBean);
            }
        });
        netIsVipData();
    }

    private void netIsVipData() {
        final int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mMainActivity.showToLoginDialog();
            return;
        }

        loadDialog = new LoadDialog(mMainActivity);

        mLoveEngin.userInfo(String.valueOf(id), "user/info").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(loadDialog) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
                if (idCorrelationLoginBean == null) {
                    return;
                }
                fillData(idCorrelationLoginBean);
//                cacheWorker.setCache("main_t5_user_info", idCorrelationLoginBean);
                CommonInfoHelper.setO(mMainActivity, idCorrelationLoginBean, "main_t5_user_info");
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private boolean isVip;

    private void fillData(IdCorrelationLoginBean idCorrelationLoginBean) {

        Log.d("mylog", "fillData: idCorrelationLoginBean " + idCorrelationLoginBean);

        int is_vip = idCorrelationLoginBean.is_vip;
        String face = idCorrelationLoginBean.face;
        String nickName = idCorrelationLoginBean.nick_name;
        int vipTips = idCorrelationLoginBean.vip_tips; //0 未开通   1 已开通   2 已过期
        if (!TextUtils.isEmpty(nickName)) {
            mTvBtnInfo.setText("信息已完善");
        } else {
            mTvBtnInfo.setText("信息未完善");
        }
        int id = YcSingle.getInstance().id;
        switch (vipTips) {
            case 0: //0 未开通
                mTvVipState.setText("未开通");
                mTvName.setText("普通用户:".concat(String.valueOf(id)));
                break;
            case 1: // 1 已开通
                mTvVipState.setText("已开通");
                mTvName.setText("VIP用户:".concat(String.valueOf(id)));
                isVip = true;
                break;
            case 2: //2 已过期
                mTvVipState.setText("已过期");
                mTvName.setText("普通用户:".concat(String.valueOf(id)));
                break;
        }
        if (!TextUtils.isEmpty(face)) {
            Glide.with(mMainActivity).load(face).apply(RequestOptions.circleCropTransform().error(R.mipmap.main_icon_default_head)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)).into(mIvIcon);
        }
        if (!TextUtils.isEmpty(nickName)) {
            mTvName.setText(nickName.concat(":").concat(String.valueOf(id)));
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventLoginState event) {
        switch (event.state) {
            case EventLoginState.STATE_EXIT:
                mTvName.setText("未登录");
                mMainActivity.scroolToHomeFragment();
                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
                break;
            case EventLoginState.STATE_LOGINED:
//                setTitleName();
                netIsVipData();
                break;
            case EventLoginState.STATE_UPDATE_INFO:
                int id = YcSingle.getInstance().id;
                String nickName = event.nick_name;
                if (!TextUtils.isEmpty(nickName)) {
                    mTvName.setText(nickName.concat(":").concat(String.valueOf(id)));
                }

                mTvBtnInfo.setText("信息已完善");
                String face = YcSingle.getInstance().face;
                if (face != null) {
                    Glide.with(mMainActivity).load(face).apply(RequestOptions.circleCropTransform()).into(mIvIcon);
                }
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccess(EventPayVipSuccess eventPayVipSuccess) {
        netIsVipData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_t5_ll_title:
            case R.id.main_t5_tv_btn_info:
                mMainActivity.startActivity(new Intent(mMainActivity, UserInfoActivity.class));
//                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
                break;
            case R.id.main_t5_ll_iv_vip:
                if (!isVip) {
                    startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                } else {
                    ToastUtil.toast2(mMainActivity, "你已经是VIP不需要重复购买");
                }

                break;
            case R.id.main_t5_ll_item_01:
                mMainActivity.startActivity(new Intent(mMainActivity, CollectActivity.class));
//                mMainActivity.startActivity(new Intent(mMainActivity, Main4Activity.class));

                break;
            case R.id.main_t5_ll_item_02:
                startActivity(new Intent(mMainActivity, UsingHelpActivity.class));
                break;
            case R.id.main_t5_ll_item_03:
                mMainActivity.startActivity(new Intent(mMainActivity, FeedbackActivity.class));
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", "pai201807");
                clipboardManager.setPrimaryClip(myClip);


                break;
            case R.id.main_t5_ll_item_04:
                mMainActivity.startActivity(new Intent(mMainActivity, SettingActivity.class));
                break;
            case R.id.main_t5_ll_item_05:
                showToWxDialog();
//                showToCallDialog();
                break;
            case R.id.main_t5_ll_item_share:
                ShareAppFragment shareAppFragment = new ShareAppFragment();
                shareAppFragment.show(getChildFragmentManager(), "");
                break;
            case R.id.setting_ll_item_03:
                startActivity(new Intent(mMainActivity, PrivacyStatementActivity.class));
                break;
        }
    }

    private void showToWxDialog() {
        String mWechat = "pai201807";

        ClipboardManager myClipboard = (ClipboardManager) mMainActivity.getSystemService(mMainActivity.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", mWechat);
        myClipboard.setPrimaryClip(myClip);
//                mMainActivity.showToastShort("微信号已复制到剪切板");
        AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
        alertDialog.setMessage("客服微信号已复制到剪切板,前往微信添加朋友即可,客服工作时间为9:00--18:00");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialogInterface, i) -> openWeiXin());
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent);
        alertDialog.show();
    }

    private void openWeiXin() {
        boolean mIsInstall = false;
        List<String> apkList = PackageUtils.getApkList(mMainActivity);
        for (int i = 0; i < apkList.size(); i++) {
            String apkPkgName = apkList.get(i);
            if ("com.tencent.mm".equals(apkPkgName)) {
                mIsInstall = true;
                break;
            }
        }
        if (mIsInstall) {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            mMainActivity.startActivity(intent);
        } else {
            mMainActivity.showToastShort("未安装微信");
        }
    }


    private void showToCallDialog() {
        final String telPhone = "13164125027";
        AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
        alertDialog.setTitle("客服电话");
        alertDialog.setMessage(telPhone);
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", listent);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "直接拨打", (dialog, which) -> SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
//                                Intent intent = new Intent(Intent.ACTION_DIAL);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + telPhone);
                        intent.setData(data);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        mMainActivity.showToastShort("没有获取到拨打电话的权限");
                    }
                }));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "复制号码", (dialogInterface, i) -> {
            ClipboardManager myClipboard = (ClipboardManager) mMainActivity.getSystemService(mMainActivity.CLIPBOARD_SERVICE);
            ClipData myClip = ClipData.newPlainText("text", telPhone);
            myClipboard.setPrimaryClip(myClip);
            mMainActivity.showToastShort("复制号码成功");
        });
        alertDialog.show();
    }

}
