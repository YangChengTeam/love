package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;

import java.util.List;


public class LoveHealDetItemHolder extends BaseViewHolder<LoveHealDetBean> {

    private final Context context;

    public LoveHealDetItemHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_heal_det, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealDetBean loveHealDetBean) {

        Log.d("mylog", "bindData: loveHealDetBean "+loveHealDetBean.toString());

        LinearLayout llCon = itemView.findViewById(R.id.item_love_heal_det_ll_con);


        List<LoveHealDetBean.DetailsBean> details = loveHealDetBean.details;
        for (LoveHealDetBean.DetailsBean detailsBean : details
                ) {
            TextView textView = new TextView(context);
            textView.setText(detailsBean.content);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
//            layoutParams.setMargins(20,20,20,20);
            textView.setPadding(20, 20, 20, 20);
            llCon.addView(textView);
        }

//        tvName.setText(mainT2Bean.name);

    }
}