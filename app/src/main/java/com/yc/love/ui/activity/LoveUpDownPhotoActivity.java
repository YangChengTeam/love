package com.yc.love.ui.activity;

import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.adaper.vp.LoveUpDownPhotoPagerAdapter;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;

import cn.youngkaaa.yviewpager.YViewPager;

public class LoveUpDownPhotoActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_up_down_photo);

        initViews();
    }

    private void initViews() {

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add("itme " + i);
        }
        YViewPager viewPager = findViewById(R.id.love_up_down_photo_viewpager);
        LoveUpDownPhotoPagerAdapter loveUpDownPhotoPagerAdapter = new LoveUpDownPhotoPagerAdapter(getSupportFragmentManager(), datas);
        viewPager.setAdapter(loveUpDownPhotoPagerAdapter);
        viewPager.setCurrentItem(10);
    }

    @Override
    protected String offerActivityTitle() {
        return "1";
    }

    @Override
    protected boolean hindActivityTitle() {
        return true;
    }

    @Override
    protected boolean hindActivityBar() {
        return true;
    }
}
