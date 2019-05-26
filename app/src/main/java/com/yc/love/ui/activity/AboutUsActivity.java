package com.yc.love.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class AboutUsActivity extends BaseSameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView tvVersion = findViewById(R.id.about_us_tv_version);
        try {
            // 判断当前的版本与服务器上的最版版本是否一致
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            tvVersion.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String offerActivityTitle() {
        return "关于我们";
    }
}
