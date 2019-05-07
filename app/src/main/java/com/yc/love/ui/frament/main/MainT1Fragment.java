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
import com.yc.love.model.bean.BannerItemBean;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.JobBean;
import com.yc.love.model.bean.StringBean;
import com.yc.love.ui.activity.LoveHealActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
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
        isCanLoadData();
    }

    private void isCanLoadData() {
//        final LoadingDialog loadingView = new LoadingDialog(mMainActivity);
//        loadingView.showLoading();

        MainT1FragGank mainT1FragGank = new MainT1FragGank();
        RequestImpl request = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                FrontpageBean bean = (FrontpageBean) object;
                if (bean != null && bean.getResult() != null && bean.getResult().getFocus() != null && bean.getResult().getFocus().getResult() != null) {
                    final ArrayList<BannerItemBean> result = (ArrayList<BannerItemBean>) bean.getResult().getFocus().getResult();
                    ArrayList<String> mBannerImages = new ArrayList<String>();
//                    LinearLayout ll = rootView.findViewById(R.id.main_t1_ll);
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            //获取所有图片
                            mBannerImages.add(result.get(i).getRandpic());
                            Log.d("mylog", i + " MainT1Fragment loadSuccess: result.get(i) " + result.get(i).getRandpic());
                            ImageView imageView = new ImageView(mMainActivity);
                            imageView.setImageDrawable(mMainActivity.getResources().getDrawable(R.mipmap.tab_home));
//                            Glide.with(mMainActivity).load(result.get(i).getRandpic()).into(imageView);
//                            Picasso.with(mMainActivity).load(result.get(i).getRandpic())
//                                    .into(mBannerImagess.get(i));

                            TextView textView = new TextView(mMainActivity);
                            textView.setText(mBannerImages.get(i));
//                            ll.addView(textView);
                        }
//                        tvName.setText(getClass().getName());
//                        loadingView.dismissLoading();
                       /* maCache.remove(Constants.BANNER_PIC);
                        maCache.put(Constants.BANNER_PIC, mBannerImages);
                        maCache.remove(Constants.BANNER_PIC_DATA);
                        maCache.put(Constants.BANNER_PIC_DATA, result);
                        bannerDataBean.setData(mBannerImages, result);

                        bannerData.setValue(bannerDataBean);*/
                    }
                }
            }

            @Override
            public void loadFailed(Throwable throwable) {
                Log.d("mylog", "MainT1Fragment loadFailed: throwable "+throwable);
//                loadingView.dismissLoading();
            }


            @Override
            public void addSubscription(Disposable subscription) {
                Log.d("mylog", "MainT1Fragment addSubscription: " + subscription.toString());
                subscription.isDisposed();
            }
        };

        mainT1FragGank.showData(request);


        mainT1FragGank.showJobData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                JobBean jobBean = (JobBean) object;
                jobBean.toString();

                Log.d("mylog", "loadSuccess: jobBean.toString() " + jobBean.toString());
            }

            @Override
            public void loadFailed(Throwable throwable) {
                Log.d("mylog", "loadFailed: showJobData throwable "+throwable);
            }


            @Override
            public void addSubscription(Disposable subscription) {
                Log.d("mylog", "addSubscription: subscription " + subscription.toString());
            }
        });
    }


}
