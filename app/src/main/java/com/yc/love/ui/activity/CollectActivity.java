package com.yc.love.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.yc.love.R;
import com.yc.love.adaper.vp.CollectPagerAdapter;
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

public class CollectActivity extends BaseSameActivity {
    private ViewPager mViewPager;
    private MagicIndicator mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initViews();
    }

    private void initViews() {
        ImageView ivBack = findViewById(R.id.collect_pager_iv_back);
        mTabLayout = findViewById(R.id.collect_pager_tabs);
        mViewPager = findViewById(R.id.collect_view_pager);

        ivBack.setOnClickListener(this);
        netSwitchPagerData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.collect_pager_iv_back:
                finish();
                break;
        }
    }

    private void netSwitchPagerData() {
        List<String> titleLists = new ArrayList<>();
        titleLists.add("情话");
        titleLists.add("文章");
        titleLists.add("问答");

        initNavigator(titleLists);

        CollectPagerAdapter collectPagerAdapter = new CollectPagerAdapter(getSupportFragmentManager(), titleLists);
        mViewPager.setAdapter(collectPagerAdapter);
    }

    private void initNavigator(final List<String> titleList) {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
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
        return "";
    }

    @Override
    protected boolean hindActivityTitle() {
        return true;
    }

   /* @Override
    protected boolean hindActivityBar() {
        return true;
    }*/
   @Override
   protected boolean isSupportSwipeBack() {
       return false;
   }

}
