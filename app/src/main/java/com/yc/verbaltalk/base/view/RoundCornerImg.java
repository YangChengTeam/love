package com.yc.verbaltalk.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by sunshey on 2019/4/28.
 */

public class RoundCornerImg extends AppCompatImageView {
    private float width, height;
    private int corners = 12;

    private Paint mPaint;

    public RoundCornerImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

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
            //quadTo 贝塞尔曲线  quadTo(float x1, float y1, float x2, float y2)
            //x1,y1分别是控制点的横坐标纵坐标
            //x2,y2分别是结束点点横坐标纵坐标
            path.quadTo(0, 0, corners, 0);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }


}

