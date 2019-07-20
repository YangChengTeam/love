package com.yc.love.ui.frament.main;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.yc.love.R;
import com.yc.love.adaper.rv.CreateMainT3Adapter;
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
import com.yc.love.ui.activity.PracticeLoveActivity;
import com.yc.love.ui.activity.PracticeTeachActivity;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    private final int ID_ITEM_TITLE_CASE = -1;
    private final int ID_ITEM_TITLE_CURE = -2;

    private CreateMainT3Adapter mainT3Adapter;

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
        initListener();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_course_rl);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mainT3Adapter = new CreateMainT3Adapter(mDatas);
        mRecyclerView.setAdapter(mainT3Adapter);
    }

    private void netTitleData() {

        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mCategoryArticleBeans = (List<CategoryArticleBean>) mCacheWorker.getCache(mMainActivity, "main1_Article_category");
            }
        });

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
        Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<MainT3Bean>>() {
            @Override
            public List<MainT3Bean> call(String s) {
                mDatas = (List<MainT3Bean>) mCacheWorker.getCache(mMainActivity, "main3_example_ts_category");
//                Log.e("TAG", "call: " + "Observable: " + mDatas.size());
                if (mDatas != null && mDatas.size() > 0) {
                    mainT3Adapter.setNewData(mDatas);
                }
                return mDatas;
            }
        }).subscribe();

        mLoadDialog = new LoadDialog(mMainActivity);
        mLoadDialog.showLoadingDialog();

//        mLoveEngin.exampleTs("example/ts").subscribe(new MySubscriber<AResultInfo<List<ExampleTsBean>>>(loadDialog) {
        mLoveEngin.exampleTsCategory("example/ts_category").subscribe(new MySubscriber<AResultInfo<ExampleTsCategory>>(mLoadDialog) {
            @Override
            protected void onNetNext(AResultInfo<ExampleTsCategory> exampleTsCategoryAResultInfo) {

                Log.e("TAG", "onNetNext: " + "mLoveEngin");
                if (exampleTsCategoryAResultInfo != null && exampleTsCategoryAResultInfo.code == HttpConfig.STATUS_OK && exampleTsCategoryAResultInfo.data != null) {
                    ExampleTsCategory exampleTsCategory = exampleTsCategoryAResultInfo.data;
                    createNewData(exampleTsCategory);
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

    private void createNewData(ExampleTsCategory exampleTsCategory) {
        if (exampleTsCategory == null) {
            return;
        }
        List<ExampleTsCategoryList> list1 = exampleTsCategory.list1;
        List<ExampleTsCategoryList> list2 = exampleTsCategory.list2;
        if (list1 == null && list2 == null) {
            return;
        }
        mDatas = new ArrayList<>();
        mDatas.add(new MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_TITLE));
//                mDatas.add(new MainT3Bean(4, "2", "学学别人怎么把女神撩到手", ID_ITEM_TITLE_CASE, R.mipmap.main_bg_item_title_case, "实战学习", 18));
//                mDatas.add(new MainT3Bean(4, "2", "浪漫情话让你撩妹不愁", ID_ITEM_TITLE_CURE, R.mipmap.main_bg_item_title_cure, "治愈情话", 18));
        mDatas.add(new MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, "入门秘籍"));

        if (list1 != null) {
            for (int i = 0; i < list1.size(); i++) {
                ExampleTsCategoryList categoryList = list1.get(i);
                mDatas.add(new MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
            }
        }
        mDatas.add(new MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM_TITLE, "进阶秘籍"));
        if (list2 != null) {
            for (int i = 0; i < list2.size(); i++) {
                ExampleTsCategoryList categoryList = list2.get(i);
                mDatas.add(new MainT3Bean(MainT3Bean.LOVE_HEAL_TYPE_ITEM, categoryList._level, categoryList.desp, categoryList.id, categoryList.image, categoryList.name, categoryList.parent_id));
            }
        }
        mCacheWorker.setCache("main3_example_ts_category", mDatas);
        mainT3Adapter.setNewData(mDatas);
    }

    private void initListener() {
        mainT3Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MainT3Bean item = mainT3Adapter.getItem(position);
                if (item != null) {
                    if (MainT3Bean.LOVE_HEAL_TYPE_ITEM == item.type || MainT3Bean.LOVE_HEAL_TYPE_ITEM_LOCALITY == item.type) {
                        if (position < 0 || mDatas == null || mDatas.size() == 0) {
                            return;
                        }

                        int id = item.id;
                        if (id < 0) {
                            switch (id) {
                                case ID_ITEM_TITLE_CASE:
                                    startActivity(new Intent(mMainActivity, LoveCaseActivity.class));
                                    break;
                                case ID_ITEM_TITLE_CURE:
                                    startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
                                    break;
                            }
                        } else {
                            LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, item.name, String.valueOf(id));
                        }
                    }

                }
            }
        });
        mainT3Adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MainT3Bean item = mainT3Adapter.getItem(position);
                if (item != null) {
                    if (MainT3Bean.LOVE_HEAL_TYPE_TITLE == item.type) {
                        switch (view.getId()) {
                            case R.id.item_t3title_iv_title://表白入口
                                startActivity(new Intent(mMainActivity, ExpressActivity.class));
                                break;
                            case R.id.item_t3title_tv_icon_01:
                                startLoveByStagesActivity(0, "单身期");
                                break;
                            case R.id.item_t3title_tv_icon_02:
                                startLoveByStagesActivity(1, "追求期");
                                break;
                            case R.id.item_t3title_tv_icon_03:
                                startLoveByStagesActivity(2, "热恋期");
                                break;
                            case R.id.item_t3title_tv_icon_04:
                                startLoveByStagesActivity(3, "失恋期");
                                break;
                            case R.id.item_t3title_tv_icon_05:
                                startLoveByStagesActivity(4, "婚后期");
                                break;
                            case R.id.iv_practice_teach:
                                //实战学习
                                startActivity(new Intent(mMainActivity, PracticeTeachActivity.class));
                                break;
                            case R.id.iv_practice_love:
                                //实战情话
                                startActivity(new Intent(mMainActivity, PracticeLoveActivity.class));
                                break;

                        }
                    }
                }

            }
        });
    }


    private void startLoveByStagesActivity(int position, String title) {
        if (mCategoryArticleBeans == null || mCategoryArticleBeans.size() < position + 1) {
            return;
        }
        CategoryArticleBean categoryArticleBean = mCategoryArticleBeans.get(position);
        ArrayList<CategoryArticleChildrenBean> children = categoryArticleBean.children;
        LoveByStagesActivity.startLoveByStagesActivity(mMainActivity, title, children);
    }


}
