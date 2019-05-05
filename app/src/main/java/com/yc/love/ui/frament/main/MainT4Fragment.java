package com.yc.love.ui.frament.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT4Fragment extends BaseMainFragment implements View.OnClickListener {
    private TextView tvName;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }


    @Override
    protected void initViews() {
        View viewBar = rootView.findViewById(R.id.main_t4_view_bar);
        TextView tvBtnInfo = rootView.findViewById(R.id.main_t4_tv_btn_info);
        ImageView ivVip = rootView.findViewById(R.id.main_t4_ll_iv_vip);
        LinearLayout llItem01 = rootView.findViewById(R.id.main_t4_ll_item_01);
        LinearLayout llItem02 = rootView.findViewById(R.id.main_t4_ll_item_02);
        LinearLayout llItem03 = rootView.findViewById(R.id.main_t4_ll_item_03);
        LinearLayout llItem04 = rootView.findViewById(R.id.main_t4_ll_item_04);

        tvBtnInfo.setOnClickListener(this);
        ivVip.setOnClickListener(this);
        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        llItem04.setOnClickListener(this);

        mMainActivity.setStateBarHeight(viewBar);
    }

    @Override
    protected void lazyLoad() {
        isCanLoadData();
    }

    private void isCanLoadData() {
        AlertDialog alertDialog = new AlertDialog.Builder(mMainActivity).create();
        alertDialog.setTitle("提示");
        alertDialog.setMessage("您还未登录，请先登录");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
            }
        });
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent);
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_t4_tv_btn_info:
                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
                break;
            case R.id.main_t4_ll_iv_vip:
                mMainActivity.startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                break;
            case R.id.main_t4_ll_item_01:

                break;
            case R.id.main_t4_ll_item_02:

                break;
            case R.id.main_t4_ll_item_03:

                break;
            case R.id.main_t4_ll_item_04:

                break;
        }
    }
}
