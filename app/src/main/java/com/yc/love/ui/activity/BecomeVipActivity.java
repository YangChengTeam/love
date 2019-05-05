package com.yc.love.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yc.love.R;
import com.yc.love.adaper.rv.BecomeVipAdapter;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipItemViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTailViewHolder;
import com.yc.love.adaper.rv.holder.BecomeVipTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.BecomeVipPayBean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.ui.activity.base.BaseSlidingActivity;

import java.util.ArrayList;
import java.util.List;

public class BecomeVipActivity extends BaseSlidingActivity {
    private int[] imgResIds = {R.mipmap.become_vip_icon_01, R.mipmap.become_vip_icon_02, R.mipmap.become_vip_icon_03,
            R.mipmap.become_vip_icon_04, R.mipmap.become_vip_icon_05, R.mipmap.become_vip_icon_06};
    private String[] names = {"20W+话术免费搜索", "海量话术实战免费查看", "海量话术技巧免费阅读",
            "经典聊天开场白免费看", "多样表白话术免费查看", "创新幽默语录免费查看"};
    private String[] subNames = {"输入TA的话，快速查找撩心话术", "话术聊天实战案例，跟着套路学撩妹",
            "恋爱脱单的爱情秘籍，撩动TA心的话术技巧", "最全聊天开场白，跨出撩妹第一步",
            "高情商表白话术，提高表白成功率", "花样幽默风趣语句，提高妹子好感度"};
    private String[] payName = {"购买VIP30天", "购买VIP90天", "购买全年VIP", "购买永久VIP"};
    private int[] payMoney = {38, 58, 198, 298};
    private String[] payDes = {"最低1.2元/天", "最低0.6元/天", "最低0.5元/天", "一次开通永久免费限时免费"};
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_vip);
        //侵入状态栏
        invadeStatusBar();
        initViews();
        initRecyclerData();
    }

    protected void initViews() {
        initRecyclerView();
    }

    public void initRecyclerView() {
        mRecyclerView = findViewById(R.id.become_vip_rv);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(gridLayoutManager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initRecyclerData() {
        List<BecomeVipBean> datas = new ArrayList<>();
        datas.add(new BecomeVipBean(1, "升级VIP解锁全部聊天话术及20W+条话术免费搜索"));
        for (int i = 0; i < imgResIds.length; i++) {
            datas.add(new BecomeVipBean(2, names[i], subNames[i], imgResIds[i]));
        }
        List<BecomeVipPayBean> list = new ArrayList<>();
        for (int i = 0; i < payName.length; i++) {
            BecomeVipPayBean payBean = new BecomeVipPayBean(payName[i], payMoney[i], payDes[i]);
            list.add(payBean);
        }
        new BecomeVipBean(3, list);

        BecomeVipAdapter becomeVipAdapter = new BecomeVipAdapter(datas, mRecyclerView) {
            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                return new BecomeVipTitleViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new BecomeVipItemViewHolder(BecomeVipActivity.this, null, parent);
            }

            @Override
            protected RecyclerView.ViewHolder getTailViewHolder(ViewGroup parent) {
                return new BecomeVipTailViewHolder(BecomeVipActivity.this, null, parent);
            }
        };
        mRecyclerView.setAdapter(becomeVipAdapter);
    }
}
