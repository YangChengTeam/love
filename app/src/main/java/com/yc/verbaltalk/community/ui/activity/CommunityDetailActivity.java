package com.yc.verbaltalk.community.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSlidingActivity;
import com.yc.verbaltalk.base.utils.DateUtils;
import com.yc.verbaltalk.base.utils.StatusBarUtil;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.CommunityDetailInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfo;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.community.adapter.CommunityFollowAdapter;
import com.yc.verbaltalk.mine.ui.activity.BecomeVipActivity;
import com.yc.verbaltalk.mine.ui.activity.UserInfoActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2019/8/29 11:23.
 */
public class CommunityDetailActivity extends BaseSlidingActivity implements View.OnClickListener {

    private static final String TAG = "CommunityDetailActivity";


    private CommunityFollowAdapter communityMainAdapter;
    private TextView mTvTitle;
    private ImageView ivBack;
    private EditText etInputComment;
    private TextView tvSend;
    private String result;//输入结果
    private LinearLayout llVip;
    private LinearLayout llWxService;
    private boolean isVip;
    private String tile;
    private String id;
    private ImageView ivIcon;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvContent;
    private TextView tvLikeNum;
    private TextView tvCommentNum;
    private LinearLayout bottomView;

    public static void StartActivity(Context context, String title, String id) {
        Intent intent = new Intent(context, CommunityDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_community_detail);
        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变

        initView();
        initListener();

    }


