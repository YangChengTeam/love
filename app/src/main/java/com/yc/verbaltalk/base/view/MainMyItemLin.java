package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.verbaltalk.R;

/**
 * Created by sunshey on 2019/4/28.
 */

public class MainMyItemLin extends LinearLayout {
    private TextView mTvSub;


//    private TextView mTvDes;

    public MainMyItemLin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainMyItemLin);
        try {
            String text = typedArray.getString(R.styleable.MainMyItemLin_textNameMyItem);
            String textSub = typedArray.getString(R.styleable.MainMyItemLin_textSubMyItem);
            Drawable imgSrc = typedArray.getDrawable(R.styleable.MainMyItemLin_imgSrc);
            boolean isAddIntervalTop = typedArray.getBoolean(R.styleable.MainMyItemLin_isAddIntervalTop, false);
            boolean isAddIntervalBom = typedArray.getBoolean(R.styleable.MainMyItemLin_isAddIntervalBom, false);

            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_main_my_item_lin, this, true);
            TextView tvDes = inflate.findViewById(R.id.main_my_item_lin_tv_des);
            mTvSub = inflate.findViewById(R.id.main_my_item_lin_tv_sub);
            View viewLineTop = inflate.findViewById(R.id.main_my_item_lin_view_line_top);
            View viewLineBom = inflate.findViewById(R.id.main_my_item_lin_view_line_bom);
            ImageView ivIcon = inflate.findViewById(R.id.main_my_item_lin_iv_icon);
            if (!isAddIntervalTop) {
                viewLineTop.setVisibility(GONE);
            }
            if (!isAddIntervalBom) {
                viewLineBom.setVisibility(GONE);
            }
            tvDes.setText(text);
            mTvSub.setText(textSub);
            ivIcon.setImageDrawable(imgSrc);
        } catch (Exception e) {
        } finally {
            typedArray.recycle();
        }


    }

    /*    public void setDes(String des) {
            if (!TextUtils.isEmpty(des)) {
                mTvDes.setText(des);
            }
        }*/
    public void setSub(String des) {
        if (!TextUtils.isEmpty(des)) {
            mTvSub.setText(des);
        }
    }
}
