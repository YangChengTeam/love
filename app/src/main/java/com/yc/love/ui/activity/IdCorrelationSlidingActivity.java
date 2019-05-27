package com.yc.love.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.love.R;
import com.yc.love.app.YcApplication;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.model.engin.IdCorrelationEngin;
import com.yc.love.model.util.CheckNumberRegulationsUtil;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.base.BaseSlidingActivity;
import com.yc.love.ui.view.LoginEditTextLin;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class IdCorrelationSlidingActivity extends BaseSlidingActivity implements View.OnClickListener {


    public static final int ID_CORRELATION_STATE_LOGIN = 1; //登录
    private final int ID_CORRELATION_STATE_REGISTER = 2; //注册
    private final int ID_CORRELATION_STATE_SEEK = 3;  //忘记密码
    private TextView mTvTitle;
    private TextView mTvSwitch;
    private TextView mTvRegister;
    private LinearLayout mLlKeepPwdCon;
    private int mInitentState;
    private String mInitentMobile;
    private TextView mTvProtocolDes;
    private LinearLayout mLlProtocolCon;
//    private ImageView mIvKeepPwdCheck;

    private boolean mIsKeepPwd = true;
    private TextView mTvKeepPwdCheck;
    private IdCorrelationEngin mIdCorrelationEngin;

    //    private MyTimerTask mMyTimerTask;
//    private Timer mTimer = new Timer();
    private LoginEditTextLin mEtPhone, mEtCode, mEtPwd;
    private String mInputPhoneString, mInputPwdString, mInputCodeString;
    private CountDownTimer mCountDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_correlation);
        mIdCorrelationEngin = new IdCorrelationEngin(this);

//        setContentView(R.layout.activity_id_correlation_xiaomi);

        //侵入状态栏
        invadeStatusBar();
        initInitent();
        initViews();
        Log.d("mylog", "onCreate: mInitentState " + mInitentState);
        initState(mInitentState);
        YcApplication ycApplication = (YcApplication) getApplication();
        ycApplication.activityIdCorList.add(this);
    }


    private void initInitent() {
        Intent intent = getIntent();
        mInitentState = intent.getIntExtra("state", -1);
        mInitentMobile = intent.getStringExtra("mobile");
    }

    public static void startIdCorrelationActivity(Context context, int state) {
        Intent intent = new Intent(context, IdCorrelationSlidingActivity.class);
        intent.putExtra("state", state);
        context.startActivity(intent);
    }

    public static void startIdCorrelationActivity(Context context, String mobile, int state) {
        Intent intent = new Intent(context, IdCorrelationSlidingActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("mobile", mobile);
        context.startActivity(intent);
    }

    private void initState(int state) {
        if (!TextUtils.isEmpty(mInitentMobile)) {
            mEtPhone.setEditText(mInitentMobile);
        } else {
            String mobile = (String) SPUtils.get(this, SPUtils.LOGIN_MOBILE, "");
//        String mobile = YcSingle.getInstance().mobile;
            if (!TextUtils.isEmpty(mobile)) {
                mEtPhone.setEditText(mobile);
            }
        }
        switch (state) {
            case ID_CORRELATION_STATE_LOGIN:
                setTiele("登录", "注册");
                mTvRegister.setVisibility(View.VISIBLE);
                mLlKeepPwdCon.setVisibility(View.VISIBLE);
                mEtCode.setVisibility(View.GONE);
                mTvProtocolDes.setText("登录即表示您同意");
                String pwd = (String) SPUtils.get(this, SPUtils.LOGIN_PWD, "");
                mEtPwd.setEditText(pwd);
//                SPUtils.put(this, SPUtils.LOGIN_MOBILE, mInputPhoneString);
                break;
            case ID_CORRELATION_STATE_REGISTER:
                setTiele("注册", "登录");
                mTvProtocolDes.setText("注册即表示您同意");
                break;
            case ID_CORRELATION_STATE_SEEK:
                setTiele("忘记密码", "登录");
                mLlProtocolCon.setVisibility(View.GONE);
                break;
        }
    }

    private void setTiele(String mainName, String switchName) {
        mTvTitle.setText(mainName);
        mTvSwitch.setText(switchName);
    }


    private void initViews() {
        View viewBar = findViewById(R.id.id_correlation_view_bar);
        ImageView ivBack = findViewById(R.id.id_correlation_iv_back);
        mTvTitle = findViewById(R.id.id_correlation_tv_title);
        mTvSwitch = findViewById(R.id.id_correlation_tv_switch);

        mEtPhone = findViewById(R.id.id_correlation_let_phont);
        mEtCode = findViewById(R.id.id_correlation_let_code);
        mEtPwd = findViewById(R.id.id_correlation_let_pwd);
        TextView tvNext = findViewById(R.id.id_correlation_tv_next);
        mTvRegister = findViewById(R.id.id_correlation_tv_register);
        mLlKeepPwdCon = findViewById(R.id.id_correlation_ll_keep_pwd_con);
        mTvKeepPwdCheck = findViewById(R.id.id_correlation_tv_keep_pwd_check);
        TextView tvKeepPwdSeek = findViewById(R.id.id_correlation_tv_keep_pwd_seek);
        changChecked(mIsKeepPwd);
        //        mEt.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
//        mEt.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        mEtPhone.mEt.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        mEtCode.mEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtPwd.mEt.setInputType(0x81);
//        mEtCode.registerTvCodeListent();
        mEtCode.waitCode(false);


        mLlProtocolCon = findViewById(R.id.id_correlation_ll_protocol_con);
        mTvProtocolDes = findViewById(R.id.id_correlation_tv_protocol_des);
        TextView tvProtocol = findViewById(R.id.id_correlation_tv_protocol);

        mTvKeepPwdCheck.setOnClickListener(this);
        tvKeepPwdSeek.setOnClickListener(this);

        ivBack.setOnClickListener(this);
        mTvSwitch.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);

        tvNext.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);

        mEtCode.setOnClickEtCodeListent(clickEtCodeListent);

        setStateBarHeight(viewBar);
    }

