package com.yc.love.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.adaper.rv.CreateAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealItemViewHolder;
import com.yc.love.adaper.rv.holder.LoveHealTitleViewHolder;
import com.yc.love.model.bean.LoveHealBean;
import com.yc.love.ui.activity.base.BaseSameActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 恋爱药方
 */

public class LoveHealActivity extends BaseSameActivity {
    private final int LOVE_HEAL_TYPE_TITLE = 1;
    private final int LOVE_HEAL_TYPE_ITEM = 2;
    private List<LoveHealBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_heal);
        initViews();
    }

    private void initViews() {
        initRecyclerView();
    }

    int num = 10;

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.love_heal_rv);

//        HashMap<String, List<String>> adapterMaps = new HashMap<>();
        mDatas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
//            List<String> list = new ArrayList<>();
            mDatas.add(new LoveHealBean("titl--" + i, 1));
            for (int j = 0; j < num; j++) {
                mDatas.add(new LoveHealBean("item " + j, 2));
            }
            num--;
        }

        CreateAdapter createAdapter = new CreateAdapter(this, mDatas) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new LoveHealItemViewHolder(LoveHealActivity.this, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new LoveHealTitleViewHolder(LoveHealActivity.this, null, parent);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        /*gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                LoveHealBean loveHealBean = mDatas.get(position);
                int spansize = 1;
                switch (loveHealBean.type) {
                    case LOVE_HEAL_TYPE_TITLE:
                        spansize = 3;  //占据3列
                        break;
                    *//*case LOVE_HEAL_TYPE_ITEM:
                        spansize = 1;
                        break;*//*
                }
                return spansize;
            }
        });*/
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(createAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            LoveDialogueActivity.startLoveDialogueActivity(LoveHealActivity.this,mDatas.get(position).name);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };

    @Override
    protected String offerActivityTitle() {
        return "恋爱妙方";
    }

/*    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/
}
