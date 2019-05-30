package com.yc.love.ui.frament;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.LoveHealDetailsAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.EmptyViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealDetItemHolder;
import com.yc.love.adaper.rv.holder.LoveHealDetVipHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;
import com.yc.love.model.bean.OpenApkPkgInfo;
import com.yc.love.model.bean.SearchDialogueBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.model.util.PackageUtils;
import com.yc.love.ui.activity.BecomeVipActivity;
import com.yc.love.ui.activity.ShareActivity;
import com.yc.love.ui.frament.base.BaseLazyFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.OpenAkpDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class ShareFragment extends BaseLazyFragment {

    public ShareActivity mShareActivity;
    private RecyclerView mRecyclerView;
    private LoveEngin mLoveEngin;
    private int PAGE_SIZE = 10;
    private int PAGE_NUM = 1;
    private LoadDialog mLoadingDialog;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private LoveHealDetailsAdapter mAdapter;
    private List<LoveHealDetBean> mLoveHealDetBeans;
    private String keyword;
    private SwipeRefreshLayout mSwipeRefresh;


    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mShareActivity = (ShareActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_base_share;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mShareActivity);
        mSwipeRefresh = rootView.findViewById(R.id.base_share_swipe_refresh);
        mLoadingDialog = mShareActivity.mLoadingDialog;
        initRecyclerView();

        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                obtainWalletData();
                if (!TextUtils.isEmpty(keyword)) {
                   /* if (mLoveHealDetBeans != null) {
                        mLoveHealDetBeans.clear();
                        mAdapter.notifyDataSetChanged();
                    }*/
                    loadMoreEnd = false;
                    PAGE_NUM = 1;
                    netData(keyword);
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.base_share_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mShareActivity);
        mRecyclerView.setLayoutManager(layoutManager);


    }

    /**
     * @param keyword
     */
    public void netData(String keyword) {
        this.keyword = keyword;
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mShareActivity.showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.searchDialogue2(String.valueOf(id), keyword, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/search").subscribe(new MySubscriber<AResultInfo<SearchDialogueBean>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                int searchBuyVip = searchDialogueBean.search_buy_vip;
                if (1 == searchBuyVip) { //1 弹窗 0不弹
                    startActivity(new Intent(mShareActivity, BecomeVipActivity.class));
                    mShareActivity.childDisposeOnBack();
//                    notCanShart();
                    return;
                }
                mLoveHealDetBeans = searchDialogueBean.list;
                initRecyclerData();
            }

            @Override
            protected void onNetError(Throwable e) {
                mSwipeRefresh.setRefreshing(false);
            }

            @Override
            protected void onNetCompleted() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initRecyclerData() {
        mLoveHealDetBeans = addTitle(mLoveHealDetBeans);

        mAdapter = new LoveHealDetailsAdapter(mLoveHealDetBeans, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                LoveHealDetItemHolder loveHealDetItemHolder = new LoveHealDetItemHolder(mShareActivity, null, parent);
                loveHealDetItemHolder.setOnClickCopyListent(new LoveHealDetItemHolder.OnClickCopyListent() {
                    @Override
                    public void onClickCopy(LoveHealDetDetailsBean detailsBean) {
                        toCopy(detailsBean.content);
                    }
                });
                return loveHealDetItemHolder;
            }

            @Override
            protected RecyclerView.ViewHolder getPayVipHolder(ViewGroup parent) {
                return new LoveHealDetVipHolder(mShareActivity, recyclerViewItemListener, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getEmptyHolder(ViewGroup parent) {
                return new EmptyViewHolder(mShareActivity, parent, "此关键字无数据，请更换");
            }

            @Override
            protected RecyclerView.ViewHolder getBarViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_test_item_footer, parent, false);
                ProgressBarViewHolder progressBarViewHolder = new ProgressBarViewHolder(view);
                return progressBarViewHolder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        if (mLoveHealDetBeans.size() < PAGE_SIZE) {
            Log.d("ssss", "loadMoreData: data---end");
        } else {
            mAdapter.setOnMoreDataLoadListener(new LoveHealDetailsAdapter.OnLoadMoreDataListener() {
                @Override
                public void loadMoreData() {
                    if (loadDataEnd == false) {
                        return;
                    }
                    if (showProgressBar == false) {
                        //加入null值此时adapter会判断item的type
                        mLoveHealDetBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        netLoadMore();
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        if (mLoveHealDetBeans != null && mLoveHealDetBeans.size() > 0) {
                            mLoveHealDetBeans.remove(mLoveHealDetBeans.size() - 1);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    private void toCopy(String content) {
        ClipboardManager myClipboard = (ClipboardManager) mShareActivity.getSystemService(mShareActivity.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", content);
        myClipboard.setPrimaryClip(myClip);
        showOpenAkpDialog();
    }

    private void showOpenAkpDialog() {
        List<OpenApkPkgInfo> openApkPkgInfos = new ArrayList<>();
        OpenApkPkgInfo qq = new OpenApkPkgInfo(1, "", "QQ", mShareActivity.getResources().getDrawable(R.mipmap.icon_d_qq));
        OpenApkPkgInfo wx = new OpenApkPkgInfo(2, "", "微信", mShareActivity.getResources().getDrawable(R.mipmap.icon_d_wx));
        OpenApkPkgInfo mm = new OpenApkPkgInfo(3, "", "陌陌", mShareActivity.getResources().getDrawable(R.mipmap.icon_d_momo));
        OpenApkPkgInfo tt = new OpenApkPkgInfo(4, "", "探探", mShareActivity.getResources().getDrawable(R.mipmap.icon_d_tt));

        List<String> apkList = PackageUtils.getApkList(mShareActivity);
        for (int i = 0; i < apkList.size(); i++) {
            String apkPkgName = apkList.get(i);
            if ("com.tencent.mobileqq".equals(apkPkgName)) {
                qq.pkg = apkPkgName;
            } else if ("com.tencent.mm".equals(apkPkgName)) {
                wx.pkg = apkPkgName;
            } else if ("com.immomo.momo".equals(apkPkgName)) {
                mm.pkg = apkPkgName;
            } else if ("com.p1.mobile.putong".equals(apkPkgName)) {
                tt.pkg = apkPkgName;
            }
        }

        openApkPkgInfos.add(qq);
        openApkPkgInfos.add(wx);
        openApkPkgInfos.add(mm);
        openApkPkgInfos.add(tt);
        OpenAkpDialog openAkpDialog = new OpenAkpDialog(mShareActivity, openApkPkgInfos);
        openAkpDialog.show();
    }

    private List<LoveHealDetBean> addTitle(List<LoveHealDetBean> loveHealDetBeans) {
        for (LoveHealDetBean loveHealDetBean : loveHealDetBeans
                ) {
            List<LoveHealDetDetailsBean> details = loveHealDetBean.details;
            if (details == null || details.size() == 0) {
                details = loveHealDetBean.detail;
            }
            details.add(0, new LoveHealDetDetailsBean(0, loveHealDetBean.id, loveHealDetBean.chat_name, loveHealDetBean.ans_sex));
        }
        return loveHealDetBeans;
    }

    private void netLoadMore() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mShareActivity.showToLoginDialog();
            return;
        }
        mLoveEngin.searchDialogue2(String.valueOf(id), keyword, String.valueOf(++PAGE_NUM), String.valueOf(PAGE_SIZE), "Dialogue/search").subscribe(new MySubscriber<AResultInfo<SearchDialogueBean>>(mShareActivity) {


            @Override
            protected void onNetNext(AResultInfo<SearchDialogueBean> searchDialogueBeanAResultInfo) {
                SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                int searchBuyVip = searchDialogueBean.search_buy_vip;
                if (1 == searchBuyVip) { //1 弹窗 0不弹
                    PAGE_NUM = 1;
//                    startActivity(new Intent(mShareActivity, BecomeVipActivity.class));
                    return;
                }
//                mLoveHealDetBeans = searchDialogueBean.list;
                List<LoveHealDetBean> netLoadMoreData = searchDialogueBean.list;
                showProgressBar = false;
                mLoveHealDetBeans.remove(mLoveHealDetBeans.size() - 1);
                mAdapter.notifyDataSetChanged();
                if (netLoadMoreData.size() < PAGE_SIZE) {
                    loadMoreEnd = true;
                }
//                netLoadMoreData=addTitle(netLoadMoreData);
                mLoveHealDetBeans.addAll(addTitle(netLoadMoreData));
                mAdapter.notifyDataSetChanged();
                mAdapter.setLoaded();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            //TODO 购买VIP刷新数据
            startActivity(new Intent(mShareActivity, BecomeVipActivity.class));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    public void recoverData() {
        if (PAGE_NUM != 1) {
            PAGE_NUM = 1;
        }
    }

}

