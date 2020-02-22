package com.yc.verbaltalk.community.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.CommunityTagInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by suns  on 2019/8/31 09:30.
 */
public class PublishTagAdapter extends BaseQuickAdapter<CommunityTagInfo, BaseViewHolder> {
    private SparseArray<View> itemSparseArray;
    private SparseArray<View> viewSparseArray;

    public PublishTagAdapter(@Nullable List<CommunityTagInfo> data) {
        super(R.layout.item_community_tag, data);
        itemSparseArray = new SparseArray<>();
        viewSparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityTagInfo item) {

        int position = helper.getAdapterPosition();
        setItemParams(helper, position);
        helper.itemView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.community_tag_selector));

        helper.setText(R.id.tv_content, "#".concat(item.getTitle()).concat("#"));
        if (position == 0) {
            helper.itemView.setSelected(true);
            helper.getView(R.id.tv_content).setSelected(true);
        }

        itemSparseArray.put(position, helper.itemView);
        viewSparseArray.put(position, helper.getView(R.id.tv_content));
    }

    private void setItemParams(BaseViewHolder helper, int position) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();

        if (position < 3) {
            layoutParams.topMargin = UIUtil.dip2px(mContext, 10);
        }

        if (position % 3 == 0) {
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
        } else if (position % 3 == 1) {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
        } else {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
        }
        helper.itemView.setLayoutParams(layoutParams);
    }

    public void resetView() {
        for (int i = 0; i < itemSparseArray.size(); i++) {
            itemSparseArray.get(i).setSelected(false);
            viewSparseArray.get(i).setSelected(false);
        }
    }

    public void setViewState(int position) {
        itemSparseArray.get(position).setSelected(true);
        viewSparseArray.get(position).setSelected(true);
    }

}
