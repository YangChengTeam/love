package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;
import com.yc.love.model.bean.StringBean;


public class DetailsBeanViewHolder extends BaseViewHolder<LoveHealDetDetailsBean> {

    private final Context context;
    private OnClickCopyListent onClickCopyListent;

    public DetailsBeanViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_details_bean, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(final LoveHealDetDetailsBean detailsBean) {
        ImageView ivSex = itemView.findViewById(R.id.item_details_bean_iv_sex);
        TextView tvName = itemView.findViewById(R.id.item_details_bean_tv_name);
        ImageView ivCopy = itemView.findViewById(R.id.item_details_bean_iv_copy);

        tvName.setText(detailsBean.content);
        String ansSex = detailsBean.ans_sex;
        if (!TextUtils.isEmpty(ansSex)) {
            if ("1".equals(ansSex)) { //1男2女0bi'a
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_men));
            } else if ("2".equals(ansSex)) {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));
            } else {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_nothing));
            }
        }
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCopyListent.onClickCopy(detailsBean);
            }
        });

    }

    public interface OnClickCopyListent {
        void onClickCopy(LoveHealDetDetailsBean detailsBean);
    }

    public void setOnClickCopyListent(OnClickCopyListent onClickCopyListent) {
        this.onClickCopyListent = onClickCopyListent;
    }

}