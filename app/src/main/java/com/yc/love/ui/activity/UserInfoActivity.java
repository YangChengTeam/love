package com.yc.love.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.CreateAdapter;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealItemViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealTitleViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.model.engin.IdCorrelationEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;
import com.yc.love.model.util.TimeUtils;
import com.yc.love.ui.activity.base.BasePushPhotoActivity;
import com.yc.love.ui.view.CheckBoxSample;
import com.yc.love.ui.view.CircleTransform;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserInfoActivity extends BasePushPhotoActivity {

    private CheckBoxSample mCheckMen;
    private CheckBoxSample mCheckWoMen;
    private boolean mIsCheckedMen;
    private TimePickerView mPvTime;
    private EditText mEtName;
    private String mBirthdayString;
    private long mBirthdayLong;
    private TextView mTvDate;
    private String mStringEtName;
    private ImageView mIvIcon;
    private IdCorrelationEngin mIdCorrelationEngin;
    private int mYearDefault, mMonthDefault, mDayDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mIdCorrelationEngin = new IdCorrelationEngin(this);
        initViews();
        initData();
    }


    private void initViews() {
        LinearLayout llItem02 = findViewById(R.id.user_info_ll_item_02);
        RelativeLayout rlTitCon = findViewById(R.id.user_info_rl_tit_con);
        mIvIcon = findViewById(R.id.user_info_iv_icon);

        mEtName = findViewById(R.id.user_info_et_name);

        mCheckMen = findViewById(R.id.user_info_check_men);
        mCheckWoMen = findViewById(R.id.user_info_check_women);
        TextView tvWomen = findViewById(R.id.user_info_tv_women);
        TextView tvMen = findViewById(R.id.user_info_tv_men);
        mTvDate = findViewById(R.id.user_info_tv_date);

        llItem02.setOnClickListener(this);
        mCheckMen.setOnClickListener(this);
        mCheckWoMen.setOnClickListener(this);
        tvWomen.setOnClickListener(this);
        tvMen.setOnClickListener(this);
        mIsCheckedMen = false;
        isCheckedMen(mIsCheckedMen);

        mTvDate.setText(TimeUtils.dateToYyMmDdDivide(new Date(System.currentTimeMillis())));

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
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            return;
        }
        mIdCorrelationEngin.userInfo(String.valueOf(id), "user/info").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadingDialog) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
                String nickName = idCorrelationLoginBean.nick_name;
                String birthday = idCorrelationLoginBean.birthday;
                String face = idCorrelationLoginBean.face;
                int sex = idCorrelationLoginBean.sex;

                if (!TextUtils.isEmpty(nickName)) {
                    mEtName.setText(nickName);
                    mEtName.setSelection(nickName.length());
                }
                if (!TextUtils.isEmpty(birthday)) {
                    int[] ints = TimeUtils.formattingAddDivide(birthday);
                    if (ints != null && ints.length >= 3) {
                        StringBuffer stringBuffer = new StringBuffer();
                        mYearDefault = ints[0];
                        mMonthDefault = ints[1];
                        mDayDefault = ints[2];
                        stringBuffer.append(mYearDefault).append("-");
                        if (mMonthDefault < 10) {
                            stringBuffer.append("0").append(mMonthDefault).append("-");
                        } else {
                            stringBuffer.append(mMonthDefault).append("-");
                        }
                        if (mDayDefault < 10) {
                            stringBuffer.append("0").append(mDayDefault);
                        } else {
                            stringBuffer.append(mDayDefault);
                        }
                        mTvDate.setText(stringBuffer.toString());
                        mBirthdayString = birthday;
                    }
                }
                if (sex != 0) {
                    mIsCheckedMen = true;
                    isCheckedMen(mIsCheckedMen);
                }
                if (!TextUtils.isEmpty(face)) {
                    Picasso.with(UserInfoActivity.this).load(face).placeholder(R.mipmap.main_icon_default_head).error(R.mipmap.main_icon_default_head).transform(new CircleTransform()).into(mIvIcon);
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void isCheckedMen(boolean isCheckedMen) {
        if (isCheckedMen) {
            mCheckMen.setChecked(true);
            mCheckWoMen.setChecked(false);
        } else {
            mCheckMen.setChecked(false);
            mCheckWoMen.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.user_info_check_men:
            case R.id.user_info_tv_men:
                if (!mIsCheckedMen) {
                    mIsCheckedMen = !mIsCheckedMen;
                    isCheckedMen(mIsCheckedMen);
                }
                break;
            case R.id.user_info_check_women:
            case R.id.user_info_tv_women:
                if (mIsCheckedMen) {
                    mIsCheckedMen = !mIsCheckedMen;
                    isCheckedMen(mIsCheckedMen);
                }
                break;
            case R.id.user_info_ll_item_02:
                if (mYearDefault > 0) {
                    showDateTimePickerView(mYearDefault, mMonthDefault, mDayDefault);
                } else {
                    showDateTimePickerView();
                }
                break;
            case R.id.user_info_rl_tit_con:
                showSelsctPhotoDialog(mIvIcon);
                break;
            case R.id.activity_base_same_tv_sub:
                boolean isCanSub = checkInput();
                if (!isCanSub) {
                    return;
                }
                netUpdateInfo();
                break;
        }
    }

    private void netUpdateInfo() {
        String pwd = (String) SPUtils.get(this, SPUtils.LOGIN_PWD, "");
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        //TODO 图片上传 生日
        String face = "https://avatar.csdn.net/A/1/4/3_lawsonjin.jpg";
//     String sex=mIsCheckedMen?"1":"0";
        mIdCorrelationEngin.updateInfo(String.valueOf(id), mStringEtName, mBirthdayString, mIsCheckedMen ? "1" : "0", face, pwd, "user/update").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadingDialog) {
            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {

                IdCorrelationLoginBean data = idCorrelationLoginBeanAResultInfo.data;
                //持久化存储登录信息
                String str = JSON.toJSONString(data);// java对象转为jsonString
                BackfillSingle.backfillLoginData(UserInfoActivity.this, str);

                EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_UPDATE_INFO));
                showToastShort("完善信息成功");
                finish();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private boolean checkInput() {
        mStringEtName = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(mStringEtName)) {
            showToastShort("请输入昵称");
            return false;
        }
        if (TextUtils.isEmpty(mBirthdayString)) {
            showToastShort("请选择生日");
            return false;
        }
        long toDayStamp = TimeUtils.dateToStamp(new Date(System.currentTimeMillis()));
        if (mBirthdayLong > toDayStamp) {
            showToastShort("生日应大于今天");
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
            mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    mBirthdayString = TimeUtils.dateToYyMmDd(date);
                    mBirthdayLong = TimeUtils.dateToStamp(date);
                    mTvDate.setText(TimeUtils.dateToYyMmDdDivide(date));
                }
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
        byte[] bytes = new byte[]{};
        try {
            bytes = readStream(file.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = String.valueOf(bytes);
//        netSSSSData(s);
    }

    private void netSSSSData(String sss) {
        mLoadingDialog.showLoadingDialog();
        mIdCorrelationEngin.uploadUommon(sss, "common/upload").subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<String> stringAResultInfo) {
                String data = stringAResultInfo.data;
                Log.d("mylog", "onNetNext: data " + data);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }
}
