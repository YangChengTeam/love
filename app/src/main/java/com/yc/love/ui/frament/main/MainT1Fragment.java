package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT1CreateAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.base.RecyclerViewTimeoutListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealItemViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT1CategoryViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.CategoryArticleChildrenBean;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.SearchDialogueBean;
import com.yc.love.model.bean.event.EventLoginState;
import com.yc.love.model.bean.event.NetWorkChangT1Bean;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.engin.LoveEnginV2;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveHealActivity;
import com.yc.love.ui.activity.LoveHealDetailsActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.activity.ShareActivity;
import com.yc.love.ui.activity.UsingHelpHomeActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {
    private LoveEngin mLoveEngin;
    //    private LoveEnginV2 LoveEnginV2;
    private CacheWorker mCacheWorker;
    ;
    private RecyclerView mRecyclerView;
    private LinearLayout mLlNotNet;
    private boolean mIsNetData = false;
    private boolean mIsNetTitleData = false;
    private boolean mIsCacheData = false;
    private boolean mIsCacheTitleData = false;
    private List<CategoryArticleBean> mCategoryArticleBeans;
    private LoadDialog mLoadDialog;
    private List<LoveHealBean> mDatas = new ArrayList<>();
    private LoadDialog mLoadingDialog;
    private LoadDialog mLoadDialogInfo;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t1;
    }


    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);
