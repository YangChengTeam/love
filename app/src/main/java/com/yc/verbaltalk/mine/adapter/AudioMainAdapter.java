package com.yc.verbaltalk.mine.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.bean.MusicInfo;
import com.yc.verbaltalk.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * Created by wanglin  on 2019/7/20 10:08.
 */
public class AudioMainAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> {


    public AudioMainAdapter(@Nullable List<MusicInfo> data) {
        super(R.layout.audio_main_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicInfo item) {
        if (item != null) {
            helper.setText(R.id.tv_audio_title, item.getTitle())
                    .setText(R.id.tv_audio_desc, item.getDesp())
                    .setText(R.id.tv_audio_play_num, "播放量" + item.getPlay_num());

            SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

            try {
                helper.setText(R.id.tv_audio_time, sd.format(new Date(item.getAdd_time() * 1000)) + "上新");
            } catch (Exception e) {
                e.printStackTrace();
            }


            ImageView ivCover = helper.getView(R.id.iv_audio_cover);

//            //设置图片圆角角度
//            RoundedCorners roundedCorners = new RoundedCorners(15);
//
//            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
//            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

            Glide.with(mContext).asBitmap().load(item.getImg()).apply(new RequestOptions().error(R.mipmap.audio_cover)
                    .placeholder(R.mipmap.audio_holder).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false))
                    .thumbnail(0.1f).into(ivCover);
        }
    }
}
