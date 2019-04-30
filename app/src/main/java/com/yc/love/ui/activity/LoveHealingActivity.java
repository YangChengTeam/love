package com.yc.love.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class LoveHealingActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_healing);
    }

    @Override
    protected String offerActivityTitle() {
        return "治愈情话";
    }
}
