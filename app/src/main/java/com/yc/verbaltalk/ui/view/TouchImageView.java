package com.yc.verbaltalk.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by suns  on 2019/9/5 09:14.
 */
public class TouchImageView extends AppCompatImageView {
    private static final String TAG = "TouchImageView";
    private int startX;
    private int startY;

    private int screenX;

    private int endX;
    private int endY;

    public TouchImageView(Context context) {
        super(context);
        init(context);
    }

    public TouchImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

//        helper = ViewDragHelper.create(this, 1.0f, new DragViewCallback());
//        helper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
//        preRect = new Rect();


//        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        post(() -> {
            int[] location = new int[2];
            getLocationOnScreen(location);
            screenX = location[0];
            int y = location[1];
            Log.e(TAG, "init: " + screenX + "--" + y);
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
//                Log.e(TAG, "onTouch down: " + "--x--" + startX + "--y--" + startY);
                break;
            case MotionEvent.ACTION_MOVE:
                endX = x;
                endY = y;
                // 计算偏移量
                int offsetX = endX - startX;
                int offsetY = endY - startY;
                // 在当前left、top、right、bottom的基础上加上偏移量
                layout(getLeft() + offsetX,
                        getTop() + offsetY,
                        getRight() + offsetX,
                        getBottom() + offsetY);
//                offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);
//                Log.e(TAG, "onTouch move: " + "--x--" + endX + "--y--" + endY);

                break;
            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "onTouch up: " + "--endX--" + endX + "-upX-" + x);
                if (endX == 0 && endY == 0) {
                    return super.onTouchEvent(event);
                }

                if (Math.abs(x - endX) > 0 || Math.abs(y - endY) > 0) {
                    //表示移动
                    endX = 0;
                    endY = 0;
                    return true;
                } else {
                    return super.onTouchEvent(event);
                }
        }
        return super.onTouchEvent(event);
    }


}
