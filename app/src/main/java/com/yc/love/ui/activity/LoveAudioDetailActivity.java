package com.yc.love.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerSetyle;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.view.MusicPlayerController;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.AudioDetailAdapter;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.activity.base.BaseSlidingActivity;

import java.util.List;

import rx.Subscriber;


/**
 * Created by admin on 2018/1/26.
 */

public class LoveAudioDetailActivity extends BaseSlidingActivity implements OnUserPlayerEventListener {


    private MusicPlayerController mMusicPlayerController;

    private RecyclerView recyclerView;


    private AudioDetailAdapter audioDetailAdapter;
    private String spaId = "";
    private TextView tvWordDes;
    private ImageView ivUserHead;
    private TextView tvAutor;
    private TextView tvAutorDes;
    private int page = 1;
    private String typeId;
    private int limit = 10;
    private int homePage = 1;//起始页

    private LoveEngin loveEngin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);

        invadeStatusBar();
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
        init();
    }


    public void init() {

        loveEngin = new LoveEngin(this);
        Intent intent = getIntent();
        if (intent != null) {
            spaId = intent.getStringExtra("spa_id");
            homePage = page;
            LogUtil.msg("TAG: " + page + " spaId: " + spaId);
            typeId = intent.getStringExtra("type_id");
        }

        mMusicPlayerController = findViewById(R.id.music_player_controller);
        recyclerView = findViewById(R.id.recycler_view);
        MobclickAgent.onEvent(this,"audio_frequency_id","音频播放");

        getData();
        initViews();
        initAdapter();
        initListener();

    }

    private void initListener() {
        audioDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, recyclerView);
    }

    /**
     * 初始设置示例
     */
    private void initViews() {
        //设置播放器样式，不设置默认首页样式，这里以黑色为例
        mMusicPlayerController.setPlayerStyle(PlayerSetyle.PLAYER_STYLE_BLACK);
        //调用此方法目的在于当播放列表为空，会回调至持有播放控制器的所有UI组件，设置Type就是标识UI组件的身份，用来判断是是否处理 回调方法事件autoStartNewPlayTasks()，
        //参数可自定义，需要和回调的autoStartNewPlayTasks（type）对应,
        mMusicPlayerController.setUIComponentType(Constants.UI_TYPE_DETAILS);
        //设置是否显示返回按钮
        mMusicPlayerController.setBackButtonVisibility(true);
        //设置闹钟最大定时时间
//        mMusicPlayerController.setAlarmSeekBarProgressMax(1000);
        //设置闹钟初始的定时时间
//        mMusicPlayerController.setAlarmSeekBarProgress(60);
        //是否点赞,默认false
//        mMusicPlayerController.setVisivable(false);
        mMusicPlayerController.changeSeekbarTextColor(ContextCompat.getColor(this, R.color.user_name_color));

        //注册事件回调
        mMusicPlayerController.setOnClickEventListener(new MusicPlayerController.OnClickEventListener() {
            //收藏事件触发了
            @Override
            public void onEventCollect(MusicInfo musicInfo) {
//                mPresenter.collectSpa(musicInfo);
                collectAudio(musicInfo);

            }

            //随机播放触发了
            @Override
            public void onEventRandomPlay() {
                //其他界面使用播放控制器示例
//                if (!APP.getInstance().isGotoLogin(SleepDetailActivity.this)) {
//                    mPresenter.randomSpaInfo(typeId);
//                }
                randomSpaInfo(typeId);
            }

            //返回事件
            @Override
            public void onBack() {
                LoveAudioDetailActivity.this.onBackPressed();
            }

            @Override
            public void onPlayState(MusicInfo info) {
                String id = "";
                if (info != null) {
                    id = info.getId();
                    if (PlayerStatus.PLAYER_STATUS_PLAYING == info.getPlauStatus()) {
                        setPlayCont(id);
//                        collectAudio(id);

                    }
                    boolean isCollect = info.getIs_favorite() == 1;
                    mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect, id);
                }
                musicInfo = info;
                setSleepDetailInfo();
            }
        });
        //注册到被观察者中
        MusicPlayerManager.getInstance().addObservable(mMusicPlayerController);
