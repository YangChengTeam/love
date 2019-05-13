package com.yc.love.ui.frament.main;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.CategoryArticleChildrenBean;
import com.yc.love.model.bean.ExampleTsBean;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveIntroductionActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;
import com.yc.love.ui.view.LoadingDialog;

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
    //    private LoadDialog mLoadingDialog;
    private List<CategoryArticleBean> mCategoryArticleBeans;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3;
    }

    private LoveEngin mLoveEngin;

    @Override
    protected void initViews() {
        mLoveEngin = new LoveEngin(mMainActivity);
        View viewBar = rootView.findViewById(R.id.main_t3_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);
//        mLoadingDialog = mMainActivity.mLoadingDialog;
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
        netData();
        netTitleData();

    }

    private void netTitleData() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.categoryArticle("Article/category").subscribe(new MySubscriber<AResultInfo<List<CategoryArticleBean>>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<List<CategoryArticleBean>> listAResultInfo) {
                mCategoryArticleBeans = listAResultInfo.data;
                for (CategoryArticleBean categoryArticleBean : mCategoryArticleBeans
                        ) {
                    Log.d("mylog", "onNetNext: categoryArticleBean " + categoryArticleBean.toString());
                }
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void netData() {
        LoadDialog loadDialog = new LoadDialog(mMainActivity);
        loadDialog.show();
        mLoveEngin.exampleTs("example/ts").subscribe(new MySubscriber<AResultInfo<List<ExampleTsBean>>>(loadDialog) {


            @Override
            protected void onNetNext(AResultInfo<List<ExampleTsBean>> listAResultInfo) {
                mDatas = new ArrayList<>();
                mDatas.add(new MainT3Bean(1));
                mDatas.add(new MainT3Bean(2, "入门秘籍"));
                List<ExampleTsBean> exampleTsBeans = listAResultInfo.data;
                for (ExampleTsBean exampleTsBean : exampleTsBeans
                        ) {
                    List<ExampleTsListBean> list1 = exampleTsBean.list1;
                    for (int i = 0; i < list1.size(); i++) {
                        ExampleTsListBean listBean = list1.get(i);
                        mDatas.add(new MainT3Bean(3, listBean.id, listBean.post_title, listBean.image, listBean.tag, listBean.tag_id, listBean.category_name));
                    }
                    mDatas.add(new MainT3Bean(2, "进阶秘籍"));
                    List<ExampleTsListBean> list2 = exampleTsBean.list2;
                    for (int i = 0; i < list2.size(); i++) {
                        ExampleTsListBean listBean = list2.get(i);
                        mDatas.add(new MainT3Bean(3, listBean.id, listBean.post_title, listBean.image, listBean.tag, listBean.tag_id, listBean.category_name));
                    }
                }
                initRecyclerViewData();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void initRecyclerViewData() {


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
                    public void clickTitleIcon(int position) {
                        switch (position) {
                            case 0:
                                startLoveByStagesActivity(0, "单身期");
                                break;
                            case 1:
                                startLoveByStagesActivity(1, "追求期");
                                break;
                            case 2:
                                startLoveByStagesActivity(2, "热恋期");
                                break;
                            case 3:
                                startLoveByStagesActivity(3, "失恋期");
                                break;
                            case 4:
                                startLoveByStagesActivity(4, "婚后期");
                                break;
                        }
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

    private void startLoveByStagesActivity(int position, String title) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans.size() < position + 1) {
            return;
        }
        CategoryArticleBean categoryArticleBean = mCategoryArticleBeans.get(position);
        ArrayList<CategoryArticleChildrenBean> children = categoryArticleBean.children;
        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, title, children);
    }

    RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            MainT3Bean mainT3Bean = mDatas.get(position);
            int tagId = mainT3Bean.tag_id;
            LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, mainT3Bean.category_name, String.valueOf(tagId));
//            LoveDialogueActivity.startLoveDialogueActivity(mMainActivity, mDatas.get(position).name);
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };
}
