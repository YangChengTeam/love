package com.yc.love.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yc.love.R;
import com.yc.love.model.util.DataCleanManagerUtils;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.LoadingDialog;
import com.yc.love.ui.view.MainMyItemLin;

public class SettingActivity extends BaseSameActivity {

    private MainMyItemLin mLlItem04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
    }

    private void initViews() {
        LinearLayout llItem01 = findViewById(R.id.setting_ll_item_01);
        LinearLayout llItem02 = findViewById(R.id.setting_ll_item_02);
        LinearLayout llItem03 = findViewById(R.id.setting_ll_item_03);
        mLlItem04 = findViewById(R.id.setting_ll_item_04);

        llItem01.setOnClickListener(this);
        llItem02.setOnClickListener(this);
        llItem03.setOnClickListener(this);
        mLlItem04.setOnClickListener(this);

        try {
            String totalCacheSize = DataCleanManagerUtils.getTotalCacheSize(SettingActivity.this);
            mLlItem04.setSub(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.setting_ll_item_01:

                break;
            case R.id.setting_ll_item_02:

                break;
            case R.id.setting_ll_item_03:

                break;
            case R.id.setting_ll_item_04:
                clearCache();
                break;
        }
    }

    private void clearCache() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("是否清空缓存？");
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "清空缓存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataCleanManagerUtils.clearAllCache(SettingActivity.this);
                final LoadingDialog loadingView = new LoadingDialog(SettingActivity.this);
                loadingView.showLoading();
                mLlItem04.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String totalCacheSize = DataCleanManagerUtils.getTotalCacheSize(SettingActivity.this);
                            mLlItem04.setSub(totalCacheSize);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loadingView.dismissLoading();
                        showToastShort("清除成功");
                    }
                }, 600);
            }
        });
        alertDialog.show();
    }

    @Override
    protected String offerActivityTitle() {
        return "设置";
    }
}
