package com.yc.verbaltalk.index.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by suns  on 2020/9/9 10:45.
 */
public class VerbalVbsAdapter extends BaseQuickAdapter<LoveHealDetBean, BaseViewHolder> {
    public VerbalVbsAdapter(@Nullable List<LoveHealDetBean> data) {
        super(R.layout.verbal_vb_out_view, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LoveHealDetBean item) {
        List<LoveHealDetDetailsBean> details = item.details;
        RecyclerView recyclerView = helper.getView(R.id.item_love_heal_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int pos = helper.getAdapterPosition();
        if (pos == mData.size() - 1) {
            helper.setGone(R.id.verbal_divider, false);
        }

        if (details == null || details.isEmpty()) {
            details = item.detail;
        }

        VerbalVbItemAdapter verbalVbItemAdapter = new VerbalVbItemAdapter(details);

        recyclerView.setAdapter(verbalVbItemAdapter);


        verbalVbItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoveHealDetDetailsBean item1 = verbalVbItemAdapter.getItem(position);
            if (item1 != null) {
//                    item1.setTitle(mTitle)
                MobclickAgent.onEvent(mContext, "copy_dialogue_id", "复制恋爱话术");
                copyText(item1.content);
            }
        });

        helper.addOnClickListener(R.id.iv_praise)
                .addOnClickListener(R.id.iv_collect);

        verbalVbItemAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            LoveHealDetDetailsBean item2 = verbalVbItemAdapter.getItem(position);
            if (item2 != null) {
                if (view.getId() == R.id.iv_praise) {
                    view.findViewById(R.id.iv_praise).setSelected(true);
                } else if (view.getId() == R.id.iv_collect) {
                    view.findViewById(R.id.iv_collect).setSelected(true);
                }
            }
        });


    }


    private void copyText(String text) {
        ClipboardManager myClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        ToastUtils.showCenterToast("内容已复制", true);
    }
}
