package com.yc.love.ui.frament.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveIntroductionActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT3Fragment extends BaseMainFragment {
    private TextView tvName;
    private int[] imgResId01s = {R.mipmap.main_bg_t3_01, R.mipmap.main_bg_t3_02, R.mipmap.main_bg_t3_03,
            R.mipmap.main_bg_t3_04, R.mipmap.main_bg_t3_05, R.mipmap.main_bg_t3_06};
    private int[] imgResId02s = {R.mipmap.main_bg_t3_07, R.mipmap.main_bg_t3_08, R.mipmap.main_bg_t3_09,
            R.mipmap.main_bg_t3_10, R.mipmap.main_bg_t3_11, R.mipmap.main_bg_t3_12, R.mipmap.main_bg_t3_13};
    private String[] name01s = {"线上撩妹", "线下撩妹", "开场搭讪", "约会邀请", "把握主权", "完美告白"};
    private String[] name02s = {"自我提升", "相亲妙招", "关系确定", "关系进阶", "甜蜜异地", "分手挽回", "婚姻经营"};
    private String[] des01s = {"用话语撩动屏幕前的TA", "用情感感染你面前的TA", "搭讪有窍门，开场不尴尬",
            "邀请有方法，约会不尴尬", "把握有窍门，轻松搞定TA", "遇见对的TA，告白要完美"};
    private String[] des02s = {"让自己优秀，妹子自然来", "相亲小妙招，牵住女神心", "确认过眼神，TA是对的人",
            "情感升温后，水到渠自成", "距离也可以因爱产生美", "不想和TA分，那就看看吧", "婚姻并不是爱情的坟墓"};
    private RecyclerView mRecyclerView;
    private List<MainT3Bean> mDatas;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3;
    }

    @Override
    protected void initViews() {
        initRecyclerView();
    }

    public void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void lazyLoad() {
        isCanLoadData();
    }

    private void isCanLoadData() {
        initRecyclerViewData();
    }

    private void initRecyclerViewData() {
        mDatas = new ArrayList<>();
        mDatas.add(new MainT3Bean(1));
        mDatas.add(new MainT3Bean(2, "入门秘籍"));
        for (int i = 0; i < imgResId01s.length; i++) {
            mDatas.add(new MainT3Bean(3, name01s[i], des01s[i], imgResId01s[i]));
        }
        mDatas.add(new MainT3Bean(2, "进阶秘籍"));
        for (int i = 0; i < imgResId02s.length; i++) {
            mDatas.add(new MainT3Bean(3, name02s[i], des02s[i], imgResId02s[i]));
        }

        CreateMainT3Adapter mainT3Adapter = new CreateMainT3Adapter(mMainActivity, mDatas) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new MainT3ItemViewHolder(mMainActivity, recyclerViewItemListener, parent);
            }

            @Override
            public BaseViewHolder getTitleHolder(ViewGroup parent) {
                MainT3TitleViewHolder mainT3TitleViewHolder = new MainT3TitleViewHolder(mMainActivity, null, parent);
                mainT3TitleViewHolder.setOnClickTitleIconListener(new MainT3TitleViewHolder.OnClickTitleIconListener() {
                    @Override
                    public void clickTitleIcon01() {
                        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, "单身期");
                    }

                    @Override
                    public void clickTitleIcon02() {
                        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, "追求期");
                    }

                    @Override
                    public void clickTitleIcon03() {
                        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, "热恋期");
                    }

                    @Override
                    public void clickTitleIcon04() {
                        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, "失恋期");
                    }

                    @Override
                    public void clickTitleIcon05() {
                        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, "婚后期");
                    }
                });
                return mainT3TitleViewHolder;
            }

            @Override
            protected BaseViewHolder getItemTitleHolder(ViewGroup parent) {
                return new MainT3ItemTitleViewHolder(mMainActivity, null, parent);
            }
        };
        mRecyclerView.setAdapter(mainT3Adapter);
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            MainT3Bean mainT3Bean = mDatas.get(position);
            LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, mainT3Bean.name);
//            LoveDialogueActivity.startLoveDialogueActivity(mMainActivity, mDatas.get(position).name);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
