package com.yc.verbaltalk.mine.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.mine.adapter.UsingHelpPagerAdapter;
import com.yc.verbaltalk.base.activity.BaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import androidx.viewpager.widget.ViewPager;

public class UsingHelpHomeActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager viewPager;
//    private LinearLayout llVpIndicate;

    //    private int[] imageResId = new int[]{R.mipmap.using_help_01, R.mipmap.using_help_02, R.mipmap.using_help_03, R.mipmap.using_help_04, R.mipmap.using_help_05};
    private int[] imageResId = new int[]{R.mipmap.using_help_01, R.mipmap.using_help_02, R.mipmap.using_help_03};
    private TextView mTvNext;
    private MagicIndicator mMagicIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help_home);
//        setContentView(R.layout.activity_using_help);

        invadeStatusBar(); //侵入状态栏
        initViews();
    }

    private void initViews() {
        viewPager = findViewById(R.id.using_help_home_viewpager);
        mMagicIndicator = findViewById(R.id.using_help_home_magic_indicator);
        mTvNext = findViewById(R.id.using_help_home_tv_next);
        mTvNext.setOnClickListener(this);

        initMagicIndicator();
        initViewPager();
    }

    private void initMagicIndicator() {
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
        mMagicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(mMagicIndicator, viewPager);
    }

    private void initViewPager() {
        UsingHelpPagerAdapter usingHelpPagerAdapter = new UsingHelpPagerAdapter(getSupportFragmentManager(), imageResId);
        viewPager.setAdapter(usingHelpPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("mylog", "onPageScrolled: " + position);
                if (position == imageResId.length - 1) {
                    if (mTvNext != null && mTvNext.getVisibility() != View.VISIBLE && mMagicIndicator != null) {
                        mTvNext.setVisibility(View.VISIBLE);
                        mMagicIndicator.setVisibility(View.GONE);
                    }
                } else {
                    if (mTvNext != null && mTvNext.getVisibility() != View.GONE && mMagicIndicator != null) {
                        mTvNext.setVisibility(View.GONE);
                        mMagicIndicator.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.using_help_home_tv_next:
                finish();
                break;
        }
    }
}
