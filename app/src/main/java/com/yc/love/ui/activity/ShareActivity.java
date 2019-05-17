package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.vp.CollectPagerAdapter;
import com.yc.love.adaper.vp.SharePagerAdapter;
import com.yc.love.model.util.SPUtils;
import com.yc.love.model.util.TimeUtils;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.frament.ShareT1Fragment;
import com.yc.love.ui.frament.ShareT2Fragment;
import com.yc.love.ui.view.ColorFlipPagerTitleView;
import com.yc.love.ui.view.FluidLayout;

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
import java.util.Date;
import java.util.List;

public class ShareActivity extends BaseSameActivity {

    public String shareTextString;
    private MagicIndicator mTabLayout;
    private ViewPager mViewPager;
    private ShareT1Fragment mFragmentT1;
    private ShareT2Fragment mFragmentT2;
    private ConstraintLayout mClInfo;
    private ConstraintLayout mClItemCon;
    private FluidLayout mFluidLayout;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchView != null) {
            mSearchView = null;
        }
    }

    private void initViews() {
        mSearchView = findViewById(R.id.share_searview);
        TextView tvNext = findViewById(R.id.share_tv_next);
        TextView tvTodayAdd = findViewById(R.id.share_tv_today_add);
        String sDay = String.valueOf(TimeUtils.dateToStamp(new Date(System.currentTimeMillis())));
        tvTodayAdd.setText("今日新增".concat(sDay.substring(5, 8).replace("0", "1")).concat("条话术")); //.contains("条话术")

        mClInfo = findViewById(R.id.share_cons_lay_info);
        TextView tvDelete = findViewById(R.id.share_tv_delete);
        mFluidLayout = findViewById(R.id.share_fluid_layout);
        changHistoryFluidLayout("");
        ImageView ivToVip = findViewById(R.id.share_iv_to_vip);

        mClItemCon = findViewById(R.id.share_cons_lay_item_con);
        mTabLayout = findViewById(R.id.share_pager_tabs);
        mViewPager = findViewById(R.id.share_view_pager);

        tvNext.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        ivToVip.setOnClickListener(this);

        initSwitchPagerData();

        //监听关闭按钮点击事件
        ImageView mCloseButton = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        final TextView textView = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //清除搜索框并加载默认数据
                    hindShareItemShowInfo();
                    textView.setText(null);
                }
            });
        }
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索按钮回调
                staryShare(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调
                ShareActivity.this.shareTextString = newText;
                return false;
            }
        });

    }

    private void showShareItemHindInfo() {
        if (mClInfo.getVisibility() == View.VISIBLE) {
            mClInfo.setVisibility(View.INVISIBLE);
        }
        if (mClItemCon.getVisibility() == View.INVISIBLE) {
            mClItemCon.setVisibility(View.VISIBLE);
        }
    }

    private void hindShareItemShowInfo() {
        if (mClInfo.getVisibility() == View.INVISIBLE) {
            mClInfo.setVisibility(View.VISIBLE);
        }
        if (mClItemCon.getVisibility() == View.VISIBLE) {
            mClItemCon.setVisibility(View.INVISIBLE);

            mFragmentT1.recoverData();
            mFragmentT2.recoverData();
        }
    }

    private void staryShare(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        query = query.trim();
        if (TextUtils.isEmpty(query) || mViewPager == null || mFragmentT1 == null || mFragmentT2 == null) {
            return;
        }

        changHistoryFluidLayout(query);

        hindKeyboard(mViewPager);
        showShareItemHindInfo();
        int currentItem = mViewPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                mFragmentT1.netShareT1Data(query);
                break;
            case 1:
                mFragmentT2.netShareT2Data(query);
                break;
        }
    }

    private void changHistoryFluidLayout(String query) {
        String shareHistory = (String) SPUtils.get(this, SPUtils.SHARE_HISTORY, "");
        List<String> historyList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(shareHistory)) {
            String[] split = shareHistory.split("__");
            for (String s : split
                    ) {
                historyList.add(s);
            }
            if (historyList.contains(query)) {
                historyList.remove(query);
            }
        }
        if (!TextUtils.isEmpty(query)) {
            historyList.add(query);
            if (historyList.size() >= 20) {
                historyList = historyList.subList(1, historyList.size());
            }
            for (int i = 0; i < historyList.size(); i++) {
                stringBuffer.append(historyList.get(i)).append("__");
            }
            SPUtils.put(this, SPUtils.SHARE_HISTORY, stringBuffer.toString());
        }
        mFluidLayout.removeAllViews();
        for (int i = 0; i < historyList.size(); i++) {
            final TextView textView = new TextView(this);
            textView.setText(historyList.get(i));
            textView.setPadding(0, 8, 38, 8);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchView.setQuery(textView.getText(), true);
                }
            });
            mFluidLayout.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_tv_next:
                staryShare(shareTextString);
                break;
            case R.id.share_tv_delete:
                SPUtils.put(this, SPUtils.SHARE_HISTORY, "");
                changHistoryFluidLayout("");
                break;
            case R.id.share_iv_to_vip:
                //TODO 购买VIP刷新数据
                startActivity(new Intent(ShareActivity.this, BecomeVipActivity.class));
                break;
        }
    }

    private void initSwitchPagerData() {
        List<String> titleLists = new ArrayList<>();
        titleLists.add("模糊查询");
        titleLists.add("精准查询");

        initNavigator(titleLists);

        SharePagerAdapter sharePagerAdapter = new SharePagerAdapter(getSupportFragmentManager(), titleLists);
        mViewPager.setAdapter(sharePagerAdapter);

        mFragmentT1 = (ShareT1Fragment) sharePagerAdapter.getItem(0);
        mFragmentT2 = (ShareT2Fragment) sharePagerAdapter.getItem(1);
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
        return "搜索";
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
