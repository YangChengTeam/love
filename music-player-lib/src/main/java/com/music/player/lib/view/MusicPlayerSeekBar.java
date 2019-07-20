package com.music.player.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.music.player.lib.R;
import com.music.player.lib.util.MusicPlayerUtils;

/**
 * 音乐播放器进度条控制器
 */

public class MusicPlayerSeekBar extends View {

    private Context mContext = null;
    private Drawable mThumbDrawable = null;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private int mSeekBarMax = 0;
    private Paint mSeekBarBackgroundPaint = null;
    private Paint mSeekbarProgressPaint = null;
    private RectF mArcRectF = null;
    private boolean mIsShowProgressText = false;
    private Paint mProgressTextPaint = null;
    private int mProgressTextSize = 0;
    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private int mSeekBarSize = 0;
    private int mSeekBarRadius = 0;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private float mThumbLeft = 0;
    private float mThumbTop = 0;
    private float mSeekBarDegree = 0;
    private long mCurrentProgress = 0;
    private float mDownX;
    private float mDownY;


    public MusicPlayerSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViewAttrs(attrs);
        mArcRectF = new RectF();
    }

    public MusicPlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViewAttrs(attrs);
        mArcRectF = new RectF();
    }

    public MusicPlayerSeekBar(Context context) {
        super(context);
        mContext = context;
        initViewDefault();
        mArcRectF = new RectF();
    }


    private void initViewAttrs(AttributeSet attrs) {

        TypedArray localTypedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MusicPlayerSeekBar);
        //thumb的属性是使用android:thumb属性进行设置的
        //返回的Drawable为一个StateListDrawable类型，即可以实现选中效果的drawable list
        //mThumbNormal和mThumbPressed则是用于设置不同状态的效果，当点击thumb时设置mThumbPressed，否则设置mThumbNormal
        mThumbDrawable = localTypedArray.getDrawable(R.styleable.MusicPlayerSeekBar_android_thumb);
        mThumbWidth = mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = mThumbDrawable.getIntrinsicHeight();

        //未选中的播放按钮
        mThumbNormal = new int[]{-android.R.attr.state_selected, -android.R.attr.state_checked};
        //选中效果的暂停按钮
        mThumbPressed = new int[]{android.R.attr.state_selected, android.R.attr.state_checked};

        float progressWidth = localTypedArray.getDimension(R.styleable.MusicPlayerSeekBar_progress_width, 5);
        int progressBackgroundColor = localTypedArray.getColor(R.styleable.MusicPlayerSeekBar_progress_background, Color.GRAY);
        int progressFrontColor = localTypedArray.getColor(R.styleable.MusicPlayerSeekBar_progress_front, Color.BLUE);
        mSeekBarMax = localTypedArray.getInteger(R.styleable.MusicPlayerSeekBar_progress_max, 100);

        mSeekbarProgressPaint = new Paint();
        mSeekBarBackgroundPaint = new Paint();

        mSeekbarProgressPaint.setColor(progressFrontColor);
        mSeekBarBackgroundPaint.setColor(progressBackgroundColor);

        mSeekbarProgressPaint.setAntiAlias(true);
        mSeekBarBackgroundPaint.setAntiAlias(true);

        mSeekbarProgressPaint.setStyle(Paint.Style.STROKE);
        mSeekBarBackgroundPaint.setStyle(Paint.Style.STROKE);

        mSeekbarProgressPaint.setStrokeWidth(progressWidth);
        mSeekBarBackgroundPaint.setStrokeWidth(progressWidth);

        mIsShowProgressText = localTypedArray.getBoolean(R.styleable.MusicPlayerSeekBar_show_progress_text, false);
        int progressTextStroke = (int) localTypedArray.getDimension(R.styleable.MusicPlayerSeekBar_progress_text_stroke_width, 5);
        int progressTextColor = localTypedArray.getColor(R.styleable.MusicPlayerSeekBar_progress_text_color, Color.GREEN);
        mProgressTextSize = (int) localTypedArray.getDimension(R.styleable.MusicPlayerSeekBar_progress_text_size, 30);

        mProgressTextPaint = new Paint();
        mProgressTextPaint.setColor(progressTextColor);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setStrokeWidth(progressTextStroke);
        mProgressTextPaint.setTextSize(mProgressTextSize);
        localTypedArray.recycle();
    }


    private void initViewDefault() {
        mThumbDrawable = null;
        mThumbWidth = 0;
        mThumbHeight = 0;

        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed,
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed,
                android.R.attr.state_selected, android.R.attr.state_checked};

        float progressWidth = 5;
        int progressBackgroundColor = Color.GRAY;
        int progressFrontColor = Color.BLUE;
        mSeekBarMax = 100;

        mSeekbarProgressPaint = new Paint();
        mSeekBarBackgroundPaint = new Paint();

        mSeekbarProgressPaint.setColor(progressFrontColor);
        mSeekBarBackgroundPaint.setColor(progressBackgroundColor);

        mSeekbarProgressPaint.setAntiAlias(true);
        mSeekBarBackgroundPaint.setAntiAlias(true);

        mSeekbarProgressPaint.setStyle(Paint.Style.STROKE);
        mSeekBarBackgroundPaint.setStyle(Paint.Style.STROKE);

        mSeekbarProgressPaint.setStrokeWidth(progressWidth);
        mSeekBarBackgroundPaint.setStrokeWidth(progressWidth);

        mIsShowProgressText = false;
        int progressTextStroke = 5;
        int progressTextColor = Color.GREEN;
        mProgressTextSize = 50;

        mProgressTextPaint = new Paint();
        mProgressTextPaint.setColor(progressTextColor);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setStrokeWidth(progressTextStroke);
        mProgressTextPaint.setTextSize(mProgressTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //7.1系统遇到的坑，必须精确测量View的宽高
        int speHeightSize = MeasureSpec.getSize(widthMeasureSpec);
        int speWidthSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), speWidthSize);
        mViewWidth = speWidthSize;
        mViewHeight = speHeightSize;
        mSeekBarSize = mViewWidth > mViewHeight ? mViewHeight : mViewWidth;
        mSeekBarCenterX = speWidthSize / 2;
        mSeekBarCenterY = speHeightSize / 2;
        mSeekBarRadius = mSeekBarSize / 2 - mThumbWidth / 2;
        int left = mSeekBarCenterX - mSeekBarRadius;
        int right = mSeekBarCenterX + mSeekBarRadius;
        int top = mSeekBarCenterY - mSeekBarRadius;
        int bottom = mSeekBarCenterY + mSeekBarRadius;
        mArcRectF.set(left, top, right, bottom);
        // 起始位置，3点钟方向,mSeekBarDegree
        setThumbPosition(Math.toRadians(mSeekBarDegree));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆形背景颜色
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius, mSeekBarBackgroundPaint);
        //圆形进度条颜色
        canvas.drawArc(this.mArcRectF, 0.0F, mSeekBarDegree, false, mSeekbarProgressPaint);
        drawThumbBitmap(canvas);
        drawProgressText(canvas);
    }

    private void drawThumbBitmap(Canvas canvas) {
        this.mThumbDrawable.setBounds((int) mThumbLeft, (int) mThumbTop,
                (int) (mThumbLeft + mThumbWidth), (int) (mThumbTop + mThumbHeight));
        this.mThumbDrawable.draw(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        if (mIsShowProgressText) {
            float textWidth = mProgressTextPaint.measureText("" + mCurrentProgress);
            //回执进度条
            String forTime = MusicPlayerUtils.stringForTime(mCurrentProgress);
            canvas.drawText(forTime, mSeekBarCenterX - textWidth / 2, mSeekBarCenterY + mProgressTextSize / 2, mProgressTextPaint);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPointOnThumb(eventX, eventY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                seekTo(eventX, eventY, false);
                break;
            case MotionEvent.ACTION_UP:
                float newDownX = event.getX();
                float newDownY = event.getY();
                //如果松手时X轴坐标小于按下时X坐标上下10个像素&&如果松手时Y轴坐标小于按下时Y坐标上下10个像素即在点击范围内
                if (newDownX < (mDownX + 10) && newDownX > (mDownX - 10) && newDownY < (mDownY + 10) && newDownY > (mDownY - 10)) {
                    mDownY = 0;
                    mDownY = 0;
                    if (null != mOnClickListener) {
                        mOnClickListener.onClickView();
                    }
                    return true;
                }
                mDownY = 0;
                mDownY = 0;
                seekTo(eventX, eventY, true);
                break;
        }
        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp) {

        if (isPointOnThumb(eventX, eventY) && !isUp) {
            double radian = Math.atan2(eventY - mSeekBarCenterY, eventX - mSeekBarCenterX);
            /*
             * 由于atan2返回的值为[-pi,pi]
             * 因此需要将弧度值转换一下，使得区间为[0,2*pi]
             */
            if (radian < 0) {
                radian = radian + 2 * Math.PI;
            }
            setThumbPosition(radian);
            mSeekBarDegree = (float) Math.round(Math.toDegrees(radian));
            mCurrentProgress = (int) (mSeekBarMax * mSeekBarDegree / 360);
            invalidate();
        } else {
            invalidate();
            if (null != mOnSeekbarChangeListene) {
                mOnSeekbarChangeListene.onSeekBarChange(mCurrentProgress);
            }
        }
    }

    /**
     * 设置暂停、播放按钮
     *
     * @param flag
     */
    public void setPlaying(boolean flag) {
        if (null != mThumbDrawable) {
            mThumbDrawable.setState(flag ? mThumbPressed : mThumbNormal);
        }
        invalidate();
    }


    public interface OnClickListener {
        void onClickView();
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnSeekbarChangeListene {
        void onSeekBarChange(long progress);
    }

    private OnSeekbarChangeListene mOnSeekbarChangeListene;

    public void setOnSeekbarChangeListene(OnSeekbarChangeListene onSeekbarChangeListene) {
        mOnSeekbarChangeListene = onSeekbarChangeListene;
    }

    private boolean isPointOnThumb(float eventX, float eventY) {
        boolean result = false;
        double distance = Math.sqrt(Math.pow(eventX - mSeekBarCenterX, 2)
                + Math.pow(eventY - mSeekBarCenterY, 2));
        if (distance < mSeekBarSize && distance > (mSeekBarSize / 2 - mThumbWidth)) {
            result = true;
        }
        return result;
    }

    private void setThumbPosition(double radian) {

        double x = mSeekBarCenterX + mSeekBarRadius * Math.cos(radian);
        double y = mSeekBarCenterY + mSeekBarRadius * Math.sin(radian);
        mThumbLeft = (float) (x - mThumbWidth / 2);
        mThumbTop = (float) (y - mThumbHeight / 2);
    }

    /**
     * 设置进度条
     *
     * @param progress
     */
    public void setProgress(long progress) {
        if (progress > mSeekBarMax) {
            progress = mSeekBarMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        mCurrentProgress = progress;
        mSeekBarDegree = (progress * 360 / mSeekBarMax);

        setThumbPosition(Math.toRadians(mSeekBarDegree));

        invalidate();
    }

    public long getProgress() {
        return mCurrentProgress;
    }

    public void setProgressMax(int max) {

        mSeekBarMax = max;
    }

    public int getProgressMax() {
        return mSeekBarMax;
    }

    public void setProgressThumb(int thumbId) {
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }

    public void setProgressWidth(int width) {

        mSeekbarProgressPaint.setStrokeWidth(width);
        mSeekBarBackgroundPaint.setStrokeWidth(width);
    }

    public void setProgressBackgroundColor(int color) {
        mSeekBarBackgroundPaint.setColor(color);
        invalidate();
    }

    public void setProgressFrontColor(int color) {
        mSeekbarProgressPaint.setColor(color);
    }

    public void setProgressTextColor(int color) {
        mProgressTextPaint.setColor(color);
    }

    public void setProgressTextSize(int size) {

        mProgressTextPaint.setTextSize(size);
    }

    public void setProgressTextStrokeWidth(int width) {

        mProgressTextPaint.setStrokeWidth(width);
    }

    public void setIsShowProgressText(boolean isShow) {
        mIsShowProgressText = isShow;
    }
}


