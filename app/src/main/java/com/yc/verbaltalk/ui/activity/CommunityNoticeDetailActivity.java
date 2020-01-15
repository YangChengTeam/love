package com.yc.verbaltalk.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.CommunityInfo;
import com.yc.verbaltalk.model.bean.TopTopicInfo;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;
import com.yc.verbaltalk.utils.DateUtils;

import androidx.core.content.ContextCompat;
import rx.Subscriber;

/**
 * Created by suns  on 2019/8/28 17:36.
 */
public class CommunityNoticeDetailActivity extends BaseSameActivity {

    private TextView tvName;
    private TextView tvDate;
    private TextView tvContent;
    private ImageView ivIcon;
    private ImageView ivCommunityPic;
    private WebView mWebView;

    private String content = "<p>各位小伙伴大家好，为保障社区能够文明有序进行，特针对本社区制定如下相关规定，请认真查看并遵守，如违反规定将对发帖或账号进行封禁限制，希望大家配合。</p><p style='text-align:center;'><img src=\"http://ytx.wk2.com/static/ueditor/php/upload1//20190308/15520293621027.jpg\"/></p><p><strong>【发贴标准】</strong></p><p>发帖内容要以情感解惑、婚姻生活为主且内容健康、积极向上；</p><p>严禁发布宣传Q群、微信群、各种招人性质的帖子及评论；</p><p>严禁发布包含有色情，赌博，恐怖等内容的帖子及评论；</p><p>严禁发布包含盈利商业广告交易信息内容的帖子及评论；</p><p>严禁发布涉及挑衅、侮辱、威胁等人身攻击内容的帖子及评论；</p><p>严禁发布使用他人照片信息等侵害他人隐私内容的帖子及评论；</p><p><strong>【删帖标准】</strong></p><p>【非法类】：涉黄、涉黑、涉及国家政治等信息内容；</p><p>【隐私类】：利用他人照片等信息发布虚假内容；</p><p>【广告类】：任何广告、商业信息、交易帖、不安全网址。</p><p>【不文明】：恶意人身攻击、侮辱、诽谤他人。</p><p>【灌水、重复】：恶意灌水，无意义的发帖回帖行为、标题党、大量重复发贴、与本话题无关的帖子。</p><p>【头像、昵称、标题】：头像不雅、昵称含有广告、Q号宣传性内容。昵称、头像、标题不允许带联系方式、广告、涉黄、擦边、交易等信息。</p><p><strong>以上就是所有规则，如有改动将继续更新，最后祝大家在社区玩的愉快！</strong></p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_notice_detail);
        initView();
    }

    private void initView() {
        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        tvDate = findViewById(R.id.tv_date);
        tvContent = findViewById(R.id.tv_content);
        ivCommunityPic = findViewById(R.id.iv_community_pic);
        mWebView = findViewById(R.id.webview);
        getData();

    }

    private void getData() {
        mLoveEngine.getTopTopicInfos().subscribe(new Subscriber<ResultInfo<TopTopicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<TopTopicInfo> topTopicInfoResultInfo) {
                if (topTopicInfoResultInfo != null && topTopicInfoResultInfo.code == HttpConfig.STATUS_OK && topTopicInfoResultInfo.data.getTopic_info() != null) {
                    CommunityInfo topic_info = topTopicInfoResultInfo.data.getTopic_info();

                    initData(topic_info);
                }
            }
        });

    }

    private void initWebview() {
        mWebView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

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
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

//        String content = getString(R.string.community_notice);

//        mWebView.addJavascriptInterface(new LoveAudioDetailActivity.MyJavaScript(), "APP");

//        mWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
////                Log.e(TAG, "onPageFinished: ");
//
////                document.body.getBoundingClientRect().height
//                mWebView.loadUrl("javascript:window.APP.resize(document.body.getScrollHeight())");
//
//            }
//        });
        mWebView.loadDataWithBaseURL(null, formatting(content), "text/html", "utf-8", null);
    }

    private String formatting(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<head><style>img{max-width: 100%!important;height:auto!important;}body{margin:0 auto;background:#fff;position: relative;line-height:1.3;font-size:2.8em;font-family:Microsoft YaHei,Helvetica,Tahoma,Arial,\\5FAE\\8F6F\\96C5\\9ED1,sans-serif}</style></head>");
        stringBuilder.append("<body>");
        stringBuilder.append(data);
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }


    private void initData(CommunityInfo data) {
        Glide.with(this).load(R.mipmap.ic_launcher).apply(new RequestOptions().circleCrop()).into(ivIcon);
//        tvName.setText(data.name);
        tvContent.setText(data.content);
        tvDate.setText(DateUtils.formatTimeToStr(data.create_time));
        Glide.with(this).load(R.mipmap.community_detail_bg).into(ivCommunityPic);
        initWebview();
    }

    @Override
    protected String offerActivityTitle() {
        return getString(R.string.notice);
    }
}
