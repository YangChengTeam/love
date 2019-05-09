package com.yc.love.adaper.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealItemViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealTitleViewHolder;
import com.yc.love.model.bean.LoveHealBean;

import java.util.List;

/**
 * Created by mayn on 2019/4/30.
 */

public abstract class CreateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LoveHealBean> mDatas;

    private final int LOVE_HEAL_TYPE_TITLE = 1;
    private final int LOVE_HEAL_TYPE_ITEM = 2;
    private final int LOVE_HEAL_TYPE_NO_ANY = 3;

    public CreateAdapter(Context context, List<LoveHealBean> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
//                    return 1;
                    LoveHealBean loveHealBean = mDatas.get(position);
                    int spansize = 1;
                    switch (loveHealBean.type) {
                        case LOVE_HEAL_TYPE_TITLE:
                            spansize = 3;  //占据3列
                            break;
                    }
                    return spansize;
                }
            });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == LOVE_HEAL_TYPE_TITLE) {
            holder = getTitleHolder(viewGroup);
        } else {
            holder = getHolder(viewGroup);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoveHealTitleViewHolder) {
            ((LoveHealTitleViewHolder) holder).bindData(mDatas.get(position));
        } else {
            ((LoveHealItemViewHolder) holder).bindData(mDatas.get(position));
        }
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        LoveHealBean loveHealBean = mDatas.get(position);
        switch (loveHealBean.type) {
            case LOVE_HEAL_TYPE_TITLE:
                return LOVE_HEAL_TYPE_TITLE;
            case LOVE_HEAL_TYPE_ITEM:
                return LOVE_HEAL_TYPE_ITEM;
        }
        return LOVE_HEAL_TYPE_NO_ANY;
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    public abstract BaseViewHolder getHolder(ViewGroup parent);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);
}
