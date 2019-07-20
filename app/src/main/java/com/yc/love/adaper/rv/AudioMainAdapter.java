package com.yc.love.adaper.rv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;
import com.yc.love.R;
import com.yc.love.model.AudioItemInfo;
import com.yc.love.model.bean.AudioDataInfo;
import com.yc.love.ui.activity.LoveAudioDetailActivity;
import com.yc.love.utils.CommonInfoHelper;
import com.yc.love.utils.DateUtils;

import java.util.List;

/**
 * Created by wanglin  on 2018/2/28 15:33.
 */

public class AudioMainAdapter extends BaseQuickAdapter<AudioDataInfo, BaseViewHolder> {


    private SparseArray<RecyclerView> sparseArray;


    public AudioMainAdapter(List<AudioDataInfo> data) {
        super(R.layout.audio_list_item_head, data);
        sparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AudioDataInfo item) {

        Glide.with(mContext).asDrawable().load(item.getImg()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                helper.getView(R.id.type_layout).setBackground(drawable);
            }
        });
        Glide.with(mContext).asBitmap().load(item.getPlayicon()).apply(new RequestOptions()
                .error(R.mipmap.audio_play)).into((ImageView) helper.getView(R.id.iv_head_play));

        List<AudioItemInfo> first = item.getFirst();
        if (first != null && first.size() > 0) {
            AudioItemInfo audioItemInfo = item.getFirst().get(0);

            helper.setText(R.id.tv_spa_level_one, audioItemInfo.getTitle()).
                    setText(R.id.tv_head_sing_user, audioItemInfo.getAuthor_title()).
                    setText(R.id.tv_head_listen_count, TextUtils.isEmpty(audioItemInfo.getPlay_num()) ? "0" : audioItemInfo.getPlay_num() + "")
                    .addOnClickListener(R.id.iv_head_play);
            if (!TextUtils.isEmpty(audioItemInfo.getTime())) {
                helper.setText(R.id.tv_head_sing_time, audioItemInfo.getTime());
            }
//            Log.e(TAG, "convert: " + item.getImg());

            RecyclerView recyclerView = helper.getView(R.id.recyclerView_list_detail);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


            final AudioListAdapter spaListAdapter = new AudioListAdapter(first.subList(1, first.size()));
            recyclerView.setAdapter(spaListAdapter);
            sparseArray.put(mData.indexOf(item), recyclerView);

            spaListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    AudioItemInfo spaItemInfo = spaListAdapter.getItem(position);

                    if (spaItemInfo != null) {
//                        AudioItemInfo audioItemInfo = item.getFirst().get(pos);
                        Intent intent = new Intent(mContext, LoveAudioDetailActivity.class);

                        intent.putExtra("spa_id", spaItemInfo.getId());
                        intent.putExtra("type_id", spaItemInfo.getId());
                        mContext.startActivity(intent);

                    }
                }
            });
        }
    }


    public RecyclerView getView(int position) {
        return sparseArray.get(position);

    }


    public void setVisable(boolean flag, final int position) {
        if (getView(position) != null)
            getView(position).setVisibility(flag ? View.VISIBLE : View.GONE);
    }


}
