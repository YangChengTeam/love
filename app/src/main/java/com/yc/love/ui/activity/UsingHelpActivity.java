package com.yc.love.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yc.love.R;
import com.yc.love.adaper.vp.UsingHelpPagerAdapter;
import com.yc.love.ui.activity.base.BaseSameActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

public class UsingHelpActivity extends BaseSameActivity {

    private ViewPager viewPager;
//    private LinearLayout llVpIndicate;

    private int[] imageResId = new int[]{R.mipmap.using_help_01, R.mipmap.using_help_02, R.mipmap.using_help_03, R.mipmap.using_help_04, R.mipmap.using_help_05};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help);

        initViews();
    }

    private void initViews() {
        viewPager = findViewById(R.id.using_help_viewpager);
//        llVpIndicate = findViewById(R.id.using_help_ll_vp_indicate);

        initViewPager();
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.using_help_magic_indicator);
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
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initViewPager() {
        UsingHelpPagerAdapter usingHelpPagerAdapter = new UsingHelpPagerAdapter(getSupportFragmentManager(), imageResId);
        viewPager.setAdapter(usingHelpPagerAdapter);
//        viewPager.setCurrentItem(imageResId.length);
//        initSwitchIndicate();

//        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

  /*  private void initSwitchIndicate() {
        ImageView[] tips = new ImageView[imageResId.length];
        for (int i = 0; i < tips.length / 2; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.loginpage_indicator_select);
            } else {
                tips[i].setBackgroundResource(R.mipmap.loginpage_indicator_normal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            llVpIndicate.addView(imageView, layoutParams);
        }
    }*/

/*    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            int childCount = llVpIndicate.getChildCount();
            for (int mm = 0; mm < childCount; mm++) {
                ImageView childAt = (ImageView) llVpIndicate.getChildAt(mm);
                if (i % 3 == mm) {
                    childAt.setBackgroundResource(R.mipmap.loginpage_indicator_select);
                } else {
                    childAt.setBackgroundResource(R.mipmap.loginpage_indicator_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };*/

    @Override
    protected String offerActivityTitle() {
        return "使用帮助";
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
