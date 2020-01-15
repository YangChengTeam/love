package com.yc.verbaltalk.adaper.rv;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.CommunityInfo;
import com.yc.verbaltalk.utils.DateUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by suns  on 2019/8/30 15:00.
 */
public class CommunityHotAdapter extends BaseMultiItemQuickAdapter<CommunityInfo, BaseViewHolder> {


    private boolean mIsEnd;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    public CommunityHotAdapter(List<CommunityInfo> data) {
        super(data);

        addItemType(CommunityInfo.ITEM_TOP_ACTIVITY, R.layout.item_community_top_image);
        addItemType(CommunityInfo.ITEM_TAG, R.layout.item_community_tag);
        addItemType(CommunityInfo.ITEM_DIVIDER, R.layout.item_community_divider);
        addItemType(CommunityInfo.ITEM_CONTENT, R.layout.item_community_hot_view);

    }

    public void isEnd(boolean isEnd) {
        this.mIsEnd = isEnd;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityInfo item) {
        switch (item.itemType) {
            case CommunityInfo.ITEM_TOP_ACTIVITY:
                ImageView ivHot = helper.getView(R.id.iv_hot_activity);

                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(30);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mContext).load(item.pic).apply(options.error(R.mipmap.community_hot_top_bg)
                        .placeholder(R.mipmap.community_hot_top_bg)).into(ivHot);
                helper.addOnClickListener(R.id.iv_hot_activity);
                break;

            case CommunityInfo.ITEM_TAG:
                int position = helper.getAdapterPosition();
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
                if (position % 3 == 1) {
                    layoutParams.leftMargin = UIUtil.dip2px(mContext, 15);
                    layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
                } else if (position % 3 == 2) {
                    layoutParams.rightMargin = UIUtil.dip2px(mContext, 5);
                    layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
                } else {
                    layoutParams.leftMargin = UIUtil.dip2px(mContext, 5);
                    layoutParams.rightMargin = UIUtil.dip2px(mContext, 15);
                }
                helper.itemView.setLayoutParams(layoutParams);


                helper.setText(R.id.tv_content, "#".concat(item.tagInfo.getTitle()).concat("#"));

                break;
            case CommunityInfo.ITEM_CONTENT:

                helper.setText(R.id.tv_name, item.name)

                        .setText(R.id.tv_content, item.content)
                        .setText(R.id.tv_message_num, String.valueOf(item.comment_num))
                        .setText(R.id.tv_like_num, String.valueOf(item.like_num))
                        .addOnClickListener(R.id.ll_like).addOnClickListener(R.id.iv_like);
                try {
                    helper.setText(R.id.tv_date, DateUtils.formatTimeToStr(item.create_time));
                } catch (Exception e) {
                    Log.e(TAG, "convert: " + e.getMessage());
                }

                if (!TextUtils.isEmpty(item.tag)) {
                    helper.setText(R.id.tv_tag, "#".concat(item.tag).concat("#"));
                }
                helper.setImageResource(R.id.iv_like, item.is_dig == 0 ? R.mipmap.community_like : R.mipmap.community_like_selected);
                Glide.with(mContext).load(item.pic).apply(new RequestOptions()
                        .circleCrop().placeholder(R.mipmap.main_icon_default_head)
                        .error(R.mipmap.main_icon_default_head).diskCacheStrategy(DiskCacheStrategy.DATA))
                        .into((ImageView) helper.getView(R.id.iv_pic));

                CommunityInfo commentInfo = item.detail;
                if (null != commentInfo) {
                    helper.setGone(R.id.ll_comment, true);
                    String comment = "<font color='#000000'>" + item.detail.name + "：</font>" + item.detail.content;
                    helper.setText(R.id.tv_comment, Html.fromHtml(comment));
                } else {
                    helper.setGone(R.id.ll_comment, false);
                }
                int pos = helper.getAdapterPosition();


                if (mIsEnd && pos == mData.size() - 1) {
                    helper.setGone(R.id.view_divider, false);
                } else {
                    helper.setGone(R.id.view_divider, true);
                }

                break;
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                CommunityInfo communityInfo = getItem(position);
                if (communityInfo != null)
                    if (communityInfo.itemType == CommunityInfo.ITEM_TAG) {
                        return 1;
                    }
                return 3;


            }
        });

    }


}
