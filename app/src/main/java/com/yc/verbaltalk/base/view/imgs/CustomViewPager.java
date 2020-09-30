package com.yc.verbaltalk.base.view.imgs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;



public class CustomViewPager extends ViewPager {

    private boolean isLocked;

    public CustomViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

}