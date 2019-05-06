package com.yc.love.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yc.love.R;
import com.yc.love.model.util.TimeUtils;
import com.yc.love.ui.activity.base.BaseSameActivity;
import com.yc.love.ui.view.CheckBoxSample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoActivity extends BaseSameActivity {

    private CheckBoxSample mCheckMen;
    private CheckBoxSample mCheckWoMen;
    private boolean mIsCheckedMen;
    private TimePickerView mPvTime;
    private EditText mEtName;
    private String mBirthdayString;
    private long mBirthdayLong;
    private TextView mTvDate;
    private String mStringEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initViews();
    }

    private void initViews() {
        LinearLayout llItem02 = findViewById(R.id.user_info_ll_item_02);
        RelativeLayout rlTitCon = findViewById(R.id.user_info_rl_tit_con);

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

        mTvDate.setText(TimeUtils.dateToYyMmDd(new Date(System.currentTimeMillis())));

        TextView tvSub = offerActivitySubTitleView();
        tvSub.setTextColor(getResources().getColor(R.color.red_crimson));
        tvSub.setText("完成");
        tvSub.setOnClickListener(this);
        rlTitCon.setOnClickListener(this);
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
                hindKeyboard(mEtName);
                showDateTimePickerView();
                break;
            case R.id.user_info_rl_tit_con:
                //TODO 修改头像
                break;
            case R.id.activity_base_same_tv_sub:
                boolean isCanSub = checkInput();
                if (!isCanSub) {
                    return;
                }
                Log.d("mylog", "onClick: 是男性吗 mIsCheckedMen " + mIsCheckedMen + " 昵称 mStringEtName" + mStringEtName
                        + " 生日 mBirthdayString " + mBirthdayString);
                break;
        }
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
        if (mPvTime == null) {
            mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    mBirthdayString = TimeUtils.dateToYyMmDd(date);
                    mBirthdayLong = TimeUtils.dateToStamp(date);
                    mTvDate.setText(mBirthdayString);
                }
            })
                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setSubmitColor(getResources().getColor(R.color.text_gray))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.text_gray))//取消按钮文字颜色
                    .build();
        }
        mPvTime.show();
    }

    @Override
    protected String offerActivityTitle() {
        return "个人信息";
    }
}
