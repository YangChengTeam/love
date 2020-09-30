package com.yc.verbaltalk.mine.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.ItemDecorationHelper;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.ui.activity.LoveAudioDetailActivity;
import com.yc.verbaltalk.mine.adapter.AudioMainAdapter;
import com.yc.verbaltalk.model.util.SizeUtils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by wanglin  on 2019/7/17 17:56.
 */
public class CollectAudioListFragment extends BaseCollectFragment {

    private LoveEngine mLoveEngin;
    private LoadDialog mLoadingDialog;
    private RecyclerView audioListRecyclerView;
    private AudioMainAdapter audioDetailAdapter;
    private int page = 1;
    private int pageSize = 10;
    private View emptyView;
    private Handler handler;

    @Override
    protected int setContentView() {
        return R.layout.fragment_collect_love_healing;
    }

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngine(mCollectActivity);
        handler = new Handler();
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        initRecyclerView();
    }


    private void initRecyclerView() {
        audioListRecyclerView = rootView.findViewById(R.id.fragment_collect_love_healing_rv);
        emptyView = rootView.findViewById(R.id.top_empty_view);
        audioListRecyclerView.setLayoutManager(new LinearLayoutManager(mCollectActivity));
        audioDetailAdapter = new AudioMainAdapter(null);
        audioListRecyclerView.setAdapter(audioDetailAdapter);
        audioListRecyclerView.setHasFixedSize(true);
        audioListRecyclerView.addItemDecoration(new ItemDecorationHelper(mCollectActivity, 10));


        int size = SizeUtils.dp2px(mCollectActivity, 10f);

        audioListRecyclerView.setPadding(size, size, size, size);

        initListener();
    }

    private void initListener() {
        audioDetailAdapter.setOnItemClickListener((adapter, view, position) -> {

            MusicInfo musicInfo = audioDetailAdapter.getItem(position);

            if (musicInfo != null) {
//                        AudioItemInfo audioItemInfo = item.getFirst().get(pos);
                Intent intent = new Intent(mCollectActivity, LoveAudioDetailActivity.class);

                intent.putExtra("spa_id", musicInfo.getId());
                intent.putExtra("type_id", musicInfo.getId());
                mCollectActivity.startActivity(intent);

            }
        });
        audioDetailAdapter.setOnLoadMoreListener(this::getData, audioListRecyclerView);

    }

    @Override
    protected void lazyLoad() {
        getData();
    }

    public void getData() {

        mLoadingDialog.showLoadingDialog();
        handler.postDelayed(() -> mLoveEngin.getCollectAudioList(UserInfoHelper.getUid() + "", page, pageSize).subscribe(new DisposableObserver<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                if (mLoadingDialog != null) mLoadingDialog.dismissLoadingDialog();
                if (page == 0) emptyView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> musicInfoWrapperResultInfo) {
                if (mLoadingDialog != null) mLoadingDialog.dismissLoadingDialog();

                if (musicInfoWrapperResultInfo != null && musicInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && musicInfoWrapperResultInfo.data != null) {
                    List<MusicInfo> list = musicInfoWrapperResultInfo.data.getList();
                    if (page == 1) {
                        if (list == null || list.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                    createNewData(list);
                }
            }
        }), 800);

    }

    private void createNewData(List<MusicInfo> list) {
        if (page == 1) {
            audioDetailAdapter.setNewData(list);
        } else {
            audioDetailAdapter.addData(list);
        }
        if (list != null && list.size() == pageSize) {
            audioDetailAdapter.loadMoreComplete();
            page++;
        } else {
            audioDetailAdapter.loadMoreEnd();
        }

    }
}
