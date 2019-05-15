package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;
import com.yc.love.model.bean.StringBean;

import java.util.List;


public class LoveHealDetItemHolder extends BaseViewHolder<LoveHealDetBean> {

    private final Context context;
    private OnClickCopyListent onClickCopyListent;

    public LoveHealDetItemHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_love_heal_det, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(LoveHealDetBean loveHealDetBean) {
        RecyclerView recyclerView = itemView.findViewById(R.id.item_love_heal_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        List<LoveHealDetDetailsBean> details = loveHealDetBean.details;

        NoThingAdapter<LoveHealDetDetailsBean> adapter = new NoThingAdapter<LoveHealDetDetailsBean>(details, recyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                DetailsBeanViewHolder detailsBeanViewHolder = new DetailsBeanViewHolder(context, null, parent);
                detailsBeanViewHolder.setOnClickCopyListent(new DetailsBeanViewHolder.OnClickCopyListent() {
                    @Override
                    public void onClickCopy(LoveHealDetDetailsBean detailsBean) {
                        onClickCopyListent.onClickCopy(detailsBean);
                    }
                });
                return detailsBeanViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public interface OnClickCopyListent {
        void onClickCopy(LoveHealDetDetailsBean detailsBean);
    }

    public void setOnClickCopyListent(OnClickCopyListent onClickCopyListent) {
        this.onClickCopyListent = onClickCopyListent;
    }
}