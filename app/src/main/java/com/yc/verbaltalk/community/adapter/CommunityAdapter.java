package com.yc.verbaltalk.community.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.base.utils.DateUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by suns  on 2019/8/28 15:44.
 */
public class CommunityAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {


    public CommunityAdapter(@Nullable List<CommunityInfo> data) {
        super(R.layout.item_community_view, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityInfo item) {
//        SimpleDateFormat sm = new SimpleDateFormat("MM月dd日", Locale.getDefault());

        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_content, item.content)
                .setText(R.id.tv_message_num, String.valueOf(item.comment_num))
                .setText(R.id.tv_like_num, String.valueOf(item.like_num))
                .addOnClickListener(R.id.iv_like)
                .addOnClickListener(R.id.ll_like);

        if (!TextUtils.isEmpty(item.tag)) {
            helper.setText(R.id.tv_tag, "#".concat(item.tag).concat("#"));
        }
//        Log.e(TAG, "convert: " + helper.getAdapterPosition() + " --" + item.is_dig);
        helper.setImageResource(R.id.iv_like, item.is_dig == 0 ? R.mipmap.community_like : R.mipmap.community_like_selected);

        Glide.with(mContext).load(item.pic).apply(new RequestOptions()
                .circleCrop().placeholder(R.mipmap.main_icon_default_head)
                .error(R.mipmap.main_icon_default_head).diskCacheStrategy(DiskCacheStrategy.DATA))
                .into((ImageView) helper.getView(R.id.iv_avator));
        try {
            helper.setText(R.id.tv_date, DateUtils.formatTimeToStr(item.create_time));
        } catch (Exception e) {
            Log.e(TAG, "convert: " + e.getMessage());
        }

        CommunityInfo commentInfo = item.detail;

        if (null != commentInfo) {
            helper.setGone(R.id.ll_comment, true);
            String comment = "<font color='#000000'>" + item.detail.name + "：</font>" + item.detail.content;
            helper.setText(R.id.tv_comment, Html.fromHtml(comment));
        } else {
            helper.setGone(R.id.ll_comment, false);
        }


    }
}
