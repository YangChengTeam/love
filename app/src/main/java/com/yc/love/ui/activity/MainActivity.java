package com.yc.love.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.vp.MainPagerAdapter;
import com.yc.love.factory.MainFragmentFactory;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.view.ControlScrollViewPager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ControlScrollViewPager mVpFragment;
    private TextView mTvTab1, mTvTab2, mTvTab3, mTvTab4, mTvTab5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //侵入状态栏
        invadeStatusBar();

//        setStatusBarFullTransparent();
//        setFitSystemWindow(false);

        initView();
    }

    private void initView() {
        mVpFragment = findViewById(R.id.comp_main_vp_fragment);
        mTvTab1 = findViewById(R.id.comp_main_tv_tab_1);
        mTvTab2 = findViewById(R.id.comp_main_tv_tab_2);
        mTvTab3 = findViewById(R.id.comp_main_tv_tab_3);
        mTvTab4 = findViewById(R.id.comp_main_tv_tab_4);
        mTvTab5 = findViewById(R.id.comp_main_tv_tab_5);

        mTvTab1.setOnClickListener(this);
        mTvTab2.setOnClickListener(this);
        mTvTab3.setOnClickListener(this);
        mTvTab4.setOnClickListener(this);
        mTvTab5.setOnClickListener(this);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mVpFragment.setAdapter(mainPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comp_main_tv_tab_1:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_0, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_0);
                break;
            case R.id.comp_main_tv_tab_2:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_1, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_1);
                break;
            case R.id.comp_main_tv_tab_3:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_2, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_2);
                break;
            case R.id.comp_main_tv_tab_4:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_3);
                break;
            case R.id.comp_main_tv_tab_5:
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_4, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_4);
                break;
        }
    }

    private void iconSelect(int current) {
        cleatIconState();
        switch (current) {
            case MainFragmentFactory.MAIN_FRAGMENT_0:
                setCompoundDrawablesTop(mTvTab1, R.mipmap.tab_home_selection);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_1:
                setCompoundDrawablesTop(mTvTab2, R.mipmap.tab_home_selection);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_2:
                setCompoundDrawablesTop(mTvTab3, R.mipmap.tab_home_selection);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_3:
                setCompoundDrawablesTop(mTvTab4, R.mipmap.tab_home_selection);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_4:
                setCompoundDrawablesTop(mTvTab5, R.mipmap.tab_home_selection);
                break;
        }
    }

    private void cleatIconState() {
        setCompoundDrawablesTop(mTvTab1, R.mipmap.tab_home);
        setCompoundDrawablesTop(mTvTab2, R.mipmap.tab_home);
        setCompoundDrawablesTop(mTvTab3, R.mipmap.tab_home);
        setCompoundDrawablesTop(mTvTab4, R.mipmap.tab_home);
        setCompoundDrawablesTop(mTvTab5, R.mipmap.tab_home);

        mTvTab1.setTextColor(getResources().getColor(R.color.black));
        mTvTab2.setTextColor(getResources().getColor(R.color.black));
        mTvTab3.setTextColor(getResources().getColor(R.color.black));
        mTvTab4.setTextColor(getResources().getColor(R.color.black));
        mTvTab5.setTextColor(getResources().getColor(R.color.black));
    }

    public void setCompoundDrawablesTop(TextView tv_icon, int id) {
        Drawable top22 = getResources().getDrawable(id);
        tv_icon.setCompoundDrawablesWithIntrinsicBounds(null, top22, null, null);
        tv_icon.setTextColor(getResources().getColor(R.color.red));
    }
}
