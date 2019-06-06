package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.ui.activity.MainActivity;


public class MainT3TitleViewHolder extends BaseViewHolder<MainT3Bean> {

    private final Context context;
    private OnClickTitleIconListener onClickTitleIconListener;

    public MainT3TitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_t3title, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(MainT3Bean mainT3Bean) {

//        View viewBar = itemView.findViewById(R.id.item_t3title_view_bar);
        ImageView ivTitle = itemView.findViewById(R.id.item_t3title_iv_title);
        TextView tvIcon01 = itemView.findViewById(R.id.item_t3title_tv_icon_01);
        TextView tvIcon02 = itemView.findViewById(R.id.item_t3title_tv_icon_02);
        TextView tvIcon03 = itemView.findViewById(R.id.item_t3title_tv_icon_03);
        TextView tvIcon04 = itemView.findViewById(R.id.item_t3title_tv_icon_04);
        TextView tvIcon05 = itemView.findViewById(R.id.item_t3title_tv_icon_05);
        ImageView  ivTitleCase = itemView.findViewById(R.id.item_t3title_iv_title_case);
        MainActivity mainActivity = (MainActivity) context;
//        mainActivity.setStateBarHeight(viewBar);

        ivTitleCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTitleIconListener.clickTitleIcon(10);
            }
        });
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
}