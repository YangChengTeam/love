package com.yc.love.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.CreateAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealItemViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealTitleViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 恋爱妙方
 */

public class LoveHealActivity extends BaseSameActivity {
    //    private final int LOVE_HEAL_TYPE_TITLE = 1;
//    private final int LOVE_HEAL_TYPE_ITEM = 2;
    private List<LoveHealBean> mDatas = new ArrayList<>();
    private LoveEngin mLoveEngin;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_heal);
        mLoveEngin = new LoveEngin(this);
        initViews();
        initRecyclerData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, ConstantKey.UM_LOVEQUOTE_ID);
    }

    private void initViews() {
        initRecyclerView();
    }

    int num = 10;

    public void initRecyclerView() {
        mRecyclerView = findViewById(R.id.love_heal_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initRecyclerData() {
        netData();
    }

    private void netData() {
        mLoadingDialog.showLoadingDialog();
        mLoveEngin.loveCategory("Dialogue/category").subscribe(new MySubscriber<AResultInfo<List<LoveHealDateBean>>>(mLoadingDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<LoveHealDateBean>> loveHealDateBeanAResultInfo) {
                List<LoveHealDateBean> loveHealDateBeans = loveHealDateBeanAResultInfo.data;
                for (LoveHealDateBean loveHealDateBean : loveHealDateBeans
                        ) {
                    LoveHealBean loveHealBean = new LoveHealBean(1, loveHealDateBean._level, loveHealDateBean.id, loveHealDateBean.name, loveHealDateBean.parent_id);
                    mDatas.add(loveHealBean);
                    List<LoveHealDateBean.ChildrenBean> childrenBeans = loveHealDateBean.children;
                    for (LoveHealDateBean.ChildrenBean childrenBean : childrenBeans
                            ) {
                        LoveHealBean loveHealChildrenBean = new LoveHealBean(2, childrenBean._level, childrenBean.id, childrenBean.name, childrenBean.parent_id);
                        mDatas.add(loveHealChildrenBean);
                    }
                }
                CreateAdapter createAdapter = new CreateAdapter(LoveHealActivity.this, mDatas) {
                    @Override
                    public BaseViewHolder getHolder(ViewGroup parent) {
                        return new LoveHealItemViewHolder(LoveHealActivity.this, recyclerViewItemListener, parent);
                    }

                    @Override
                    public BaseViewHolder getTitleHolder(ViewGroup parent) {
                        return new LoveHealTitleViewHolder(LoveHealActivity.this, null, parent);
                    }
                };
                mRecyclerView.setAdapter(createAdapter);

            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
//            LoveDialogueActivity.startLoveDialogueActivity(LoveHealActivity.this, mDatas.get(position).name);
            LoveHealBean loveHealBean=mDatas.get(position);
            Log.d("mylog", "onItemClick: "+loveHealBean.toString());
            LoveHealDetailsActivity.startLoveHealDetailsActivity(LoveHealActivity.this, loveHealBean.name,String.valueOf(loveHealBean.id));
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    @Override
    protected String offerActivityTitle() {
        return "恋爱妙语";
    }

/*    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/
}
