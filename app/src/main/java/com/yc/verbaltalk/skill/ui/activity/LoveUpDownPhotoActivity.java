package com.yc.verbaltalk.skill.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.skill.adapter.LoveUpDownPhotoPagerAdapter;
import com.yc.verbaltalk.base.activity.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;

import cn.youngkaaa.yviewpager.YViewPager;

public class LoveUpDownPhotoActivity extends BaseSameActivity {


    private int mClickPosition;
    private String mChildUrl;

    private ImageView ivWx;


    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mClickPosition = intent.getIntExtra("position", -1);
        mChildUrl = intent.getStringExtra("childUrl");
    }

    public static void startLoveUpDownPhotoActivity(Context context, int position, String childUrl) {
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
        LoveUpDownPhotoPagerAdapter loveUpDownPhotoPagerAdapter = new LoveUpDownPhotoPagerAdapter(getSupportFragmentManager(), mChildUrl, datas);
        viewPager.setAdapter(loveUpDownPhotoPagerAdapter);
        viewPager.setCurrentItem(mClickPosition);


        ivWx = findViewById(R.id.comp_main_iv_to_wx);


        ivWx.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.comp_main_iv_to_wx:
                showToWxServiceDialog(null);
//                collectAudio(musicInfo);
                break;
        }
    }
}
