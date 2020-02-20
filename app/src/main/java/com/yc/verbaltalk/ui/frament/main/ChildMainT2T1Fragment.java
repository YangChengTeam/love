package com.yc.verbaltalk.ui.frament.main;

import android.content.Intent;
import android.media.midi.MidiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.TypeReference;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.MainT2MoreItemAdapter;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.ExampDataBean;
import com.yc.verbaltalk.model.bean.ExampListsBean;
import com.yc.verbaltalk.model.bean.MainT2Bean;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.ui.activity.ConsultDetailActivity;
import com.yc.verbaltalk.ui.activity.ExampleDetailActivity;
import com.yc.verbaltalk.ui.frament.base.BaseMainT2ChildFragment;
import com.yc.verbaltalk.ui.view.LoadDialog;
import com.yc.verbaltalk.ui.view.imgs.Constant;
import com.yc.verbaltalk.utils.CommonInfoHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by mayn on 2019/6/18.
 */

//LoveCaseActivity
public class ChildMainT2T1Fragment extends BaseMainT2ChildFragment implements OnAdvStateListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private LoveEngine mLoveEngin;

    private List<MainT2Bean> mMainT2Beans;

    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;

    private MainT2MoreItemAdapter mAdapter;

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


        String brand = Build.BRAND;
        if (TextUtils.equals(brand, "huawei") || TextUtils.equals(brand, "Huawei")) {
            bottomContainer.setVisibility(View.GONE);
        } else {
            bottomContainer.setVisibility(View.VISIBLE);
            TTAdDispatchManager.getManager().init(mMainActivity, TTAdType.BANNER, bottomContainer, Constant.TOUTIAO_BANNER_ID, 0, null, 0, null, 0, this);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
//        RecyclerViewNoBugLinearLayoutManager layoutManager = new RecyclerViewNoBugLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainT2MoreItemAdapter(mMainT2Beans);
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
            MainT2Bean mainT2Bean = mAdapter.getItem(position);
            if (mainT2Bean != null) {
                if (MainT2Bean.VIEW_ITEM == mainT2Bean.type) {
                    ExampleDetailActivity.startExampleDetailActivity(mMainActivity, mainT2Bean.id, mainT2Bean.post_title);
                } else if (MainT2Bean.VIEW_TO_PAY_VIP == mainT2Bean.type || MainT2Bean.VIEW_VIP == mainT2Bean.type) {
                    startActivity(new Intent(mMainActivity, BecomeVipActivity.class));
                    MobclickAgent.onEvent(mMainActivity, "practice_id", "实战学习");
                }
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MainT2Bean item = mAdapter.getItem(position);
            if (item != null) {
                if (item.type == MainT2Bean.VIEW_TITLE && view.getId() == R.id.roundCornerImg_banner) {
                    startActivity(new Intent(mMainActivity, ConsultDetailActivity.class));
                }
            }

        });
    }


    @Override
    protected void lazyLoad() {
        netData(false);
    }


    private void netData(final boolean isRefresh) {

        CommonInfoHelper.getO(mMainActivity, "main2_example_lists", new TypeReference<List<MainT2Bean>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<MainT2Bean>>) o -> {
            mMainT2Beans = o;
            if (PAGE_NUM == 1 && mMainT2Beans != null && mMainT2Beans.size() > 0) {
                mAdapter.setNewData(mMainT2Beans);
            }
        });


        if (PAGE_NUM == 1 && !isRefresh) {
            mLoadingDialog = new LoadDialog(mMainActivity);
            mLoadingDialog.showLoadingDialog();
        }

        mLoveEngin.exampLists(String.valueOf(YcSingle.getInstance().id), String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "example/lists")
                .subscribe(new MySubscriber<AResultInfo<ExampDataBean>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<ExampDataBean> exampDataBeanAResultInfo) {
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        ExampDataBean exampDataBean = exampDataBeanAResultInfo.data;
                        createNewData(exampDataBean);
                    }

                    @Override
                    protected void onNetError(Throwable e) {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    protected void onNetCompleted() {
                        if (mSwipeRefresh.isRefreshing()) {
                            mSwipeRefresh.setRefreshing(false);
                        }
                        if (PAGE_NUM == 1 && !isRefresh) {
                            mLoadingDialog.dismissLoadingDialog();
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
            MainT2Bean mainT2Bean = new MainT2Bean("tit", MainT2Bean.VIEW_TITLE);
            mainT2Bean.imgId = R.mipmap.consult_banner;
            mMainT2Beans.add(mainT2Bean);
        }
        if (exampListsBeans != null && exampListsBeans.size() > 0) {

            for (int i = 0; i < exampListsBeans.size(); i++) {
                ExampListsBean exampListsBean = exampListsBeans.get(i);
                int type;
                if (PAGE_NUM == 1 && i < 3) {
                    type = MainT2Bean.VIEW_ITEM;
//                    mMainT2Beans.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
                } else {
                    type = exampDataBean.is_vip > 0 ? MainT2Bean.VIEW_ITEM : MainT2Bean.VIEW_TO_PAY_VIP;
                }


                mMainT2Beans.add(new MainT2Bean(type, exampListsBean.create_time, exampListsBean.id, exampListsBean.image, exampListsBean.post_title));
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
            mMainT2Beans.add(6, new MainT2Bean("vip", MainT2Bean.VIEW_VIP));
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TTAdDispatchManager.getManager().onDestroy();
    }
}
