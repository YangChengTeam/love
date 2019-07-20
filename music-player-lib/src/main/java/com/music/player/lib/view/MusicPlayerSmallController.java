package com.music.player.lib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.R;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.util.Logger;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wanglin  on 2018/2/26 10:25.
 */

public class MusicPlayerSmallController extends FrameLayout implements Observer, OnUserPlayerEventListener {
    private static final String TAG = "MusicPlayerSmallControl";

    private ImageView ivCovorThumb;

    private MarqueeTextView tvMusicTitle;

    private TextView tvAuthor;

    private ImageView ivNextPlayer;

    private ImageView ivPlayPause;
    private MusicInfo musicInfo;
    private RequestOptions mOptions;
    private static int UI_COMPONENT_TYPE = Constants.UI_TYPE_HOME;


    public MusicPlayerSmallController(@NonNull Context context) {
        super(context);
        initView(context, null);

    }

    public MusicPlayerSmallController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        View.inflate(context, R.layout.view_music_small_player_controller, this);


        ivCovorThumb = findViewById(R.id.iv_covor_thumb);
        tvMusicTitle = findViewById(R.id.tv_music_title);
        tvAuthor = findViewById(R.id.tv_author);
        ivNextPlayer = findViewById(R.id.iv_next_player);
        ivPlayPause = findViewById(R.id.iv_play_pause);

        ivPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = MusicPlayerManager.getInstance().playPause();//暂停和播放
                if (!flag) {
                    //通知所有UI组件，自动开始新的播放
                    MusicPlayerManager.getInstance().autoStartNewPlayTasks(UI_COMPONENT_TYPE, 0);
                }
            }
        });

        ivNextPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerManager.getInstance().playNext();
            }
        });
        MusicPlayerManager.getInstance().addPlayerStateListener(this);

    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            musicInfo = ((MusicInfo) arg);
            switch (musicInfo.getPlauStatus()) {
                case PlayerStatus.PLAYER_STATUS_EMPOTY:
                    Logger.d(TAG, "播放为空");

                    break;
                case PlayerStatus.PLAYER_STATUS_ASYNCPREPARE:
                    Logger.d(TAG, "异步缓冲中");
                    if (null != tvMusicTitle) {

                        tvMusicTitle.setText(musicInfo.getTitle());
                        String author = musicInfo.getAuthor_title();
                        tvAuthor.setText(!TextUtils.isEmpty(author) ? author : "咩咩睡眠");

                    }

                    if (null != ivCovorThumb) {
                        if (null == mOptions) {
                            mOptions = new RequestOptions();
                            mOptions.error(R.drawable.ic_launcher)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存源资源和转换后的资源
                                    .skipMemoryCache(true);//跳过内存缓存
                        }
                        Glide.with(getContext()).load(musicInfo.getImg()).apply(mOptions).thumbnail(0.1f).into(ivCovorThumb);//音标
                    }
                    break;
                case PlayerStatus.PLAYER_STATUS_PLAYING:
                    Logger.d(TAG, "开始播放中");

                    setPlaying(true);

                    break;
                case PlayerStatus.PLAYER_STATUS_PAUSE:
                    Logger.d(TAG, "暂停了播放");
                    setPlaying(false);
                    break;
                case PlayerStatus.PLAYER_STATUS_STOP:
                    Logger.d(TAG, "结束、强行停止播放");
                    setPlaying(false);
                    if (null != tvMusicTitle) {
                        tvMusicTitle.setText("");
                        tvAuthor.setText("");
                    }
                    //封面
                    if (null != ivCovorThumb)
                        ivCovorThumb.setImageResource(R.drawable.ic_launcher);
                    break;
                case PlayerStatus.PLAYER_STATUS_ERROR:
                    Logger.d(TAG, "播放失败");
                    setPlaying(false);
                    break;


            }

        }
    }


    /**
     * 设置暂停、播放按钮
     *
     * @param flag
     */
    public void setPlaying(boolean flag) {
        if (null != ivPlayPause) {
            ivPlayPause.setImageResource(flag ? R.drawable.ic_player_pause : R.drawable.ic_player_play);
        }
    }

    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {

    }

    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer) {
        if (null != tvMusicTitle) tvMusicTitle.setText(musicInfo.getTitle());
        //封面
        if (null != ivCovorThumb) {
            if (MusicPlayerManager.getInstance().getCurrentMusicInfo() != null &&
                    !MusicPlayerManager.getInstance().getCurrentMusicInfo().getId().equals(musicInfo.getId())) {
                if (null == mOptions) {
                    mOptions = new RequestOptions();
                    mOptions.error(R.drawable.ic_launcher);
                    mOptions.diskCacheStrategy(DiskCacheStrategy.ALL);//缓存源资源和转换后的资源
                    mOptions.skipMemoryCache(true);//跳过内存缓存

                }
                Glide.with(getContext()).load(musicInfo.getImg()).apply(mOptions).thumbnail(0.1f).into(ivCovorThumb);
            }//音标
            setPlaying(2 == musicInfo.getPlauStatus());//是否正在播放
        }
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
        setPlaying(true);
    }

    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {

    }

    @Override
    public void taskRemmainTime(long durtion) {

    }

    @Override
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {

    }

    public void onDestroy() {
        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
    }

}
