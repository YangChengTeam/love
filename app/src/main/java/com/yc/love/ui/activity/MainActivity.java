package com.yc.love.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.vp.MainPagerAdapter;
import com.yc.love.factory.MainFragmentFactory;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.view.ControlScrollViewPager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ControlScrollViewPager mVpFragment;
    private TextView mTvTab1, mTvTab2, mTvTab3, mTvTab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变

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

        mTvTab1.setOnClickListener(this);
        mTvTab2.setOnClickListener(this);
        mTvTab3.setOnClickListener(this);
        mTvTab4.setOnClickListener(this);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mVpFragment.setAdapter(mainPagerAdapter);


    }

    public void scroolToHomeFragment(){
        mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_0, false);
        iconSelect(MainFragmentFactory.MAIN_FRAGMENT_0);
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
                int id = YcSingle.getInstance().id;
                if (id <= 0) {
                    showToLoginDialog();
                    return;
                }
                mVpFragment.setCurrentItem(MainFragmentFactory.MAIN_FRAGMENT_3, false);
                iconSelect(MainFragmentFactory.MAIN_FRAGMENT_3);
                break;
        }
    }

    private void iconSelect(int current) {
        cleatIconState();
        switch (current) {
            case MainFragmentFactory.MAIN_FRAGMENT_0:
                setCompoundDrawablesTop(mTvTab1, R.mipmap.main_icon_tab_01_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_1:
                setCompoundDrawablesTop(mTvTab2, R.mipmap.main_icon_tab_02_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_2:
                setCompoundDrawablesTop(mTvTab3, R.mipmap.main_icon_tab_03_s);
                break;
            case MainFragmentFactory.MAIN_FRAGMENT_3:
                setCompoundDrawablesTop(mTvTab4, R.mipmap.main_icon_tab_04_s);
                break;
        }
    }

    private void cleatIconState() {
        setCompoundDrawablesTop(mTvTab1, R.mipmap.main_icon_tab_01);
        setCompoundDrawablesTop(mTvTab2, R.mipmap.main_icon_tab_02);
        setCompoundDrawablesTop(mTvTab3, R.mipmap.main_icon_tab_03);
        setCompoundDrawablesTop(mTvTab4, R.mipmap.main_icon_tab_04);

        mTvTab1.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab2.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab3.setTextColor(getResources().getColor(R.color.text_gray));
        mTvTab4.setTextColor(getResources().getColor(R.color.text_gray));
    }

    public void setCompoundDrawablesTop(TextView tv_icon, int id) {
        Drawable top22 = getResources().getDrawable(id);
        tv_icon.setCompoundDrawablesWithIntrinsicBounds(null, top22, null, null);
        tv_icon.setTextColor(getResources().getColor(R.color.black));
    }




}
