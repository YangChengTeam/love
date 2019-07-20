package com.music.player.lib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * TinyHung@Outlook.com
 * 2017/11/27.
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {


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
