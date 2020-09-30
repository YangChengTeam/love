package com.yc.verbaltalk.index.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.LoveHealDetBeanWrapper;
import com.yc.verbaltalk.index.bean.AIItem;
import com.yc.verbaltalk.index.bean.SmartChatItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by suns  on 2020/9/9 10:07.
 */
public class SmartChatVerbalAdapter extends BaseMultiItemQuickAdapter<SmartChatItem, BaseViewHolder> {
    private String face;

    public SmartChatVerbalAdapter(@Nullable List<SmartChatItem> data) {
        super(data);
        addItemType(SmartChatItem.CHAT_ITEM_SELF, R.layout.chat_item_my_view);
        addItemType(SmartChatItem.CHAT_ITEM_VERBAL, R.layout.chat_item_verbal_view);
        if (UserInfoHelper.getUserInfo() != null)
            face = UserInfoHelper.getUserInfo().face;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SmartChatItem item) {
        switch (item.getItemType()) {
            case SmartChatItem.CHAT_ITEM_SELF:
                helper.setText(R.id.tv_chat_message, item.getContent());
                ImageView imageView = helper.getView(R.id.iv_avtor);
                Glide.with(mContext).load(face).apply(new RequestOptions().error(R.mipmap.main_icon_default_head).circleCrop()).into(imageView);
                break;
            case SmartChatItem.CHAT_ITEM_VERBAL:
                if (item.getAiItems() != null && item.getAiItems().size() > 0) {
                    RecyclerView recyclerViewAi = helper.getView(R.id.recyclerview_ai);
                    recyclerViewAi.setLayoutManager(new LinearLayoutManager(mContext));
                    VerbalAiItemAdapter verbalAiItemAdapter = new VerbalAiItemAdapter(item.getAiItems());
                    recyclerViewAi.setAdapter(verbalAiItemAdapter);
                    recyclerViewAi.setItemAnimator(new DefaultItemAnimator());
                    verbalAiItemAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                        AIItem aiItem = verbalAiItemAdapter.getItem(position);
                        if (view.getId() == R.id.iv_praise) {
                            if (aiItemClickListener != null) {
                                aiItemClickListener.onPraise(aiItem);
                            }
                            view.findViewById(R.id.iv_praise).setSelected(true);
                        } else if (view.getId() == R.id.iv_collect) {
                            if (aiItemClickListener != null) {
                                aiItemClickListener.onCollect(aiItem);
                            }
                            view.findViewById(R.id.iv_collect).setSelected(true);
                        }
                    });


                    verbalAiItemAdapter.setOnItemClickListener((adapter, view, position) -> {
                        AIItem aiItem = verbalAiItemAdapter.getItem(position);
                        if (aiItem != null)
                            copyText(aiItem.getContent());
                    });


                }
                LoveHealDetBeanWrapper dialogue = item.getDialogue();
                if (dialogue != null && dialogue.getList() != null && dialogue.getList().size() > 0) {
                    RecyclerView recyclerViewVerbal = helper.getView(R.id.recyclerview_verbal);
                    recyclerViewVerbal.setLayoutManager(new LinearLayoutManager(mContext));

                    VerbalVbsAdapter verbalVbsAdapter = new VerbalVbsAdapter(item.getDialogue().getList());
                    recyclerViewVerbal.setAdapter(verbalVbsAdapter);
                    verbalVbsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                        if (view.getId() == R.id.iv_praise) {
                            view.findViewById(R.id.iv_praise).setSelected(true);
                        } else if (view.getId() == R.id.iv_collect) {
                            view.findViewById(R.id.iv_collect).setSelected(true);
                        }
                    });

                }
                helper.addOnClickListener(R.id.ll_ai_change)
                        .addOnClickListener(R.id.ll_verbal_change);
                break;
        }
    }

    private OnAiItemClickListener aiItemClickListener;

    public void setOnAiItemClickListener(OnAiItemClickListener listener) {
        this.aiItemClickListener = listener;
    }

    public interface OnAiItemClickListener {
        void onPraise(AIItem aiItem);

        void onCollect(AIItem aiItem);

    }


    private void copyText(String text) {
        ClipboardManager myClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        ToastUtils.showCenterToast("内容已复制", true);
    }
}
