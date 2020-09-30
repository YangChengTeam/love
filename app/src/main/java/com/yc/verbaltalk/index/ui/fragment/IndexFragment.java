package com.yc.verbaltalk.index.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.music.player.lib.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.YcApplication;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.utils.GlideImageLoader;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.BannerInfo;
import com.yc.verbaltalk.chat.bean.IndexHotInfo;
import com.yc.verbaltalk.chat.bean.IndexHotInfoWrapper;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.event.NetWorkChangT1Bean;
import com.yc.verbaltalk.index.adapter.IndexVerbalAdapter;
import com.yc.verbaltalk.index.adapter.SearchTintAdapter;
import com.yc.verbaltalk.index.ui.activity.SmartChatVerbalActivity;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.model.util.SPUtils;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by sunshey on 2019/4/23.
 * 首页
 */

public class IndexFragment extends BaseMainFragment {
    private LoveEngine mLoveEngine;


    private RecyclerView mRecyclerView;
    private LinearLayout mLlNotNet;

    private List<LoveHealDateBean> mDatas = new ArrayList<>();


    private IndexVerbalAdapter indexVerbalAdapter;


    private ImageView ivApplication, ivSence;

    private LoadDialog loadDialog = null;

    private RecyclerView searchRecyclerView;
    private SearchTintAdapter tintAdapter;


    private RelativeLayout rlApplication;
    private RelativeLayout rlSence;
    private AppBarLayout appBarLayout;

    private LinearLayout ll_top_container;
    private Toolbar toolbar;

    private RelativeLayout rl_search_container;
    private EditText et_verbal_search;
    private ImageView iv_delete;
    private TextView tv_verbal_search;
    private LoadDialog mLoadDialogInfo;
    private int page = 1;
    private int PAGE_SIZE = 5;

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

        mLoveEngine = new LoveEngine(mMainActivity);

        mLlNotNet = rootView.findViewById(R.id.main_t1_not_net);


        ivApplication = rootView.findViewById(R.id.iv_application);
        ivSence = rootView.findViewById(R.id.iv_sence);
        rlApplication = rootView.findViewById(R.id.rl_application);
        rlSence = rootView.findViewById(R.id.rl_sence);


        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);
        searchRecyclerView = rootView.findViewById(R.id.search_down_rv);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        ll_top_container = rootView.findViewById(R.id.ll_top_container);
        toolbar = rootView.findViewById(R.id.toolbar);
        rl_search_container = rootView.findViewById(R.id.rl_search_container);
        et_verbal_search = rootView.findViewById(R.id.et_verbal_search);
        iv_delete = rootView.findViewById(R.id.iv_delete);
        tv_verbal_search = rootView.findViewById(R.id.tv_verbal_search);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        indexVerbalAdapter = new IndexVerbalAdapter(null);
        mRecyclerView.setAdapter(indexVerbalAdapter);


        searchRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));

        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tintAdapter = new SearchTintAdapter(null);
        searchRecyclerView.setAdapter(tintAdapter);

        boolean o = (boolean) SPUtils.get(mMainActivity, SPUtils.INDEX_DIALOG, false);
