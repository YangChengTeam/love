package com.yc.verbaltalk.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import com.kk.securityhttp.domain.ResultInfo;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.index.adapter.SearchAdapter;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.SPUtils;
import com.yc.verbaltalk.model.util.TimeUtils;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.mine.ui.activity.UsingHelpActivity;
import com.yc.verbaltalk.index.ui.fragment.SearchFragment;
import com.yc.verbaltalk.index.ui.fragment.ShareT1Fragment;
import com.yc.verbaltalk.index.ui.fragment.ShareT2Fragment;
import com.yc.verbaltalk.base.view.ColorFlipPagerTitleView;
import com.yc.verbaltalk.base.view.FluidLayout;

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

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import rx.Subscriber;

/**
 * 搜索话术类
 */

public class SearchActivity extends BaseSameActivity {

    public String shareTextString;
    private MagicIndicator mTabLayout;
    private ViewPager mViewPager;
    private ShareT1Fragment mFragmentT1;
    private ShareT2Fragment mFragmentT2;
    //    private ConstraintLayout mClInfo;

    private FluidLayout mFluidLayout;
    private SearchView mSearchView;
    private SearchFragment mSearchFragment;
    private LoveEngine mLoveEngin;
    private String mKeyword;

    public static void startSearchActivity(Context context, String keyword) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("keyword", keyword);
        context.startActivity(intent);
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("keyword")) {
            mKeyword = intent.getStringExtra("keyword");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mLoveEngin = new LoveEngine(this);
        initViews();

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, ConstantKey.UM_SEARCH_ID);
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
        TextView tvToHelp = findViewById(R.id.share_tv_to_help);
        String sDay = String.valueOf(TimeUtils.dateToStamp(new Date(System.currentTimeMillis())));
        tvTodayAdd.setText("今日新增".concat(sDay.substring(5, 8).replace("0", "1")).concat("条话术")); //.contains("条话术")

//        mClInfo = findViewById(R.love_id.share_cons_lay_info);
        TextView tvDelete = findViewById(R.id.share_tv_delete);
        mFluidLayout = findViewById(R.id.share_fluid_layout);
        changHistoryFluidLayout("");  //初始化搜索记录
        ImageView ivToVip = findViewById(R.id.share_iv_to_vip);

        mTabLayout = findViewById(R.id.share_pager_tabs);
        mViewPager = findViewById(R.id.share_view_pager);

        tvNext.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        ivToVip.setOnClickListener(this);
        tvToHelp.setOnClickListener(this);

        initSwitchPagerData();

        //修改键入的文字字体大小、颜色和hint的字体颜色
        final EditText editText = mSearchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.size_16));
//        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        ImageView mCloseButton = mSearchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        final TextView textView = mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(view -> {
                //清除搜索框并加载默认数据
//                    hindShareItemShowInfo();
                textView.setText(null);
            });

        }
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索按钮回调
                startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调
                SearchActivity.this.shareTextString = newText;
                return false;
            }
        });
        replaceFragment(mKeyword);


        YcSingle instance = YcSingle.getInstance();
        int vip = instance.vip;
        if (vip > 0) {
            Log.d("mylog", "initViews:  已经购买了vip");
            ivToVip.setVisibility(View.GONE);
        } else {
            Log.d("mylog", "initViews:  未购买了vip");
        }

        hindKeyboard(mSearchView);

        textView.setText(mKeyword);
        editText.setSelection(mKeyword.length());
//        startSearch(mKeyword);

    }


    private void replaceFragment(String keyword) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        //获取管理者
        FragmentManager supportFragmentManager = getSupportFragmentManager();//开启事务
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();//碎片
        //提交事务
        mSearchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        mSearchFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.share_frame_layout, mSearchFragment).commit();


        searchCount(String.valueOf(id), keyword);
    }


    private void startSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        query = query.trim();
        if (TextUtils.isEmpty(query) || mViewPager == null || mFragmentT1 == null || mFragmentT2 == null) {
            return;
        }


        changHistoryFluidLayout(query);

        hindKeyboard(mViewPager);

//        netPagerOneData(query); //为了解决Fragment切换白屏的问题，第一页数据在Activity中请求

        replaceFragment(query);


//        mSearchFragment.netData(query);
//        showShareItemHindInfo();

        if (3 > 0) {
            return;
        }
        int currentItem = mViewPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                mSearchFragment.netData(query);
                break;
            case 1:
                mFragmentT2.netShareT2Data(query);
                break;
        }
    }


    /**
     * 增加搜索历史
     *
     * @param query 最新的一条历史
     */
    private void changHistoryFluidLayout(String query) {
        String shareHistory = (String) SPUtils.get(this, SPUtils.SHARE_HISTORY, "");
        List<String> historyList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(shareHistory)) {
            String[] split = shareHistory.split("__");
            for (String s : split) {
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
            textView.setPadding(0, 12, 38, 12);
            textView.setClickable(true);
            textView.setTextColor(getResources().getColor(R.color.select_color_text_gray_dark));
            textView.setOnClickListener(v -> mSearchView.setQuery(textView.getText(), true));
            mFluidLayout.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_tv_next:
                startSearch(shareTextString);
                break;
            case R.id.share_tv_delete:
                SPUtils.put(this, SPUtils.SHARE_HISTORY, "");
                changHistoryFluidLayout("");
                break;
            case R.id.share_iv_to_vip:
                //TODO 购买VIP刷新数据
                startActivity(new Intent(SearchActivity.this, BecomeVipActivity.class));
                break;
            case R.id.share_tv_to_help:
                startActivity(new Intent(this, UsingHelpActivity.class));
                break;
        }
    }

    private void initSwitchPagerData() {
        List<String> titleLists = new ArrayList<>();
        titleLists.add("模糊查询");
        titleLists.add("精准查询");

        initNavigator(titleLists);

        SearchAdapter sharePagerAdapter = new SearchAdapter(getSupportFragmentManager(), titleLists);
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

    private void searchCount(String userid, String keyword) {
        mLoveEngin.searchCount(userid, keyword).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
    }

    @Override
    protected String offerActivityTitle() {
        return "搜索";
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    /*@Override
    public boolean childDisposeOnBack() {
        if (mFrameLayout.getVisibility() != View.GONE) {
            hindShareItemShowInfo();
        } else {
            finish();
        }
        return true;
    }*/
    @Override
    public boolean childDisposeOnBack() {
        finish();
        return true;
    }
}
