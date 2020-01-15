package com.yc.verbaltalk.adaper.rv;

import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.bean.MusicInfo;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.ChatInfo;
import com.yc.verbaltalk.ui.view.RoundCornerImg;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by suns  on 2019/11/18 08:38.
 */
public class ChatMainAdapter extends BaseMultiItemQuickAdapter<ChatInfo, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatMainAdapter(List<ChatInfo> data) {
        super(data);
        addItemType(ChatInfo.ITEM_TYPE_RECOMMEND, R.layout.layout_chat_item_recommend);
        addItemType(ChatInfo.ITEM_TYPE_COURSE, R.layout.layout_chat_item_course);
        addItemType(ChatInfo.ITEM_TYPE_DIVIDER, R.layout.layout_chat_item_divider);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatInfo item) {
        switch (item.type) {
            case ChatInfo.ITEM_TYPE_RECOMMEND:

                helper.setGone(R.id.rl_recommend_category, item.isShowCateTitle);

                helper.setGone(R.id.tv_recommend_divider, item.isShowDivider).addOnClickListener(R.id.tv_more_audio);
                MusicInfo musicInfo = item.musicInfo;
                try {
                    helper.setText(R.id.tv_recommend_title, musicInfo.getTitle())
                            .setText(R.id.tv_recommend_play_count, "播放次数：" + musicInfo.getPlay_num());
                    Glide.with(mContext).load(musicInfo.getImg())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).error(R.mipmap.tease_course_small))
                            .into((RoundCornerImg) helper.getView(R.id.roundCornerImg_recommend));
                } catch (Exception e) {

                }


                break;

            case ChatInfo.ITEM_TYPE_COURSE:

                if (item.isShowCateTitle) {
                    helper.setGone(R.id.tv_course_category, true);
                } else {
                    helper.setGone(R.id.tv_course_category, false);
                    if (item.isShowCourseCategory) {
                        helper.setVisible(R.id.tv_course_category, false);
                    }

                }

                RoundCornerImg roundCornerImg = helper.getView(R.id.iv_course_pic);
                try {

                    Glide.with(mContext).load(item.courseInfo.getImg())
                            .apply(new RequestOptions().error(R.mipmap.tease_course_small).diskCacheStrategy(DiskCacheStrategy.DATA)).thumbnail(0.1f)
                            .into(roundCornerImg);
                } catch (Exception e) {

                }

                setItemParams(helper);

                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                ChatInfo chatInfo = getItem(i);
                if (chatInfo != null) {
                    if (ChatInfo.ITEM_TYPE_COURSE == chatInfo.type) {
                        return 1;
                    }
                }
                return 2;
            }
        });

    }

    private void setItemParams(BaseViewHolder helper) {
        int position = helper.getAdapterPosition();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        if (position % 2 == 0) {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 15);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 7.5f);
        } else {
            layoutParams.leftMargin = UIUtil.dip2px(mContext, 7.5f);
            layoutParams.rightMargin = UIUtil.dip2px(mContext, 15);

        }

        helper.itemView.setLayoutParams(layoutParams);

    }
}
