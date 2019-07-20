package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.IndexHotAdapter;
import com.yc.love.adaper.rv.MainT1CreateAdapter;
import com.yc.love.adaper.rv.SearchTintAdapter;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.IndexHotInfo;
import com.yc.love.model.bean.IndexHotInfoWrapper;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.SearchDialogueBean;
import com.yc.love.model.bean.event.NetWorkChangT1Bean;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.engin.LoveEnginV2;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.ShareActivity;
import com.yc.love.ui.activity.UsingHelpHomeActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.Subscriber;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {
    private LoveEngin mLoveEngin;

    private CacheWorker mCacheWorker;
    private RecyclerView mRecyclerView;
    private LinearLayout mLlNotNet;

    private List<LoveHealDateBean> mDatas = new ArrayList<>();

    private LoadDialog mLoadDialogInfo;

    private MainT1CreateAdapter mAdapter;

//    private TextView tvApplication, tvSence;

//    private ImageView ivApplication, ivSence;

    private LoadingDialog loadDialog = null;

    private RecyclerView searchRecyclerView, hotRecyclerView;
    private SearchTintAdapter tintAdapter;
    private List<String> hotWords;
    private IndexHotAdapter indexHotAdapter;

    private int page = 1;
    private int PAGE_SIZE = 10;
    private RelativeLayout rlApplication;
    private RelativeLayout rlSence;
    private View viewApplication;
    private View viewSence;


    @Override
    protected int setContentView() {

        return R.layout.fragment_main_t1_new;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("mylog", "666666666666   onResume: ");

        if (mRecyclerView != null) {
            mRecyclerView.setFocusable(true);
            mRecyclerView.setFocusableInTouchMode(true);
            mRecyclerView.requestFocus();
        }

    }


    /**
     * 获取应用话术数据
     */
    private void geApplicationData() {

        netDialogueData("");
    }

    /**
     * 获取场景话术数据
     */
    private void getSceneData() {

        netDialogueData("2");
    }


    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);
        mCacheWorker = new CacheWorker();
        mLlNotNet = rootView.findViewById(R.id.main_t1_not_net);

        mLoadDialogInfo = new LoadDialog(mMainActivity);
//        tvApplication = rootView.findViewById(R.id.tv_application);
//        tvSence = rootView.findViewById(R.id.tv_sence);
//        ivApplication = rootView.findViewById(R.id.iv_application);
//        ivSence = rootView.findViewById(R.id.iv_sence);
        rlApplication = rootView.findViewById(R.id.rl_application);
        rlSence = rootView.findViewById(R.id.rl_sence);
        viewApplication = rootView.findViewById(R.id.view_application);
        viewSence = rootView.findViewById(R.id.view_sence);


        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);
        searchRecyclerView = rootView.findViewById(R.id.search_down_rv);
        hotRecyclerView = rootView.findViewById(R.id.recyclerView_hot);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainT1CreateAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mMainActivity);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        hotRecyclerView.setLayoutManager(layoutManager);
        indexHotAdapter = new IndexHotAdapter(null);
        hotRecyclerView.setAdapter(indexHotAdapter);


        hotWords = Arrays.asList(getResources().getStringArray(R.array.search_tint));


        searchRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));

        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tintAdapter = new SearchTintAdapter(getRandomWords());
        searchRecyclerView.setAdapter(tintAdapter);


        initSearchView();
        initListener();

        initData();
    }

    /**
     * 模拟服务器获取随机顺序的热词
     *
     * @return
     */
    private List<String> getRandomWords() {
        List<String> titles = new ArrayList<>();

        int size = hotWords.size();

//        for (int i = 0; i < size; i++) {
        boolean isContinue = true;

        while (isContinue) {
            int random = (int) (Math.random() * size);
            String word = hotWords.get(random);
            if (!titles.contains(word))
                titles.add(word);
            if (titles.size() == size) isContinue = false;

        }

        return titles;

    }

    private void initListener() {

        rlApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelect();
                if (viewApplication.getVisibility() != View.VISIBLE)
                    viewApplication.setVisibility(View.VISIBLE);
                geApplicationData();
                MobclickAgent.onEvent(mMainActivity, "applied_speech_id", "应用话术");
            }
        });
        rlSence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelect();
                if (viewSence.getVisibility() != View.VISIBLE)
                    viewSence.setVisibility(View.VISIBLE);
                getSceneData();
                MobclickAgent.onEvent(mMainActivity, "scene_speech_id", "场景话术");
            }
        });

        tintAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String tint = tintAdapter.getItem(position);
                MobclickAgent.onEvent(mMainActivity, "hot_words_id", "热词点击");
