package com.yc.verbaltalk.mine.ui.activity;

import android.os.Bundle;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;


public class ProtocolActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
    }

    @Override
    protected String offerActivityTitle() {
        return getString(R.string.app_name) + "用户协议";
    }
}
