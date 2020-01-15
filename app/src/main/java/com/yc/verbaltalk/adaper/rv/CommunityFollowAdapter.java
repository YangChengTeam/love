package com.yc.verbaltalk.adaper.rv;

import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.CommunityInfo;
import com.yc.verbaltalk.utils.DateUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/8/28 15:44.
 * 评论跟帖
 */
public class CommunityFollowAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private SparseArray<TextView> titleArray;

    public CommunityFollowAdapter(@Nullable List<CommunityInfo> data) {
        super(R.layout.item_community_follow_view, data);
        titleArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityInfo item) {
        helper.setText(R.id.tv_name, item.name)

                .setText(R.id.tv_content, item.content)
                .setText(R.id.tv_like_num, String.valueOf(item.like_num))
                .addOnClickListener(R.id.ll_like)
                .addOnClickListener(R.id.iv_like);

        try {
          helper.setText(R.id.tv_date,  DateUtils.formatTimeToStr(item.create_time));
        } catch (Exception e) {
            Log.e(TAG, "convert: " + e.getMessage());
        }
        helper.setImageResource(R.id.iv_like,item.is_dig==0?R.mipmap.community_like:R.mipmap.community_like_selected);
        Glide.with(mContext).load(item.pic).apply(new RequestOptions().circleCrop()
                .placeholder(R.mipmap.main_icon_default_head).error(R.mipmap.main_icon_default_head))
                .into((ImageView) helper.getView(R.id.iv_pic));
        titleArray.put(helper.getAdapterPosition(), helper.getView(R.id.tv_like_num));

    }


    public TextView getView(int position) {
        return titleArray.get(position);
    }
}
