package com.yc.love.adaper.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemLocalityViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.model.bean.MainT3Bean;

import java.util.List;

/**
 * Created by mayn on 2019/4/30.
 */

public abstract class CreateMainT3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MainT3Bean> mDatas;

    private final int LOVE_HEAL_TYPE_TITLE = 1;
    private final int LOVE_HEAL_TYPE_ITEM_TITLE = 2;
    private final int LOVE_HEAL_TYPE_ITEM = 3;
    private final int LOVE_HEAL_TYPE_ITEM_LOCALITY = 4;

    public CreateMainT3Adapter(Context context, List<MainT3Bean> mDatas) {
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
                MainT3Bean mainT3Bean = mDatas.get(position);
                int spansize = 1;
                switch (mainT3Bean.type) {
                    case LOVE_HEAL_TYPE_TITLE:
                        spansize = 2;  //占据2列
                        break;
                    case LOVE_HEAL_TYPE_ITEM_TITLE:
                        spansize = 2;  //占据2列
                        break;
                }
                return spansize;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case LOVE_HEAL_TYPE_TITLE:
                holder = getTitleHolder(viewGroup);
                break;
            case LOVE_HEAL_TYPE_ITEM:
                holder = getHolder(viewGroup);
                break;
            case LOVE_HEAL_TYPE_ITEM_LOCALITY:
                holder = getLocalityHolder(viewGroup);
                break;
            case LOVE_HEAL_TYPE_ITEM_TITLE:
                holder = getItemTitleHolder(viewGroup);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainT3ItemTitleViewHolder) {
            ((MainT3ItemTitleViewHolder) holder).bindData(mDatas.get(position));
        } else if (holder instanceof MainT3TitleViewHolder) {
            ((MainT3TitleViewHolder) holder).bindData(mDatas.get(position));
        } else if (holder instanceof MainT3ItemLocalityViewHolder) {
            ((MainT3ItemLocalityViewHolder) holder).bindData(mDatas.get(position));
        } else {
            ((MainT3ItemViewHolder) holder).bindData(mDatas.get(position));
        }
    }

    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        MainT3Bean mainT3Bean = mDatas.get(position);
        int type = LOVE_HEAL_TYPE_ITEM;
        switch (mainT3Bean.type) {
            case LOVE_HEAL_TYPE_TITLE:
                type = LOVE_HEAL_TYPE_TITLE;
                break;
            case LOVE_HEAL_TYPE_ITEM:
                type = LOVE_HEAL_TYPE_ITEM;
                break;
            case LOVE_HEAL_TYPE_ITEM_LOCALITY:
                type = LOVE_HEAL_TYPE_ITEM_LOCALITY;
                break;
            case LOVE_HEAL_TYPE_ITEM_TITLE:
                type = LOVE_HEAL_TYPE_ITEM_TITLE;
                break;
        }
        return type;
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    public abstract BaseViewHolder getHolder(ViewGroup parent);

    protected abstract RecyclerView.ViewHolder getLocalityHolder(ViewGroup viewGroup);

    public abstract BaseViewHolder getTitleHolder(ViewGroup parent);

    protected abstract BaseViewHolder getItemTitleHolder(ViewGroup viewGroup);
}
