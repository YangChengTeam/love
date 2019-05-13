package com.yc.love.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class CollectActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected String offerActivityTitle() {
        return "";
    }
}
