package com.yc.verbaltalk.ui.view.imgs;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/7.
 */

public interface  ImageLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}