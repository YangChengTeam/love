package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSlidingActivity;
import com.yc.love.ui.view.LoginEditTextLin;

public class IdCorrelationSlidingActivity extends BaseSlidingActivity implements View.OnClickListener {

    private LoginEditTextLin mEtCode;

    public static final int ID_CORRELATION_STATE_LOGIN = 1; //登录
    private final int ID_CORRELATION_STATE_REGISTER = 2; //注册
    private final int ID_CORRELATION_STATE_SEEK = 3;  //忘记密码
    private TextView mTvTitle;
    private TextView mTvSwitch;
    private TextView mTvRegister;
    private LinearLayout mLlKeepPwdCon;
    private int mInitentState;
    private TextView mTvProtocolDes;
    private LinearLayout mLlProtocolCon;
//    private ImageView mIvKeepPwdCheck;

    private boolean mIsKeepPwd = false;
    private TextView mTvKeepPwdCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_correlation);

        //侵入状态栏
        invadeStatusBar();
        initInitent();
        initViews();
        Log.d("mylog", "onCreate: mInitentState " + mInitentState);
        initState(mInitentState);
    }

    private void initInitent() {
        Intent intent = getIntent();
        mInitentState = intent.getIntExtra("state", -1);
    }

    public static void startIdCorrelationActivity(Context context, int state) {
        Intent intent = new Intent(context, IdCorrelationSlidingActivity.class);
        intent.putExtra("state", state);
        context.startActivity(intent);
    }

    private void initState(int state) {
        switch (state) {
            case ID_CORRELATION_STATE_LOGIN:
                setTiele("登录", "注册");
                mTvRegister.setVisibility(View.VISIBLE);
                mLlKeepPwdCon.setVisibility(View.VISIBLE);
                mEtCode.setVisibility(View.GONE);
                mTvProtocolDes.setText("登录即表示您同意");
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

        LoginEditTextLin etPhone = findViewById(R.id.id_correlation_let_phont);
        mEtCode = findViewById(R.id.id_correlation_let_code);
        LoginEditTextLin etPwd = findViewById(R.id.id_correlation_let_pwd);
        TextView tvNext = findViewById(R.id.id_correlation_tv_next);
        mTvRegister = findViewById(R.id.id_correlation_tv_register);
        mLlKeepPwdCon = findViewById(R.id.id_correlation_ll_keep_pwd_con);
        mTvKeepPwdCheck = findViewById(R.id.id_correlation_tv_keep_pwd_check);
        TextView tvKeepPwdSeek = findViewById(R.id.id_correlation_tv_keep_pwd_seek);
        changChecked(mIsKeepPwd);
        //        mEt.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
//        mEt.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        etPhone.mEt.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        mEtCode.mEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPwd.mEt.setInputType(0x81);


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

    private LoginEditTextLin.OnClickEtCodeListent clickEtCodeListent = new LoginEditTextLin.OnClickEtCodeListent() {
        @Override
        public void onClickEtCode() {
            mEtCode.setEditCodeText("mEtCode");
        }
    };


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
                //TODO  mIsKeepPwd true-->记住密码  false-->不用记住密码
                break;
            case R.id.id_correlation_tv_protocol:
                showToastShort("protocol");
                break;
            case R.id.id_correlation_tv_register:
                IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_REGISTER);
                break;
            case R.id.id_correlation_tv_keep_pwd_check:
                mIsKeepPwd = !mIsKeepPwd;
                changChecked(mIsKeepPwd);
                break;
            case R.id.id_correlation_tv_keep_pwd_seek:
                IdCorrelationSlidingActivity.startIdCorrelationActivity(IdCorrelationSlidingActivity.this, ID_CORRELATION_STATE_REGISTER);
                break;
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