//    private Timer timer = new Timer();

//    private final int RECLEN_TIME = 60;
//    private int recLen = RECLEN_TIME;

    private long countDoenInterval = 980;
    private long millisInFuture = 1000 * 60 + 500;

    private LoginEditTextLin.OnClickEtCodeListent clickEtCodeListent = new LoginEditTextLin.OnClickEtCodeListent() {
        @Override
        public void onClickEtCode() {
            if (!checkInputPhone()) {
                return;
            }
            mLoadingDialog.showLoadingDialog();
            mIdCorrelationEngin.sms("0", mInputPhoneString).subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {

                @Override
                protected void onNetNext(AResultInfo<String> myIdCorrelationSmsBeanAResultInfo) {
                    String msg = myIdCorrelationSmsBeanAResultInfo.msg;
                    int code = myIdCorrelationSmsBeanAResultInfo.code;
                    String data = myIdCorrelationSmsBeanAResultInfo.data;
                    mEtCode.waitCode(true);
                    mEtCode.setEditCodeWaitText("60秒后重试");
                    //                                    tv_count_down.setText(String.valueOf(countDown - 1) + "  跳过");
                    mCountDownTimer = new CountDownTimer(millisInFuture, countDoenInterval) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int countDown = (int) (millisUntilFinished / 1000);
                            Log.d("mylog", "onTick: countDown " + countDown);
                            if (countDown <= 1) {
                                mEtCode.waitCode(false);
                            } else {
                                if (countDown != 1) {
                                    mEtCode.setEditCodeWaitText(countDown-- + "秒后重试");
//                                    tv_count_down.setText(String.valueOf(countDown - 1) + "  跳过");
                                }
                            }
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                }


                @Override
                protected void onNetError(Throwable e) {

                }

                @Override
                protected void onNetCompleted() {

                }
            });
        }
    };


    private boolean checkInputLogin() {
        if (!checkInputPhone()) {
            return false;
        }
        mInputPwdString = mEtPwd.getEditTextTrim();
        if (TextUtils.isEmpty(mInputPwdString)) {
            showToastShort("密码不能为空");
            return false;
        }
        if (mInputPwdString.length() < 6) {
            showToastShort("密码不合法");
            return false;
        }
        return true;
    }

    private boolean checkInputRegisterOrSeek() {
        mInputCodeString = mEtCode.getEditTextTrim();
        if (TextUtils.isEmpty(mInputCodeString)) {
            showToastShort("验证码不能为空");
            return false;
        }
        if (mInputCodeString.length() != 6) {
            showToastShort("验证码格式错误");
            return false;
        }
        return checkInputLogin();
    }

    private boolean checkInputPhone() {
        mInputPhoneString = mEtPhone.getEditTextTrim();
        if (TextUtils.isEmpty(mInputPhoneString)) {
            showToastShort("手机号不能为空");
            return false;
        }
        if (mInputPhoneString.length() != 11 || CheckNumberRegulationsUtil.isTelPhoneNumber(mInputCodeString)) {
            showToastShort("手机号不合法");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_correlation_iv_back:
                finish();
                break;
            case R.id.id_correlation_tv_switch:
                switch (mInitentState) {
                    case ID_CORRELATION_STATE_LOGIN:
                        IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_REGISTER);
                        break;
                    case ID_CORRELATION_STATE_REGISTER:
                    case ID_CORRELATION_STATE_SEEK:
                        IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_LOGIN);
                        break;
                }
                break;
            case R.id.id_correlation_tv_next:
                switch (mInitentState) {
                    case ID_CORRELATION_STATE_LOGIN:
                        boolean isCanLogin = checkInputLogin();
                        if (!isCanLogin) {
                            return;
                        }
                        netLogin();
                        break;
                    case ID_CORRELATION_STATE_REGISTER:
                        boolean isCanRegister = checkInputRegisterOrSeek();
                        if (!isCanRegister) {
                            return;
                        }
                        netRegister();
                        break;
                    case ID_CORRELATION_STATE_SEEK:
                        boolean isCanSeek = checkInputRegisterOrSeek();
                        if (!isCanSeek) {
                            return;
                        }
                        netResetPassword();
                        break;
                }
                break;
            case R.id.id_correlation_tv_protocol:
                startActivity(new Intent(this,ProtocolActivity.class));
                break;
            case R.id.id_correlation_tv_register:
                IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_REGISTER);
                break;
            case R.id.id_correlation_tv_keep_pwd_check:
                mIsKeepPwd = !mIsKeepPwd;
                changChecked(mIsKeepPwd);
                break;
            case R.id.id_correlation_tv_keep_pwd_seek:
                IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_SEEK);
                break;
        }
    }

    private void netResetPassword() {
        mLoadingDialog.showLoadingDialog();
        mIdCorrelationEngin.resetPassword(mInputCodeString, mInputPhoneString, mInputPwdString, "user/reset").subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<String> myIdCorrelationSmsBeanAResultInfo) {
                String msg = myIdCorrelationSmsBeanAResultInfo.msg;
                int code = myIdCorrelationSmsBeanAResultInfo.code;
                String data = myIdCorrelationSmsBeanAResultInfo.data;
                Log.d("mylog", "onNetNext: myIdCorrelationSmsBeanAResultInfo " + myIdCorrelationSmsBeanAResultInfo.toString());

                SPUtils.put(IdCorrelationSlidingActivity.this, SPUtils.LOGIN_PWD, "");
                IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_LOGIN);
                finishAllIdCorActivity();
            }


            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netRegister() {
        mLoadingDialog.showLoadingDialog();
        mIdCorrelationEngin.register(mInputCodeString, mInputPhoneString, mInputPwdString, "user/reg").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> myIdCorrelationSmsBeanAResultInfo) {
                String msg = myIdCorrelationSmsBeanAResultInfo.msg;
                int code = myIdCorrelationSmsBeanAResultInfo.code;
                //{"vip_end_time":0,"mobile":"15927678095","id":"2","vip":0}
                IdCorrelationLoginBean data = myIdCorrelationSmsBeanAResultInfo.data;

                loginSuccess(data);
            }


            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netLogin() {
        mLoadingDialog.showLoadingDialog();
        /*mIdCorrelationEngin.myLogin(mInputPhoneString, mInputPwdString, "user/login").subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<String> info) {
                String string=info.data;
                IdCorrelationLoginBean myIdCorrelationSmsBeanAResultInfo = new Gson().fromJson(string, IdCorrelationLoginBean.class);
                Log.d("mylog", "onNetNext: myIdCorrelationSmsBeanAResultInfo "+myIdCorrelationSmsBeanAResultInfo.toString());

//                loginSuccess(data);
            }
            @Override
            protected void onNetError(Throwable e) {
            }

            @Override
            protected void onNetCompleted() {
            }
        });*/
        mIdCorrelationEngin.login(mInputPhoneString, mInputPwdString, "user/login").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadingDialog, true) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> myIdCorrelationSmsBeanAResultInfo) {

                String msg = myIdCorrelationSmsBeanAResultInfo.msg;
                int code = myIdCorrelationSmsBeanAResultInfo.code;

                if (code == 0) { //未注册
//                    showToastShort("");
                    IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, mInputPhoneString, ID_CORRELATION_STATE_REGISTER);
                    return;
                }

                final IdCorrelationLoginBean data = myIdCorrelationSmsBeanAResultInfo.data;
                Log.d("mylog", "onNetNext: data " + data.toString());

                mEtPhone.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginSuccess(data);
                    }
                }, 500);

            }


            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
        /*mIdCorrelationEngin.login(mInputPhoneString, mInputPwdString, "user/login").subscribe(new Subscriber<ResultInfo<IdCorrelationLoginBean>>() {
            @Override
            public void onCompleted() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mylog", "onError: Throwable "+e);
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<IdCorrelationLoginBean> myIdCorrelationSmsBeanAResultInfo) {
                String msg = myIdCorrelationSmsBeanAResultInfo.message;
                int code = myIdCorrelationSmsBeanAResultInfo.code;
                IdCorrelationLoginBean data = myIdCorrelationSmsBeanAResultInfo.data;
                Log.d("mylog", "onNext: data "+data.toString());
                loginSuccess(data);
            }*/

           /* @Override
            protected void onNetNext(ResultInfo<IdCorrelationLoginBean> myIdCorrelationSmsBeanAResultInfo) {

                String msg = myIdCorrelationSmsBeanAResultInfo.message;
                int code = myIdCorrelationSmsBeanAResultInfo.code;
                IdCorrelationLoginBean data = myIdCorrelationSmsBeanAResultInfo.data;
                Log.d("mylog", "onNetNext: data "+myIdCorrelationSmsBeanAResultInfo.toString());
                loginSuccess(data);
            }


            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });*/
    }

    private void loginSuccess(IdCorrelationLoginBean data) {
        if (mIsKeepPwd) {
            SPUtils.put(this, SPUtils.LOGIN_PWD, mInputPwdString);
        } else {
            SPUtils.put(this, SPUtils.LOGIN_PWD, "");
        }
        SPUtils.put(this, SPUtils.LOGIN_MOBILE, mInputPhoneString);

        //持久化存储登录信息
        String str = JSON.toJSONString(data);// java对象转为jsonString
        BackfillSingle.backfillLoginData(this, str);

        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));

        finishAllIdCorActivity();
    }

    private void finishAllIdCorActivity() {
//        finish();
        YcApplication instance = YcApplication.getInstance();
        List<Activity> activities = instance.activityIdCorList;
        for (Activity activitie : activities
                ) {
            if (activitie != null && !activitie.isFinishing()) {
                activitie.finish();
            }
        }
    }

    private void changChecked(boolean isKeepPwd) {
        if (isKeepPwd) {
            mTvKeepPwdCheck.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_check_red, 0, 0,
                    0);
            Log.d("mylog", "changChecked: 保存密码");
        } else {
            mTvKeepPwdCheck.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_check_gray, 0, 0,
                    0);
            Log.d("mylog", "changChecked: 不用保存密码");
        }
    }

}
