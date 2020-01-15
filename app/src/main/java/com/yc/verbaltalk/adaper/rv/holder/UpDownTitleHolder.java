package com.yc.verbaltalk.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.base.RecyclerViewItemListener;
import com.yc.verbaltalk.model.bean.LoveHealingDetailBean;

/**
 * Created by Administrator on 2017/9/12.
 */

public class UpDownTitleHolder extends BaseViewHolder<LoveHealingDetailBean> {

    public UpDownTitleHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_up_down_title, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealingDetailBean loveHealingDetailBean) {

    }
}