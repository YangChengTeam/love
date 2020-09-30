package com.yc.verbaltalk.mine.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;


/**
 * Created by suns  on 2020/4/23 18:28.
 */
public class RegisterSetPwdFragment extends BaseLazyFragment {

    private TextView tvSetPwd;
    private EditText etPwd;

    private UserInfoEngine mLoveEngine;

    @Override
    protected int setContentView() {
        return R.layout.fragment_register_set_pwd;
    }

    @Override
    protected void initViews() {
        tvSetPwd = rootView.findViewById(R.id.tv_set_pwd);
        etPwd = rootView.findViewById(R.id.et_pwd);
        mLoveEngine = new UserInfoEngine(getActivity());
        initListener();
    }

    private void initListener() {
        tvSetPwd.setOnClickListener(v -> {
            String pwd = etPwd.getText().toString().trim();

            setPwd(pwd);
        });
    }


    @Override
    protected void lazyLoad() {

    }

    private void setPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showCenterToast("密码不能为空");
            return;
        }

        mLoveEngine.setPwd(pwd).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    UserInfo userInfo = UserInfoHelper.getUserInfo();
                    if (null != userInfo) {
                        userInfo.pwd = pwd;
                    }
                    UserInfoHelper.saveUserInfo(userInfo);
                    EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            }
        });
    }
}
