package com.yc.verbaltalk.community.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.community.adapter.CommunityMainPagerAdapter;
import com.yc.verbaltalk.community.ui.activity.CommunityNoticeDetailActivity;
import com.yc.verbaltalk.community.ui.activity.CommunityPublishActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.ColorFlipPagerTitleView;

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

/**
 * Created by suns  on 2019/8/29 09:51.
 */
public class CommunityMainFragment extends BaseMainFragment implements View.OnClickListener {

    private MagicIndicator mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout llTopNotice;
    private ImageView ivAddCommunity;

    private static final String TAG = "CommunityMainFragment";

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t2;
    }

    @Override
    protected void initViews() {
        View viewBar = rootView.findViewById(R.id.main_t2_new_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);
        mTabLayout = rootView.findViewById(R.id.main_t2_new_pager_tabs);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTabLayout.getLayoutParams();

        layoutParams.width = ScreenUtil.getWidth(mMainActivity) * 2 / 3;
        mTabLayout.setLayoutParams(layoutParams);

        mViewPager = rootView.findViewById(R.id.main_t2_new_view_pager);
        llTopNotice = rootView.findViewById(R.id.ll_top_notice);
        ivAddCommunity = rootView.findViewById(R.id.iv_add_community);
        llTopNotice.setVisibility(View.VISIBLE);
        ivAddCommunity.setVisibility(View.VISIBLE);
        netSwitchPagerData();
        initListener();
    }

    private void initListener() {
        llTopNotice.setOnClickListener(this);
        ivAddCommunity.setOnClickListener(this);

    }

    @Override
    protected void lazyLoad() {

    }

    private void netSwitchPagerData() {

        String[] arrays = getResources().getStringArray(R.array.community_array);

        List<String> titleList = Arrays.asList(arrays);

        initNavigator(titleList);

        CommunityMainPagerAdapter communityMainAdapter = new CommunityMainPagerAdapter(getChildFragmentManager(), titleList);
        mViewPager.setAdapter(communityMainAdapter);
        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.setCurrentItem(1);
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
                Log.e(TAG, "getTitleView: " + index);
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
                indicator.setLineHeight(UIUtil.dip2px(context, 3));
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
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

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                resetNavigator(commonNavigator);

                SimplePagerTitleView pagerTitleView = (SimplePagerTitleView) commonNavigator.getPagerTitleView(i);
//                pagerTitleView.setTextSize(22);
                pagerTitleView.setTypeface(Typeface.DEFAULT_BOLD);
            }


        });

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_top_notice:
                startActivity(new Intent(mMainActivity, CommunityNoticeDetailActivity.class));
                break;
            case R.id.iv_add_community:
                MobclickAgent.onEvent(mMainActivity, "publish_community", "点击发帖");
                startActivity(new Intent(mMainActivity, CommunityPublishActivity.class));
                break;
        }
    }
}
