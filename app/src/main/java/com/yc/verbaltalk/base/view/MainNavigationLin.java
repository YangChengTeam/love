package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.verbaltalk.R;

/**
 * Created by mayn on 2019/4/27.
 */

public class MainNavigationLin extends LinearLayout {


    private TextView mTvDes;

    public MainNavigationLin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainNavigationLin);
        String text = typedArray.getString(R.styleable.MainNavigationLin_textName);

        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_main_navigation_lin, this, true);
        mTvDes = inflate.findViewById(R.id.main_navigation_lin_tv_des);
        mTvDes.setText(text);
    }

    public void setDes(String des) {
        if (!TextUtils.isEmpty(des)) {
            mTvDes.setText(des);
        }
    }
}
