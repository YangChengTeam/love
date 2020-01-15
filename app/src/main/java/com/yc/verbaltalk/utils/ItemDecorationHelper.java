package com.yc.verbaltalk.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.kk.utils.ScreenUtil;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wanglin  on 2018/10/25 13:45.
 */
public class ItemDecorationHelper extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int right = 0;
    private int bottom = 0;
    private int left = 0;
    private int top = 0;

    public ItemDecorationHelper(Context context, int right, int bottom) {
        this(context, 0, 0, right, bottom);
    }

    public ItemDecorationHelper(Context context, int bottom) {
        this(context, 0, bottom);
    }

    public ItemDecorationHelper(Context context, int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(ScreenUtil.dip2px(mContext, left), ScreenUtil.dip2px(mContext, top), ScreenUtil.dip2px(mContext, right), ScreenUtil.dip2px(mContext, bottom));
    }
}
