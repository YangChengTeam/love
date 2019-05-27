package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.adaper.vp.LoveUpDownPhotoPagerAdapter;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;

import cn.youngkaaa.yviewpager.YViewPager;

public class LoveUpDownPhotoActivity extends BaseSameActivity {


    private int mClickPosition;
    private String mChildUrl;

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mClickPosition = intent.getIntExtra("position", -1);
        mChildUrl = intent.getStringExtra("childUrl");
    }

    public static void startLoveUpDownPhotoActivity(Context context, int position,String childUrl) {
        Intent intent = new Intent(context, LoveUpDownPhotoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("childUrl", childUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_up_down_photo);

        initViews();
    }

    private void initViews() {

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            datas.add("itme " + i);
        }
        YViewPager viewPager = findViewById(R.id.love_up_down_photo_viewpager);
        LoveUpDownPhotoPagerAdapter loveUpDownPhotoPagerAdapter = new LoveUpDownPhotoPagerAdapter(getSupportFragmentManager(), mChildUrl,datas);
        viewPager.setAdapter(loveUpDownPhotoPagerAdapter);
        viewPager.setCurrentItem(mClickPosition );
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
