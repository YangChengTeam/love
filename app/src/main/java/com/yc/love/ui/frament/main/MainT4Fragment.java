package com.yc.love.ui.frament.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.model.bean.EventLoginState;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.FeedbackActivity;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;
import com.yc.love.ui.activity.Main4Activity;
import com.yc.love.ui.activity.MainActivity;
import com.yc.love.ui.activity.SettingActivity;
import com.yc.love.ui.activity.UserInfoActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT4Fragment extends BaseMainFragment implements View.OnClickListener {
    private TextView tvName;
    private TextView mTvName;
    private TextView mTvBtnInfo;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
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
        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        mTvBtnInfo = rootView.findViewById(R.id.main_t4_tv_btn_info);
        mTvName = rootView.findViewById(R.id.main_t4_tv_name);
        ImageView ivVip = rootView.findViewById(R.id.main_t4_ll_iv_vip);
        LinearLayout llTitle = rootView.findViewById(R.id.main_t4_ll_title);
        LinearLayout llItem01 = rootView.findViewById(R.id.main_t4_ll_item_01);
        LinearLayout llItem02 = rootView.findViewById(R.id.main_t4_ll_item_02);
        LinearLayout llItem03 = rootView.findViewById(R.id.main_t4_ll_item_03);
        LinearLayout llItem04 = rootView.findViewById(R.id.main_t4_ll_item_04);

        mTvBtnInfo.setOnClickListener(this);
        llTitle.setOnClickListener(this);
        ivVip.setOnClickListener(this);
        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        llItem04.setOnClickListener(this);

        mMainActivity.setStateBarHeight(viewBar, 14);

        setTitleName();
    }

    @Override
    protected void lazyLoad() {
        isCanLoadData();
    }

    private void isCanLoadData() {

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
                setTitleName();
                break;
            case EventLoginState.STATE_UPDATE_INFO:
                mTvBtnInfo.setText("信息已完善");
                break;
        }
    }

    private void setTitleName() {
        String nick_name = YcSingle.getInstance().nick_name;
        String name = YcSingle.getInstance().name;
        String mobile = YcSingle.getInstance().mobile;
        if (!TextUtils.isEmpty(mobile)) {
            mTvName.setText(mobile);
            if (!TextUtils.isEmpty(name)) {
                mTvName.setText(name);
                if (!TextUtils.isEmpty(nick_name)) {
                    mTvName.setText(nick_name);
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_t4_ll_title:
            case R.id.main_t4_tv_btn_info:
                mMainActivity.startActivity(new Intent(mMainActivity, UserInfoActivity.class));
//                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
                break;
            case R.id.main_t4_ll_iv_vip:
                mMainActivity.startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                break;
            case R.id.main_t4_ll_item_01:
//                mMainActivity.startActivity(new Intent(mMainActivity, CollectActivity.class));
                mMainActivity.startActivity(new Intent(mMainActivity, Main4Activity.class));

                break;
            case R.id.main_t4_ll_item_02:

                break;
            case R.id.main_t4_ll_item_03:
                mMainActivity.startActivity(new Intent(mMainActivity, FeedbackActivity.class));
                break;
            case R.id.main_t4_ll_item_04:
                mMainActivity.startActivity(new Intent(mMainActivity, SettingActivity.class));
                break;
        }
    }
}
