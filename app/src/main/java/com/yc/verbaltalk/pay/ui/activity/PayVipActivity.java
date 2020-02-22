package com.yc.verbaltalk.pay.ui.activity;

import android.os.Bundle;

import com.yc.verbaltalk.R;

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
