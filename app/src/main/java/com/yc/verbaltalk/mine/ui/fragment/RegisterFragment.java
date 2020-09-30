package com.yc.verbaltalk.mine.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;
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

/**
 * Created by suns  on 2020/4/23 15:49.
 */
public class RegisterFragment extends BaseLazyFragment {

    private TextView tvRegister;
    private EditText etPhone;
    private EditText etCode;
    private TextView tvGetCode;
    private UserInfoEngine loveEngine;
    private LoadDialog loadDialog;
    private boolean isCodeLogin = true;//是否是验证码登录 默认为true
    private TextView tvUserPolicy;


    @Override
    protected int setContentView() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initViews() {
        if (loveEngine == null) {
            loveEngine = new UserInfoEngine(getActivity());
        }
        loadDialog = new LoadDialog(getActivity());

        Bundle bundle = getArguments();
        if (null != bundle) {
            isCodeLogin = bundle.getBoolean("isCodeLogin", true);
        }


        tvUserPolicy = rootView.findViewById(R.id.tv_user_policy);
        tvRegister = rootView.findViewById(R.id.tv_register);
        etPhone = rootView.findViewById(R.id.et_phone);
        etCode = rootView.findViewById(R.id.et_code);
        tvGetCode = rootView.findViewById(R.id.tv_get_code);
        if (getActivity() != null) {
            String phone = (String) SPUtils.get(getActivity(), SPUtils.USER_PHONE, "");
            if (!TextUtils.isEmpty(phone)) {
                etPhone.setText(phone);
                etPhone.setSelection(phone.length());
            }
        }
        tvUserPolicy.setText(Html.fromHtml("注册即代表同意<font color='#f35a4c'>《用户协议》</font>"));
        initListener();
    }

    private void initListener() {

        tvGetCode.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            senCode(phone);
        });

        tvRegister.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();

            register(phone, code);


        });
        tvUserPolicy.setOnClickListener(v -> {
            UserPolicyFragment userPolicyFragment = new UserPolicyFragment();
            if (null != getActivity())
                userPolicyFragment.show(getActivity().getSupportFragmentManager(), "");
        });

    }

    @Override
    protected void lazyLoad() {

    }

    private void senCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号不能为空");
            return;
        }
        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast("手机号格式不正确");
            return;
        }
        loveEngine.sendCode(phone).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                        ToastUtils.showCenterToast(stringResultInfo.data);
                        if (null != getActivity())
                            ((BaseActivity) getActivity()).showGetCodeDisplay(tvGetCode);
                    } else {
                        ToastUtils.showCenterToast(stringResultInfo.message);
                    }
                }
            }
        });
    }

    private void register(String phone, String code) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showCenterToast("手机号不能为空");
            return;
        }
        if (!UIUtils.isPhoneNumber(phone)) {
            ToastUtils.showCenterToast("手机号格式不正确");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.showCenterToast("验证码不能为空");
            return;
        }

        loadDialog.showLoadingDialog("登录中...");
        loveEngine.codeLogin(phone, code).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
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
                    if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                        UserInfo userInfo = userInfoResultInfo.data;
                        if (getActivity() != null)
                            SPUtils.put(getActivity(), SPUtils.USER_PHONE, phone);

                        UserInfoHelper.saveUserInfo(userInfo);
                        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));

                        if (isCodeLogin) {//如果是验证码登录 直接关闭页面
                            if (null != getActivity())
                                getActivity().finish();

                        } else {//注册 跳转到设置密码界面

                            Navigation.findNavController(tvRegister).navigate(R.id.action_to_setPwd);
                        }
                    } else {
                        ToastUtils.showCenterToast(userInfoResultInfo.message);
                    }

                }
            }
        });
    }
}
