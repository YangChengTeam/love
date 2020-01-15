package com.yc.verbaltalk.ui.frament;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.LoveUpDownPhotoAdapter;
import com.yc.verbaltalk.adaper.rv.holder.BaseViewHolder;
import com.yc.verbaltalk.adaper.rv.holder.UpDownMenHolder;
import com.yc.verbaltalk.adaper.rv.holder.UpDownTitleHolder;
import com.yc.verbaltalk.adaper.rv.holder.UpDownWomenHolder;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.LoveHealingBean;
import com.yc.verbaltalk.model.bean.LoveHealingDetailBean;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.ui.activity.LoveUpDownPhotoActivity;
import com.yc.verbaltalk.ui.frament.base.BaseLazyFragment;
import com.yc.verbaltalk.ui.view.LoadDialog;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by mayn on 2019/5/5.
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
        mLoveEngin.recommendLovewords(String.valueOf(YcSingle.getInstance().id), String.valueOf(mPosition + 1), String.valueOf(1), mChildUrl)
                .subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        List<LoveHealingBean> loveHealingBeanList = listAResultInfo.data;
                        if (loveHealingBeanList == null || loveHealingBeanList.size() <= 0) {
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

                    @Override
                    protected void onNetError(Throwable e) {

                    }

                    @Override
                    protected void onNetCompleted() {

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
                int id = YcSingle.getInstance().id;
                if (id <= 0) {
                    mLoveUpDownPhotoActivity.showToLoginDialog();
                    return;
                }
                netCollect(mIsCollectLovewords, id);
                break;
        }
    }

    private void netCollect(boolean isCollect, int userId) {
        mLoadingDialog.showLoadingDialog();
        String url = "";
        if (isCollect) {
            url = "Lovewords/uncollect";
        } else {
            url = "Lovewords/collect";
        }
        mLoveEngin.collectLovewords(String.valueOf(userId), String.valueOf(mLovewordsId), url)
                .subscribe(new MySubscriber<AResultInfo<String>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<String> listAResultInfo) {
                        String msg = listAResultInfo.msg;
                        mLoveUpDownPhotoActivity.showToastShort(msg);
                        mIsCollectLovewords = !mIsCollectLovewords;
                        changSubImg();
                    }

                    @Override
                    protected void onNetError(Throwable e) {

                    }

                    @Override
                    protected void onNetCompleted() {

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
