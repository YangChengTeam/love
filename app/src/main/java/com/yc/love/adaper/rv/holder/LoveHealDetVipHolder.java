package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;

import java.util.List;


public class LoveHealDetVipHolder extends BaseViewHolder<LoveHealDetBean> {

    private final Context context;

    public LoveHealDetVipHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_heal_det_vip, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealDetBean loveHealDetBean) {

//        TextView tvSex = itemView.findViewById(R.id.item_love_heal_det_vip_tv_sex);
        ImageView ivSex = itemView.findViewById(R.id.item_love_heal_det_vip_iv_sex);
        TextView tvName = itemView.findViewById(R.id.item_love_heal_det_vip_tv_name);

        List<LoveHealDetDetailsBean> details = loveHealDetBean.details;
        if(details==null||details.size()==0){
            details = loveHealDetBean.detail;
        }

        if (details != null && details.size() > 0) {
            LoveHealDetDetailsBean detailsBean = details.get(0);
            tvName.setText(detailsBean.content);
            String ansSex = detailsBean.ans_sex;
            if (!TextUtils.isEmpty(ansSex)) {

//                tvSex.setText("女:");
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));

               /* if ("1".equals(ansSex)) { //1男2女0bi'a
                    tvSex.setText("男:");
                } else {
                    tvSex.setText("女:");
                }*/

                /*if ("1".equals(ansSex)) { //1男2女0bi'a
                    ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_men));
                } else if ("2".equals(ansSex)) {
                    ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));
                } else {
                    ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_nothing));
                }*/
            }
        }
    }
}