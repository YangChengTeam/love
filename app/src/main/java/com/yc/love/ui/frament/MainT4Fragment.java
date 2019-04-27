package com.yc.love.ui.frament;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.activity.RecyclerViewActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadingDialog;

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
        tvName = rootView.findViewById(R.id.main_t4_tv_name);
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

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mMainActivity, RecyclerViewActivity.class));
            }
        });
    }
}
