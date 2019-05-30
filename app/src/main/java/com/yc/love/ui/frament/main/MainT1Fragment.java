package com.yc.love.ui.frament.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT1MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.base.RecyclerViewTimeoutListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT1ItemHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.TimeoutItemHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.CategoryArticleChildrenBean;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.event.NetWorkChangT1Bean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.util.SPUtils;
import com.yc.love.ui.activity.ExampleDetailActivity;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveHealActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.activity.ShareActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.utils.CacheUtils;
import com.yc.love.utils.SerializableFileUtli;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {
    //    private List<StringBean> mExampListsBeans;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private MainT1MoreItemAdapter mAdapter;
    private ProgressBarViewHolder progressBarViewHolder;
    private LoveEngin mLoveEngin;
    private List<ExampListsBean> mExampListsBeans = new ArrayList<>();
    ;
    private RecyclerView mRecyclerView;
    private LinearLayout mLlNotNet;
    private boolean mIsNetData = false;
    private boolean mIsNetTitleData = false;
    private boolean mIsCacheData = false;
    private boolean mIsCacheTitleData = false;
    private List<CategoryArticleBean> mCategoryArticleBeans;
    private boolean mIsDataToCache;
    private LoadDialog mLoadDialog;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t1;
    }


    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);

        mLlNotNet = rootView.findViewById(R.id.main_t1_not_net);

        mRecyclerView = rootView.findViewById(R.id.main_t1_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
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
//            ExampListsBean exampListsBean = mExampListsBeans.get(position);
//            LoveByStagesDetailsActivity.startLoveByStagesDetailsActivity(mMainActivity, exampListsBean.id, exampListsBean.post_title);
            if (position < 0) {
                return;
            }
            ExampListsBean exampListsBean = mExampListsBeans.get(position);
            ExampleDetailActivity.startExampleDetailActivity(mMainActivity, exampListsBean.id, exampListsBean.post_title);
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
        if (mIsDataToCache) {
            mIsNetData = false;
            mIsNetTitleData = false;
        }
//        mIsNetData = false;
        if (!mIsNetData) {
            netData();
        }
        if (!mIsNetTitleData) {
            netTitleData();
        }
    }

    private void netData() {
        mExampListsBeans = (List<ExampListsBean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main1_example_index");
        if (mExampListsBeans != null && mExampListsBeans.size() != 0) {
            mIsCacheData = true;
            if (mIsCacheData && mIsCacheTitleData) {
                initRecyclerViewData();
            }
        } else {
            mLoadDialog = new LoadDialog(mMainActivity);
            mLoadDialog.showLoadingDialog();
        }
        mLoveEngin.indexExample(String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/index").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                if (exampDataBean != null) {
                    mExampListsBeans = exampDataBean.lists;
                }
//                mExampListsBeans.add(0, new ExampListsBean(3, "Article_Category"));
                mExampListsBeans.add(0, new ExampListsBean(0, "title"));
                SerializableFileUtli.checkPermissionWriteData(mExampListsBeans, "main1_example_index");
                mIsNetData = true;
                if (mIsNetData && mIsNetTitleData) {
                    initRecyclerViewData();
                }
            }

            @Override
            protected void onNetError(Throwable e) {
              /*  mExampListsBeans = (List<ExampListsBean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main1_example_index");
                if (mExampListsBeans != null && mExampListsBeans.size() != 0) {
                    mIsCacheTitleData = true;
                    if (mIsCacheData && mIsCacheTitleData) {
                        initRecyclerViewData();
                    }
                }
//                java.net.SocketTimeoutException: timeout
//                java.net.UnknownHostException: Unable to resolve host "love.bshu.com": No address associated with hostname
                if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
//                    mExampListsBeans.add(new ExampListsBean(-1, "title"));
//                    initRecyclerViewData();
                    Log.d("mylog", "onNetError: SocketTimeoutException 网络超时 " + e.toString());
                }*/
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netTitleData() {

        mCategoryArticleBeans = (List<CategoryArticleBean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main1_Article_category");
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
//                CacheUtils.cacheBeanData(mMainActivity, "main_Article_category", mCategoryArticleBeans);
                SerializableFileUtli.checkPermissionWriteData(mCategoryArticleBeans, "main1_Article_category");
                mIsNetTitleData = true;
                if (mIsNetData && mIsNetTitleData) {
                    initRecyclerViewData();
                }
            }

            @Override
            protected void onNetError(Throwable e) {
              /*  mCategoryArticleBeans = (List<CategoryArticleBean>) SerializableFileUtli.checkReadPermission(mMainActivity, "main1_Article_category");
                if (mCategoryArticleBeans != null && mCategoryArticleBeans.size() != 0) {
                    mIsNetTitleData = true;
                    if (mIsNetData && mIsNetTitleData) {
                        initRecyclerViewData();
                    }
                }*/

                /*String data = (String) SPUtils.get(mMainActivity, "main_Article_category", "");
                mCategoryArticleBeans = new Gson().fromJson(data, new TypeToken<ArrayList<CategoryArticleBean>>() {
                }.getType());
                if (mCategoryArticleBeans != null && mCategoryArticleBeans.size() != 0) {
                    mIsNetTitleData = true;
                    if (mIsNetData && mIsNetTitleData) {
                        initRecyclerViewData();
                    }
                }*/
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {
        Log.d("mylog", "initRecyclerViewData: ");
        mAdapter = new MainT1MoreItemAdapter(mExampListsBeans, mRecyclerView) {

            @Override
            protected RecyclerView.ViewHolder getTimeoutHolder(ViewGroup parent) {
                return new TimeoutItemHolder(mMainActivity, parent, "", recyclerViewTimeoutListener);
            }

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT1ItemHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                TitleT1ViewHolder titleT1ViewHolder = new TitleT1ViewHolder(mMainActivity, null, parent);
                titleT1ViewHolder.setOnClickTitleIconListener(new TitleT1ViewHolder.OnClickTitleIconListener() {
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
                            case 7:
                                mMainActivity.startActivity(new Intent(mMainActivity, ShareActivity.class));
                                break;
                        }
                    }
                });
                return titleT1ViewHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mExampListsBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new MainT1MoreItemAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mExampListsBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        mExampListsBeans.remove(mExampListsBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void startLoveByStagesActivity(int position, String title) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans.size() < position + 1) {
            return;
        }
        CategoryArticleBean categoryArticleBean = mCategoryArticleBeans.get(position);
        ArrayList<CategoryArticleChildrenBean> children = categoryArticleBean.children;
        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, title, children);
    }


    private void netLoadMore() {
        mLoveEngin.indexExample(String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "example/index").subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mMainActivity) {
            @Override
            protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {

                ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                if (exampDataBean != null) {
                    List<ExampListsBean> netLoadMoreData = exampDataBean.lists;
                    changLoadMoreView(netLoadMoreData);
                }
            }

            @Override
            protected void onNetError(Throwable e) {
                if (PAGE_NUM != 1) {
                    PAGE_NUM--;
                }
                changLoadMoreView(null);
            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void changLoadMoreView(List<ExampListsBean> netLoadMoreData) {
        showProgressBar = false;
        mExampListsBeans.remove(mExampListsBeans.size() - 1);
        mAdapter.notifyDataSetChanged();
        if (netLoadMoreData != null) {
            if (netLoadMoreData.size() < PAGE_SIZE) {
                loadMoreEnd = true;
            }
            mExampListsBeans.addAll(netLoadMoreData);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setLoaded();
    }
}
