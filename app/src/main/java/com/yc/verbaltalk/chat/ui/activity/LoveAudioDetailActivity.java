package com.yc.verbaltalk.chat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.manager.MusicPlayerManager;
import com.music.player.lib.mode.PlayerStatus;
import com.music.player.lib.view.MusicPlayerController;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.StatusBarUtil;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.MyScrollView;
import com.yc.verbaltalk.model.util.SizeUtils;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ScreenUtil;


/**
 * Created by admin on 2018/1/26.
 */

public class LoveAudioDetailActivity extends BaseSameActivity implements OnUserPlayerEventListener {


    private MusicPlayerController mMusicPlayerController;

    private WebView mWebView;
    private String typeId;
    private LoveEngine loveEngin;
    private MyScrollView nestedScrollView;
    private LinearLayout topContainer;
    private boolean isToTop = false;
    private int mScaledTouchSlop;//滑动的最小距离
    private int containerTop;
    private int nestTop;
    private LinearLayout llGetWeChat;
    private LinearLayout llCollect;
    private ImageView ivCollect;
    private TextView tvCollect;
    private View bottomView;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);

        invadeStatusBar();
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变
        init();
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    }

    @Override
    protected String offerActivityTitle() {
        return getString(R.string.audio);
    }


    public void init() {

        loveEngin = new LoveEngine(this);
        Intent intent = getIntent();
        if (intent != null) {
            typeId = intent.getStringExtra("type_id");

        }
        mMusicPlayerController = findViewById(R.id.music_player_controller);
        mWebView = findViewById(R.id.tv_desc);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        topContainer = findViewById(R.id.top_container);
        llGetWeChat = findViewById(R.id.ll_get_wechat);
        llCollect = findViewById(R.id.ll_collect);
        ivCollect = findViewById(R.id.iv_collect);
        tvCollect = findViewById(R.id.tv_collect_count);
        bottomView = findViewById(R.id.rlBottom);
        llContainer = findViewById(R.id.ll_container);


        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));


        topContainer.post(() -> {
            containerTop = topContainer.getTop();
            nestTop = nestedScrollView.getTop();

            Log.e("TAG", "init: " + containerTop + "---" + nestTop);
        });


        MobclickAgent.onEvent(this, "audio_frequency_id", "音频播放");

        getData();
        initViews();
        initAdapter();
        initListener();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ConstraintLayout.MarginLayoutParams layoutParams = (ConstraintLayout.LayoutParams) bottomView.getLayoutParams();
        LinearLayout.LayoutParams webLayoutParams = (LinearLayout.LayoutParams) nestedScrollView.getLayoutParams();
        int bottomHeight = 0;
        int webBottomHeight = 0;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottomHeight = StatusBarUtil.getNavigationBarHeight(this);
            webBottomHeight = ScreenUtil.dip2px(this, 50f);
        }
        layoutParams.bottomMargin = bottomHeight;
        bottomView.setLayoutParams(layoutParams);

        webLayoutParams.bottomMargin = webBottomHeight;

        llContainer.setLayoutParams(webLayoutParams);


    }

    private int startY;

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        nestedScrollView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = (int) event.getRawY();
                    break;

                case MotionEvent.ACTION_UP:
                    int deltaY = (int) (event.getRawY() - startY);
//                    startY = (int) event.getY();
                    if (Math.abs(deltaY) >= mScaledTouchSlop && isToTop) {
                        forwardTop();
                    } else if (Math.abs(deltaY) >= mScaledTouchSlop && !isToTop) {
                        forwardTopBottom();
                    }
//                    Log.e("TAG", "initListener: " + deltaY + "--startY--" + startY + "--endY--" + event.getRawY());

                    break;
            }
            return false;
        });
        nestedScrollView.setOnScrollListener((l, t, oldl, oldt) -> {
//            Log.e("TAG", "initListener: " + t + "--oldt--" + oldt);
            if (t > oldt) {
                //向上滑
                isToTop = true;

            } else {
                //向下滑

                isToTop = false;
            }

        });

        llGetWeChat.setOnClickListener(this);
        llCollect.setOnClickListener(this);

    }

    private void forwardTopBottom() {
        topContainer.setVisibility(View.VISIBLE);
//        nestedScrollView.smoothScrollTo(0, nestTop);

    }

    private void forwardTop() {
        topContainer.setVisibility(View.GONE);
//        nestedScrollView.smoothScrollTo(0, containerTop);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_get_wechat:
                showToWxServiceDialog("audio", null);
                break;
            case R.id.ll_collect:
                if (UserInfoHelper.isLogin(this))
                    collectAudio(musicInfo);
                break;
        }
    }

    /**
     * 初始设置示例
     */
    private void initViews() {
        //设置播放器样式，不设置默认首页样式，这里以黑色为例

        //调用此方法目的在于当播放列表为空，会回调至持有播放控制器的所有UI组件，设置Type就是标识UI组件的身份，用来判断是是否处理 回调方法事件autoStartNewPlayTasks()，
        //参数可自定义，需要和回调的autoStartNewPlayTasks（type）对应,
//        mMusicPlayerController.setUIComponentType(Constants.UI_TYPE_DETAILS);

        //设置闹钟最大定时时间
        mMusicPlayerController.setAlarmSeekBarProgressMax(1000);
        //设置闹钟初始的定时时间
        mMusicPlayerController.setAlarmSeekBarProgress(60);
        //是否点赞,默认false
//        mMusicPlayerController.setVisivable(false);
//        mMusicPlayerController.changeSeekbarTextColor(ContextCompat.getColor(this, R.color.user_name_color));

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

                    }
                    boolean isCollect = info.getIs_favorite() == 1;
                    mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect, id);
                    setWechatStatus(isCollect);
                }
                musicInfo = info;
                setSleepDetailInfo(info);
            }
        });
        //注册到被观察者中
        MusicPlayerManager.getInstance().addObservable(mMusicPlayerController);