//        if (!o) {
//            IndexDialogFragment indexDialogFragment = new IndexDialogFragment();
//            indexDialogFragment.show(getChildFragmentManager(), "");
//            indexDialogFragment.setOnConfirmListener(() -> {
////                boolean isOpenUsingHelp = (boolean) SPUtils.get(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, false);
////                if (!isOpenUsingHelp) {
////                    SPUtils.put(mMainActivity, SPUtils.IS_OPEN_USING_HELP_HOME, true);
////                    mMainActivity.startActivity(new Intent(mMainActivity, UsingHelpHomeActivity.class));
////                }
//            });
//        }


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

        mLoveEngine.getIndexBanner().subscribe(new DisposableObserver<ResultInfo<List<BannerInfo>>>() {
            @Override
            public void onComplete() {

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
//        List<String> images = new ArrayList<>();
//
//        if (listResultInfos.size() > 0) {
//            for (BannerInfo resultInfo : listResultInfos) {
//                images.add(resultInfo.getImg());
//            }
//        }
        initBanner(listResultInfos);
    }


    /**
     * 模拟服务器获取随机顺序的热词
     *
     * @return
     */


    private void getRandomWords(String keyWord) {


        mLoveEngine.getIndexDropInfos(keyWord).subscribe(new DisposableObserver<ResultInfo<IndexHotInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<IndexHotInfoWrapper> indexHotInfoWrapperResultInfo) {
                if (indexHotInfoWrapperResultInfo != null &&
                        indexHotInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && indexHotInfoWrapperResultInfo.data != null) {
                    tintAdapter.setNewData(indexHotInfoWrapperResultInfo.data.getList());

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

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
//verticalOffset  当前偏移量 appBarLayout.getTotalScrollRange() 最大高度 便宜值
            int offset = Math.abs(verticalOffset); //目的是将负数转换为绝对正数；
            //标题栏的渐变

            ll_top_container.setBackgroundColor(mMainActivity.changeAlpha(ContextCompat.getColor(mMainActivity, R.color.white)
                    , Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
            /**
             * 当前最大高度偏移值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (offset <= appBarLayout.getTotalScrollRange() / 2) {
                toolbar.setTitle("");
                ll_top_container.setAlpha((appBarLayout.getTotalScrollRange() / 2 - offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
//                toolbar.visibility = View.GONE
                rl_search_container.setVisibility(View.GONE);
                tv_verbal_search.setVisibility(View.GONE);
                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (offset > appBarLayout.getTotalScrollRange() / 2) {
                float floate = (offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
                ll_top_container.setAlpha(floate);

                rl_search_container.setVisibility(View.VISIBLE);

            }
        });

        et_verbal_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    tv_verbal_search.setVisibility(View.VISIBLE);
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    tv_verbal_search.setVisibility(View.GONE);
                    iv_delete.setVisibility(View.GONE);
                }
            }
        });

        tv_verbal_search.setOnClickListener(v -> {
            String content = et_verbal_search.getText().toString().trim();
            searchWord(content);
        });

        iv_delete.setOnClickListener(v -> et_verbal_search.setText(""));
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
//        mMainActivity.showToWxServiceDialog("homewx", this::showService);
        getIndexBanner();
//        getRandomWords();

        
    }


    private void showService(String wx) {

        IndexActivityFragment indexActivityFragment = new IndexActivityFragment();
        indexActivityFragment.show(getChildFragmentManager(), "");
        indexActivityFragment.setWX(wx);
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


    private void netDialogueData(final String sence) {

        if (sence.equals(""))
            getCacheData("main1_Dialogue_category");
        else if (sence.equals("2")) {
            getCacheData("main1_Dialogue_sence");
        }

        loadDialog = new LoadDialog(mMainActivity);
        loadDialog.showLoadingDialog();

        mLoveEngine.loveCategory(sence).subscribe(new DisposableObserver<ResultInfo<List<LoveHealDateBean>>>() {
            @Override
            public void onComplete() {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                if (loadDialog != null) loadDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<List<LoveHealDateBean>> listAResultInfo) {
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


        indexVerbalAdapter.setNewData(mDatas);

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
                    ToastUtils.showCenterToast("请输入搜索关键字");
                    return false;
                }
//                netIsVipData(query);
                searchWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //输入变化回调

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
                ToastUtils.showCenterToast("请输入搜索关键字");
                return;
            }
//                netIsVipData(keyWord);
            searchWord(keyWord);
        });
    }


    private void initBanner(List<BannerInfo> listResultInfos) {

        List<String> images = new ArrayList<>();

        if (listResultInfos.size() > 0) {
            for (BannerInfo resultInfo : listResultInfos) {
                images.add(resultInfo.getImg());
            }
        }
        Banner banner = rootView.findViewById(R.id.banner);
        if (mMainActivity != null && !mMainActivity.isDestroyed()) {
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
            banner.setDelayTime(2500);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            banner.setOnBannerListener(position -> {
//            if (position == 0) {
                String action = listResultInfos.get(position).getType_value();
                if (!TextUtils.isEmpty(action)) {
                    Uri uri = Uri.parse(action);
                    //String android.intent.action.VIEW 比较通用，会根据用户的数据类型打开相应的Activity。如:浏览器,电话,播放器,地图
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

//            }
//            else if (position == 1) {
//                mContext.startActivity(new Intent(mContext, AudioActivity.class));
//            }
            });

            banner.start();

        }
    }


    private void searchWord(final String keyword) {

        if (UserInfoHelper.isLogin(mMainActivity)) {
            MobclickAgent.onEvent(mMainActivity, "index_search_box_click", "首页搜索框搜索");
//
            if (mLoadDialogInfo == null) {
                mLoadDialogInfo = new LoadDialog(mMainActivity);
            }
            mLoadDialogInfo.showLoadingDialog();
            mLoveEngine.searchDialogue2(UserInfoHelper.getUid(), keyword, String.valueOf(page), String.valueOf(PAGE_SIZE))
                    .subscribe(new DisposableObserver<ResultInfo<SearchDialogueBean>>() {


                        @Override
                        public void onComplete() {
                            mLoadDialogInfo.dismissLoadingDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mLoadDialogInfo.dismissLoadingDialog();
                        }

                        @Override
                        public void onNext(ResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                            if (searchDialogueBeanAResultInfo != null && searchDialogueBeanAResultInfo.code == HttpConfig.STATUS_OK && searchDialogueBeanAResultInfo.data != null) {
                                SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                                int searchBuyVip = searchDialogueBean.search_buy_vip;
                                if (1 == searchBuyVip) { //1 弹窗 0不弹
                                    startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                                } else {
                                    SmartChatVerbalActivity.startActivity(mMainActivity, keyword);
                                }
                            }
                        }


                    });
            searchCount(UserInfoHelper.getUid(), keyword);
        }
    }

    private void searchCount(String userId, String keyword) {
        mLoveEngine.searchCount(userId, keyword).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

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
