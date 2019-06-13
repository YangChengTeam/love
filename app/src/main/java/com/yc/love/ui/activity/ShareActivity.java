package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.vp.SharePagerAdapter;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.SearchDialogueBean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;
import com.yc.love.model.util.TimeUtils;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.frament.ShareFragment;
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
    private ShareFragment mShareFragment;
    private FrameLayout mFrameLayout;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initViews();
        initData();

    }

    private void initData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
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

        mClInfo = findViewById(R.id.share_cons_lay_info);
        TextView tvDelete = findViewById(R.id.share_tv_delete);
        mFluidLayout = findViewById(R.id.share_fluid_layout);
        changHistoryFluidLayout("");  //初始化搜索记录
        ImageView ivToVip = findViewById(R.id.share_iv_to_vip);


        mClItemCon = findViewById(R.id.share_cons_lay_item_con);
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
        mFrameLayout = findViewById(R.id.share_frame_layout);

        //获取管理者
        FragmentManager supportFragmentManager = getSupportFragmentManager();//开启事务
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();//碎片
        //提交事务
        mShareFragment = new ShareFragment();
        fragmentTransaction.add(R.id.share_frame_layout, mShareFragment).commit();

        YcSingle instance = YcSingle.getInstance();
        int vip = instance.vip;
        if (vip > 0) {
            Log.d("mylog", "initViews:  已经购买了vip");
            ivToVip.setVisibility(View.GONE);
        } else {
            Log.d("mylog", "initViews:  未购买了vip");
        }

    }

    private void showShareItemHindInfo() {
        if (mClInfo.getVisibility() != View.GONE) {
            mClInfo.setVisibility(View.GONE);
        }
        /*if (mClItemCon.getVisibility() == View.INVISIBLE) {
            mClItemCon.setVisibility(View.VISIBLE);
        }*/
        if (mFrameLayout.getVisibility() != View.VISIBLE) {
            mFrameLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hindShareItemShowInfo() {
        if (mClInfo.getVisibility() != View.VISIBLE) {
            mClInfo.setVisibility(View.VISIBLE);
        }
       /* if (mClItemCon.getVisibility() == View.VISIBLE) {
            mClItemCon.setVisibility(View.INVISIBLE);

            mFragmentT1.recoverData();
            mFragmentT2.recoverData();
        }*/
        if (mFrameLayout.getVisibility() != View.GONE) {
            mFrameLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 启动搜索
     *
     * @param query 搜索关键字
     */
   /* private void staryShare(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        query = query.trim();
        if (TextUtils.isEmpty(query) || mViewPager == null || mFragmentT1 == null || mFragmentT2 == null) {
            return;
        }

        mFragmentT1.recoverData();
        mFragmentT2.recoverData();

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
    }*/
    private void staryShare(String query) {
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

        mShareFragment.recoverData();
//        mFragmentT2.recoverData();

        changHistoryFluidLayout(query);

        hindKeyboard(mViewPager);

        netPagerOneData(query); //为了解决Fragment切换白屏的问题，第一页数据在Activity中请求


//        mShareFragment.netData(query);
//        showShareItemHindInfo();

        if (3 > 0) {
            return;
        }
        int currentItem = mViewPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                mShareFragment.netData(query);
                break;
            case 1:
                mFragmentT2.netShareT2Data(query);
                break;
        }
    }

    private void netPagerOneData(final String keyword) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        if (mLoveEngin == null) {
            mLoveEngin = new LoveEngin(this);
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.searchDialogue2(String.valueOf(id), keyword, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/search").subscribe(new MySubscriber<AResultInfo<SearchDialogueBean>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                int searchBuyVip = searchDialogueBean.search_buy_vip;
                if (1 == searchBuyVip) { //1 弹窗 0不弹
                    startActivity(new Intent(ShareActivity.this, BecomeVipActivity.class));
                    return;
                } else {
                    List<LoveHealDetBean> list = searchDialogueBean.list;
                    Log.d("mylog", "onNetNext: list " + list.size());
                    boolean isCanLoadPagerMore = true;
                    if (list == null || list.size() < PAGE_SIZE) {
                        isCanLoadPagerMore = false;
                    }
                    Log.d("mylog", "onNetNext: isCanLoadPagerMore " + isCanLoadPagerMore);
                    mShareFragment.loadOnePagerData(keyword, list, isCanLoadPagerMore);
                    showShareItemHindInfo();
                }
            }

            @Override
            protected void onNetError(Throwable e) {
//                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            protected void onNetCompleted() {
//                mSwipeRefresh.setRefreshing(false);
            }
        });
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
            textView.setPadding(0, 12, 38, 12);
            textView.setClickable(true);
            textView.setTextColor(getResources().getColor(R.color.select_color_text_gray_dark));
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

    @Override
    public boolean childDisposeOnBack() {
        if (mFrameLayout.getVisibility() != View.GONE) {
            hindShareItemShowInfo();
        } else {
            finish();
        }
        return true;
    }
}
