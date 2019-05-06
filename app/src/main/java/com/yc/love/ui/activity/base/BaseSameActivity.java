package com.yc.love.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.love.R;

/**
 * Created by mayn on 2019/4/30.
 */

public abstract class BaseSameActivity extends BaseSlidingActivity implements View.OnClickListener {

    private TextView mTvSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntentData();
        super.setContentView(R.layout.activity_base_same);
        if (!isTaskRoot()) {
            /* If this is not the root activity,finish it.*/
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                Log.d("mylog", "Activity is not the root. Finishing Activity instead of launching.");
                finish();
                return;
            }
        }
        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
    }


    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {

        LinearLayout linearLayout = findViewById(R.id.activity_base_same_linear_layout);
        View viewBar = findViewById(R.id.activity_base_same_view_bar);
        RelativeLayout rlTitleCon = findViewById(R.id.activity_base_same_rl_title_con);
        ImageView ivBack = findViewById(R.id.activity_base_same_iv_back);
        TextView tvTitle = findViewById(R.id.activity_base_same_tv_title);
        mTvSub = findViewById(R.id.activity_base_same_tv_sub);
        boolean isHind = hindActivityTitle();
        if (isHind) {
            rlTitleCon.setVisibility(View.GONE);
        } else {
            rlTitleCon.setVisibility(View.VISIBLE);
        }
        if (hindActivityBar()) {
            viewBar.setVisibility(View.GONE);
        } else {
            setStateBarHeight(viewBar);
        }

        tvTitle.setText(offerActivityTitle());
        mTvSub.setText(offerActivitySubTitle());

        ivBack.setOnClickListener(this);
//        tvSub.setOnClickListener(this);

        linearLayout.addView(view, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
    }

    protected boolean hindActivityTitle() {
        return false;
    }

    protected boolean hindActivityBar() {
        return false;
    }

    protected String offerActivitySubTitle() {
        return "";
    }
    protected TextView offerActivitySubTitleView() {
        return mTvSub;
    }

    protected abstract String offerActivityTitle();

    protected void initIntentData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_same_iv_back:
                finish();
                break;
            /*case R.id.activity_base_same_tv_sub:

                break;*/
        }
    }
}
