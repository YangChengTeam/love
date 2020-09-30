package com.yc.verbaltalk.mine.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

/**
 * Created by suns  on 2020/4/23 14:47.
 * 登录或注册的界面
 */
public class LoginRegisterActivity extends BaseActivity {


    private ImageView ivBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        invadeStatusBar();
        initView();
    }


    @Override
    public boolean onSupportNavigateUp() {

        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }


    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        initListener();

    }

    private void initListener() {
        ivBack.setOnClickListener(v -> onBackPressed());
    }


}