//        //注册播放变化监听
        MusicPlayerManager.getInstance().addPlayerStateListener(this);
        MusicPlayerManager.getInstance().onResumeChecked();//先让播放器刷新起来

    }


    private void randomSpaInfo(String type_id) {
        loveEngin.randomSpaInfo(type_id).subscribe(new DisposableObserver<ResultInfo<List<MusicInfo>>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<MusicInfo>> musicInfoResultInfo) {
                if (musicInfoResultInfo != null && musicInfoResultInfo.code == HttpConfig.STATUS_OK && musicInfoResultInfo.data != null) {
//                    showSpaDetailInfo(musicInfoResultInfo.data, true);
                }
            }
        });

    }

    /**
     * 配合播放列表示例
     */
    private void initAdapter() {

        //注册观察者以刷新列表
    }

    private void collectAudio(final MusicInfo musicInfo) {

        loveEngin.collectAudio(UserInfoHelper.getUid(), musicInfo.getId()).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

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
                    showCollectSuccess(isCollect);
                    setWechatStatus(isCollect);
                }
            }
        });
    }


    private void setWechatStatus(boolean isFavorate) {
        String count = tvCollect.getText().toString().trim();
        if (isFavorate) {
            ivCollect.setImageResource(R.mipmap.search_knack_collected_icon);
            int countInt = Integer.parseInt(count);
            tvCollect.setText(String.valueOf(++countInt));
        } else {
            ivCollect.setImageResource(R.mipmap.search_knack_collect_icon);
            int countInt = Integer.parseInt(count);
            tvCollect.setText(String.valueOf(--countInt));
        }
    }

    private void setPlayCont(String musicId) {
        loveEngin.audioPlay(musicId).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
    }

    /**
     * 在这里响应当播放器列表为空 是否播放新的歌曲事件
     *
     * @param viewTupe UI组件身份ID
     * @param position
     */
    @Override
    public void autoStartNewPlayTasks(int viewTupe, int position) {

        if (Constants.UI_TYPE_DETAILS == viewTupe) {

            MusicPlayerManager.getInstance().playMusic(musicInfo);//这个position默认是0，由控制器传出
        }
    }


    public void getData() {


        loveEngin.getMusicDetailInfo(UserInfoHelper.getUid(), typeId).subscribe(new DisposableObserver<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> musicInfoWrapperResultInfo) {
                if (musicInfoWrapperResultInfo != null && musicInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && musicInfoWrapperResultInfo.data != null)
                    showSpaDetaiInfo(musicInfoWrapperResultInfo.data.getInfo());
            }
        });
    }


    private void showSpaDetaiInfo(MusicInfo musicInfo) {
        if (musicInfo != null) {
            showCollectSuccess(musicInfo.getIs_favorite() == 1);
            MusicPlayerManager.getInstance().playMusic(musicInfo);
            setSleepDetailInfo(musicInfo);
        }
//        MusicPlayerManager.getInstance().onResumeChecked();//在刷新之后检查，防止列表为空，无法全局同步
    }


    private MusicInfo musicInfo;


    public void showCollectSuccess(boolean isCollect) {
        mMusicPlayerController.setCollectIcon(isCollect ? R.drawable.ic_player_collect_true : R.drawable.ic_player_collect, isCollect);
    }


    private void initWebView(MusicInfo musicInfo) {


        mWebView.setClickable(true);
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDefaultTextEncodingName("gb2312");
        settings.setSaveFormData(true);
        settings.setDomStorageEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);


        mWebView.addJavascriptInterface(new MyJavaScript(), "APP");

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                Log.e(TAG, "onPageFinished: ");

//                document.body.getBoundingClientRect().height
                mWebView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())");

            }
        });
        mWebView.loadDataWithBaseURL(null, musicInfo.getContent(), "text/html", "utf-8", null);


    }

    private static final String TAG = "LoveAudioDetailActivity";

    private void setSleepDetailInfo(MusicInfo musicInfo) {
        initWebView(musicInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("TAG", "onDestroy: ");
        //必须注销所有已注册的监听
//        MusicPlayerManager.getInstance().detelePlayerStateListener(this);
        if (null != mMusicPlayerController) {
            MusicPlayerManager.getInstance().deleteObserver(mMusicPlayerController);
            mMusicPlayerController.onDestroy();
        }
        destroyWebView();
    }

    public void destroyWebView() {

        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
//            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now mWebView.freeMemory(); mWebView.pauseTimers(); mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing } }
            ((ViewGroup) mWebView.getParent()).removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }

    }


    public class MyJavaScript {
        @JavascriptInterface
        public void resize(float height) {
            Log.e(TAG, "resize: " + height);
            runOnUiThread(() -> {

                MyScrollView.LayoutParams layoutParams = new MyScrollView.LayoutParams(getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(LoveAudioDetailActivity.this, 12f), (int) (height * getResources().getDisplayMetrics().density));
                layoutParams.leftMargin = SizeUtils.dp2px(LoveAudioDetailActivity.this, 8f);
//                layoutParams.rightMargin = SizeUtils.dp2px(LoveAudioDetailActivity.this, 12f);
                layoutParams.topMargin = SizeUtils.dp2px(LoveAudioDetailActivity.this, 5f);
                mWebView.setLayoutParams(layoutParams);
            });
        }

    }

}