//        LoveEnginV2 = new LoveEnginV2(mMainActivity);
        mCacheWorker = new CacheWorker();
        mLlNotNet = rootView.findViewById(R.id.main_t1_not_net);

        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);



        /*LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    RecyclerViewTimeoutListener recyclerViewTimeoutListener = new RecyclerViewTimeoutListener() {
        @Override
        public void onItemClick(int position) {
            lazyLoad();
        }
    };


    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
            LoveHealBean loveHealBean = mDatas.get(position);
            Log.d("mylog", "onItemClick: " + loveHealBean.toString());
            LoveHealDetailsActivity.startLoveHealDetailsActivity(mMainActivity, loveHealBean.name, String.valueOf(loveHealBean.id));
        }

        @Override
        public void onItemLongClick(int position) {

        }

    };

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetWorkChangT1Bean netWorkChangBean) {  //无网状态
        List<String> connectionTypeList = netWorkChangBean.connectionTypeList;
        if (connectionTypeList == null || connectionTypeList.size() == 0) {
            if (mLlNotNet.getVisibility() != View.VISIBLE) {
                mLlNotNet.setVisibility(View.VISIBLE);
            }
        } else {
            if (mLlNotNet.getVisibility() != View.GONE) {
                mLlNotNet.setVisibility(View.GONE);
                lazyLoad();
              /*  if (!mIsNetData) {
                    netData();
                }*/
            }
        }
    }


    @Override
    protected void lazyLoad() {
        netUserReg();
        netOtherData();
    }

    private void netOtherData() {
        netDialogueData();
        netTitleData();
    }

    private void netUserReg() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        LoveEnginV2 loveEnginV2 = new LoveEnginV2(mMainActivity);
        loveEnginV2.userReg("user/reg").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {

                final IdCorrelationLoginBean data = idCorrelationLoginBeanAResultInfo.data;
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginSuccess(data);
                    }
                }, 500);

            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void loginSuccess(IdCorrelationLoginBean data) {
        //持久化存储登录信息
        String str = JSON.toJSONString(data);// java对象转为jsonString
        BackfillSingle.backfillLoginData(mMainActivity, str);

//        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
    }


    private void netDialogueData() {
        LoadDialog loadDialog = null;
        mDatas = (List<LoveHealBean>) mCacheWorker.getCache(mMainActivity, "main1_Dialogue_category");
        if (mDatas != null && mDatas.size() != 0) {
            mIsCacheData = true;
            if (mIsCacheData && mIsCacheTitleData) {
                initRecyclerViewData();
            }
        } else {
            loadDialog = new LoadDialog(mMainActivity);
            loadDialog.showLoadingDialog();
        }
        mLoveEngin.loveCategory("Dialogue/category").subscribe(new MySubscriber<AResultInfo<List<LoveHealDateBean>>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDateBean>> loveHealDateBeanAResultInfo) {
                List<LoveHealDateBean> loveHealDateBeans = loveHealDateBeanAResultInfo.data;
                if (loveHealDateBeans == null || loveHealDateBeans.size() == 0) {
                    return;
                }
                mDatas = new ArrayList<>();
                for (LoveHealDateBean loveHealDateBean : loveHealDateBeans) {
                    LoveHealBean loveHealBean = new LoveHealBean(1, loveHealDateBean._level, loveHealDateBean.id, loveHealDateBean.name, loveHealDateBean.parent_id);
                    mDatas.add(loveHealBean);
                    List<LoveHealDateBean.ChildrenBean> childrenBeans = loveHealDateBean.children;
                    for (LoveHealDateBean.ChildrenBean childrenBean : childrenBeans) {
                        LoveHealBean loveHealChildrenBean = new LoveHealBean(2, childrenBean._level, childrenBean.id, childrenBean.name, childrenBean.parent_id);
                        mDatas.add(loveHealChildrenBean);
                    }
                }
                mDatas.add(0, new LoveHealBean(0, "title"));

                Log.d("mylog", "onNetNext: mDatas.size() " + mDatas.size());

                mCacheWorker.setCache("main1_Dialogue_category", mDatas);
                mIsNetData = true;
                if (mIsNetData && mIsNetTitleData) {
                    initRecyclerViewData();
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netTitleData() {
        mCategoryArticleBeans = (List<CategoryArticleBean>) mCacheWorker.getCache(mMainActivity, "main1_Article_category");
        if (mCategoryArticleBeans != null && mCategoryArticleBeans.size() != 0) {
            mIsCacheTitleData = true;
            if (mIsCacheData && mIsCacheTitleData) {
                initRecyclerViewData();
            }
        } else {
            mLoadDialog = new LoadDialog(mMainActivity);
            mLoadDialog.showLoadingDialog();
        }
        mLoveEngin.categoryArticle("Article/category").subscribe(new MySubscriber<AResultInfo<List<CategoryArticleBean>>>(mLoadDialog) {
            @Override
            protected void onNetNext(AResultInfo<List<CategoryArticleBean>> listAResultInfo) {
                mCategoryArticleBeans = listAResultInfo.data;
                mCacheWorker.setCache("main1_Article_category", mCategoryArticleBeans);
                mIsNetTitleData = true;
                if (mIsNetData && mIsNetTitleData) {
                    initRecyclerViewData();
                }
            }

            @Override
            protected void onNetError(Throwable e) {
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {
        boolean isOpenUsingHelp = (boolean) SPUtils.get(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, false);
        if (!isOpenUsingHelp) {
            SPUtils.put(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, true);
            mMainActivity.startActivity(new Intent(mMainActivity, UsingHelpHomeActivity.class));
        }

        MainT1CreateAdapter adapter = new MainT1CreateAdapter(mMainActivity, mDatas) {

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveHealItemViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getTypeTitleHolder(ViewGroup viewGroup) {
                return new LoveHealTitleViewHolder(mMainActivity, null, viewGroup);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                MainT1CategoryViewHolder mainT1CategoryViewHolder = new MainT1CategoryViewHolder(mMainActivity, null, parent);
                mainT1CategoryViewHolder.setOnClickTitleIconListener(new MainT1CategoryViewHolder.OnClickTitleIconListener() {
                    @Override
                    public void clickTitleIcon(int position) {
                        switch (position) {
                            case 0:
                                startLoveByStagesActivity(0, "单身期");
                                break;
                            case 1:
                                startLoveByStagesActivity(1, "追求期");
                                break;
                            case 2:
                                startLoveByStagesActivity(2, "热恋期");
                                break;
                            case 3:
                                startLoveByStagesActivity(3, "失恋期");
                                break;
                            case 4:
                                startLoveByStagesActivity(4, "婚后期");
                                break;
                            case 5:
                                startActivity(new Intent(mMainActivity, LoveHealActivity.class));
                                break;
                            case 6:
                                startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
                                break;
                            case 7:  //去搜索页面
                                mMainActivity.startActivity(new Intent(mMainActivity, ShareActivity.class));
                                break;
                        }
                    }

                    @Override
                    public void clickIconShare(String keyword) {  //搜索icon 直接搜索
                        /*if (etShare == null) {
                            return;
                        }
                        String keyword = etShare.getText().toString().trim();*/
                        if (TextUtils.isEmpty(keyword)) {
                            mMainActivity.showToastShort("请输入搜索关键字");
                            return;
                        }

//                        netPagerOneData(keyword);
                        netIsVipData(keyword);
                    }
                });
                return mainT1CategoryViewHolder;
            }
        };
        mRecyclerView.setAdapter(adapter);

        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.d("mylog", "onScrollStateChanged: 77777777777777777");
                mMainActivity.hindKeyboard(mRecyclerView);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("mylog", "onScrolled: 8888888888");
            }
        });*/

        mMainActivity.hindKeyboard(mRecyclerView);
    }

    private void startLoveByStagesActivity(int position, String title) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans.size() < position + 1) {
            return;
        }
        CategoryArticleBean categoryArticleBean = mCategoryArticleBeans.get(position);
        ArrayList<CategoryArticleChildrenBean> children = categoryArticleBean.children;
        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, title, children);
    }

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private void netPagerOneData(final String keyword) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mMainActivity.showToLoginDialog();
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadDialog(mMainActivity);
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.searchDialogue2(String.valueOf(id), keyword, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/search").subscribe(new MySubscriber<AResultInfo<SearchDialogueBean>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                int searchBuyVip = searchDialogueBean.search_buy_vip;
                if (1 == searchBuyVip) { //1 弹窗 0不弹
                    startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                    return;
                } else {
                    List<LoveHealDetBean> list = searchDialogueBean.list;

                }
            }

            @Override
            protected void onNetError(Throwable e) {
//                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            protected void onNetCompleted() {
//                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void netIsVipData(final String keyword) {
        final int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mMainActivity.showToLoginDialog();
            return;
        }
        if (mLoadDialogInfo == null) {
            mLoadDialogInfo = new LoadDialog(mMainActivity);
        }
        mLoadDialogInfo.showLoadingDialog();
        mLoveEngin.userInfo(String.valueOf(id), "user/info").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(mLoadDialogInfo) {

            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
//                ShareActivity.startShareActivity(mMainActivity, keyword);
                int isVip = idCorrelationLoginBean.is_vip;
                if (isVip < 1) { // 0弹窗 1不弹
                    startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                    return;
                } else {
                    ShareActivity.startShareActivity(mMainActivity, keyword);
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }
}
