package com.yc.verbaltalk.mine.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;


import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.base.utils.UIUtils;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.index.ui.fragment.UserPolicyFragment;
import com.yc.verbaltalk.model.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.navigation.Navigation;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2020/4/23 15:48.
 */
public class LoginFragment extends BaseLazyFragment {

    private EditText etPhone, etPwd;

    private TextView tvRegister, tvCodeLogin, tvLogin, tvUserPolicy;
    private LoadDialog loadDialog;

    @Override
    protected int setContentView() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews() {
        loadDialog = new LoadDialog(getActivity());
        etPhone = rootView.findViewById(R.id.et_phone);
        etPwd = rootView.findViewById(R.id.et_pwd);
        tvRegister = rootView.findViewById(R.id.tv_register);
        tvCodeLogin = rootView.findViewById(R.id.tv_code_login);
        tvLogin = rootView.findViewById(R.id.tv_login);
        tvUserPolicy = rootView.findViewById(R.id.tv_user_policy);

        tvCodeLogin.setText(Html.fromHtml("忘记了？<font color='#f35a4c'>验证码登录</font>"));
        tvUserPolicy.setText(Html.fromHtml("登录即代表同意<font color='#f35a4c'>《用户协议》</font>"));

        if (getActivity() != null) {
            String phone = (String) SPUtils.get(getActivity(), SPUtils.USER_PHONE, "");
            if (!TextUtils.isEmpty(phone)) {
                etPhone.setText(phone);
                etPhone.setSelection(phone.length());
            }
        }
        initListener();
    }

    private void initListener() {
        tvRegister.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isCodeLogin", false);
            Navigation.findNavController(v).navigate(R.id.action_to_register, bundle);
        });

        tvCodeLogin.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_to_register));
        tvLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();

            login(phone, pwd);
        });

        tvUserPolicy.setOnClickListener(v -> {
            //todo 添加用户协议
            UserPolicyFragment userPolicyFragment = new UserPolicyFragment();
            if (getActivity() != null)
                userPolicyFragment.show(getActivity().getSupportFragmentManager(), "");

        });
    }

    private UserInfoEngine loveEngine;

    private void login(String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号不能为空");
            return;
        }

        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast("手机号格式不正确，请重新输入");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空");
            return;
        }
        if (loveEngine == null) {
            loveEngine = new UserInfoEngine(getActivity());
        }

        loadDialog.showLoadingDialog("登录中...");
        loveEngine.login(phone, pwd).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
            @Override
            public void onComplete() {
                loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                loadDialog.dismissLoadingDialog();
                if (userInfoResultInfo != null) {
                    int code = userInfoResultInfo.code;
                    if (code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                        UserInfo userInfo = userInfoResultInfo.data;
                        userInfo.pwd = pwd;
                        if (getActivity() != null)
                            SPUtils.put(getActivity(), SPUtils.USER_PHONE, phone);
                        UserInfoHelper.saveUserInfo(userInfo);
                        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    } else {
                        if (code == 2 || userInfoResultInfo.message.contains("未注册")) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isCodeLogin", false);
                            Navigation.findNavController(tvLogin).navigate(R.id.action_to_register, bundle);
                        } else {
                            ToastUtil.toast(getActivity(), userInfoResultInfo.message);
                        }
                    }
                }
            }
        });

    }


    @Override
    protected void lazyLoad() {

    }
}