//        //注册播放变化监听
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
//        MusicPlayerManager.getInstance().onResumeChecked();//先让播放器刷新起来
    }


    private void randomSpaInfo(String type_id) {
        loveEngin.randomSpaInfo(type_id).subscribe(new Subscriber<ResultInfo<List<MusicInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<MusicInfo>> musicInfoResultInfo) {
                if (musicInfoResultInfo != null && musicInfoResultInfo.code == HttpConfig.STATUS_OK && musicInfoResultInfo.data != null) {
                    showSpaDetailInfo(musicInfoResultInfo.data, true);
                }
            }
        });

    }

    /**
     * 配合播放列表示例
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(LoveAudioDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        //如列表需要实时更新播放动态的话，Adapter需要实现Observer接口
        audioDetailAdapter = new AudioDetailAdapter(null);
        audioDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicPlayerManager.getInstance().playPauseMusic(audioDetailAdapter.getData(), position);
            }
        });
        recyclerView.setAdapter(audioDetailAdapter);
        //注册观察者以刷新列表
        MusicPlayerManager.getInstance().addObservable(audioDetailAdapter);

        View haedView = LayoutInflater.from(this).inflate(R.layout.audio_detail_head, null);
        tvWordDes = haedView.findViewById(R.id.tv_word_des);
        ivUserHead = haedView.findViewById(R.id.iv_user_head);
        tvAutor = haedView.findViewById(R.id.tv_autor);
        tvAutorDes = haedView.findViewById(R.id.tv_autor_des);

        audioDetailAdapter.addHeaderView(haedView);
    }

    private void collectAudio(final MusicInfo musicInfo) {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        loveEngin.collectAudio(id + "", musicInfo.getId()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                boolean isCollect = musicInfo.getIs_favorite() == 1;
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {

                    isCollect = !isCollect;
                    musicInfo.setIs_favorite(isCollect ? 1 : 0);
                    showCollectSucess(isCollect);
                }
            }
        });
    }

    private void setPlayCont(String musicId) {
        loveEngin.audioPlay(musicId).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //必须注销所有已注册的监听
//        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
        if (null != mMusicPlayerController) {
            MusicPlayerManager.getInstance().deleteObserver(mMusicPlayerController);
            mMusicPlayerController.onDestroy();
        }
        if (null != audioDetailAdapter) {
            MusicPlayerManager.getInstance().deleteObserver(audioDetailAdapter);
        }
    }

    //========================================播放器发生了变化========================================

    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {

    }

    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer) {
    }

    @Override
    public void changePlayModelResult(int playModel) {

    }

    @Override
    public void changeAlarmModelResult(int model) {

    }

    @Override
    public void onMusicPlayerConfig(MusicPlayerConfig musicPlayerConfig) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {

    }

    /**
     * 在这里响应当播放器列表为空 是否播放新的歌曲事件
     *
     * @param viewTupe UI组件身份ID
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {

        if (Constants.UI_TYPE_DETAILS == viewTupe && null != audioDetailAdapter) {

            MusicPlayerManager.getInstance().playMusic(musicInfo);//这个position默认是0，由控制器传出
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }


    public void getData() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
//        mPresenter.getSpaDetailList(typeId, page, homePage, limit, spaId);
        loveEngin.getLoveItemList(id + "", typeId, page, limit).subscribe(new Subscriber<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> musicInfoWrapperResultInfo) {
                if (musicInfoWrapperResultInfo != null && musicInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && musicInfoWrapperResultInfo.data != null)
                    showSpaDetailList(filterData(musicInfoWrapperResultInfo.data.getList(), spaId));
            }
        });
    }

    private List<MusicInfo> filterData(List<MusicInfo> list, String spaId) {

        MusicInfo currentInfo = null;
        if (list.size() > 0) {
            for (MusicInfo musicInfo : list) {
                musicInfo.setType(2);
                if (musicInfo.getId().equals(spaId)) {
                    currentInfo = musicInfo;
                    break;
                }
            }

            if (currentInfo != null) {
                list.remove(currentInfo);
                list.add(0, currentInfo);
            }
        }
        return list;

    }


    private MusicInfo musicInfo;


    public void showSpaDetailInfo(List<MusicInfo> data, boolean isRandom) {

        if (null != audioDetailAdapter && data != null && data.size() > 0) {
            musicInfo = data.get(0);
            showCollectSucess(musicInfo.getIs_favorite() == 1);

            setSleepDetailInfo();

            MusicPlayerManager.getInstance().playPauseMusic(data, 0);

        }
        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }


    public void showCollectSucess(boolean isCollect) {
        mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
    }


    public void showSpaDetailList(List<MusicInfo> list) {

        if (list != null && list.size() > 0) {
            if (page == homePage) {
                musicInfo = list.get(0);
                showCollectSucess(list.get(0).getIs_favorite() == 1);
                setSleepDetailInfo();
                MusicInfo mMusicInfo = MusicPlayerManager.getInstance().getCurrentMusicInfo();

                if (mMusicInfo == null || !TextUtils.equals(mMusicInfo.getId(), musicInfo.getId())) {
                    MusicPlayerManager.getInstance().playPauseMusic(list, 0);
                }
                audioDetailAdapter.setNewData(list);
            } else {
                audioDetailAdapter.addData(list);
            }

            if (list.size() == limit) {
                page++;
                audioDetailAdapter.loadMoreComplete();
            } else {
                audioDetailAdapter.loadMoreEnd();
            }
        }

        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }

    private void setSleepDetailInfo() {
        tvWordDes.setText(musicInfo.getDesp());
        tvAutor.setText(musicInfo.getAuthor_title());
        tvAutorDes.setText(musicInfo.getAuthor_desp());
        Glide.with(LoveAudioDetailActivity.this).load(musicInfo.getAuthor_img()).apply(new RequestOptions().error(R.mipmap.main_icon_default_head).circleCrop()).into(ivUserHead);
    }


}