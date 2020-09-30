package com.yc.verbaltalk.community.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.utils.SnackBarUtils;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.CommunityTagInfo;
import com.yc.verbaltalk.chat.bean.CommunityTagInfoWrapper;
import com.yc.verbaltalk.chat.bean.event.CommunityPublishSuccess;
import com.yc.verbaltalk.community.adapter.PublishTagAdapter;
import com.yc.verbaltalk.mine.ui.fragment.ExitPublishFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;

import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2019/8/30 18:12.
 */
public class CommunityPublishActivity extends BaseSameActivity {

    private RecyclerView tagRecyclerView;
    private RecyclerView addPicRecyclerView;
    private PublishTagAdapter tagAdapter;
    private CommunityTagInfo tagInfo;
    private EditText etCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_publish);
        invadeStatusBar(); //侵入状态栏
        setAndroidNativeLightStatusBar(); //状态栏字体颜色改变

        initView();
        initListener();


    }

    private void initView() {

        tagRecyclerView = findViewById(R.id.tag_recyclerView);
        addPicRecyclerView = findViewById(R.id.add_pic_recyclerView);


        mBaseSameTvSub.setText(getString(R.string.publish));
        etCommunity = findViewById(R.id.et_community);


        tagRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        tagAdapter = new PublishTagAdapter(null);
        tagRecyclerView.setAdapter(tagAdapter);


        getTag();

    }

    private void initListener() {
        tagAdapter.setOnItemClickListener((adapter, view, position) -> {
            tagInfo = tagAdapter.getItem(position);
            tagAdapter.resetView();
            tagAdapter.setViewState(position);
        });
        ivBack.setOnClickListener(v -> {
            showExitFragment();
        });
        mBaseSameTvSub.setOnClickListener(v -> {
            String content = etCommunity.getText().toString().trim();
            hindKeyboard(etCommunity);
            if (TextUtils.isEmpty(content)) {
                SnackBarUtils.tips(CommunityPublishActivity.this, "请输入您的问题");
                return;
            }
            if (UserInfoHelper.isLogin(this))
                publishComment(content);

        });


    }


    private void publishComment(String content) {

        if (mLoadingDialog == null) mLoadingDialog = new LoadDialog(this);

        mLoadingDialog.showLoadingDialog();
        mLoveEngine.publishCommunityInfo(UserInfoHelper.getUid(), tagInfo.getId(), content).subscribe(new DisposableObserver<ResultInfo<String>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    SnackBarUtils.tips(CommunityPublishActivity.this, stringResultInfo.message);
                    mLoadingDialog.dismissLoadingDialog();
                    EventBus.getDefault().post(new CommunityPublishSuccess());
                    finish();
                }
            }
        });

    }

    public void getTag() {
        if (mLoadingDialog == null) mLoadingDialog = new LoadDialog(this);
        mLoadingDialog.showLoadingDialog();
        mLoveEngine.getCommunityTagInfos().subscribe(new DisposableObserver<ResultInfo<CommunityTagInfoWrapper>>() {
            @Override
            public void onComplete() {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mLoadingDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<CommunityTagInfoWrapper> communityTagInfoWrapperResultInfo) {
                mLoadingDialog.dismissLoadingDialog();
                if (communityTagInfoWrapperResultInfo != null && communityTagInfoWrapperResultInfo.code == HttpConfig.STATUS_OK
                        && communityTagInfoWrapperResultInfo.data != null) {
                    List<CommunityTagInfo> tagInfos = communityTagInfoWrapperResultInfo.data.getList();
                    if (tagInfos != null && tagInfos.size() > 0) {
                        tagInfo = tagInfos.get(0);
                    }
                    tagAdapter.setNewData(tagInfos);
                }
            }
        });

    }

    @Override
    protected String offerActivityTitle() {
        return getString(R.string.publish);
    }


    @Override
    public void onBackPressed() {

        showExitFragment();
    }

    private void showExitFragment() {
        ExitPublishFragment exitPublishFragment = new ExitPublishFragment();
        exitPublishFragment.show(getSupportFragmentManager(), "");
        exitPublishFragment.setOnConfirmListener(this::finish);
    }
}
