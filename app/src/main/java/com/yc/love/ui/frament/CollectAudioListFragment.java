package com.yc.love.ui.frament;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.music.player.lib.manager.MusicPlayerManager;
import com.yc.love.R;
import com.yc.love.adaper.rv.AudioDetailAdapter;
import com.yc.love.model.AudioItemInfo;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.LoveAudioDetailActivity;
import com.yc.love.ui.frament.base.BaseCollectFragment;
import com.yc.love.ui.view.LoadDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Subscriber;

/**
 * Created by wanglin  on 2019/7/17 17:56.
 */
public class CollectAudioListFragment extends BaseCollectFragment {

    private LoveEngin mLoveEngin;
    private LoadDialog mLoadingDialog;
    RecyclerView audioListRecyclerView;
    private AudioDetailAdapter audioDetailAdapter;
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
        mLoveEngin = new LoveEngin(mCollectActivity);
        handler = new Handler();
        mLoadingDialog = mCollectActivity.mLoadingDialog;
        initRecyclerView();
    }



    private void initRecyclerView() {
        audioListRecyclerView = rootView.findViewById(R.id.fragment_collect_love_healing_rv);
        emptyView = rootView.findViewById(R.id.top_empty_view);
        audioListRecyclerView.setLayoutManager(new LinearLayoutManager(mCollectActivity));
        audioDetailAdapter = new AudioDetailAdapter(null);
        audioListRecyclerView.setAdapter(audioDetailAdapter);
        audioListRecyclerView.setHasFixedSize(true);
        initListener();
    }

    private void initListener() {
        audioDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MusicInfo musicInfo = audioDetailAdapter.getItem(position);

                if (musicInfo != null) {
//                        AudioItemInfo audioItemInfo = item.getFirst().get(pos);
                    Intent intent = new Intent(mCollectActivity, LoveAudioDetailActivity.class);

                    intent.putExtra("spa_id", musicInfo.getId());
                    intent.putExtra("type_id", musicInfo.getId());
                    mCollectActivity.startActivity(intent);

                }
            }
        });
        audioDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, audioListRecyclerView);

    }

    @Override
    protected void lazyLoad() {
        getData();
    }

    public void getData() {
        final int id = YcSingle.getInstance().id;
        if (id <= 0) {
            mCollectActivity.showToLoginDialog();
            return;
        }
        mLoadingDialog.showLoadingDialog();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoveEngin.getCollectAudioList(id + "", page, pageSize).subscribe(new Subscriber<ResultInfo<MusicInfoWrapper>>() {
                    @Override
                    public void onCompleted() {

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
                });
            }
        }, 800);

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
