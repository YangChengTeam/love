package com.yc.love.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class PrivacyStatementActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);
    }

    @Override
    protected String offerActivityTitle() {
        return "恋爱话术宝隐私声明";
    }
}
