package com.yc.verbaltalk.mine.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.utils.UIUtils;

/**
 * Created by suns  on 2020/9/9 14:56.
 */
public class UserPolicyActivity extends BaseSameActivity {
    @Override
    protected String offerActivityTitle() {
        return UIUtils.getAppName(this) + "用户协议";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);

        TextView tvPrivacy = findViewById(R.id.tv_privacy);
        tvPrivacy.setText(getString(R.string.user_policy));
    }
}
