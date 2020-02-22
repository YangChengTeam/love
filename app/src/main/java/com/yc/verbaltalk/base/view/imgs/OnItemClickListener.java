package com.yc.verbaltalk.base.view.imgs;

/**
 * Created by mayn on 2019/5/7.
 */

public interface OnItemClickListener {

    int onCheckedClick(int position, Image image);

    void onImageClick(int position, Image image);
}