//                netIsVipData(tint);
                searchWord(tint);
            }
        });

        indexHotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2019/7/17 热门话题点击跳转
                IndexHotInfo indexHotInfo = indexHotAdapter.getItem(position);
                if (indexHotInfo != null) {
//                    netIsVipData(indexHotInfo.getSearch());
                    searchWord(indexHotInfo.getSearch());
                    MobclickAgent.onEvent(mMainActivity, "hot_topic_id", "热门话题");
                }
            }
        });
    }

    private void resetSelect() {
        viewApplication.setVisibility(View.GONE);
        viewSence.setVisibility(View.GONE);
    }


    private void initData() {
        netUserReg();  //用户注册登录
        getSceneData();
        getHotData();
    }


    private void getHotData() {
        List<IndexHotInfo> indexHotInfos = (List<IndexHotInfo>) mCacheWorker.getCache(mMainActivity, "index_hot");
        if (indexHotInfos != null) {
            indexHotAdapter.setNewData(indexHotInfos);
        }
        mLoveEngin.getIndexHotInfo().subscribe(new Subscriber<ResultInfo<IndexHotInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<IndexHotInfoWrapper> indexHotInfoWrapperResultInfo) {
                if (indexHotInfoWrapperResultInfo != null && indexHotInfoWrapperResultInfo.code == HttpConfig.STATUS_OK
                        && indexHotInfoWrapperResultInfo.data != null && indexHotInfoWrapperResultInfo.data.getList() != null) {
                    indexHotAdapter.setNewData(indexHotInfoWrapperResultInfo.data.getList());
                    mCacheWorker.setCache("index_hot", indexHotInfoWrapperResultInfo.data.getList());
                }
            }
        });
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
            }
        }
    }


    @Override
    protected void lazyLoad() {

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


    private void netDialogueData(final String sence) {

        if (sence.equals(""))
            mDatas = (List<LoveHealDateBean>) mCacheWorker.getCache(mMainActivity, "main1_Dialogue_category");
        else if (sence.equals("2")) {
            mDatas = (List<LoveHealDateBean>) mCacheWorker.getCache(mMainActivity, "main1_Dialogue_sence");
        }
        if (mDatas != null && mDatas.size() > 0) {
            initRecyclerViewData();
        } else {
            loadDialog = new LoadingDialog(mMainActivity);
            loadDialog.showLoading();
        }
        mLoveEngin.loveCategory("Dialogue/category", sence).subscribe(new Subscriber<AResultInfo<List<LoveHealDateBean>>>() {
            @Override
            public void onCompleted() {
                if (loadDialog != null) loadDialog.dismissLoading();
            }

            @Override
            public void onError(Throwable e) {
                if (loadDialog != null) loadDialog.dismissLoading();
            }

            @Override
            public void onNext(AResultInfo<List<LoveHealDateBean>> listAResultInfo) {
                List<LoveHealDateBean> loveHealDateBeans = listAResultInfo.data;
                createNewData(loveHealDateBeans, sence);
                if (loadDialog != null) loadDialog.dismissLoading();
            }

        });
    }

    private void createNewData(List<LoveHealDateBean> loveHealDateBeans, String sence) {
        if (loveHealDateBeans == null || loveHealDateBeans.size() == 0) {
            return;
        }
        mDatas = loveHealDateBeans;


        Log.d("mylog", "onNetNext: mDatas.size() " + mDatas.size());

        if (sence.equals(""))
            mCacheWorker.setCache("main1_Dialogue_category", loveHealDateBeans);
        else if (sence.equals("2")) {
            mCacheWorker.setCache("main1_Dialogue_sence", loveHealDateBeans);
        }
        initRecyclerViewData();

    }

    private void initRecyclerViewData() {
        boolean isOpenUsingHelp = (boolean) SPUtils.get(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, false);
        if (!isOpenUsingHelp) {
            SPUtils.put(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, true);
            mMainActivity.startActivity(new Intent(mMainActivity, UsingHelpHomeActivity.class));
        }

        mAdapter.setNewData(mDatas);

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

    private boolean isVisable = false;

    private void initSearchView() {
        final SearchView searchView = rootView.findViewById(R.id.item_t1category_share_view);
        final ImageView ivIconShare = rootView.findViewById(R.id.item_t1category_iv_icon_share);


        try { //--拿到字节码
            Class<?> argClass = searchView.getClass();
            //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            //--暴力反射,只有暴力反射才能拿到私有属性
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            //--设置背景
            mView.setBackgroundResource(R.drawable.search_view_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchIcon.setImageDrawable(null);

                searchIcon.setVisibility(View.GONE);
            }
        });
        //修改键入的文字字体大小、颜色和hint的字体颜色
        final EditText editText = searchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.size_16));
//        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        ImageView mCloseButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        final TextView textView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //清除搜索框并加载默认数据
//                    hindShareItemShowInfo();
                    textView.setText(null);
                }
            });
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isVisable = !isVisable;

                searchRecyclerView.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
//                if (searchRecyclerView.getVisibility() == View.VISIBLE)
//                    tintAdapter.setNewData(getRandomWords());
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisable = !isVisable;
                searchRecyclerView.setVisibility(isVisable ? View.VISIBLE : View.GONE);
//                if (searchRecyclerView.getVisibility() == View.VISIBLE)
//                    tintAdapter.setNewData(getRandomWords());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索按钮回调
                if (TextUtils.isEmpty(query)) {
                    mMainActivity.showToastShort("请输入搜索关键字");
                    return false;
                }
//                        netPagerOneData(keyword);
//                netIsVipData(query);
                searchWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调
//                ShareActivity.this.shareTextString = newText;

                return false;
            }
        });

        ivIconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = searchView.getQuery().toString().trim();
                if (TextUtils.isEmpty(keyWord)) {
                    mMainActivity.showToastShort("请输入搜索关键字");
                    return;
                }
//                netIsVipData(keyWord);
                searchWord(keyWord);

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


    private void searchWord(final String keyword) {
        final int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mMainActivity.showToLoginDialog();
            return;
        }
        if (mLoadDialogInfo == null) {
            mLoadDialogInfo = new LoadDialog(mMainActivity);
        }
        mLoadDialogInfo.showLoadingDialog();
        mLoveEngin.searchDialogue2(String.valueOf(id), keyword, String.valueOf(page), String.valueOf(PAGE_SIZE), "Dialogue/search")
                .subscribe(new MySubscriber<AResultInfo<SearchDialogueBean>>(mLoadDialogInfo) {

                    @Override
                    protected void onNetNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                        SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                        int searchBuyVip = searchDialogueBean.search_buy_vip;
                        if (1 == searchBuyVip) { //1 弹窗 0不弹
                            startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
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
        searchCount(String.valueOf(id), keyword);
    }

    private void searchCount(String userid, String keyword) {
        mLoveEngin.searchCount(userid, keyword).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
    }
}
