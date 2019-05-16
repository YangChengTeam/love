package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.StringBean;

/**
 * Created by mayn on 2019/4/26.
 */

public class TitleT1ViewHolder extends BaseViewHolder<ExampListsBean> {

    private final Context context;
    private OnClickMainT1TitleListent onClickMainT1TitleListent;
    private TextView mTvShare;

    public TitleT1ViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_t1_view, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(ExampListsBean exampListsBean) {
        ImageView ivTitle = itemView.findViewById(R.id.item_title_view_iv_title);
        ImageView ivShare = itemView.findViewById(R.id.item_title_view_iv_share);
        mTvShare = itemView.findViewById(R.id.item_title_view_tv_share);
        ImageView ivModule02 = itemView.findViewById(R.id.item_title_view_iv_module_02);
        ImageView ivModule03 = itemView.findViewById(R.id.item_title_view_iv_module_03);


        ivModule02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMainT1TitleListent.clickIvModule02Listent();
            }
        });
        ivModule03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMainT1TitleListent.clickIvModule03Listent();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMainT1TitleListent.clickShareListent();
            }
        });
    }

    public interface OnClickMainT1TitleListent {
        void clickShareListent();

        void clickIvModule02Listent();

        void clickIvModule03Listent();
    }

    public void setOnClickShareListent(OnClickMainT1TitleListent onClickMainT1TitleListent) {
        this.onClickMainT1TitleListent = onClickMainT1TitleListent;
    }


    public void setTvShareText(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvShare.setText(text);
        }
    }

}
