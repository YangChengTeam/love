package com.yc.verbaltalk.skill.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.base.holder.BaseViewHolder;
import com.yc.verbaltalk.base.holder.UpDownMenHolder;
import com.yc.verbaltalk.base.holder.UpDownTitleHolder;
import com.yc.verbaltalk.base.holder.UpDownWomenHolder;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;
import com.yc.verbaltalk.chat.bean.LoveHealingDetailBean;
import com.yc.verbaltalk.skill.adapter.LoveUpDownPhotoAdapter;
import com.yc.verbaltalk.skill.ui.activity.LoveUpDownPhotoActivity;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by sunshey on 2019/5/5.
 */

public class LoveUpDownPhotoFragment extends BaseLazyFragment implements View.OnClickListener {

    private String mDataString;
    private LoveUpDownPhotoActivity mLoveUpDownPhotoActivity;
    private LoadDialog mLoadingDialog;
    private LoveEngine mLoveEngin;
    private int mPosition;
    private boolean mIsVisibleFragment;
    private RecyclerView mRecyclerView;

    private String[] names = {"正在小鹿乱撞", "正在输入", "正在不知所措", "笑容逐渐浮现", "正在害羞", "心中一愣",
            "正在小鹿乱撞", "正在输入", "正在不知所措", "笑容逐渐浮现", "正在害羞", "心中一愣"};
    private TextView mTvSub;
    private int mLovewordsId = 0;
    private boolean mIsCollectLovewords = false;
    private String mChildUrl;

    @Override
    protected int setContentView() {
        return R.layout.fragment_love_up_down_photo;
    }

    @Override
    protected void initViews() {
        mLoveUpDownPhotoActivity = (LoveUpDownPhotoActivity) getActivity();
        mLoadingDialog = mLoveUpDownPhotoActivity.mLoadingDialog;
        mLoveEngin = new LoveEngine(mLoveUpDownPhotoActivity);
//        TextView tv = rootView.findViewById(R.love_id.fragment_love_up_down_photo_tv);

        initTitle();

        mRecyclerView = rootView.findViewById(R.id.fragment_love_up_down_photo_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveUpDownPhotoActivity);
        mRecyclerView.setLayoutManager(layoutManager);
//        tv.setText(mDataString);

        netIdData();
    }

    private void initTitle() {
        View viewBar = rootView.findViewById(R.id.activity_base_same_view_bar);
        RelativeLayout rlTitleCon = rootView.findViewById(R.id.activity_base_same_rl_title_con);
        ImageView ivBack = rootView.findViewById(R.id.activity_base_same_iv_back);
        TextView tvTitle = rootView.findViewById(R.id.activity_base_same_tv_title);
        mTvSub = rootView.findViewById(R.id.activity_base_same_tv_sub);

        mLoveUpDownPhotoActivity.setStateBarHeight(viewBar);
        int i = new Random().nextInt(names.length);
        tvTitle.setText("对方".concat(names[i]).concat("..."));

//        mTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);

        ivBack.setOnClickListener(this);
        mTvSub.setOnClickListener(this);
    }

    @Override
    protected void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPosition = arguments.getInt("position");
            mDataString = arguments.getString("dataString", "-1");
            mChildUrl = arguments.getString("childUrl", "");//lovewords/recommend
        }
    }

    @Override
    protected void lazyLoad() {
//        netIdData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
        if (isVisibleToUser) {
            mIsVisibleFragment = true;
        } else {
            mIsVisibleFragment = false;
        }
    }

    private void netIdData() {
        if (mIsVisibleFragment) {
            mLoadingDialog.showLoadingDialog();
        }
        mLoveEngin.recommendLovewords(UserInfoHelper.getUid(), String.valueOf(mPosition + 1), String.valueOf(1), mChildUrl)
                .subscribe(new DisposableObserver<ResultInfo<List<LoveHealingBean>>>() {
                    @Override
                    public void onComplete() {
                        if (mIsVisibleFragment) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mIsVisibleFragment) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onNext(ResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        if (mIsVisibleFragment) {
                            mLoadingDialog.dismissLoadingDialog();
                        }
                        if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK && listAResultInfo.data != null) {
                            List<LoveHealingBean> loveHealingBeanList = listAResultInfo.data;
                            if (loveHealingBeanList.size() <= 0) {
                                Log.d("mylog", "onNetNext: loveHealingBeanList==null ");
                                return;
                            }
                            LoveHealingBean loveHealingBean = loveHealingBeanList.get(0);
                            List<LoveHealingDetailBean> detail = loveHealingBean.detail;
                            if (detail != null && detail.size() >= 1) {
                                LoveHealingDetailBean loveHealingDetailBean = detail.get(0);
                                mLovewordsId = loveHealingDetailBean.lovewords_id;
                            }

                            int isCollect = loveHealingBean.is_collect;
                            if (isCollect > 0) { //是否收藏
                                mIsCollectLovewords = true;
                            }
                            changSubImg();

                            initRecyclerData(detail);
                        }
                    }


                });
    }

    private void initRecyclerData(List<LoveHealingDetailBean> detail) {
        LoveHealingDetailBean loveHealingDetailBeanTitle = new LoveHealingDetailBean("");
        detail.add(0, loveHealingDetailBeanTitle);

        LoveUpDownPhotoAdapter adapter = new LoveUpDownPhotoAdapter(detail, mRecyclerView) {
            @Override
            protected RecyclerView.ViewHolder getUpDownTitleHolder(ViewGroup parent) {
                return new UpDownTitleHolder(mLoveUpDownPhotoActivity, null, parent);
            }

            @Override
            public BaseViewHolder getMenHolder(ViewGroup parent) {
                return new UpDownMenHolder(mLoveUpDownPhotoActivity, null, parent);
            }

            @Override
            public BaseViewHolder getWomenHolder(ViewGroup parent) {
                return new UpDownWomenHolder(mLoveUpDownPhotoActivity, null, parent);
            }

        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_same_iv_back:
                mLoveUpDownPhotoActivity.finish();
                break;
            case R.id.activity_base_same_tv_sub:
                if (UserInfoHelper.isLogin(getActivity()))
                    netCollect(mIsCollectLovewords, UserInfoHelper.getUid());
                break;
        }
    }

    private void netCollect(boolean isCollect, String userId) {
        mLoadingDialog.showLoadingDialog();
        String url = "";
        if (isCollect) {
            url = "Lovewords/uncollect";
        } else {
            url = "Lovewords/collect";
        }
        mLoveEngin.collectLovewords(userId, String.valueOf(mLovewordsId), url)
                .subscribe(new DisposableObserver<ResultInfo<String>>() {
                    @Override
                    public void onComplete() {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(ResultInfo<String> stringAResultInfo) {
                        mLoadingDialog.dismissLoadingDialog();
                        if (stringAResultInfo != null && stringAResultInfo.code == HttpConfig.STATUS_OK && stringAResultInfo.data != null) {
                            String msg = stringAResultInfo.message;
                            ToastUtils.showCenterToast(msg);
                            mIsCollectLovewords = !mIsCollectLovewords;
                            changSubImg();
                        }
                    }


                });
    }

    private void changSubImg() {
        if (mIsCollectLovewords) {
            mTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_s, 0);
        } else {
            mTvSub.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_star_black, 0);
        }

//        EventBus.getDefault().post(new EventLoveUpDownCollectChangBean(mIsCollectLovewords));
    }
}
