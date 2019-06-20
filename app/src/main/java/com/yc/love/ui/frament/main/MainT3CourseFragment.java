package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.adaper.rv.holder.BaseViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemLocalityViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemTitleViewHolder;
import com.yc.love.adaper.rv.holder.MainT3ItemViewHolder;
import com.yc.love.adaper.rv.holder.MainT3TitleViewHolder;
import com.yc.love.cache.CacheWorker;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.CategoryArticleChildrenBean;
import com.yc.love.model.bean.ExampleTsCategory;
import com.yc.love.model.bean.ExampleTsCategoryList;
import com.yc.love.model.bean.MainT3Bean;
import com.yc.love.model.constant.ConstantKey;
import com.yc.love.model.engin.LoveEngin;
import com.yc.love.ui.activity.ExpressActivity;
import com.yc.love.ui.activity.LoveByStagesActivity;
import com.yc.love.ui.activity.LoveCaseActivity;
import com.yc.love.ui.activity.LoveHealingActivity;
import com.yc.love.ui.activity.LoveIntroductionActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayn on 2019/6/17.
 */

public class MainT3CourseFragment extends BaseMainFragment {

    private RecyclerView mRecyclerView;
    private List<CategoryArticleBean> mCategoryArticleBeans;
    private CacheWorker mCacheWorker;
    private List<MainT3Bean> mDatas;
    private LoadDialog mLoadDialog;
    private LoveEngin mLoveEngin;
    //    private boolean mIsNetData = false;
    private final int ID_ITEM_TITLE_CASE = -1;
    private final int ID_ITEM_TITLE_CURE = -2;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t3_course;
    }

    @Override
    protected void initViews() {

    }


    @Override
    protected void lazyLoad() {
        MobclickAgent.onEvent(mMainActivity, ConstantKey.UM_LOVE_SECRET_ID);
        initView();
        netData();
        netTitleData();
    }

    private void initView() {
        mLoveEngin = new LoveEngin(mMainActivity);
        mCacheWorker = new CacheWorker();

        View viewBar = rootView.findViewById(R.id.main_t3_course_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_course_rl);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void netTitleData() {
        mCategoryArticleBeans = (List<CategoryArticleBean>) mCacheWorker.getCache(mMainActivity, "main1_Article_category");
//        LoadDialog loadDialog = new LoadDialog(mMainActivity);
//        loadDialog.show();
        mLoveEngin.categoryArticle("Article/category").subscribe(new MySubscriber<AResultInfo<List<CategoryArticleBean>>>(mMainActivity) {
            @Override
            protected void onNetNext(AResultInfo<List<CategoryArticleBean>> listAResultInfo) {
                List<CategoryArticleBean> data = listAResultInfo.data;
                if (data == null || data.size() == 0) {
                    return;
                }
                mCategoryArticleBeans = data;
                mCacheWorker.setCache("main1_Article_category", mCategoryArticleBeans);
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
        mDatas = (List<MainT3Bean>) mCacheWorker.getCache(mMainActivity, "main3_example_ts_category");
        if (mDatas != null && mDatas.size() != 0) {
            initRecyclerViewData();
        } else {
            mLoadDialog = new LoadDialog(mMainActivity);
            mLoadDialog.showLoadingDialog();
        }
//        mLoveEngin.exampleTs("example/ts").subscribe(new MySubscriber<AResultInfo<List<ExampleTsBean>>>(loadDialog) {
        mLoveEngin.exampleTsCategory("example/ts_category").subscribe(new MySubscriber<AResultInfo<ExampleTsCategory>>(mLoadDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampleTsCategory> exampleTsCategoryAResultInfo) {

                ExampleTsCategory exampleTsCategory = exampleTsCategoryAResultInfo.data;
                if (exampleTsCategory == null) {
                    return;
                }
                List<ExampleTsCategoryList> list1 = exampleTsCategory.list1;
                List<ExampleTsCategoryList> list2 = exampleTsCategory.list2;
                if (list1 == null && list2 == null) {
                    return;
                }
                mDatas = new ArrayList<>();
                mDatas.add(new MainT3Bean(1));
//                mDatas.add(new MainT3Bean(4, "2", "学学别人怎么把女神撩到手", ID_ITEM_TITLE_CASE, R.mipmap.main_bg_item_title_case, "实战学习", 18));
//                mDatas.add(new MainT3Bean(4, "2", "浪漫情话让你撩妹不愁", ID_ITEM_TITLE_CURE, R.mipmap.main_bg_item_title_cure, "治愈情话", 18));
                mDatas.add(new MainT3Bean(2, "入门秘籍"));

                if (list1 != null) {
                    for (int i = 0; i < list1.size(); i++) {
                        ExampleTsCategoryList categoryList = list1.get(i);
                        mDatas.add(new MainT3Bean(3, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
                    }
                }
                mDatas.add(new MainT3Bean(2, "进阶秘籍"));
                if (list2 != null) {
                    for (int i = 0; i < list2.size(); i++) {
                        ExampleTsCategoryList categoryList = list2.get(i);
                        mDatas.add(new MainT3Bean(3, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
                    }
                }
                mCacheWorker.setCache("main3_example_ts_category", mDatas);
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
            protected RecyclerView.ViewHolder getLocalityHolder(ViewGroup viewGroup) {
                return new MainT3ItemLocalityViewHolder(mMainActivity, recyclerViewItemListener, viewGroup);
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
                            case 10: //表白入口
                                startActivity(new Intent(mMainActivity, ExpressActivity.class));
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

    private RecyclerViewItemListener recyclerViewItemListener = new RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            if (position < 0 || mDatas == null || mDatas.size() == 0) {
                return;
            }
            MainT3Bean mainT3Bean = mDatas.get(position);
            if (mainT3Bean == null) {
                Log.d("mylog", "onItemClick: mainT3Bean == null ");
                return;
            }
            int id = mainT3Bean.id;
            if (id < 0) {
                switch (id) {
                    case ID_ITEM_TITLE_CASE:
                        mMainActivity.startActivity(new Intent(mMainActivity, LoveCaseActivity.class));
                        break;
                    case ID_ITEM_TITLE_CURE:
                        mMainActivity.startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
                        break;
                }
            } else {
                LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, mainT3Bean.name, String.valueOf(id));
            }
        }

        @Override
        public void onItemLongClick(int position) {

        }
    };


}
