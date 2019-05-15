package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.LoveHealingDetailBean;

/**
 * Created by Administrator on 2017/9/12.
 */

public class UpDownWomenHolder extends BaseViewHolder<LoveHealingDetailBean> {

    public UpDownWomenHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_up_down_women, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(LoveHealingDetailBean loveHealingDetailBean) {

        TextView tvName = itemView.findViewById(R.id.item_up_down_women_tv_name);
        tvName.setText(loveHealingDetailBean.content);
//        String ss = "这个意思就是说studio在解析这个图片时出错了，那我们点开这个图片重新画一下，这是第一步，有可能有的朋友到这儿就解决问题了这个意思就是说studio在解析这个图片时出错了，那我们点开这个图片重新画一下，这是第一步，有可能有的朋友到这儿就解决问题了";
//        tvName.setText(ss);

    }
}