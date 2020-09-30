package com.yc.verbaltalk.skill.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.video.player.lib.view.VideoRoundImageView;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.BlurDrawable;
import com.yc.verbaltalk.skill.model.bean.ChatCheatsInfo;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;


/**
 * Created by Administrator on 2017/9/12.
 */

public class ChatSkillItemAdapter extends BaseMultiItemQuickAdapter<ChatCheatsInfo, BaseViewHolder> {

    private Random random = new Random();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatSkillItemAdapter(List<ChatCheatsInfo> data) {
        super(data);
        addItemType(ChatCheatsInfo.VIEW_ITEM, R.layout.recycler_view_item_main_t2);
        addItemType(ChatCheatsInfo.VIEW_TITLE, R.layout.recycler_view_item_title_t2_view);
        addItemType(ChatCheatsInfo.VIEW_TO_PAY_VIP, R.layout.item_title_view_main_to_pay_vip);
        addItemType(ChatCheatsInfo.VIEW_VIP, R.layout.item_title_view_vip);
        addItemType(ChatCheatsInfo.VIEW_FEED_AD, R.layout.item_ad_native_express);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ChatCheatsInfo item) {
        if (item != null) {
            switch (item.type) {
                case ChatCheatsInfo.VIEW_ITEM:


                    helper.setText(R.id.item_main_t2_tv_name, item.post_title)
                            .setText(R.id.tv_cheats_count, random.nextInt(2000 - 1000 + 1) + 1000 + "人阅读");
                    VideoRoundImageView ivItemIcon = helper.getView(R.id.item_main_t2_iv_icon);
//                    ivItemIcon.setCorner(12);

//                    Glide.with(mContext).asBitmap().load(item.image).apply(new RequestOptions().error(R.mipmap.main_bg_t3_placeholder)).thumbnail(0.1f).into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                            Bitmap rsBlur = BlurDrawable.rsBlur(mContext, bitmap, 25);
//                            ivItemIcon.setImageBitmap(rsBlur);
//                        }
//
//                    });
                    if (mContext != null) {
                        if (mContext instanceof Activity) {
                            Activity activity = ((Activity) mContext);
                            if (!activity.isDestroyed())
                                Glide.with(mContext).load(item.image).apply(new RequestOptions().error(R.mipmap.main_bg_t3_placeholder)).thumbnail(0.1f).into(ivItemIcon);
                        }
                    }
                    break;
                case ChatCheatsInfo.VIEW_TITLE:
                    helper.setImageResource(R.id.roundCornerImg_banner, item.imgId);
                    helper.addOnClickListener(R.id.roundCornerImg_banner);

                    break;
                case ChatCheatsInfo.VIEW_TO_PAY_VIP:
                    helper.setText(R.id.item_main_to_pay_vip_tv_name, item.post_title)
                            .setText(R.id.tv_cheats_count, random.nextInt(2000 - 1000 + 1) + 1000 + "人阅读");
                    VideoRoundImageView ivItemPayIcon = helper.getView(R.id.item_main_to_pay_vip_iv_icon);


                    if (mContext != null) {
                        if (mContext instanceof Activity) {
                            Activity activity = ((Activity) mContext);
                            if (!activity.isDestroyed()) {
                                Glide.with(mContext).asBitmap().load(item.image).apply(new RequestOptions().error(R.mipmap.main_bg_t3_placeholder)).into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                        Bitmap rsBlur = BlurDrawable.rsBlur(mContext, bitmap, 10);
                                        ivItemPayIcon.setImageBitmap(rsBlur);
                                    }

                                });
                            }
                        }
                    }

                    break;

                case ChatCheatsInfo.VIEW_VIP:

                    break;
                case ChatCheatsInfo.VIEW_FEED_AD:
                    //绑定广告数据、设置交互回调
                    FrameLayout convertView = helper.getView(R.id.fl_item_express);
                    TTNativeExpressAd ad = item.expressAdView;

                    //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                    View video = ad.getExpressAdView();
                    if (video != null) {
                        if (video.getParent() == null) {
                            convertView.removeAllViews();
                            convertView.addView(video);
//                            ad.render();
                        }
                    }

                    break;

            }
        }
    }


}