package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.StringBean;

/**
 * Created by mayn on 2019/4/26.
 */

public class TitleViewHolder extends BaseViewHolder<StringBean> {

    private OnClickShareListent onClickShareListent;

    public TitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_view, listener);   //一个类对应一个布局文件
    }

    @Override
    public void bindData(StringBean stringBean) {
        ImageView ivShare = itemView.findViewById(R.id.item_title_view_iv_share);
        RelativeLayout rlShare = itemView.findViewById(R.id.item_title_view_rl_share);
//        RelativeLayout rlShare = itemView.findViewById(R.id.item_title_view_view_temp);
        View viewTemp = itemView.findViewById(R.id.item_title_view_view_temp);
        ViewGroup.LayoutParams layoutParams = rlShare.getLayoutParams();
        int heightShare = layoutParams.height;
        int heightShareHalf = heightShare / 2;

        Log.d("mylog", "bindData:  viewTemp.getHeight() " + viewTemp.getHeight());

        /*ViewGroup.LayoutParams layoutParams1 = viewTemp.getLayoutParams();
        layoutParams1.height=heightShareHalf;
        viewTemp.setLayoutParams(layoutParams1);*/
        Log.d("mylog", "bindData:  viewTemp.getHeight() " + viewTemp.getHeight());

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShareListent.clickShareListent();
            }
        });
    }

    public interface OnClickShareListent {
        void clickShareListent();
    }

    public void setOnClickShareListent(OnClickShareListent onClickShareListent) {
        this.onClickShareListent = onClickShareListent;
    }
}
