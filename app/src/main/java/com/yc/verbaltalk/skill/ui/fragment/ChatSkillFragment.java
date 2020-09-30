package com.yc.verbaltalk.skill.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.FrameLayout;

import com.alibaba.fastjson.TypeReference;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.skill.adapter.ChatSkillItemAdapter;
import com.yc.verbaltalk.skill.model.bean.ChatCheatsInfo;
import com.yc.verbaltalk.skill.ui.activity.ConsultDetailActivity;
import com.yc.verbaltalk.skill.ui.activity.ExampleDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;


/**
 * Created by sunshey on 2019/6/18.
 * 聊天秘技
 */

//LoveCaseActivity
public class ChatSkillFragment extends BaseMainT2ChildFragment implements OnAdvStateListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private LoveEngine mLoveEngin;

    private List<ChatCheatsInfo> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private ChatSkillItemAdapter mAdapter;

    private boolean mUserIsVip = false;

    private LoadDialog mLoadingDialog;

    private FrameLayout bottomContainer;


    @Override
    protected int setContentView() {
        return R.layout.activity_practice_teach;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngine(mMainActivity);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mSwipeRefresh = rootView.findViewById(R.id.child_main_t2_t1_swipe_refresh);
        mRecyclerView = rootView.findViewById(R.id.lchild_main_t2_t1_rl);
        bottomContainer = rootView.findViewById(R.id.bottom_container);


        String brand = Build.BRAND.toLowerCase();
//        if (TextUtils.equals(brand, "huawei") || TextUtils.equals(brand, "honor") || UserInfoHelper.isVip()) {
//            bottomContainer.setVisibility(View.GONE);
//        } else {
//            bottomContainer.setVisibility(View.VISIBLE);
//            TTAdDispatchManager.getManager().init(mMainActivity, TTAdType.BANNER, bottomContainer, Constant.TOUTIAO_BANNER_ID, 0, null, 0, null, 0, this);
//        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChatSkillItemAdapter(mMainT2Beans);
        mRecyclerView.setAdapter(mAdapter);

        initListener();

    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(() -> {
            PAGE_NUM = 1;
            netData(true);
        });
        mAdapter.setOnLoadMoreListener(() -> netData(false), mRecyclerView);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatCheatsInfo mainT2Bean = mAdapter.getItem(position);
            if (mainT2Bean != null) {
                if (ChatCheatsInfo.VIEW_ITEM == mainT2Bean.type) {
                    ExampleDetailActivity.startExampleDetailActivity(mMainActivity, mainT2Bean.id, mainT2Bean.post_title);
                } else if (ChatCheatsInfo.VIEW_TO_PAY_VIP == mainT2Bean.type || ChatCheatsInfo.VIEW_VIP == mainT2Bean.type) {
                    if (UserInfoHelper.isLogin(mMainActivity)) {
                        startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                        MobclickAgent.onEvent(mMainActivity, "practice_id", "实战学习");
                    }
                }
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ChatCheatsInfo item = mAdapter.getItem(position);
            if (item != null) {
                if (item.type == ChatCheatsInfo.VIEW_TITLE && view.getId() == R.id.roundCornerImg_banner) {
                    startActivity(new Intent(mMainActivity, ConsultDetailActivity.class));
                }
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(EventLoginState event) {
        switch (event.state) {
            case EventLoginState.STATE_LOGINED:
            case EventLoginState.STATE_EXIT:
                PAGE_NUM = 1;
                netData(false);
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void lazyLoad() {
        initData(false);
    }


    private void initData(boolean isRefresh) {
        CommonInfoHelper.getO(mMainActivity, "main2_example_lists", new TypeReference<List<ChatCheatsInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<ChatCheatsInfo>>) o -> {
            mMainT2Beans = o;
            if (PAGE_NUM == 1 && mMainT2Beans != null && mMainT2Beans.size() > 0) {
                mAdapter.setNewData(mMainT2Beans);
            }
        });
        netData(isRefresh);
    }

    private void netData(final boolean isRefresh) {

        if (PAGE_NUM == 1 && !isRefresh) {
            mLoadingDialog = new LoadDialog(mMainActivity);
            mLoadingDialog.showLoadingDialog();
        }

        mLoveEngin.exampLists(UserInfoHelper.getUid(), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<ExampDataBean>>() {
                    @Override
                    public void onComplete() {
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(ResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (PAGE_NUM == 1) {
                            boolean isVip = UserInfoHelper.isVip();
                            if (!isVip) {
                                TTAdDispatchManager.getManager().init(mMainActivity, TTAdType.NATIVE_EXPRESS, null, Constant.TOUTIAO_FEED_ID, 3, null, 0, "", 0, ChatSkillFragment.this);
                            }
                        }
                        if (exampDataBeanAResultInfo != null && exampDataBeanAResultInfo.code == HttpConfig.STATUS_OK && exampDataBeanAResultInfo.data != null) {
                            ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                            createNewData(exampDataBean);
                        }


                    }


                });
    }

    private void createNewData(ExampDataBean exampDataBean) {
        int isVip = exampDataBean.is_vip;
        if (isVip > 0) {
            mUserIsVip = true;
        }
        List<ExampListsBean> exampListsBeans = exampDataBean.lists;


        mMainT2Beans = new ArrayList<>();
        if (PAGE_NUM == 1) {
            ChatCheatsInfo mainT2Bean = new ChatCheatsInfo("tit", ChatCheatsInfo.VIEW_TITLE);
            mainT2Bean.imgId = R.mipmap.consult_banner;
            mMainT2Beans.add(mainT2Bean);
        }


        if (exampListsBeans != null && exampListsBeans.size() > 0) {

            for (int i = 0; i < exampListsBeans.size(); i++) {
                ExampListsBean exampListsBean = exampListsBeans.get(i);
                int type;
                if (PAGE_NUM == 1 && i < 3) {
                    type = ChatCheatsInfo.VIEW_ITEM;
//                    mMainT2Beans.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
                } else {
                    type = exampDataBean.is_vip > 0 ? ChatCheatsInfo.VIEW_ITEM : ChatCheatsInfo.VIEW_TO_PAY_VIP;
                }


                mMainT2Beans.add(new ChatCheatsInfo(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
            }

//            for (ExampListsBean exampListsBean : exampListsBeans) {
//                int type = MainT2Bean.VIEW_ITEM;
//                if (PAGE_NUM > 1) {
//                    type = exampDataBean.is_vip > 0 ? MainT2Bean.VIEW_ITEM : MainT2Bean.VIEW_TO_PAY_VIP;
//                }
//                mMainT2Beans.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
//            }

        }
        if (!mUserIsVip && mMainT2Beans != null && mMainT2Beans.size() > 6 && PAGE_NUM == 1) {
            mMainT2Beans.add(6, new ChatCheatsInfo("vip", ChatCheatsInfo.VIEW_VIP));
        }


        if (PAGE_NUM == 1) {
            mAdapter.setNewData(mMainT2Beans);
            CommonInfoHelper.setO(mMainActivity, mMainT2Beans, "main2_example_lists");
        } else {
            mAdapter.addData(mMainT2Beans);
        }
        if (exampListsBeans != null && exampListsBeans.size() == PAGE_SIZE) {
            mAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            mAdapter.loadMoreEnd();
        }
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {

    }


    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

        if (ads != null && ads.size() > 0) {

            if (mMainT2Beans == null) {
                mMainT2Beans = new ArrayList<>();
            }
            int count = mMainT2Beans.size();
            for (TTNativeExpressAd ad : ads) {
                int random = (int) (Math.random() * PAGE_SIZE) + count - PAGE_SIZE;
                ChatCheatsInfo chatCheatsInfo = new ChatCheatsInfo();
                chatCheatsInfo.expressAdView = ad;
                chatCheatsInfo.type = ChatCheatsInfo.VIEW_FEED_AD;
//                if (random > count) {
//                    random = (int) (Math.random() * PAGE_SIZE) + count - PAGE_SIZE;
//                }
                mMainT2Beans.set(random, chatCheatsInfo);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onDrawFeedAd(List<TTFeedAd> feedAdList) {

    }

    @Override
    public void removeNativeAd(TTNativeExpressAd ad, int position) {

        List<ChatCheatsInfo> data = mAdapter.getData();
        if (data.size() > 0) {
            int removePos = 0;
            for (int i = 0; i < data.size(); i++) {
                ChatCheatsInfo chatCheatsInfo = data.get(i);
                if (chatCheatsInfo.type != ChatCheatsInfo.VIEW_FEED_AD) {
                    continue;
                }
                if (chatCheatsInfo.expressAdView == ad) {
                    removePos = i;
                    break;
                }

            }
            data.remove(removePos);

            mAdapter.notifyItemRemoved(removePos);
        }
    }


    @Override
    public void rewardComplete() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTAdDispatchManager.getManager().onDestroy();
    }


}
