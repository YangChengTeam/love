package com.yc.love.ui.frament;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.yc.love.R;
import com.yc.love.adaper.rv.AudioMainAdapter;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.AudioItemInfo;
import com.yc.love.model.bean.AudioDataInfo;
import com.yc.love.model.bean.AudioDataWrapperInfo;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.LoveAudioDetailActivity;
import com.yc.love.ui.frament.base.BaseLazyFragment;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadingDialog;

import java.util.List;

import rx.Subscriber;


/**
 * Created by wanglin  on 2018/1/10 17:18.
 */

public class AudioFragment extends BaseMainFragment {


    private RecyclerView recyclerViewSleep;


    private int itemPage = 1;

    private int pageSize = 10;

    private List<AudioDataInfo> dataTypes;
    private LoveEngin loveEngin;

    private SparseIntArray pages;
    private SparseBooleanArray booleanSparseArray;
    private AudioMainAdapter audioMainAdapter;
    private SparseBooleanArray booleanArray;//是否是首次点击
    private CacheWorker mCacheWorker;
    private LoadingDialog loadingDialog = null;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_audio;
    }

    @Override
    protected void initViews() {
        recyclerViewSleep = rootView.findViewById(R.id.recyclerView_sleep);

        init();
        initData();
    }

    @Override
    protected void lazyLoad() {

    }


    public void init() {

        loveEngin = new LoveEngin(getActivity());
        mCacheWorker = new CacheWorker();

        audioMainAdapter = new AudioMainAdapter(null);
        recyclerViewSleep.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSleep.setAdapter(audioMainAdapter);
        pages = new SparseIntArray();

        booleanSparseArray = new SparseBooleanArray();
        booleanArray = new SparseBooleanArray();
        recyclerViewSleep.addItemDecoration(new MyDecoration());
        initListener();

    }

    private void initData() {
        loadingDialog = new LoadingDialog(mMainActivity);
        loadingDialog.showLoading();
        List<AudioDataInfo> datas = (List<AudioDataInfo>) mCacheWorker.getCache(mMainActivity, "audio_main_data");
        if (datas != null) {
            createNewData(datas);
            if (loadingDialog != null) loadingDialog.dismissLoading();
        }


        loveEngin.getAudioDataInfo().subscribe(new Subscriber<ResultInfo<AudioDataWrapperInfo>>() {
            @Override
            public void onCompleted() {
                if (loadingDialog != null) loadingDialog.dismissLoading();
            }

            @Override
            public void onError(Throwable e) {
                if (loadingDialog != null) loadingDialog.dismissLoading();
            }

            @Override
            public void onNext(ResultInfo<AudioDataWrapperInfo> audioDataWrapperInfoResultInfo) {
                if (loadingDialog != null) loadingDialog.dismissLoading();
                if (audioDataWrapperInfoResultInfo != null && audioDataWrapperInfoResultInfo.code == HttpConfig.STATUS_OK && audioDataWrapperInfoResultInfo.data != null) {
                    List<AudioDataInfo> datas = audioDataWrapperInfoResultInfo.data.getList();
                    createNewData(datas);
                    mCacheWorker.setCache("audio_main_data", datas);
                }
            }
        });


    }


    private void createNewData(List<AudioDataInfo> datas) {
        dataTypes = datas;
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                pages.put(i, itemPage);
                booleanSparseArray.put(i, true);
                booleanArray.put(i, true);//是否是首次点击
            }
        }
        audioMainAdapter.setNewData(datas);
    }


    private void initListener() {

        audioMainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), LoveAudioDetailActivity.class);
                AudioDataInfo audioDataInfo = audioMainAdapter.getItem(position);
                if (audioDataInfo != null) {
                    List<AudioItemInfo> first = audioDataInfo.getFirst();
                    if (first != null && first.size() > 0) {
                        AudioItemInfo spaItemInfo = first.get(0);
                        intent.putExtra("type_id", spaItemInfo.getId());
                        intent.putExtra("spa_id", spaItemInfo.getId());
                        startActivity(intent);
                    }
                }

            }
        });
        audioMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                boolean isGroup = booleanSparseArray.get(position);
                if (isGroup) {

                    if (dataTypes != null && dataTypes.size() > 0 && booleanArray.get(position)) {
                        booleanArray.put(position, false);
//                        getAudioItemList(dataTypes.get(position).getId(), pages.get(position), pageSize, position);
                    }
                    audioMainAdapter.setVisable(true, position);

                } else {
                    pages.put(position, itemPage);
                    audioMainAdapter.setVisable(false, position);
                }
                isGroup = !isGroup;
                booleanSparseArray.put(position, isGroup);
            }
        });

        recyclerViewSleep.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = recyclerView.getChildAt(0);
                if (child.getTop() < 0) {
                    recyclerViewSleep.setPadding(recyclerViewSleep.getPaddingLeft(), 0, recyclerViewSleep.getPaddingRight(), 0);

                } else {
                    recyclerViewSleep.setPadding(recyclerViewSleep.getPaddingLeft(), ScreenUtil.dip2px(mMainActivity, 15), recyclerViewSleep.getPaddingRight(), 0);
                }
//                recyclerViewSleep.setLayoutParams(layoutParams);
            }
        });


    }


//    private void getAudioItemList(String typeId, final int page, int limit, final int position) {
//        loveEngin.getAudioItemList(typeId, page, limit).subscribe(new Subscriber<ResultInfo<List<AudioItemInfo>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(ResultInfo<List<AudioItemInfo>> listResultInfo) {
//                setTagList(page, listResultInfo.data, position);
//            }
//        });
//    }
//
//    private void setTagList(int page, List<AudioItemInfo> list, int position) {
//        if (list != null && list.size() > 0) {
//            for (AudioItemInfo spaItemInfo : list) {
//                spaItemInfo.setGroupPos(page);
//            }
//        }
//        showSpaItemList(list, position);
//    }
//
//
//    public void showSpaItemList(List<AudioItemInfo> itemInfos, int position) {
//
//        if (itemInfos != null) {
//
//            loadMore(itemInfos, position);
//        }
//
//    }
//
//
//    private void loadMore(List<AudioItemInfo> itemInfos, int position) {
//
//        int currentPage = pages.get(position);
//
//        if (itemInfos.size() > 0) {
//            //当前分类的页面
//            int nextPage = currentPage + 1;
//            pages.put(position, nextPage);
//            audioMainAdapter.getAdapter(position).loadMoreComplete();
//        } else {
//            audioMainAdapter.getAdapter(position).loadMoreEnd();
//        }
//
//        if (currentPage == 1) {
//            itemInfos.remove(0);
//            audioMainAdapter.getAdapter(position).setNewData(itemInfos);
//        } else {
//            audioMainAdapter.getAdapter(position).addData(itemInfos);
//        }
//
//
//    }


    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(mMainActivity, 10f));
        }


    }


}
