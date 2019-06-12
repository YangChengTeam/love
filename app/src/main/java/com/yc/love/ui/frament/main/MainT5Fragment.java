package com.yc.love.ui.frament.main;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.PackageUtils;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.CollectActivity;
import com.yc.love.ui.activity.FeedbackActivity;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;
import com.yc.love.ui.activity.SettingActivity;
import com.yc.love.ui.activity.UserInfoActivity;
import com.yc.love.ui.activity.UsingHelpActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.CircleTransform;
import com.yc.love.ui.view.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT5Fragment extends BaseMainFragment implements View.OnClickListener {
    private TextView mTvName;
    private TextView mTvBtnInfo;
    private ImageView mIvIcon;
    private LoveEngin mLoveEngin;
    private TextView mTvVipState;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t5;
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


        mLoveEngin = new LoveEngin(mMainActivity);
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

        mTvBtnInfo.setOnClickListener(this);
        llTitle.setOnClickListener(this);
        ivVip.setOnClickListener(this);
        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        llItem04.setOnClickListener(this);
        llItem05.setOnClickListener(this);

        mMainActivity.setStateBarHeight(viewBar, 14);


    }

    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_PERSONAL_ID);
        netIsVipData();
    }

    private void netIsVipData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mMainActivity.showToLoginDialog();
            return;
        }
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        mLoveEngin.userInfo(String.valueOf(id), "user/info").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(loadDialog) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
                int is_vip = idCorrelationLoginBean.is_vip;
                String nickName = idCorrelationLoginBean.nick_name;
                if (!TextUtils.isEmpty(nickName)) {
                    mTvBtnInfo.setText("信息已完善");
                } else {
                    mTvBtnInfo.setText("信息未完善");
                }
                Log.d("mylog", "onNetNext: is_vip " + is_vip);
                if (is_vip > 0) {
                    setTitleName(true);
                    mTvVipState.setText("已开通");
                } else {
                    setTitleName(false);
                    mTvVipState.setText("未开通");
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
                String nickName = event.nick_name;
                if (!TextUtils.isEmpty(nickName)) {
                    mTvName.setText(nickName);
                }

                mTvBtnInfo.setText("信息已完善");
                String face = YcSingle.getInstance().face;
                if (face != null) {
                    Picasso.with(mMainActivity).load(face).transform(new CircleTransform()).into(mIvIcon);
                }
                break;
        }
    }

    private void setTitleName(boolean isVip) {
        String nick_name = YcSingle.getInstance().nick_name;
        String mobile = YcSingle.getInstance().mobile;
        String face = YcSingle.getInstance().face;
        if (!TextUtils.isEmpty(nick_name) && !"123456".equals(nick_name)) {
            mTvBtnInfo.setText("信息已完善");
            mTvName.setText(nick_name);

            if (isVip) {
            } else {
            }

        } else {
            int id = YcSingle.getInstance().id;
            if (id > 0) {
                if (isVip) {
                    mTvName.setText("VIP用户:".concat(String.valueOf(id)));
                } else {
                    mTvName.setText("普通用户:".concat(String.valueOf(id)));
                }
            }
        }
        if (face != null) {
            Picasso.with(mMainActivity).load(face).error(R.mipmap.main_icon_default_head).transform(new CircleTransform()).into(mIvIcon);
        }
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
                mMainActivity.startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
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
                break;
            case R.id.main_t5_ll_item_04:
                mMainActivity.startActivity(new Intent(mMainActivity, SettingActivity.class));
                break;
            case R.id.main_t5_ll_item_05:
                showToWxDialog();
//                showToCallDialog();
                break;
        }
    }

    private void showToWxDialog() {
        String mWechat = "13164125027";

        ClipboardManager myClipboard = (ClipboardManager) mMainActivity.getSystemService(mMainActivity.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", mWechat);
        myClipboard.setPrimaryClip(myClip);
//                mMainActivity.showToastShort("微信号已复制到剪切板");
        AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
        alertDialog.setMessage("专属客服微信号已复制到剪切板,去添加好友吧");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openWeiXin();
            }
        });
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
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "直接拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
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
                        });
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "复制号码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager myClipboard = (ClipboardManager) mMainActivity.getSystemService(mMainActivity.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", telPhone);
                myClipboard.setPrimaryClip(myClip);
                mMainActivity.showToastShort("复制号码成功");
            }
        });
        alertDialog.show();
    }

}
