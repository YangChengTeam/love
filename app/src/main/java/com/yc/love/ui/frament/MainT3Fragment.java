package com.yc.love.ui.frament;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.activity.FruitActivity;
import com.yc.love.ui.activity.base.SlidingBaseActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadingDialog;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT3Fragment extends BaseMainFragment {
    private TextView tvName;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3;
    }

    @Override
    protected void initViews() {
        tvName = rootView.findViewById(R.id.main_t3_tv_name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mMainActivity,FruitActivity.class));
            }
        });
    }

    @Override
    protected void lazyLoad() {
        isCanLoadData();
    }

    private void isCanLoadData() {
        final LoadingDialog loadingView = new LoadingDialog(mMainActivity);
        loadingView.showLoading();
        tvName.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.setText(getClass().getName());
                loadingView.dismissLoading();
            }
        }, 400);
    }
}
