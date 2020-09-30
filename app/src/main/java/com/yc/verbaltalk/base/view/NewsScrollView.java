package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by sunshey on 2019/5/10.
 */

public class NewsScrollView  extends ScrollView {

    private int scaledTouchSlop;
    private int y;
    private int x;


    public NewsScrollView(Context context) {
        this(context, null);
    }

    public NewsScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NewsScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) ev.getY();
                x = (int) ev.getX();
                break;

            case MotionEvent.ACTION_UP:
                int curY = (int) ev.getY();
                int curX = (int) ev.getX();
                if (Math.abs(curY - this.y) > scaledTouchSlop) {
                    return true;
                }

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScrollChange(l, t, oldl, oldt);
        }

    }

    public interface onScrollChangeListener {
        void onScrollChange(int l, int t, int oldl, int oldt);
    }

    private onScrollChangeListener listener;

    public onScrollChangeListener getListener() {
        return listener;
    }

    public void setOnScrollChangeListener(onScrollChangeListener listener) {
        this.listener = listener;
    }
}