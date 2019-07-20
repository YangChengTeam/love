package com.yc.love.adaper.rv;

import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.love.R;
import com.yc.love.model.AudioItemInfo;
import com.yc.love.utils.DateUtils;

import java.util.List;

/**
 * Created by admin on 2018/1/25.
 */

public class AudioListAdapter extends BaseQuickAdapter<AudioItemInfo, BaseViewHolder> {

    public AudioListAdapter(List<AudioItemInfo> datas) {
        super(R.layout.audio_list_item_content, datas);
    }

    @Override
    protected void convert(final BaseViewHolder helper, AudioItemInfo item) {
        if (item != null) {
            helper.setText(R.id.tv_spa_level_two, item.getTitle())
                    .setText(R.id.tv_spa_number, (helper.getAdapterPosition() + 2) + "")
                    .setText(R.id.tv_spa_sing_user, item.getAuthor_title())
                    .setText(R.id.tv_spa_listen_count, TextUtils.isEmpty(item.getPlay_num()) ? "0" : item.getPlay_num() + "")
                    .setText(R.id.tv_spa_sing_time, item.getTime());

        }
    }
}
