package com.yc.love.ui.frament.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yc.love.R;
import com.yc.love.adaper.vp.MainT2NewPagerAdapter;
import com.yc.love.ui.frament.base.BaseMainFragment;
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

import java.util.Arrays;
import java.util.List;

/**
 * Created by mayn on 2019/6/17.
 */

public class MainT2NewFragment extends BaseMainFragment {

    private ViewPager mViewPager;
    private MagicIndicator mTabLayout;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t2_new;
    }

    @Override
    protected void initViews() {
        View viewBar = rootView.findViewById(R.id.main_t2_new_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);
        mTabLayout = rootView.findViewById(R.id.main_t2_new_pager_tabs);
        mViewPager = rootView.findViewById(R.id.main_t2_new_view_pager);

        netSwitchPagerData();
    }

    private void netSwitchPagerData() {

        String[] arrays = getResources().getStringArray(R.array.love_practice);

        List<String> titleList = Arrays.asList(arrays);

        initNavigator(titleList);

        MainT2NewPagerAdapter mainT2NewPagerAdapter = new MainT2NewPagerAdapter(mMainActivity.getSupportFragmentManager(), titleList);
        mViewPager.setAdapter(mainT2NewPagerAdapter);
    }

    private void initNavigator(final List<String> titleList) {
        CommonNavigator commonNavigator = new CommonNavigator(mMainActivity);
//        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        CommonNavigatorAdapter navigatorAdapter = new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(titleList.get(index));
                simplePagerTitleView.setTextSize(20);
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
                indicator.setLineWidth(UIUtil.dip2px(context, 60));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.red_crimson));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.8f;
                } else if (index == 1) {
                    return 1.8f;
                } else {
                    return 1.0f;
                }
            }

        };
        commonNavigator.setAdapter(navigatorAdapter);
        mTabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTabLayout, mViewPager);
    }


    @Override
    protected void lazyLoad() {

    }
}
