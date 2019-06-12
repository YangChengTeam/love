package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.LoveHealBean;

/**
 * Created by mayn on 2019/4/26.
 */

public class TitleT1CreateViewHolder extends BaseViewHolder<LoveHealBean> {

    //TODO delit

    private final Context context;
//    private OnClickMainT1TitleListent onClickMainT1TitleListent;
    private TextView mTvShare;

    private OnClickTitleIconListener onClickTitleIconListener;

    public TitleT1CreateViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_t1_view, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealBean exampListsBean) {
        ImageView ivTitle = itemView.findViewById(R.id.item_title_view_iv_title);
        ImageView ivShare = itemView.findViewById(R.id.item_title_view_iv_share);
        mTvShare = itemView.findViewById(R.id.item_title_view_tv_share);
        ImageView ivModule02 = itemView.findViewById(R.id.item_title_view_iv_module_02);
        ImageView ivModule03 = itemView.findViewById(R.id.item_title_view_iv_module_03);

        TextView tvIcon01 = itemView.findViewById(R.id.item_title_t1_tv_icon_01);
        TextView tvIcon02 = itemView.findViewById(R.id.item_title_t1_tv_icon_02);
        TextView tvIcon03 = itemView.findViewById(R.id.item_title_t1_tv_icon_03);
        TextView tvIcon04 = itemView.findViewById(R.id.item_title_t1_tv_icon_04);
        TextView tvIcon05 = itemView.findViewById(R.id.item_title_t1_tv_icon_05);
//        MainActivity mainActivity = (MainActivity) context;?
//        mainActivity.setStateBarHeight(viewBar);

        tvIcon01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(0);
            }
        });
        tvIcon02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(1);
            }
        });
        tvIcon03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(2);
            }
        });
        tvIcon04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(3);
            }
        });
        tvIcon05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(4);
            }
        });


        ivModule02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(5);
//                onClickMainT1TitleListent.clickIvModule02Listent();
            }
        });
        ivModule03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(6);
//                onClickMainT1TitleListent.clickIvModule03Listent();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(7);
//                onClickMainT1TitleListent.clickShareListent();
            }
        });
    }
    public interface OnClickTitleIconListener {
        void clickTitleIcon(int position);

        /*void clickTitleIcon02();

        void clickTitleIcon03();

        void clickTitleIcon04();

        void clickTitleIcon05();*/
    }

    public void setOnClickTitleIconListener(OnClickTitleIconListener onClickTitleIconListener) {
        this.onClickTitleIconListener = onClickTitleIconListener;
    }


    /*public interface OnClickMainT1TitleListent {
        void clickShareListent();

        void clickIvModule02Listent();

        void clickIvModule03Listent();
    }

    public void setOnClickShareListent(OnClickMainT1TitleListent onClickMainT1TitleListent) {
        this.onClickMainT1TitleListent = onClickMainT1TitleListent;
    }*/


    public void setTvShareText(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvShare.setText(text);
        }
    }

}
