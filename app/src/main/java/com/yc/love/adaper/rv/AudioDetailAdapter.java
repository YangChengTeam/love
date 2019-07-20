package com.yc.love.adaper.rv;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.DateUtil;
import com.music.player.lib.util.Logger;
import com.yc.love.R;


import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by admin on 2018/1/26.
 */

public class AudioDetailAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> implements Observer {


    public AudioDetailAdapter(@Nullable List<MusicInfo> datas) {
        super(R.layout.item_audio_music_list, datas);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicInfo musicInfo) {
        if (null != musicInfo) {
            String seconds = musicInfo.getTime();
            if (TextUtils.isEmpty(seconds)) {
                seconds = "00:01";
            }
            TextView tv_item_num = helper.itemView.findViewById(R.id.tv_item_num);
            ImageView ic_play_anim = helper.itemView.findViewById(R.id.ic_play_anim);

//            float second = Float.parseFloat(seconds);


            helper.setText(R.id.tv_item_name, musicInfo.getTitle()).setText(R.id.tv_head_sing_user,
                    TextUtils.isEmpty(musicInfo.getAuthor_title()) ? "西门" : musicInfo.getAuthor_title())
                    .setText(R.id.tv_head_sing_time, seconds)
                    .setText(R.id.tv_head_listen_count, !TextUtils.isEmpty(musicInfo.getPlay_num()) ? musicInfo.getPlay_num() + "" : "0")
                    .setText(R.id.tv_item_num, (helper.getAdapterPosition()) + "");


            switch (musicInfo.getPlauStatus()) {
                //缓冲中
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:
                    tv_item_num.setVisibility(View.VISIBLE);
                    ic_play_anim.setVisibility(View.GONE);
                    helper.setTextColor(R.id.tv_item_name, ContextCompat.getColor(mContext, R.color.red_crimson));
                    break;
                //播放中
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    tv_item_num.setVisibility(View.GONE);
                    ic_play_anim.setVisibility(View.VISIBLE);
                    ic_play_anim.setImageResource(R.drawable.play_anim);

                    AnimationDrawable drawable = (AnimationDrawable) ic_play_anim.getDrawable();
                    if (null != drawable) {
                        if (drawable.isRunning()) {
                            drawable.stop();
                        }
                        drawable.start();
                    }
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_play_pause);
                    helper.setTextColor(R.id.tv_item_name, ContextCompat.getColor(mContext, R.color.red_crimson));
                    break;
                //暂停中
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    ic_play_anim.setVisibility(View.VISIBLE);
                    tv_item_num.setVisibility(View.GONE);
                    ic_play_anim.setImageResource(R.drawable.play_anim);
                    helper.setTextColor(R.id.tv_item_name, ContextCompat.getColor(mContext, R.color.red_crimson));
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_item_play_true);
                    AnimationDrawable drawableAnimation = (AnimationDrawable) ic_play_anim.getDrawable();
                    if (null != drawableAnimation) {
                        drawableAnimation.stop();
                    }
                    break;
                //播放完成或已停止
                case PlayerStatus.PLAYER_STATUS_STOP:
                    //播放失败
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    //为空
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    ic_play_anim.setImageResource(0);
                    ic_play_anim.setVisibility(View.GONE);
                    tv_item_num.setVisibility(View.VISIBLE);
                    helper.setImageResource(R.id.ic_item_play, R.drawable.ic_item_play_true);
                    helper.setTextColor(R.id.tv_item_name, ContextCompat.getColor(mContext, R.color.coment_color));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 刷新列表状态
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (null != arg) {
            MusicInfo musicInfo = (MusicInfo) arg;
            if (null != musicInfo && null != mData && mData.size() > 0) {
                int position = 0;
                for (int i = 0; i < mData.size(); i++) {
                    MusicInfo info = mData.get(i);
                    if (musicInfo.getId().equals(info.getId())) {
                        info.setPlauStatus(musicInfo.getPlauStatus());

                    } else {
                        info.setPlauStatus(0);
                    }
                }
                Logger.d(TAG, "播放器示例界面列表收到观察者刷新,类型：" + musicInfo.getPlauStatus() + "，位置：" + position + ",musicID：" + musicInfo.getId());
//                notifyItemChanged(position);
                notifyDataSetChanged();
            }
        }
    }
}