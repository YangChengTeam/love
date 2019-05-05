package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yc.love.R;
import com.yc.love.adaper.vp.LoveByStagesPagerAdapter;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.ColorFlipPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页提升--》状态
 */

public class LoveByStagesActivity extends BaseSameActivity {

    private ViewPager mViewPager;
    private MagicIndicator mTabLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_by_stages);
        initViews();
    }

    private void initViews() {
        mTabLayout = findViewById(R.id.love_by_stages_pager_tabs);
        mViewPager = findViewById(R.id.love_by_stages_view_pager);

        netSwitchPagerData();
    }

    private void netSwitchPagerData() {
        List<String> titleList = new ArrayList<>();
        titleList.add(mActivityTitle + " 1");
        titleList.add(mActivityTitle + " 2");
        titleList.add(mActivityTitle + " 3");
        titleList.add(mActivityTitle + " 4");
        titleList.add(mActivityTitle + " 5");
        titleList.add(mActivityTitle + " 6");

        initNavigator(titleList);

        LoveByStagesPagerAdapter loveByStagesPagerAdapter = new LoveByStagesPagerAdapter(getSupportFragmentManager(), titleList);
        mViewPager.setAdapter(loveByStagesPagerAdapter);
    }

    private void initNavigator(final List<String> titleList) {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        //                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        CommonNavigatorAdapter navigatorAdapter = new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(titleList.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.red_crimson));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.red_crimson));
                return indicator;
            }
        };
        commonNavigator.setAdapter(navigatorAdapter);
        mTabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTabLayout, mViewPager);
    }

    @Override
    protected String offerActivityTitle() {
        return mActivityTitle;
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mActivityTitle = intent.getStringExtra("title");
    }

    public static void startLoveByStagesActivity(Context context, String title) {
        Intent intent = new Intent(context, LoveByStagesActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
