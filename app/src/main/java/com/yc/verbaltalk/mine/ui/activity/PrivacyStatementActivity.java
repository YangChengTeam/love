package com.yc.verbaltalk.mine.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.YcApplication;
import com.yc.verbaltalk.base.activity.BaseSameActivity;

public class PrivacyStatementActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);
        ((TextView) findViewById(R.id.tv_privacy)).setText(YcApplication.privacyPolicy);
    }

    @Override
    protected String offerActivityTitle() {
        return getString(R.string.app_name) + "隐私声明";
    }
}