    private void initView() {
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            tile = intent.getStringExtra("title");
        }
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        }

        mTvTitle = findViewById(R.id.activity_base_same_tv_title);
        ivBack = findViewById(R.id.activity_base_same_iv_back);
        mTvTitle.setText(tile);
        etInputComment = findViewById(R.id.et_input_comment);
        tvSend = findViewById(R.id.tv_send);
        llVip = findViewById(R.id.ll_vip);
        llWxService = findViewById(R.id.ll_wx_service);
        ivIcon = findViewById(R.id.iv_icon);
        tvName = findViewById(R.id.tv_name);
        tvDate = findViewById(R.id.tv_date);
        tvContent = findViewById(R.id.tv_content);
        tvLikeNum = findViewById(R.id.tv_like_num);
        tvCommentNum = findViewById(R.id.tv_comment_num);
        bottomView = findViewById(R.id.ll_bottom);


        RecyclerView detailRecyclerView = findViewById(R.id.detail_recyclerView);
        detailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        communityMainAdapter = new CommunityFollowAdapter(null);
        detailRecyclerView.setAdapter(communityMainAdapter);

        getData();
        netIsVipData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams) bottomView.getLayoutParams();
        int bottom = 0;
        if (StatusBarUtil.isNavigationBarExist(this)) {
            bottom = StatusBarUtil.getNavigationBarHeight(this);
        }
        layoutParams.bottomMargin = bottom;
        bottomView.setLayoutParams(layoutParams);

        Log.e(TAG, "onWindowFocusChanged: ");
    }


    private void initListener() {
        ivBack.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        llVip.setOnClickListener(this);
        llWxService.setOnClickListener(this);
        etInputComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                result = s.toString().trim();
//                Log.e(TAG, "afterTextChanged: " + result);
                if (TextUtils.isEmpty(result)) {
                    tvSend.setBackground(ContextCompat.getDrawable(CommunityDetailActivity.this, R.drawable.community_gray_send_selector));
                } else {
                    tvSend.setBackground(ContextCompat.getDrawable(CommunityDetailActivity.this, R.drawable.community_red_send_selector));
                }

            }
        });
        communityMainAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            CommunityInfo communityInfo = communityMainAdapter.getItem(position);
            if (communityInfo != null) {
                switch (view.getId()) {
                    case R.id.iv_like:
                    case R.id.ll_like:
//
                        if (UserInfoHelper.isLogin(this))
                            if (communityInfo.is_dig == 0) {
                                like(communityInfo, position);
                            }
                        break;
                }
            }
        });

    }

    private void like(CommunityInfo communityInfo, int position) {


        mLoveEngine.commentLike(UserInfoHelper.getUid(), communityInfo.comment_id).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    communityInfo.is_dig = 1;
                    communityInfo.like_num = communityInfo.like_num + 1;

                    communityMainAdapter.notifyItemChanged(position);

                }
            }
        });
    }

    private void getData() {

        if (mLoadingDialog == null)
            mLoadingDialog = new LoadDialog(this);

        mLoadingDialog.showLoadingDialog();

        mLoveEngine.getCommunityDetailInfo(UserInfoHelper.getUid(), id).subscribe(new DisposableObserver<ResultInfo<CommunityDetailInfo>>() {
            @Override
            public void onComplete() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<CommunityDetailInfo> communityDetaiInfoResultInfo) {
                mLoadingDialog.dismissLoadingDialog();
                if (communityDetaiInfoResultInfo != null && communityDetaiInfoResultInfo.code == HttpConfig.STATUS_OK &&
                        communityDetaiInfoResultInfo.data != null) {
                    CommunityDetailInfo detailInfo = communityDetaiInfoResultInfo.data;
                    initData(detailInfo);
                }

            }
        });
    }

    private void initData(CommunityDetailInfo detailInfo) {
        CommunityInfo communityInfo = detailInfo.getTopic_info();
        Glide.with(this).load(communityInfo.pic).apply(new RequestOptions().circleCrop().
                error(R.mipmap.main_icon_default_head).placeholder(R.mipmap.main_icon_default_head))
                .into(ivIcon);
        tvName.setText(communityInfo.name);
        tvDate.setText(DateUtils.formatTimeToStr(communityInfo.create_time));
        tvContent.setText(communityInfo.content);
        tvLikeNum.setText(String.valueOf(communityInfo.like_num));
        tvCommentNum.setText(String.valueOf(communityInfo.comment_num));
        communityMainAdapter.setNewData(detailInfo.getComment_list());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_same_iv_back:
                finish();
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(result)) {
                    ToastUtil.toast(this, "评论内容不能为空");
                    return;
                }
                // TODO: 2019/8/30 提交到服务器

                if (UserInfoHelper.isLogin(this))
                    submitComment(id, result);

                break;
            case R.id.ll_vip:


                //1.isVip 有两种情况

                if (UserInfoHelper.isLogin(this)) {//2.没登录返回false 提示登录 跳转到登录界面 登录成功后继续请求
                    if (isVip) {//4.登录返回true 提示用户已购买vip
                        ToastUtil.toast(CommunityDetailActivity.this, "你已经是VIP不需要重复购买");
                        return;
                    }
                    //3.登录返回false  跳转到支付界面
                    startActivity(new Intent(CommunityDetailActivity.this, BecomeVipActivity.class));
                }


                break;
            case R.id.ll_wx_service:
                showToWxServiceDialog(null);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(EventLoginState event) {
        switch (event.state) {
            case EventLoginState.STATE_LOGINED:
            case EventLoginState.STATE_EXIT:
                netIsVipData();
                break;
        }
//        if (TextUtils.equals("login", tint)) {
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void submitComment(String topicId, String content) {

        if (mLoadingDialog == null)
            mLoadingDialog = new LoadDialog(this);
        mLoadingDialog.showLoadingDialog();

        mLoveEngine.createComment(UserInfoHelper.getUid(), topicId, content).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                mLoadingDialog.dismissLoadingDialog();
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                        hindKeyboard(etInputComment);
                        etInputComment.setText("");
                        saveToList(content);
                    } else if (stringResultInfo.code == 2) {
                        startActivity(new Intent(CommunityDetailActivity.this, UserInfoActivity.class));
                    }

                }
            }
        });
    }


    private void netIsVipData() {

        mLoveEngine.userInfo(UserInfoHelper.getUid()).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> idCorrelationLoginBeanAResultInfo) {
                if (idCorrelationLoginBeanAResultInfo != null && idCorrelationLoginBeanAResultInfo.code == HttpConfig.STATUS_OK) {
                    UserInfo idCorrelationLoginBean = idCorrelationLoginBeanAResultInfo.data;
                    if (idCorrelationLoginBean == null) {
                        return;
                    }

                    int vipTips = idCorrelationLoginBean.vip_tips; //0 未开通   1 已开通   2 已过期
                    isVip = vipTips == 1;

                }

            }

        });
    }


    private void saveToList(String content) {

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo == null) return;
        List<CommunityInfo> communityInfos = communityMainAdapter.getData();

//        SimpleDateFormat sd = new SimpleDateFormat("MM月dd日", Locale.getDefault());

        CommunityInfo communityInfo = new CommunityInfo(userInfo.nick_name, new Date().getTime() / 1000, content, "", 0, 0);
        communityInfo.pic = userInfo.face;
        communityInfos.add(0, communityInfo);
        communityMainAdapter.setNewData(communityInfos);


    }

}
