package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.adaper.rv.MainT1MoreItemAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.ProgressBarViewHolder;
import com.yc.love.adaper.rv.holder.StringBeanViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.cont.gank.MainT1FragGank;
import com.yc.love.cont.http.RequestImpl;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.BannerItemBean;
import com.yc.love.model.bean.ExampleTsBean;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.JobBean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.model.bean.StringBean;
import com.yc.love.model.data.BackfillSingle;
import com.yc.love.ui.activity.LoveHealActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {
    private List<StringBean> stringBeans;
    private int PAGE_NUM = 10;
    private boolean loadMoreEnd;
    private boolean loadDataEnd;
    private boolean showProgressBar = false;
    private int num = 10;
    private MainT1MoreItemAdapter mAdapter;
    private ProgressBarViewHolder progressBarViewHolder;

    //    private TextView tvName;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t1;
    }

    @Override
    protected void initViews() {
//        tvName = rootView.findViewById(R.id.main_t1_tv_name);

        final RecyclerView recyclerView = rootView.findViewById(R.id.main_t1_rl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mMainActivity);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        stringBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringBeans.add(new StringBean("name " + i));
        }

        mAdapter = new MainT1MoreItemAdapter<StringBean>(stringBeans, recyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new StringBeanViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                TitleT1ViewHolder titleT1ViewHolder = new TitleT1ViewHolder(mMainActivity, null, parent);
                titleT1ViewHolder.setOnClickShareListent(new TitleT1ViewHolder.OnClickMainT1TitleListent() {
                    @Override
                    public void clickShareListent() {
                        Toast.makeText(mMainActivity, "clickShare", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void clickIvModule02Listent() {
                        startActivity(new Intent(mMainActivity,LoveHealActivity.class));

                    }

                    @Override
                    public void clickIvModule03Listent() {
                        startActivity(new Intent(mMainActivity,LoveHealingActivity.class));
//                        Toast.makeText(mMainActivity, "clickIvModule03", Toast.LENGTH_SHORT).show();
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
        recyclerView.setAdapter(mAdapter);
        if (stringBeans.size() < PAGE_NUM) {
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
                        stringBeans.add(null);
                        mAdapter.notifyDataSetChanged();
                        showProgressBar = true;
                    }
                    if (!loadMoreEnd) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (num >= 41) {
                                    progressBarViewHolder.removePbChangDes("已加载全部数据");
                                    return;
                                }

                                List<StringBean> netLoadMoreData = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    netLoadMoreData.add(new StringBean("name " + (i + num)));
                                }
                                num += 10;
                                showProgressBar = false;
                                stringBeans.remove(stringBeans.size() - 1);
                                mAdapter.notifyDataSetChanged();
                                if (netLoadMoreData.size() < PAGE_NUM) {
                                    loadMoreEnd = true;
                                }
                                stringBeans.addAll(netLoadMoreData);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }
                        }, 1000);
                    } else {
                        Log.d("mylog", "loadMoreData: loadMoreEnd end 已加载全部数据 ");
                        stringBeans.remove(stringBeans.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        loadDataEnd = true;
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mMainActivity, "onItemClick " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };


    @Override
    protected void lazyLoad() {
        /*tvName.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);*/
        netData();
    }

    private void netData() {
        /*LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.exampleTs("example/ts").subscribe(new MySubscriber<AResultInfo<List<ExampleTsBean>>>(loadDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<ExampleTsBean>> listAResultInfo) {
                mDatas = new ArrayList<>();
                mDatas.add(new MainT3Bean(1));
                mDatas.add(new MainT3Bean(2, "入门秘籍"));
                List<ExampleTsBean> exampleTsBeans = listAResultInfo.data;
                for (ExampleTsBean exampleTsBean : exampleTsBeans
                        ) {
                    List<ExampleTsListBean> list1 = exampleTsBean.list1;
                    for (int i = 0; i < list1.size(); i++) {
                        ExampleTsListBean listBean = list1.get(i);
                        mDatas.add(new MainT3Bean(3, listBean.id, listBean.post_title, listBean.image, listBean.tag, listBean.tag_id, listBean.category_name));
                    }
                    mDatas.add(new MainT3Bean(2, "进阶秘籍"));
                    List<ExampleTsListBean> list2 = exampleTsBean.list2;
                    for (int i = 0; i < list2.size(); i++) {
                        ExampleTsListBean listBean = list2.get(i);
                        mDatas.add(new MainT3Bean(3, listBean.id, listBean.post_title, listBean.image, listBean.tag, listBean.tag_id, listBean.category_name));
                    }
                }
                initRecyclerViewData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });*/
    }




}
