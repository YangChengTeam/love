package com.yc.verbaltalk.mine.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2020/4/23 17:05.
 */
public class PwdInfoActivity extends BaseSameActivity {


    private boolean isSetPwd;
    private EditText etPwd;
    private EditText etNewPwd;

    private UserInfoEngine mLoveEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_info);
        if (getIntent() != null) {
            isSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
        }

        initView();

    }

    private void initView() {
        mLoveEngine = new UserInfoEngine(this);
        LinearLayout llOriginPwd = findViewById(R.id.ll_origin_pwd);
        TextView tvOriginPwd = findViewById(R.id.tv_origin_pwd);
        etPwd = findViewById(R.id.et_pwd);
        View divider = findViewById(R.id.divider);
        LinearLayout llNewPwd = findViewById(R.id.ll_new_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
        if (!isSetPwd) {
            divider.setVisibility(View.GONE);
            llNewPwd.setVisibility(View.GONE);
            tvOriginPwd.setText("设置密码");
            etPwd.setHint("请输入密码");
        }

        initListener();
    }

    private void initListener() {
        offerActivitySubTitleView().setOnClickListener(v -> {
            String pwd = etPwd.getText().toString().trim();
            String newPwd = etNewPwd.getText().toString().trim();
            if (isSetPwd) {
                modifyPwd(pwd, newPwd);
            } else {
                setPwd(pwd);
            }

        });
    }

    @Override
    protected String offerActivityTitle() {
        return isSetPwd ? "修改密码" : "设置密码";
    }

    @Override
    protected String offerActivitySubTitle() {
        return "完成";
    }


    private void setPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空");
            return;
        }
        if (mLoadingDialog != null)
            mLoadingDialog.showLoadingDialog("设置密码中...");
        mLoveEngine.setPwd(pwd).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {
                if (mLoadingDialog != null) mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (mLoadingDialog != null) mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (mLoadingDialog != null) mLoadingDialog.dismissLoadingDialog();
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    ToastUtils.showCenterToast(stringResultInfo.message);
                    UserInfo userInfo = UserInfoHelper.getUserInfo();
                    if (null != userInfo) {
                        userInfo.pwd = pwd;
                    }
                    UserInfoHelper.saveUserInfo(userInfo);
                    EventBus.getDefault().post("set_pwd_success");
                    finish();
                }
            }
        });
    }

    private void modifyPwd(String pwd, String newPwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("原密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showCenterToast("新密码不能为空");
            return;
        }

        if (null != mLoadingDialog) {
            mLoadingDialog.showLoadingDialog("设置密码中...");
        }
        mLoveEngine.modifyPwd(pwd, newPwd).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
            @Override
            public void onComplete() {
                if (null != mLoadingDialog) mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (null != mLoadingDialog) mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (null != mLoadingDialog) mLoadingDialog.dismissLoadingDialog();
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                        UserInfo userInfo = UserInfoHelper.getUserInfo();
                        if (null != userInfo) {
                            userInfo.pwd = newPwd;
                        }
                        UserInfoHelper.saveUserInfo(userInfo);
                        finish();
                    }
                }
            }
        });
    }
}
