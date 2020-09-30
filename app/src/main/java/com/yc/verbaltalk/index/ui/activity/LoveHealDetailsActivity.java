package com.yc.verbaltalk.index.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.event.EventPayVipSuccess;
import com.yc.verbaltalk.index.adapter.LoveHealDetailsAdapter;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * 首页话术详情页
 */
public class LoveHealDetailsActivity extends BaseSameActivity {

    private String mTitle;
    private String mCategoryId;
    private LoveEngine mLoveEngine;
    private LoveHealDetailsAdapter loveHealDetailsAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private int PAGE_SIZE = 8;
    private int PAGE_NUM = 1;
    private List<LoveHealDetBean> mLoveHealDetBeans;
    private SearchView share_searchView;
    private String shareTextString;
    private TextView tv_search_btn;
    private ImageView ivToTop;

    private LoadDialog mLoadDialogInfo;
    private boolean isSearch;//是否搜索

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mCategoryId = intent.getStringExtra("category_id");
    }

    public static void startLoveHealDetailsActivity(Context context, String title, String categoryId) {
        Intent intent = new Intent(context, LoveHealDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("category_id", categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_heal_details);
        mLoveEngine = new LoveEngine(this);
        initViews();
        initData();
        initListener();
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

    private void initData() {
        netData();
    }


    private void initViews() {
        ImageView ivToWx = findViewById(R.id.love_heal_details_iv_to_wx);
        share_searchView = findViewById(R.id.share_searchView);
        tv_search_btn = findViewById(R.id.tv_search_btn);
        ivToTop = findViewById(R.id.love_heal_details_iv_to_top);
        ivToWx.setOnClickListener(this);
        initRecyclerView();
        initSearchView();
    }

    private void initSearchView() {
        //修改键入的文字字体大小、颜色和hint的字体颜色
        EditText editText = share_searchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.size_14));
        //        editText.setTextColor(ContextCompat.getColor(this,R.color.nb_text_primary));

        //监听关闭按钮点击事件
        ImageView mCloseButton = share_searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        TextView textView = share_searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (mCloseButton.isClickable()) {
            mCloseButton.setOnClickListener(v -> {
                //清除搜索框并加载默认数据
                textView.setText("");
            });
        }
        share_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//搜索按钮回调
                PAGE_NUM = 1;

                searchKeyWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//输入变化回调
                LoveHealDetailsActivity.this.shareTextString = newText;
                return false;
            }

        });
    }

    private void searchKeyWord(String query) {
        if (mLoadDialogInfo == null) {
            mLoadDialogInfo = new LoadDialog(this);
        }
        mLoadDialogInfo.showLoadingDialog();
        mLoveEngine.searchDialogue2(UserInfoHelper.getUid(), query, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
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
                        mLoadDialogInfo.dismissLoadingDialog();
                        isSearch = true;
                        if (searchDialogueBeanAResultInfo != null && searchDialogueBeanAResultInfo.code == HttpConfig.STATUS_OK && searchDialogueBeanAResultInfo.data != null) {
                            SearchDialogueBean searchDialogueBean = searchDialogueBeanAResultInfo.data;
                            int searchBuyVip = searchDialogueBean.search_buy_vip;
                            if (1 == searchBuyVip) { //1 弹窗 0不弹
                                startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
                            } else {
                                createNewData(searchDialogueBean.list);
                            }
                        }
                    }


                });

    }

    private void initListener() {
        mSwipeRefresh.setColorSchemeResources(R.color.red_crimson);
        mSwipeRefresh.setOnRefreshListener(() -> {


            PAGE_NUM = 1;
            if (isSearch) {
                searchKeyWord(this.shareTextString);
            } else {
                netData();
            }
        });
        loveHealDetailsAdapter.setOnLoadMoreListener(this::netData, mRecyclerView);

        loveHealDetailsAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoveHealDetBean loveHealDetBean = loveHealDetailsAdapter.getItem(position);
            if (UserInfoHelper.isLogin(this)) {
                if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                    //TODO 购买VIP刷新数据

                    startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
            }
        });
        loveHealDetailsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            LoveHealDetBean loveHealDetBean = loveHealDetailsAdapter.getItem(position);
            if (UserInfoHelper.isLogin(this)) {
                if (loveHealDetBean != null && LoveHealDetBean.VIEW_VIP == loveHealDetBean.type)
                    //TODO 购买VIP刷新数据
                    startActivity(new Intent(LoveHealDetailsActivity.this, BecomeVipActivity.class));
            }
        });
        tv_search_btn.setOnClickListener(v -> {
            MobclickAgent.onEvent(this, "verbal_detail_search", "话术详情搜索");
            if (UserInfoHelper.isLogin(this)) {
                PAGE_NUM = 1;
                searchKeyWord(shareTextString);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);//dy>0 向下 dy<0 向上

                if (dy > 0 && PAGE_NUM > 2) {
                    ivToTop.setVisibility(View.VISIBLE);
                } else if (dy < 0) {
                    ivToTop.setVisibility(View.INVISIBLE);
                }
            }
        });
        ivToTop.setOnClickListener(v -> {
            mRecyclerView.smoothScrollToPosition(0);
            ivToTop.setVisibility(View.INVISIBLE);
            PAGE_NUM = 1;
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.love_heal_details_iv_to_wx) {
            showToWxServiceDialog(null);
        }
    }

    private void initRecyclerView() {

        mSwipeRefresh = findViewById(R.id.love_heal_details_swipe_refresh);
        mRecyclerView = findViewById(R.id.love_heal_details_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loveHealDetailsAdapter = new LoveHealDetailsAdapter(mLoveHealDetBeans, mTitle);
        mRecyclerView.setAdapter(loveHealDetailsAdapter);


    }

    private void netData() {

        if (PAGE_NUM == 1)
            mLoadingDialog.showLoadingDialog();
        mLoveEngine.loveListCategory(UserInfoHelper.getUid(), mCategoryId, String.valueOf(PAGE_NUM), String.valueOf(PAGE_SIZE))
                .subscribe(new DisposableObserver<ResultInfo<List<LoveHealDetBean>>>() {

                    @Override
                    public void onComplete() {
                        if (PAGE_NUM == 1 && mLoadingDialog != null) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (PAGE_NUM == 1 && mLoadingDialog != null) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onNext(ResultInfo<List<LoveHealDetBean>> listAResultInfo) {
                        if (PAGE_NUM == 1 && mLoadingDialog != null) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);

                        if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK && listAResultInfo.data != null) {
                            createNewData(listAResultInfo.data);
                        }
                    }

                });
    }

    private void createNewData(List<LoveHealDetBean> loveHealDetBeans) {

        if (loveHealDetBeans != null && loveHealDetBeans.size() > 0) {
            for (LoveHealDetBean loveHealDetBean : loveHealDetBeans) {
                if (loveHealDetBean.is_vip == 0) {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_ITEM;
                } else {
                    loveHealDetBean.type = LoveHealDetBean.VIEW_VIP;
                }

            }
        }
        mLoveHealDetBeans = loveHealDetBeans;
        if (PAGE_NUM == 1) {
            loveHealDetailsAdapter.setNewData(mLoveHealDetBeans);
        } else {
            loveHealDetailsAdapter.addData(mLoveHealDetBeans);
        }
        if (loveHealDetBeans != null && loveHealDetBeans.size() == PAGE_SIZE) {
            loveHealDetailsAdapter.loadMoreComplete();
            PAGE_NUM++;
        } else {
            loveHealDetailsAdapter.loadMoreEnd();
        }


    }

    @Override
    protected String offerActivityTitle() {
        return mTitle;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySuccess(EventPayVipSuccess eventPayVipSuccess) {
        netData();
    }


}
