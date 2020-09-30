package com.yc.verbaltalk.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.music.player.lib.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseActivity;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetBeanWrapper;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.index.adapter.SmartChatVerbalAdapter;
import com.yc.verbaltalk.index.bean.AIItem;
import com.yc.verbaltalk.index.bean.SmartChatItem;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2020/9/9 09:57.
 */
public class SmartChatVerbalActivity extends BaseActivity {

    private String kewword;
    private SmartChatVerbalAdapter smartChatVerbalAdapter;

    private int section = 0;

    private Map<String, SmartChatItem> contentTotalMap = new HashMap<>();
    private RecyclerView recyclerview_smart_chat;
    private ImageView iv_ai_back;
    private ImageView iv_fly;
    private EditText et_content;

    public static void startActivity(Context context, String kewword) {
        Intent intent = new Intent(context, SmartChatVerbalActivity.class);
        intent.putExtra("keyword", kewword);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_smart_chat);
        invadeStatusBar();
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
        initViews();
        initListener();
        MobclickAgent.onEvent(this, "smart_ai_verbal", "智能AI话术");

    }

    private void initViews() {
        if (getIntent() != null) {
            kewword = getIntent().getStringExtra("keyword");
        }


        recyclerview_smart_chat = findViewById(R.id.recyclerview_smart_chat);

        iv_ai_back = findViewById(R.id.iv_ai_back);
        iv_fly = findViewById(R.id.iv_fly);
        et_content = findViewById(R.id.et_content);

        recyclerview_smart_chat.setLayoutManager(new LinearLayoutManager(this));

        smartChatVerbalAdapter = new SmartChatVerbalAdapter(null);
        recyclerview_smart_chat.setAdapter(smartChatVerbalAdapter);
        if (!TextUtils.isEmpty(kewword)) {
            SmartChatItem aiItem = new SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, kewword);
            createNewData(aiItem, SmartChatItem.CHAT_ITEM_SELF, false);
            getData(false);
        }
    }

    private void initListener() {
        iv_ai_back.setOnClickListener(v -> finish());
        smartChatVerbalAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SmartChatItem item = smartChatVerbalAdapter.getItem(position);
            if (item != null) {
                if (item.getItemType() == SmartChatItem.CHAT_ITEM_VERBAL) {
                    if (view.getId() == R.id.ll_ai_change) {
                        refreshAni(view.findViewById(R.id.iv_ai_change));
                        section = 1;
                        getData(true);

                    } else if (view.getId() == R.id.ll_verbal_change) {
                        refreshAni(view.findViewById(R.id.iv_verbal_refresh));
                        section = 2;
                        getData(true);

                    }
                }
            }
        });

        smartChatVerbalAdapter.setOnAiItemClickListener(
                new SmartChatVerbalAdapter.OnAiItemClickListener() {
                    @Override
                    public void onPraise(AIItem aiItem) {
                        aiPraise(aiItem.getId());
                    }

                    @Override
                    public void onCollect(AIItem aiItem) {
                        aiCollect(aiItem.getId());
//                                                                (mPresenter as AIChatPresenter).aiCollect(aiItem ?.id)
                    }
                });

        iv_fly.setOnClickListener(v -> {
            String content = et_content.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showCenterToast("发送内容不能为空");
                return;
            }
            MobclickAgent.onEvent(SmartChatVerbalActivity.this, "smart_ai_search", "智能AI话术搜索");
            kewword = content;
            et_content.setText("");
            createNewData(new SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, content), SmartChatItem.CHAT_ITEM_SELF, false);

            section = 0;
            getData(false);
        });
    }

    private void refreshAni(ImageView iv) {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.ani_rotate);
        iv.startAnimation(rotate);

    }

    private void createNewData(SmartChatItem data, int type, Boolean refresh) {
        List<SmartChatItem> contentList = new ArrayList<SmartChatItem>();


        if (data != null) {

            if (section == 0) {
                contentTotalMap.put(kewword, data);
            } else {
                List<AIItem> aiItems = data.getAiItems();

                SmartChatItem item = contentTotalMap.get(kewword);
                if (aiItems == null || aiItems.size() == 0) {
                    if (item != null && item.getAiItems() != null)
                        data.setAiItems(item.getAiItems());
                }
                LoveHealDetBeanWrapper dialogue = data.getDialogue();
                if (dialogue != null) {
                    List<LoveHealDetBean> loveHealDetBeans = dialogue.getList();
                    if (loveHealDetBeans == null || loveHealDetBeans.size() == 0) {
                        if (item != null && item.getDialogue().getList() != null)
                            dialogue.setList(item.getDialogue().getList());
                    }
                }

            }
            if (refresh) {
                if (section == 0) {
                    contentList.addAll(smartChatVerbalAdapter.getData());

                } else {
                    contentList.add(new SmartChatItem(SmartChatItem.CHAT_ITEM_SELF, kewword));
                }
                data.setType(type);
                contentList.add(data);
                smartChatVerbalAdapter.setNewData(contentList);
            } else {

                data.setType(type);
                contentList.add(data);
                smartChatVerbalAdapter.addData(contentList);
            }

            recyclerview_smart_chat.scrollToPosition(smartChatVerbalAdapter.getItemCount() - 1);

        }


    }


    private void getData(boolean isRefresh) {
        mLoveEngine.smartSearchVerbal(kewword, section).subscribe(new DisposableObserver<ResultInfo<SmartChatItem>>() {
            @Override
            public void onNext(ResultInfo<SmartChatItem> smartChatItemResultInfo) {
                if (smartChatItemResultInfo != null) {
                    if (smartChatItemResultInfo.code == HttpConfig.STATUS_OK && smartChatItemResultInfo.data != null) {
                        createNewData(smartChatItemResultInfo.data, isRefresh);
                    } else {
                        if (smartChatItemResultInfo.code == 2) {
                            showUseCountUp(smartChatItemResultInfo.message);
                        }
                        ToastUtils.showCenterToast(smartChatItemResultInfo.message);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: "+e.getMessage() );

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void aiPraise(String id) {
        mLoveEngine.aiPraise(id).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void aiCollect(String id) {
        mLoveEngine.aiCollect(id).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void showUseCountUp(String message) {
        if (UserInfoHelper.isLogin(this)) {
            Intent intent = new Intent(this, BecomeVipActivity.class);
            startActivity(intent);
        }
    }

    private void createNewData(SmartChatItem data, Boolean refresh) {
        if (data != null) {
            if (data.getDialogue() != null) {
                List<LoveHealDetBean> loveHealDetBeans = data.getDialogue().getList();
                if (loveHealDetBeans != null && loveHealDetBeans.size() > 0) {
                    for (LoveHealDetBean loveHealDetBean : loveHealDetBeans) {
                        List<LoveHealDetDetailsBean> details = loveHealDetBean.details;
                        if (details == null || details.isEmpty()) {
                            details = loveHealDetBean.detail;
                        }
                        LoveHealDetDetailsBean loveHealDetDetailsBean = new LoveHealDetDetailsBean();

                        loveHealDetDetailsBean.content = loveHealDetBean.chat_name;
                        loveHealDetDetailsBean.ans_sex = loveHealDetBean.quiz_sex;
                        loveHealDetDetailsBean.dialogue_id = loveHealDetBean.id;
                        if (details != null)
                            details.add(0, loveHealDetDetailsBean);
                    }
                }
            }

            createNewData(data, SmartChatItem.CHAT_ITEM_VERBAL, refresh);
        }
    }
}
