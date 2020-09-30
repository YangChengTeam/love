package com.yc.verbaltalk.mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BasePushPhotoActivity;
import com.yc.verbaltalk.base.engine.UploadPhotoEngin;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.CheckBoxSample;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.UploadPhotoBean;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.model.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

public class UserInfoActivity extends BasePushPhotoActivity {

    private CheckBoxSample mCheckMen;
    private CheckBoxSample mCheckWoMen;
    private boolean mIsCheckedMen;
    private TimePickerView mPvTime;
    private EditText mEtName;
    private String mBirthdayString;
    private long mBirthdayLong;
    private TextView mTvBirthday;
    private String mStringEtName;
    private ImageView mIvIcon;
    private UserInfoEngine userInfoEngine;
    private int mYearDefault, mMonthDefault, mDayDefault;
    private String mPhotoUrl;
    private boolean isSetPwd;
    private TextView tvPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userInfoEngine = new UserInfoEngine(this);
        initViews();
        initData();
    }


    private void initViews() {
        LinearLayout llBirthday = findViewById(R.id.user_info_ll_birthday);
        RelativeLayout rlTitCon = findViewById(R.id.user_info_rl_tit_con);
        mIvIcon = findViewById(R.id.user_info_iv_icon);

        mEtName = findViewById(R.id.user_info_et_name);

        mCheckMen = findViewById(R.id.user_info_check_men);
        mCheckWoMen = findViewById(R.id.user_info_check_women);
        TextView tvWomen = findViewById(R.id.user_info_tv_women);
        TextView tvMen = findViewById(R.id.user_info_tv_men);
        mTvBirthday = findViewById(R.id.user_info_tv_birthday);
        RelativeLayout rlInfoPwd = findViewById(R.id.rl_info_pwd);
        tvPwd = findViewById(R.id.tv_pwd);
        TextView tvPhone = findViewById(R.id.tv_phone);
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (null != userInfo) {
            if (!TextUtils.isEmpty(userInfo.pwd)) {
                isSetPwd = true;
            }
            String mobile = userInfo.mobile;
            if (!TextUtils.isEmpty(mobile)) {
                tvPhone.setText(mobile.replace(mobile.substring(3, 7), "****"));
            }
        }

        tvPwd.setText(isSetPwd ? "修改密码" : "设置密码");


        llBirthday.setOnClickListener(this);
        mCheckMen.setOnClickListener(this);
        mCheckWoMen.setOnClickListener(this);
        tvWomen.setOnClickListener(this);
        tvMen.setOnClickListener(this);
        rlInfoPwd.setOnClickListener(this);

//        isCheckedMen(mIsCheckedMen);

        mTvBirthday.setText(TimeUtils.dateToYyMmDdDivide(new Date(System.currentTimeMillis())));

        TextView tvSub = offerActivitySubTitleView();
        tvSub.setTextColor(getResources().getColor(R.color.red_crimson));
        tvSub.setText("完成");
        tvSub.setOnClickListener(this);
        rlTitCon.setOnClickListener(this);
    }

    private void initData() {
       /* YcSingle instance = YcSingle.getInstance();
        String nick_name = instance.nick_name;
        if (!TextUtils.isEmpty(nick_name)) {
            mEtName.setText(nick_name);
            mEtName.setSelection(nick_name.length());
        }*/

        netData();
    }

    private void netData() {

        if (null == mLoadingDialog) mLoadingDialog = new LoadDialog(this);
        mLoadingDialog.showLoadingDialog("加载中...");
        userInfoEngine.userInfo(UserInfoHelper.getUid()).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {

            @Override
            public void onComplete() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                mLoadingDialog.dismissLoadingDialog();
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    UserInfo idCorrelationLoginBean = userInfoResultInfo.data;
                    String nickName = idCorrelationLoginBean.nick_name;
                    String birthday = idCorrelationLoginBean.birthday;
                    String face = idCorrelationLoginBean.face;
                    int sex = idCorrelationLoginBean.sex;

                    if (!TextUtils.isEmpty(nickName)) {
                        mEtName.setText(nickName);
                        mEtName.setSelection(nickName.length());
                    }
                    if (!TextUtils.isEmpty(birthday)) {

                        mTvBirthday.setText(TimeUtils.formattingDate(birthday));
                        mBirthdayString = birthday;

                    }
                    mIsCheckedMen = sex != 0;
                    isCheckedMen(mIsCheckedMen);
                    if (!TextUtils.isEmpty(face)) {
                        Glide.with(UserInfoActivity.this).load(face).apply(RequestOptions.circleCropTransform()
                                .error(R.mipmap.main_icon_default_head).placeholder(R.mipmap.main_icon_default_head)).into(mIvIcon);
                        mPhotoUrl = face;
                    }
                }
            }


        });
    }

    private void isCheckedMen(boolean isCheckedMen) {

        mCheckMen.setChecked(isCheckedMen);
        mCheckWoMen.setChecked(!isCheckedMen);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.user_info_check_men:
            case R.id.user_info_tv_men:
                if (!mIsCheckedMen) {
                    mIsCheckedMen = true;
                    isCheckedMen(true);
                }
                break;
            case R.id.user_info_check_women:
            case R.id.user_info_tv_women:
                if (mIsCheckedMen) {
                    mIsCheckedMen = false;
                    isCheckedMen(false);
                }
                break;
            case R.id.user_info_ll_birthday:
                if (mYearDefault > 0) {
                    showDateTimePickerView(mYearDefault, mMonthDefault, mDayDefault);
                } else {
                    showDateTimePickerView();
                }
                break;
            case R.id.user_info_rl_tit_con:
//                showToastShort("");
                showSelsctPhotoDialog(mIvIcon);
                break;
            case R.id.activity_base_same_tv_sub:
                boolean isCanSub = checkInput();
                if (!isCanSub) {
                    return;
                }
//                if (TextUtils.isEmpty(mPhotoUrl)) {
//                    SnackBarUtils.tips(this, "请修改头像");
//                    return;
//                }
                netUpdateInfo();
                break;
            case R.id.rl_info_pwd:

                Intent intent = new Intent(this, PwdInfoActivity.class);
                intent.putExtra("isSetPwd", isSetPwd);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPwdSuccess(String success) {
        if (TextUtils.equals("set_pwd_success", success)) {
            isSetPwd = true;
            tvPwd.setText("修改密码");
        }
    }

    private void netUpdateInfo() {


        mLoadingDialog.showLoadingDialog();
        userInfoEngine.updateInfo(UserInfoHelper.getUid(), mStringEtName, mBirthdayString, mIsCheckedMen ? "1" : "0", mPhotoUrl, "")
                .subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (userInfoResultInfo != null) {
                            if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                                UserInfo userInfo = UserInfoHelper.getUserInfo();
                                UserInfo data = userInfoResultInfo.data;
                                if (null != userInfo && TextUtils.isEmpty(userInfo.pwd)) {
                                    data.pwd = userInfo.pwd;
                                }
                                UserInfoHelper.saveUserInfo(data);


                                EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_UPDATE_INFO, data.nick_name));
                                ToastUtils.showCenterToast("完善信息成功");
                                finish();
                            } else {
                                ToastUtils.showCenterToast(userInfoResultInfo.message);
                            }
                        }
                    }


                });
    }

    private boolean checkInput() {
        mStringEtName = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(mStringEtName)) {
            ToastUtils.showCenterToast("请输入昵称");
            return false;
        }
        if (TextUtils.isEmpty(mBirthdayString)) {
            ToastUtils.showCenterToast("请选择生日");
            return false;
        }
        long toDayStamp = TimeUtils.dateToStamp(new Date(System.currentTimeMillis()));
        if (mBirthdayLong > toDayStamp) {
            ToastUtils.showCenterToast("生日应小于今天");
            return false;
        }
        return true;
    }

    private void showDateTimePickerView() {
        showDateTimePickerView(0, 0, 0);
    }

    private void showDateTimePickerView(int year, int month, int day) {
        hindKeyboard(mEtName);
        if (mPvTime == null) {
            mPvTime = new TimePickerBuilder(this, (date, v) -> {
                mBirthdayString = TimeUtils.dateToYyMmDd(date);
                mBirthdayLong = TimeUtils.dateToStamp(date);
                mTvBirthday.setText(TimeUtils.dateToYyMmDdDivide(date));
            })
                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setSubmitColor(getResources().getColor(R.color.text_gray))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.text_gray))//取消按钮文字颜色
                    .build();
            if (year > 0) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month - 1, day);
                mPvTime.setDate(selectedDate);// 如果不设置的话，默认是系统时间
            }
        }
        mPvTime.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPvTime != null) {
            mPvTime = null;
        }
    }

    @Override
    protected String offerActivityTitle() {
        return "个人信息";
    }

    @Override
    protected void onLubanFileSuccess(File file) {

        new UploadPhotoEngin(this, file) {
            @Override
            public void onSuccess(ResponseBody body) {
                String string = null;
                try {
                    string = body.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("securityhttp", "common/upload: response body " + string);
                Log.d("mylog", "onResponse: response body " + string);
                if (!TextUtils.isEmpty(string)) {
                    UploadPhotoBean uploadPhotoBean = null;
                    try {
                        uploadPhotoBean = new Gson().fromJson(string, UploadPhotoBean.class);
                    } catch (IllegalStateException e) {

                    }
                    if (uploadPhotoBean != null) {
                        List<UploadPhotoBean.DataBean> data = uploadPhotoBean.data;
                        if (data != null && data.size() > 0) {
                            UploadPhotoBean.DataBean dataBean = data.get(0);
                            mPhotoUrl = dataBean.url;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }
        };

//        netSSSSData(s);
    }

}
