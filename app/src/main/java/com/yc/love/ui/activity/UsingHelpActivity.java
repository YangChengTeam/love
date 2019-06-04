package com.yc.love.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.vp.UsingHelpPagerAdapter;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.activity.base.BasePushPhotoActivity;
import com.yc.love.ui.activity.base.BaseSameActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

public class UsingHelpActivity extends BaseSameActivity {

    private ViewPager viewPager;
//    private LinearLayout llVpIndicate;

    private int[] imageResId = new int[]{R.mipmap.using_help_01, R.mipmap.using_help_02, R.mipmap.using_help_03, R.mipmap.using_help_04, R.mipmap.using_help_05};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help);
//        invadeStatusBar(); //侵入状态栏

        initViews();
    }

    boolean isTransparency = false;

    private void initViews() {
//        RelativeLayout rlCon = findViewById(R.id.using_help_rl_con);


        /*if (!isTransparency) {
            View viewBar = findViewById(R.id.activity_base_same_view_bar);
            TextView mTvTitle = findViewById(R.id.activity_base_same_tv_title);
            mTvTitle.setText("使用帮助");
            setStateBarHeight(viewBar);
            rlCon.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            RelativeLayout rlTitle = findViewById(R.id.using_help_rl_title);
            rlTitle.setVisibility(View.GONE);
            rlCon.setBackgroundColor(getResources().getColor(R.color.transparency_gray));
        }*/

        viewPager = findViewById(R.id.using_help_viewpager);
//        llVpIndicate = findViewById(R.id.using_help_ll_vp_indicate);

        initViewPager();
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.using_help_magic_indicator);
        CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setFollowTouch(false);
        circleNavigator.setCircleCount(imageResId.length);
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initViewPager() {
        UsingHelpPagerAdapter usingHelpPagerAdapter = new UsingHelpPagerAdapter(getSupportFragmentManager(), imageResId);
        viewPager.setAdapter(usingHelpPagerAdapter);
    }


    @Override
    protected String offerActivityTitle() {
        if(isTransparency){
            return "";
        }
        return "使用帮助";
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
