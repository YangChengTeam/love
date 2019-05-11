package com.yc.love.ui.frament;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.LoveUpDownPhotoAdapter;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.UpDownMenHolder;
import com.yc.love.adaper.rv.holder.UpDownWomenHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.bean.LoveHealingDetailBean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.LoveUpDownPhotoActivity;
import com.yc.love.ui.frament.base.BaseLazyFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveUpDownPhotoFragment extends BaseLazyFragment {

    private String mDataString;
    private LoveUpDownPhotoActivity mLoveUpDownPhotoActivity;
    private LoadDialog mLoadingDialog;
    private LoveEngin mLoveEngin;
    private int mPosition;
    private boolean mIsVisibleFragment;
    private RecyclerView mRecyclerView;

    @Override
    protected int setContentView() {
        return R.layout.fragment_love_up_down_photo;
    }

    @Override
    protected void initViews() {
        mLoveUpDownPhotoActivity = (LoveUpDownPhotoActivity) getActivity();
        mLoadingDialog = mLoveUpDownPhotoActivity.mLoadingDialog;
        mLoveEngin = new LoveEngin(mLoveUpDownPhotoActivity);
        TextView tv = rootView.findViewById(R.id.fragment_love_up_down_photo_tv);
        mRecyclerView = rootView.findViewById(R.id.fragment_love_up_down_photo_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveUpDownPhotoActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        tv.setText(mDataString);

        netIdData();
    }

    @Override
    protected void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPosition = arguments.getInt("position");
            mDataString = arguments.getString("dataString", "-1");
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

        mLoveEngin.recommendLovewords(String.valueOf(mPosition), String.valueOf(1), "lovewords/recommend")
                .subscribe(new MySubscriber<AResultInfo<List<LoveHealingBean>>>(mLoadingDialog) {
                    @Override
                    protected void onNetNext(AResultInfo<List<LoveHealingBean>> listAResultInfo) {
                        List<LoveHealingBean> loveHealingBeanList = listAResultInfo.data;
                        if(loveHealingBeanList==null||loveHealingBeanList.size()<=0){
                            Log.d("mylog", "onNetNext: loveHealingBeanList==null ");
                        }
                        LoveHealingBean loveHealingBean = loveHealingBeanList.get(0);
                        List<LoveHealingDetailBean> detail = loveHealingBean.detail;

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

    private void initRecyclerData( List<LoveHealingDetailBean> detail) {
        for (LoveHealingDetailBean loveHealingDetailBean:detail
             ) {
            Log.d("mylog", "initRecyclerData: loveHealingDetailBean "+loveHealingDetailBean.toString());
        }

        LoveUpDownPhotoAdapter adapter = new LoveUpDownPhotoAdapter(detail, mRecyclerView) {
            @Override
            public BaseViewHolder getMenHolder(ViewGroup parent) {
                return new UpDownMenHolder(mLoveUpDownPhotoActivity,null,parent);
            }

            @Override
            public BaseViewHolder getWomenHolder(ViewGroup parent) {
                return new UpDownWomenHolder(mLoveUpDownPhotoActivity,null,parent);
            }

        };
        mRecyclerView.setAdapter(adapter);
    }
}
