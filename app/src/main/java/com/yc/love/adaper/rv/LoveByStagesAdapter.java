package com.yc.love.adaper.rv;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.bean.LoveByStagesBean;

import java.util.List;

/**
 * Created by wanglin  on 2019/6/29 11:32.
 */
public class LoveByStagesAdapter extends BaseQuickAdapter<LoveByStagesBean, BaseViewHolder> {
    public LoveByStagesAdapter(@Nullable List<LoveByStagesBean> data) {
        super(R.layout.recycler_view_item_love_by_stages, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoveByStagesBean item) {

        helper.setText(R.id.item_love_by_stages_tv_name, item.post_title)
                .setText(R.id.item_love_by_stages_tv_des, String.valueOf(item.feeluseful).concat("人觉得有用"));
    }
}
