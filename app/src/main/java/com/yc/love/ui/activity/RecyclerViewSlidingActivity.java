package com.yc.love.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.BaseLoadMoreAdapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.StringBeanViewHolder;
import com.yc.love.adaper.rv.holder.TitleT1ViewHolder;
import com.yc.love.model.bean.StringBean;
import com.yc.love.ui.activity.base.BaseSlidingActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewSlidingActivity extends BaseSlidingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
//        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        //        recyclerView.addItemDecoration( new DividerGridItemDecoration(this ));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<StringBean> stringBeans = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            stringBeans.add(new StringBean("name " + i));
        }

        ImageView ll_toolbar =findViewById(R.id.recycler_view_iv);

        BaseLoadMoreAdapter adapter = new BaseLoadMoreAdapter<StringBean>(stringBeans, recyclerView,ll_toolbar) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {

                return new StringBeanViewHolder(RecyclerViewSlidingActivity.this, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new TitleT1ViewHolder(RecyclerViewSlidingActivity.this, null, parent);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
