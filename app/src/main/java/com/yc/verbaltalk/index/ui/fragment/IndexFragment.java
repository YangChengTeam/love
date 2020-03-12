package com.yc.verbaltalk.index.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.index.adapter.IndexHotAdapter;
import com.yc.verbaltalk.index.adapter.IndexVerbalAdapter;
import com.yc.verbaltalk.index.adapter.SearchTintAdapter;
import com.yc.verbaltalk.base.engine.MySubscriber;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.BannerInfo;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.chat.bean.IndexHotInfo;
import com.yc.verbaltalk.chat.bean.IndexHotInfoWrapper;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.event.NetWorkChangT1Bean;
import com.yc.verbaltalk.base.utils.BackfillSingle;
import com.yc.verbaltalk.base.engine.LoveEnginV2;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.SPUtils;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.chat.ui.activity.PracticeLoveActivity;
import com.yc.verbaltalk.chat.ui.activity.PracticeTeachActivity;
import com.yc.verbaltalk.index.ui.activity.SearchActivity;
import com.yc.verbaltalk.mine.ui.activity.UsingHelpHomeActivity;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import rx.Subscriber;

/**
 * Created by mayn on 2019/4/23.
 * 首页
 */

public class IndexFragment extends BaseMainFragment {
    private LoveEngine mLoveEngin;


    private RecyclerView mRecyclerView;
    private LinearLayout mLlNotNet;

    private List<LoveHealDateBean> mDatas = new ArrayList<>();

    private LoadDialog mLoadDialogInfo;

    private IndexVerbalAdapter mAdapter;

    private TextView tvApplication, tvSence;

    private ImageView ivApplication, ivSence;

    private LoadDialog loadDialog = null;

    private RecyclerView searchRecyclerView, hotRecyclerView;
    private SearchTintAdapter tintAdapter;

    private IndexHotAdapter indexHotAdapter;

    private int page = 1;
    private int PAGE_SIZE = 10;
    private RelativeLayout rlApplication;
    private RelativeLayout rlSence;
    //    private View viewApplication;
//    private View viewSence;
    private ImageView ivPracticeTeach;
    private ImageView ivPracticelove;


