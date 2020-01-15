package com.yc.verbaltalk.ui.view;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

public class ColorFlipPagerTitleView extends SimplePagerTitleView {
    private float mChangePercent = 0.5f;

    public ColorFlipPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (leavePercent >= mChangePercent) {
            setTextColor(mNormalColor);
        } else {
            setTextColor(mSelectedColor);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (enterPercent >= mChangePercent) {
            setTextColor(mSelectedColor);
        } else {
            setTextColor(mNormalColor);
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    public float getChangePercent() {
        return mChangePercent;
    }

    public void setChangePercent(float changePercent) {
        mChangePercent = changePercent;
    }
}
