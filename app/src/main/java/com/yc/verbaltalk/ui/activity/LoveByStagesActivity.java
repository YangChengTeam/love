package com.yc.verbaltalk.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.vp.LoveByStagesPagerAdapter;
import com.yc.verbaltalk.model.bean.CategoryArticleChildrenBean;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;
import com.yc.verbaltalk.ui.view.ColorFlipPagerTitleView;

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

import androidx.viewpager.widget.ViewPager;

/**
 * 首页提升--》不同恋爱阶段
 */

public class LoveByStagesActivity extends BaseSameActivity {

    private ViewPager mViewPager;
    private MagicIndicator mTabLayout;
    private String mActivityTitle;
    private List<CategoryArticleChildrenBean> mCategoryArticleChildrenBeans;

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
        List<String> titleLists = new ArrayList<>();
        List<Integer> idLists = new ArrayList<>();
        for (int i = 0; i < mCategoryArticleChildrenBeans.size(); i++) {
            CategoryArticleChildrenBean categoryArticleChildrenBean = mCategoryArticleChildrenBeans.get(i);
            titleLists.add(categoryArticleChildrenBean.name);
            idLists.add(categoryArticleChildrenBean.id);

            Log.d("mylog", "netSwitchPagerData: categoryArticleChildrenBean.name " + categoryArticleChildrenBean.name
                    + " categoryArticleChildrenBean.id " + categoryArticleChildrenBean.id);
        }

        initNavigator(titleLists);

        LoveByStagesPagerAdapter loveByStagesPagerAdapter = new LoveByStagesPagerAdapter(getSupportFragmentManager(), titleLists, idLists);
        mViewPager.setAdapter(loveByStagesPagerAdapter);
        if (mCategoryArticleChildrenBeans != null && mCategoryArticleChildrenBeans.size() > 0)
            mViewPager.setOffscreenPageLimit(mCategoryArticleChildrenBeans.size() - 1);
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
                simplePagerTitleView.setTextSize(14);
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
        mCategoryArticleChildrenBeans = intent.getParcelableArrayListExtra("CategoryArticleChildrenBeans");
    }

    public static void startLoveByStagesActivity(Context context, String title, ArrayList<CategoryArticleChildrenBean> children) {
        Intent intent = new Intent(context, LoveByStagesActivity.class);
        intent.putExtra("title", title);
        intent.putParcelableArrayListExtra("CategoryArticleChildrenBeans", children);
        context.startActivity(intent);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
