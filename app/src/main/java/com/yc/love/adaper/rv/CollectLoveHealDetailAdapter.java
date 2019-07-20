package com.yc.love.adaper.rv;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.bean.LoveHealDetDetailsBean;

import java.util.List;

/**
 * Created by wanglin  on 2019/7/8 14:43.
 */
public class CollectLoveHealDetailAdapter extends BaseQuickAdapter<LoveHealDetDetailsBean, BaseViewHolder> {

    public CollectLoveHealDetailAdapter(List<LoveHealDetDetailsBean> data) {
        super(R.layout.recycler_view_item_love_healing, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoveHealDetDetailsBean item) {
        helper.setText(R.id.item_love_healing_tv_name, item.content)
                .setText(R.id.tv_tag, item.title).setVisible(R.id.tv_tag, true);
        TextView tv = helper.getView(R.id.item_love_healing_tv_name);
        tv.setCompoundDrawables(null, null, null, null);

    }
}
