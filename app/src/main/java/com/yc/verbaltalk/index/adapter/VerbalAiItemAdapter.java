package com.yc.verbaltalk.index.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.index.bean.AIItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by suns  on 2020/9/9 10:22.
 */
public class VerbalAiItemAdapter extends BaseQuickAdapter<AIItem, BaseViewHolder> {
    public VerbalAiItemAdapter(@Nullable List<AIItem> data) {
        super(R.layout.verbal_ai_item_view, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AIItem item) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_ai_verbal, (position + 1) + "、" + item.getContent())
                .addOnClickListener(R.id.iv_praise)
                .addOnClickListener(R.id.iv_collect);

        ImageView ivPraise = helper.getView(R.id.iv_praise);
        ivPraise.setSelected(item.is_favour() == 1);
        helper.getView(R.id.iv_collect).setSelected(item.is_collect() == 1);
    }
}