    @Override
    protected int setContentView() {

        return R.layout.fragment_main_index;
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
        mLoveEngin = new LoveEngine(mMainActivity);
//        mCacheWorker = new CacheWorker();
        mLlNotNet = rootView.findViewById(R.id.main_t1_not_net);
        ivPracticeTeach = rootView.findViewById(R.id.iv_practice_teach);
        ivPracticelove = rootView.findViewById(R.id.iv_practice_love);

        mLoadDialogInfo = new LoadDialog(mMainActivity);
        tvApplication = rootView.findViewById(R.id.tv_application);
        tvSence = rootView.findViewById(R.id.tv_sence);
        ivApplication = rootView.findViewById(R.id.iv_application);
        ivSence = rootView.findViewById(R.id.iv_sence);
        rlApplication = rootView.findViewById(R.id.rl_application);
        rlSence = rootView.findViewById(R.id.rl_sence);
//        viewApplication = rootView.findViewById(R.id.view_application);
//        viewSence = rootView.findViewById(R.id.view_sence);


        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);
        searchRecyclerView = rootView.findViewById(R.id.search_down_rv);
        hotRecyclerView = rootView.findViewById(R.id.recyclerView_hot);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new IndexVerbalAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mMainActivity);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        hotRecyclerView.setLayoutManager(layoutManager);
        indexHotAdapter = new IndexHotAdapter(null);
        hotRecyclerView.setAdapter(indexHotAdapter);


        searchRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));

        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tintAdapter = new SearchTintAdapter(null);
        searchRecyclerView.setAdapter(tintAdapter);

        boolean o = (boolean) SPUtils.get(mMainActivity, SPUtils.INDEX_DIALOG, false);
        if (!o) {
            IndexDialogFragment indexDialogFragment = new IndexDialogFragment();
            indexDialogFragment.show(getChildFragmentManager(), "");
            indexDialogFragment.setOnConfirmListener(() -> {
                boolean isOpenUsingHelp = (boolean) SPUtils.get(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, false);
                if (!isOpenUsingHelp) {
                    SPUtils.put(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, true);
                    mMainActivity.startActivity(new Intent(mMainActivity, UsingHelpHomeActivity.class));
                }
            });
        }


        initSearchView();
        initListener();
        getRandomWords("");
        initData();

    }


    private void getIndexBanner() {

        CommonInfoHelper.getO(mMainActivity, "main_index_banner", new TypeReference<List<BannerInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<BannerInfo>>) o -> {
            if (o != null && o.size() > 0) {
                setBannerDatas(o);
            }
        });

        mLoveEngin.getIndexBanner().subscribe(new Subscriber<ResultInfo<List<BannerInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<BannerInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    List<BannerInfo> listResultInfos = listResultInfo.data;
                    CommonInfoHelper.setO(mMainActivity, listResultInfos, "main_index_banner");
                    setBannerDatas(listResultInfos);
                }
            }
        });
    }

    private void setBannerDatas(List<BannerInfo> listResultInfos) {
        List<String> images = new ArrayList<>();
        if (listResultInfos.size() > 0) {
            for (BannerInfo resultInfo : listResultInfos) {
                images.add(resultInfo.getImg());
            }
        }
        initBanner(images);
    }


    /**
     * 模拟服务器获取随机顺序的热词
     *
     * @return
     */


    private void getRandomWords(String keyWord) {


        mLoveEngin.getIndexDropInfos(keyWord).subscribe(new Subscriber<ResultInfo<IndexHotInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<IndexHotInfoWrapper> indexHotInfoWrapperResultInfo) {
                if (indexHotInfoWrapperResultInfo != null &&
                        indexHotInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && indexHotInfoWrapperResultInfo.data != null) {
                    tintAdapter.setNewData(indexHotInfoWrapperResultInfo.data.getList());
//                    mCacheWorker.setCache("index_hot_drop_data", indexHotInfoWrapperResultInfo.data.getList());
                }

            }
        });

    }

    private void initListener() {

        rlApplication.setOnClickListener(v -> {
            resetSelect();
            if (ivApplication.getVisibility() != View.VISIBLE)
                ivApplication.setVisibility(View.VISIBLE);
            geApplicationData();
            MobclickAgent.onEvent(mMainActivity, "applied_speech_id", "应用话术");
        });
        rlSence.setOnClickListener(v -> {
            resetSelect();
            if (ivSence.getVisibility() != View.VISIBLE)
                ivSence.setVisibility(View.VISIBLE);
            getSceneData();
            MobclickAgent.onEvent(mMainActivity, "scene_speech_id", "场景话术");
        });

        tintAdapter.setOnItemClickListener((adapter, view, position) -> {
            IndexHotInfo tint = tintAdapter.getItem(position);
            MobclickAgent.onEvent(mMainActivity, "hot_words_id", "热词点击");
//                netIsVipData(tint);
            if (tint != null)
                searchWord(tint.getSearch());
        });

        indexHotAdapter.setOnItemClickListener((adapter, view, position) -> {
            // TODO: 2019/7/17 热门话题点击跳转
            IndexHotInfo indexHotInfo = indexHotAdapter.getItem(position);
            if (indexHotInfo != null) {
//                    netIsVipData(indexHotInfo.getSearch());
                searchWord(indexHotInfo.getSearch());
                MobclickAgent.onEvent(mMainActivity, "hot_topic_id", "热门话题");
            }
        });

        ivPracticelove.setOnClickListener(v -> startActivity(new Intent(mMainActivity, PracticeLoveActivity.class)));
        ivPracticeTeach.setOnClickListener(v -> {
            startActivity(new Intent(mMainActivity, PracticeTeachActivity.class));
            MobclickAgent.onEvent(mMainActivity, "practice_id", "实战学习");
        });
    }

    private void resetSelect() {
//        viewApplication.setVisibility(View.GONE);
//        viewSence.setVisibility(View.GONE);
        ivApplication.setVisibility(View.GONE);
        ivSence.setVisibility(View.GONE);
    }


    private void initData() {
//        netUserReg();  //用户注册登录
        geApplicationData();
        getHotData();
        mMainActivity.showToWxServiceDialog("homewx", this::showService);
        getIndexBanner();
//        getRandomWords();
    }


    private void getHotData() {


        CommonInfoHelper.getO(mMainActivity, "index_hot", new TypeReference<List<IndexHotInfo>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<IndexHotInfo>>) indexHotInfos -> {
            if (indexHotInfos != null) {
                indexHotAdapter.setNewData(indexHotInfos);
            }
        });

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
//                    mCacheWorker.setCache("index_hot", indexHotInfoWrapperResultInfo.data.getList());
                    CommonInfoHelper.setO(mMainActivity, indexHotInfoWrapperResultInfo.data.getList(), "index_hot");


                }
            }
        });
    }


    private void showService() {

        IndexActivityFragment indexActivityFragment = new IndexActivityFragment();
        indexActivityFragment.show(getChildFragmentManager(), "");
        indexActivityFragment.setListener(() -> {
            mMainActivity.openWeiXin();
            Toast.makeText(mMainActivity, "导师微信复制成功，请在微信中添加", Toast.LENGTH_SHORT).show();
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


//    private void netUserReg() {
//        LoadDialog loadDialog = new LoadDialog(mMainActivity);
//        LoveEnginV2 loveEnginV2 = new LoveEnginV2(mMainActivity);
//        loveEnginV2.userReg("user/reg").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(loadDialog) {
//            @Override
//            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
//
//                final IdCorrelationLoginBean data = idCorrelationLoginBeanAResultInfo.data;
//                mRecyclerView.postDelayed(() -> loginSuccess(data), 500);
//
//            }
//
//            @Override
//            protected void onNetError(Throwable e) {
//
//            }
//
//            @Override
//            protected void onNetCompleted() {
//
//            }
//        });
//    }
//
//
//    private void loginSuccess(IdCorrelationLoginBean data) {
//        //持久化存储登录信息
//        String str = JSON.toJSONString(data);// java对象转为jsonString
//        BackfillSingle.backfillLoginData(mMainActivity, str);
//        UserInfoHelper.saveUserInfo(data);
////        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
//    }


    private void netDialogueData(final String sence) {

        if (sence.equals(""))
            getCacheData("main1_Dialogue_category");
        else if (sence.equals("2")) {

            getCacheData("main1_Dialogue_sence");
        }

        loadDialog = new LoadDialog(mMainActivity);
        loadDialog.showLoadingDialog();

        mLoveEngin.loveCategory("Dialogue/category", sence).subscribe(new Subscriber<AResultInfo<List<LoveHealDateBean>>>() {
            @Override
            public void onCompleted() {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(AResultInfo<List<LoveHealDateBean>> listAResultInfo) {
                List<LoveHealDateBean> loveHealDateBeans = listAResultInfo.data;
                createNewData(loveHealDateBeans, sence);
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
            }

        });
    }

    private void getCacheData(String key) {
        CommonInfoHelper.getO(mMainActivity, key, new TypeReference<List<LoveHealDateBean>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<LoveHealDateBean>>) loveHealDateBeans -> {
            mDatas = loveHealDateBeans;
            if (mDatas != null && mDatas.size() > 0) {
                initRecyclerViewData();
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

            CommonInfoHelper.setO(mMainActivity, loveHealDateBeans, "main1_Dialogue_category");
        else if (sence.equals("2")) {

            CommonInfoHelper.setO(mMainActivity, loveHealDateBeans, "main1_Dialogue_sence");
        }
        initRecyclerViewData();

    }

    private void initRecyclerViewData() {


        mAdapter.setNewData(mDatas);

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

        final ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchView.post(() -> {
            searchIcon.setImageDrawable(null);

            searchIcon.setVisibility(View.GONE);
        });
        //修改键入的文字字体大小、颜色和hint的字体颜色
        final EditText editText = searchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
                .getDimension(R.dimen.size_16));
//        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        ImageView mCloseButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        final TextView textView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(view -> {
                //清除搜索框并加载默认数据
//                    hindShareItemShowInfo();
                textView.setText(null);
            });
        }

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            isVisable = !isVisable;

            searchRecyclerView.setVisibility(hasFocus ? View.VISIBLE : View.GONE);

        });

        editText.setOnClickListener(v -> {
            isVisable = !isVisable;
            searchRecyclerView.setVisibility(isVisable ? View.VISIBLE : View.GONE);

        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索按钮回调
                if (TextUtils.isEmpty(query)) {
                    mMainActivity.showToastShort("请输入搜索关键字");
                    return false;
                }
//                netIsVipData(query);
                searchWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调
//                SearchActivity.this.shareTextString = newText;


                if (!TextUtils.isEmpty(newText) && !TextUtils.isEmpty(newText.trim())) {
                    getRandomWords(newText.trim());
                    searchRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    searchRecyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        ivIconShare.setOnClickListener(v -> {
            String keyWord = searchView.getQuery().toString().trim();
            if (TextUtils.isEmpty(keyWord)) {
                mMainActivity.showToastShort("请输入搜索关键字");
                return;
            }
//                netIsVipData(keyWord);
            searchWord(keyWord);

        });
    }


    private void initBanner(List<String> images) {
        Banner banner = rootView.findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader(false));
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Accordion);

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.setOnBannerListener(position -> {
//            if (position == 0) {
//                mContext.startActivity(new Intent(mContext, ExpressFragment.class));
//
//            } else if (position == 1) {
//                mContext.startActivity(new Intent(mContext, AudioActivity.class));
//            }
        });
        if (mMainActivity != null && !mMainActivity.isDestroyed())
            banner.start();
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
                .subscribe(new Subscriber<AResultInfo<SearchDialogueBean>>() {


                    @Override
                    public void onCompleted() {
                        mLoadDialogInfo.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadDialogInfo.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                        if (searchDialogueBeanAResultInfo != null && searchDialogueBeanAResultInfo.code == HttpConfig.STATUS_OK && searchDialogueBeanAResultInfo.data != null) {
                            SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                            int searchBuyVip = searchDialogueBean.search_buy_vip;
                            if (1 == searchBuyVip) { //1 弹窗 0不弹
                                startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                            } else {
                                SearchActivity.startSearchActivity(mMainActivity, keyword);
                            }
                        }
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
