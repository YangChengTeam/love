package com.yc.verbaltalk.ui.frament.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.vp.MainT2PagerAdapter;
import com.yc.verbaltalk.ui.frament.base.BaseMainFragment;
import com.yc.verbaltalk.ui.view.ColorFlipPagerTitleView;
import com.yc.verbaltalk.ui.view.imgs.Constant;

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

import androidx.viewpager.widget.ViewPager;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by mayn on 2019/6/17.
 */

public class MainT2NewFragment extends BaseMainFragment {

    private ViewPager mViewPager;
    private MagicIndicator mTabLayout;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t2;
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

        MainT2PagerAdapter mainT2NewPagerAdapter = new MainT2PagerAdapter(getChildFragmentManager(), titleList);
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
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.red_crimson));
                simplePagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
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
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                resetNavigator(commonNavigator);
                //
                SimplePagerTitleView pagerTitleView = (SimplePagerTitleView) commonNavigator.getPagerTitleView(position);
                //                pagerTitleView.setTextSize(12);
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
            }
        });
        commonNavigator.setAdapter(navigatorAdapter);
        mTabLayout.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mTabLayout, mViewPager);
    }


    private void resetNavigator(CommonNavigator commonNavigator) {
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        int childCount = titleContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SimplePagerTitleView pagerTitleView = (SimplePagerTitleView) titleContainer.getChildAt(i);
            pagerTitleView.setTypeface(Typeface.DEFAULT);

            //            pagerTitleView.setTextSize(20);
        }

    }

    @Override
    protected void lazyLoad() {

    }

}
