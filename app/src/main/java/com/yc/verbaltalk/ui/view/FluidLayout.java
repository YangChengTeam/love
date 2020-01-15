package com.yc.verbaltalk.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.yc.verbaltalk.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 流式布局
 */

public class FluidLayout extends ViewGroup {

    /**
     * 最大的行数，为0表示自适应
     */
    private int maxlinecount;

    @ViewDebug.ExportedProperty(category = "measurement", flagMapping = {
            @ViewDebug.FlagToString(mask = Gravity.TOP,
                    equals = Gravity.TOP, name = "TOP"),
            @ViewDebug.FlagToString(mask = Gravity.BOTTOM,
                    equals = Gravity.BOTTOM, name = "BOTTOM"),
            @ViewDebug.FlagToString(mask = Gravity.CENTER,
                    equals = Gravity.CENTER, name = "CENTER")
    }, formatToHexString = true)
    private int mGravity = Gravity.TOP;

    private List<List<View>> mViews = new ArrayList<List<View>>();
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public FluidLayout(Context context) {
        this(context, null);
    }

    public FluidLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FluidLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FluidLayout);
        int index = a.getInt(R.styleable.FluidLayout_fluid_layout_gravity, -1);
        maxlinecount = a.getInt(R.styleable.FluidLayout_maxlinecount, 0);
        if (index >= 0) {
            setGravity(index);
        }
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom()
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mViews.add(lineViews);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList<View>();
            }

            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        //记录每行高度，为后面设置子view单独top做准备，比如gravity
        mLineHeight.add(lineHeight);
        mViews.add(lineViews);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineCount = mViews.size();

        if (maxlinecount > 0 && maxlinecount < lineCount) {
            lineCount = maxlinecount;
        }

        for (int i = 0; i < lineCount; i++) {
            lineViews = mViews.get(i);
            lineHeight = mLineHeight.get(i);
            int lineViewCount = lineViews.size();

            for (int j = 0; j < lineViewCount; j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }

                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity = lp.gravity;
                if (gravity < 0) {
                    gravity = mGravity;
                }

                int tmpTop = top;
                if (gravity == Gravity.CENTER) {
                    tmpTop = top + (lineHeight - lp.topMargin - lp.bottomMargin - child.getMeasuredHeight()) / 2;
                } else if (gravity == Gravity.BOTTOM) {
                    tmpTop = top + (lineHeight - lp.topMargin - lp.bottomMargin - child.getMeasuredHeight());
                }

                int lv = left + lp.leftMargin;
                int tv = tmpTop + lp.topMargin;
                int rv = lv + child.getMeasuredWidth();
                int bv = tv + child.getMeasuredHeight();

                child.layout(lv, tv, rv, bv);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            requestLayout();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        @ViewDebug.ExportedProperty(category = "layout", mapping = {
                @ViewDebug.IntToString(from = Gravity.TOP, to = "TOP"),
                @ViewDebug.IntToString(from = Gravity.BOTTOM, to = "BOTTOM"),
                @ViewDebug.IntToString(from = Gravity.CENTER, to = "CENTER")
        })
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a =
                    c.obtainStyledAttributes(attrs, R.styleable.FluidLayout_Layout);
            gravity = a.getInt(R.styleable.FluidLayout_Layout_fluid_layout_layout_gravity, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }
    }
}
