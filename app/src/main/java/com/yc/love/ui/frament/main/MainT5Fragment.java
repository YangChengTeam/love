package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
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

        mTvBtnInfo.setOnClickListener(this);
        llTitle.setOnClickListener(this);
        ivVip.setOnClickListener(this);
        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        llItem04.setOnClickListener(this);

        mMainActivity.setStateBarHeight(viewBar, 14);


    }

    @Override
    protected void lazyLoad() {
        netIsVipData();
    }

    private void netIsVipData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
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
                }else {
                    mTvBtnInfo.setText("信息未完善");
                }
                Log.d("mylog", "onNetNext: is_vip " + is_vip);
                if (is_vip > 0) {
                    mTvVipState.setText("已开通");
                }else {
                    mTvVipState.setText("未开通");
                }
                setTitleName();
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
                mTvBtnInfo.setText("信息已完善");
                String face = YcSingle.getInstance().face;
                if (face != null) {
                    Picasso.with(mMainActivity).load(face).transform(new CircleTransform()).into(mIvIcon);
                }
                break;
        }
    }

    private void setTitleName() {
        String nick_name = YcSingle.getInstance().nick_name;
        String mobile = YcSingle.getInstance().mobile;
        String face = YcSingle.getInstance().face;
        if (!TextUtils.isEmpty(nick_name)) {
            mTvBtnInfo.setText("信息已完善");
            mTvName.setText(nick_name);
        } else {
            mTvName.setText(mobile);
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
        }
    }


}
