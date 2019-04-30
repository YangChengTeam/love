package com.yc.love.ui.frament;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.ui.activity.FruitSlidingActivity;
import com.yc.love.ui.activity.IdCorrelationSlidingActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT4Fragment extends BaseMainFragment {
    private TextView tvName;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t4;
    }


    @Override
    protected void initViews() {
//        tvName = rootView.findViewById(R.id.main_t4_tv_name);
        TextView tvBtnInfo = rootView.findViewById(R.id.main_t4_tv_btn_info);
        LinearLayout llItem01 = rootView.findViewById(R.id.main_t4_ll_item_01);
        LinearLayout llItem02 = rootView.findViewById(R.id.main_t4_ll_item_02);
        tvBtnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mMainActivity, "tvBtnInfo", Toast.LENGTH_SHORT).show();
            }
        });
        llItem01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdCorrelationSlidingActivity.startIdCorrelationActivity(mMainActivity, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN);
            }
        });
        llItem02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mMainActivity, FruitSlidingActivity.class));
            }
        });


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
}
