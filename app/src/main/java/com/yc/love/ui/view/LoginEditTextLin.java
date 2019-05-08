package com.yc.love.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;

/**
 * Created by mayn on 2019/4/27.
 */

public class LoginEditTextLin extends LinearLayout {


    private final Context context;
    public EditText mEt;
    private TextView mTvCode,mTvCodeWait;


    public LoginEditTextLin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        this.context=context;
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginEditTextLin);
        String textHint = typedArray.getString(R.styleable.LoginEditTextLin_textHintLoginEt);
        Drawable iconSrc = typedArray.getDrawable(R.styleable.LoginEditTextLin_iconSrcLoginEt);
        int maxLength = typedArray.getInt(R.styleable.LoginEditTextLin_maxLengthLoginEt, 0);
        boolean isShowCode = typedArray.getBoolean(R.styleable.LoginEditTextLin_isShowCodeLoginEt, false);

        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_login_et_lin, this, true);
        ImageView ivIcon = inflate.findViewById(R.id.login_et_lin_iv_icon);
        mEt = inflate.findViewById(R.id.login_et_lin_et);
        mTvCode = inflate.findViewById(R.id.login_et_lin_tv_code);
        mTvCodeWait = inflate.findViewById(R.id.login_et_lin_tv_code_await);
        boolean isWait=true;
        waitCode(isWait);

        mEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)}); //最大输入长度
//        mEt.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
//        mEt.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框


        if (!isShowCode) {
            mTvCode.setVisibility(GONE);
        }else {
            mTvCode.setOnClickListener(onClickTvCodeListener);
        }
        if (!TextUtils.isEmpty(textHint)) {
            mEt.setHint(textHint);
        }
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }

    }

    public void waitCode(boolean isWait) {
        if(isWait){
            mTvCode.setVisibility(GONE);
            mTvCodeWait.setVisibility(VISIBLE);
        }else {
            mTvCodeWait.setVisibility(GONE);
            mTvCode.setVisibility(VISIBLE);
        }
    }

    /*  public void registerTvCodeListent(){
        mTvCode.setOnClickListener(onClickTvCodeListener);
        mTvCode.setTextColor(context.getResources().getColor(R.color.select_color_red_crimson));
    }
    public void unRegisterTvCodeListent(){
        mTvCode.setTextColor(context.getResources().getColor(R.color.red_crimson));
        mTvCode.setOnClickListener(null);
    }*/

    private OnClickListener onClickTvCodeListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickEtCodeListent.onClickEtCode();
        }
    };


    public String getEditTextTrim() {
        return mEt.getText().toString().trim();
    }

    public void setEditText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mEt.setText(text);
        }
    }

    public void setEditCodeText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvCode.setText(text);
        }
    }
    public void setEditCodeWaitText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvCodeWait.setText(text);
        }
    }



    public interface OnClickEtCodeListent {
        void onClickEtCode();
    }

    public void setOnClickEtCodeListent(OnClickEtCodeListent onClickEtCodeListent) {
        this.onClickEtCodeListent = onClickEtCodeListent;
    }

    private OnClickEtCodeListent onClickEtCodeListent;
}
