package com.music.player.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.music.player.lib.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/7/25 09:39.
 */
public class PlayerSeekbar extends FrameLayout {


    private SeekBar seekBar;
    private TextView tvProgress;
    private Context mContext;
    private Drawable mThumb;
    private int mThumbOffset;
    private int mScaledTouchSlop;
    private long mSeekBarMax;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private float mThumbLeft = 0;
    private float mThumbTop;
    private long mCurrentProgress;
    private int maxWidth;
    private int tvWidth;

    public PlayerSeekbar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PlayerSeekbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlayerSeekbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        View.inflate(context, R.layout.seekbar_view, this);
        seekBar = findViewById(R.id.play_seekBar);
        tvProgress = findViewById(R.id.play_text_progress);


        int w = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED);
        tvProgress.measure(w, h);
        measure(w, h);
        maxWidth = getMeasuredWidth();
        tvWidth = tvProgress.getMeasuredWidth();

        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void setSeekBarMax(long mSeekBarMax) {
        this.mSeekBarMax = mSeekBarMax;
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
//        mSeekBarDegree = (progress * 360 / mSeekBarMax);
//
        setThumbPosition();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

        String playProgress = simpleDateFormat.format(new Date((int) progress));//设置播放进度
        String totalProgress = simpleDateFormat.format(new Date((int) mSeekBarMax));//设置总进度

        tvProgress.setText(String.format(mContext.getString(R.string.play_progress), playProgress, totalProgress));

//        invalidate();
    }


    private void setThumbPosition() {

        float x = mSeekBarCenterX + (mCurrentProgress / mSeekBarMax * 1f) / mSeekBarMax;


        if (x + tvWidth <= maxWidth)
            tvProgress.setTranslationX(x);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
//        layoutParams.leftMargin = x;
//        tvProgress.setLayoutParams(layoutParams);
//        tvProgress.requestLayout();
    }


}
