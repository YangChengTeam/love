package com.music.player.lib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * TinyHung@Outlook.com
 * 2017/11/27.
 */

public class MarqueeTextView extends AppCompatTextView {


    public MarqueeTextView(Context context) {
        super(context);

    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }


}
