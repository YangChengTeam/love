package com.yc.love.ui.frament.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.CategoryArticleChildrenBean;
import com.yc.love.model.bean.ExampleTsCategory;
import com.yc.love.model.bean.ExampleTsCategoryList;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.model.bean.event.NetWorkChangT3Bean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveIntroductionActivity;
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

public class MainT3Fragment extends BaseMainFragment {
    private TextView tvName;
    /*private int[] imgResId01s = {R.mipmap.main_bg_t3_01, R.mipmap.main_bg_t3_02, R.mipmap.main_bg_t3_03,
            R.mipmap.main_bg_t3_04, R.mipmap.main_bg_t3_05, R.mipmap.main_bg_t3_06};
    private int[] imgResId02s = {R.mipmap.main_bg_t3_07, R.mipmap.main_bg_t3_08, R.mipmap.main_bg_t3_09,
            R.mipmap.main_bg_t3_10, R.mipmap.main_bg_t3_11, R.mipmap.main_bg_t3_12, R.mipmap.main_bg_t3_13};*/
    private String[] name01s = {"线上撩妹", "线下撩妹", "开场搭讪", "约会邀请", "把握主权", "完美告白"};
    private String[] name02s = {"自我提升", "相亲妙招", "关系确定", "关系进阶", "甜蜜异地", "分手挽回", "婚姻经营"};
    private String[] des01s = {"用话语撩动屏幕前的TA", "用情感感染你面前的TA", "搭讪有窍门，开场不尴尬",
            "邀请有方法，约会不尴尬", "把握有窍门，轻松搞定TA", "遇见对的TA，告白要完美"};
    private String[] des02s = {"让自己优秀，妹子自然来", "相亲小妙招，牵住女神心", "确认过眼神，TA是对的人",
            "情感升温后，水到渠自成", "距离也可以因爱产生美", "不想和TA分，那就看看吧", "婚姻并不是爱情的坟墓"};
    private RecyclerView mRecyclerView;
    private List<MainT3Bean> mDatas;
    //    private LoadDialog mLoadingDialog;
    private List<CategoryArticleBean> mCategoryArticleBeans;
    private LinearLayout mLlNotNet;
    private boolean mIsNetData = false;
    private boolean mIsDataToCache;
    private LoadDialog mLoadDialog;
    private CacheWorker mCacheWorker;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3;
    }

    private LoveEngin mLoveEngin;

    @Override
    protected void initViews() {


    }

    private void initView() {
        if (mLoveEngin == null && mLlNotNet == null) {

            Log.d("mylog", "initView: MainT3Fragment 333333333 initView ");

            MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_PROMOTION_ID);
            mLoveEngin = new LoveEngin(mMainActivity);
            mCacheWorker = new CacheWorker();
            mLlNotNet = rootView.findViewById(R.id.main_t3_not_net);
            View viewBar = rootView.findViewById(R.id.main_t3_view_bar);
            mMainActivity.setStateBarHeight(viewBar, 1);
//        mLoadingDialog = mMainActivity.mLoadingDialog;
            initRecyclerView();
        }
    }

    public void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
    public void onMessageEvent(NetWorkChangT3Bean netWorkChangBean) {
        Log.d("mylog", "onMessageEvent: NetWorkChangT3Bean ");
        List<String> connectionTypeList = netWorkChangBean.connectionTypeList;
        checkNetChangUI(connectionTypeList);

        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
        //1213455
    }

    private void checkNetChangUI(List<String> connectionTypeList) {
        if (mLlNotNet != null) {
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
    }


    @Override
    protected void lazyLoad() {
        initView();
//        List<String> connectionTypeList = YcSingle.getInstance().connectionTypeList;
//        checkNetChangUI(connectionTypeList);
        if (mIsDataToCache) {
            mIsNetData = false;
        }
        if (!mIsNetData) {
            netData();
        }
    }

    private void netTitleData() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.categoryArticle("Article/category").subscribe(new MySubscriber<AResultInfo<List<CategoryArticleBean>>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<List<CategoryArticleBean>> listAResultInfo) {
                mIsNetData = true;
                mCategoryArticleBeans = listAResultInfo.data;
                for (CategoryArticleBean categoryArticleBean : mCategoryArticleBeans
                ) {
                    Log.d("mylog", "onNetNext: categoryArticleBean " + categoryArticleBean.toString());
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

    private void netData() {
//        mDatas = (List<MainT3Bean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main3_example_ts_category");
        mDatas = (List<MainT3Bean>) mCacheWorker.getCache(mMainActivity, "main3_example_ts_category");
        if (mDatas != null && mDatas.size() != 0) {
            initRecyclerViewData();
        } else {
            mLoadDialog = new LoadDialog(mMainActivity);
            mLoadDialog.showLoadingDialog();
        }
//        mLoveEngin.exampleTs("example/ts").subscribe(new MySubscriber<AResultInfo<List<ExampleTsBean>>>(loadDialog) {
        mLoveEngin.exampleTsCategory("example/ts_category").subscribe(new MySubscriber<AResultInfo<ExampleTsCategory>>(mLoadDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampleTsCategory> exampleTsCategoryAResultInfo) {
                mIsNetData = true;
                mDatas = new ArrayList<>();
                mDatas.add(new MainT3Bean(1));
//                mDatas.add(new MainT3Bean(2, "入门秘籍"));
                ExampleTsCategory exampleTsCategory = exampleTsCategoryAResultInfo.data;
                List<ExampleTsCategoryList> list1 = exampleTsCategory.list1;
                List<ExampleTsCategoryList> list2 = exampleTsCategory.list2;
                if (list1 != null) {
                    for (int i = 0; i < list1.size(); i++) {
                        ExampleTsCategoryList categoryList = list1.get(i);
                        mDatas.add(new MainT3Bean(3, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
                    }
                }
                mDatas.add(new MainT3Bean(2, "进阶秘籍"));
                if (list2 != null) {
                    for (int i = 0; i < list2.size(); i++) {
                        ExampleTsCategoryList categoryList = list2.get(i);
                        mDatas.add(new MainT3Bean(3, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
                    }
                }
//                SerializableFileUtli.checkPermissionWriteData(mDatas, "main3_example_ts_category");
                mCacheWorker.setCache("main3_example_ts_category", mDatas);
//                CacheUtils.cacheBeanData(mMainActivity, "main3_example_ts_category", mDatas);
                initRecyclerViewData();
            }

            @Override
            protected void onNetError(Throwable e) {
                /*mDatas = (List<MainT3Bean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main3_example_ts_category");
                if (mDatas != null && mDatas.size() != 0) {
                    mIsDataToCache = true;
                    mIsNetData = true;
                    initRecyclerViewData();
                }*/

                /*String data = (String) SPUtils.get(mMainActivity, "main3_example_ts_category", "");
                mDatas = new Gson().fromJson(data, new TypeToken<ArrayList<MainT3Bean>>() {
                }.getType());
                if (mDatas != null && mDatas.size() != 0) {
                    mIsDataToCache = true;
                    mIsNetData = true;
                    initRecyclerViewData();
                }*/
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {


        CreateMainT3Adapter mainT3Adapter = new CreateMainT3Adapter(mMainActivity, mDatas) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT3ItemViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new MainT3TitleViewHolder(mMainActivity, null, parent);
            }

            @Override
            protected BaseViewHolder getItemTitleHolder(ViewGroup parent) {
                return new MainT3ItemTitleViewHolder(mMainActivity, null, parent);
            }
        };
        mRecyclerView.setAdapter(mainT3Adapter);
    }

    private void startLoveByStagesActivity(int position, String title) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans.size() < position + 1) {
            return;
        }
        CategoryArticleBean categoryArticleBean = mCategoryArticleBeans.get(position);
        ArrayList<CategoryArticleChildrenBean> children = categoryArticleBean.children;
        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, title, children);
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0) {
                return;
            }
            MainT3Bean mainT3Bean = mDatas.get(position);
            LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, mainT3Bean.name, String.valueOf(mainT3Bean.id));
//            ExampleDetailActivity.startExampleDetailActivity(mMainActivity, mainT3Bean.id, mainT3Bean.desp);

        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

}
