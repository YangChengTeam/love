package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by mayn on 2019/4/28.
 */

public class RoundCornerImg extends AppCompatImageView {
    float width, height;
    int corners = 12;

    public RoundCornerImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    public void setCorner(int corner) {
        this.corners = corner;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= 12 && height > 12) {
            Path path = new Path();
            //四个圆角
            path.moveTo(corners, 0);
            path.lineTo(width - corners, 0);
            path.quadTo(width, 0, width, corners);
            path.lineTo(width, height - corners);
            path.quadTo(width, height, width - corners, height);
            path.lineTo(corners, height);
            path.quadTo(0, height, 0, height - corners);
            path.lineTo(0, corners);
            path.quadTo(0, 0, corners, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}

