package com.yc.verbaltalk.chat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.video.player.lib.manager.VideoPlayerManager;
import com.video.player.lib.manager.VideoWindowManager;
import com.video.player.lib.view.VideoPlayerTrackView;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.utils.StatusBarUtil;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.CommonWebView;
import com.yc.verbaltalk.model.util.SizeUtils;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;


/**
 * Created by suns  on 2019/11/18 11:45.
 */
public class ChatRecommendDetailActivity extends BaseSameActivity implements View.OnClickListener {

    private LinearLayout llBottom;
    private VideoPlayerTrackView videoPlayer;
    private String typeId;

    private CommonWebView webView;
    private TextView tvRecommendTitle;
    private TextView tvRecommendTutorName;
    private TextView tvRecommendPlayCount;
    private ImageView ivBack;
    private ImageView ivRecommendCollect;
    private MusicInfo mMusicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_recommend_detail);
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            typeId = intent.getStringExtra("type_id");
        }

        webView = findViewById(R.id.webView);
        llBottom = findViewById(R.id.ll_tutor_buy);
        videoPlayer = findViewById(R.id.videoPlayer);
        tvRecommendTitle = findViewById(R.id.tv_recommend_title);
        tvRecommendTutorName = findViewById(R.id.tv_recommend_tutor_name);
        tvRecommendPlayCount = findViewById(R.id.tv_recommend_play_count);
        ivBack = findViewById(R.id.iv_tutor_back);
        ivRecommendCollect = findViewById(R.id.iv_course_collect);

        getData();

        initListener();

    }

    private void initWebView(String data) {
        WebSettings settings = webView.getSettings();

        webView.addJavascriptInterface(new AndroidJavaScript(), "android");//设置js接口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局


        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())");
            }
        });

    }

    private void initListener() {
        llBottom.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivRecommendCollect.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_tutor_buy:
                showToWxServiceDialog(null);
                break;
            case R.id.iv_tutor_back:
                finish();
                break;
            case R.id.iv_course_collect:
                // TODO: 2019/11/18 收藏
                if (UserInfoHelper.isLogin(this))
                    collectAudio(mMusicInfo);
                break;
        }
    }

    private void setWechatStatus(boolean isFavorate) {

        if (isFavorate) {
            ivRecommendCollect.setImageResource(R.mipmap.icon_collection_sel);
        } else {
            ivRecommendCollect.setImageResource(R.mipmap.icon_collection);
        }
    }


    public void getData() {


        mLoveEngine.getMusicDetailInfo(UserInfoHelper.getUid() + "", typeId).subscribe(new DisposableObserver<ResultInfo<MusicInfoWrapper>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<MusicInfoWrapper> musicInfoWrapperResultInfo) {
                if (musicInfoWrapperResultInfo != null && musicInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && musicInfoWrapperResultInfo.data != null)
                    setData(musicInfoWrapperResultInfo.data.getInfo());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llBottom.getLayoutParams();
        LinearLayout.LayoutParams webLayoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();

        int bottom = 0;
        int webBottomHeight = 0;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this);
            webBottomHeight = StatusBarUtil.getNavigationBarHeight(this);
        }

        layoutParams.bottomMargin = bottom;
        llBottom.setLayoutParams(layoutParams);

        webLayoutParams.bottomMargin = webBottomHeight;
        webView.setLayoutParams(webLayoutParams);


    }

    @Override
    protected String offerActivityTitle() {
        return "";
    }

    @Override
    protected boolean hindActivityTitle() {
        return true;
    }

    @Override
    protected boolean hindActivityBar() {
        return true;
    }

    private void startPlayer(MusicInfo musicInfo) {
        Glide.with(this).load(musicInfo.getImg()).apply(new RequestOptions().error(R.mipmap.audio_pic)).into(videoPlayer.getCoverController().mVideoCover);
        videoPlayer.startPlayVideo(musicInfo.getFile(), musicInfo.getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.getInstance().onPause();
    }


    public void onBackPressed() {
        //尝试弹射返回
        if (VideoPlayerManager.getInstance().isBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().onDestroy();
        //如果你的Activity是MainActivity并且你开启过悬浮窗口播放器，则还需要对其释放
        VideoWindowManager.getInstance().onDestroy();
    }


    public void setData(MusicInfo data) {
        this.mMusicInfo = data;
        initWebView(data.getContent());
        tvRecommendTitle.setText(data.getTitle());
        tvRecommendTutorName.setText("导师：" + data.getAuthor_title());
        tvRecommendPlayCount.setText(data.getPlay_num() + "次播放");
        boolean isCollect = data.getIs_favorite() == 1;
        setWechatStatus(isCollect);
        startPlayer(data);

    }


    private void collectAudio(final MusicInfo musicInfo) {
        if (musicInfo == null) return;

        mLoveEngine.collectAudio(UserInfoHelper.getUid(), musicInfo.getId()).subscribe(new DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<String>>() {
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
                    setWechatStatus(isCollect);
                }
            }
        });
    }


    private class AndroidJavaScript {


        @JavascriptInterface
        public void resize(Float height) {
            //            Log.e("TAG", "resize: " + height);
            runOnUiThread(() -> {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(ChatRecommendDetailActivity.this, 10f), (int) (height * getResources().getDisplayMetrics().density));

                //                layoutParams.rightMargin = SizeUtils.dp2px(LoveCaseDetailActivity.this, 10f);
                webView.setLayoutParams(layoutParams);
            });
        }
    }
}
