package com.yc.love.ui.activity;

import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.ui.activity.base.PayActivity;

public class PayVipActivity extends PayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_vip);
    }

    @Override
    protected void onZfbPauResult(boolean result, String des) {

    }
}
