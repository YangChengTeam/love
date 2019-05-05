package com.yc.love.ui.frament;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.NoThingAdapter;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.StringBeanViewHolder;
import com.yc.love.model.bean.StringBean;
import com.yc.love.ui.frament.base.BaseLoveByStagesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveByStagesFragment extends BaseLoveByStagesFragment {

    private RecyclerView mRecyclerView;

    @Override
    protected int setContentView() {
        return R.layout.fragment_love_by_stages;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView =  rootView.findViewById(R.id.fragment_love_by_stages_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mLoveByStagesActivity);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void lazyLoad() {
        List<StringBean> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            StringBean stringBean = new StringBean("name " + i);
            datas.add(stringBean);
        }
        NoThingAdapter<StringBean> adapter = new NoThingAdapter<StringBean>(datas, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new StringBeanViewHolder(mLoveByStagesActivity, null, parent);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }
}
