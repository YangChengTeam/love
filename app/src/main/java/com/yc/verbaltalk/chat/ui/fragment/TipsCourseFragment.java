package com.yc.verbaltalk.chat.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseMainFragment;
import com.yc.verbaltalk.base.utils.CommonInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.adapter.TipsCourseAdapter;
import com.yc.verbaltalk.chat.bean.CategoryArticleBean;
import com.yc.verbaltalk.chat.bean.CategoryArticleChildrenBean;
import com.yc.verbaltalk.chat.bean.ExampleTsCategory;
import com.yc.verbaltalk.chat.bean.ExampleTsCategoryList;
import com.yc.verbaltalk.chat.bean.MainT3Bean;
import com.yc.verbaltalk.chat.ui.activity.LoveByStagesActivity;
import com.yc.verbaltalk.chat.ui.activity.LoveCaseActivity;
import com.yc.verbaltalk.chat.ui.activity.LoveIntroductionActivity;
import com.yc.verbaltalk.chat.ui.activity.PracticeLoveActivity;
import com.yc.verbaltalk.chat.ui.activity.PracticeTeachActivity;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.skill.ui.fragment.ExpressFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by sunshey on 2019/6/17.
 * 秘籍
 */

public class TipsCourseFragment extends BaseMainFragment {

    private RecyclerView mRecyclerView;
    private List<CategoryArticleBean> mCategoryArticleBeans;

    private List<MainT3Bean> mDatas;
    private LoadDialog mLoadDialog;
    private LoveEngine mLoveEngin;

    private final int ID_ITEM_TITLE_CASE = -1;
    private final int ID_ITEM_TITLE_CURE = -2;

    private TipsCourseAdapter mainT3Adapter;

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
        mLoveEngin = new LoveEngine(mMainActivity);

        View viewBar = rootView.findViewById(R.id.main_t3_course_view_bar);
        mMainActivity.setStateBarHeight(viewBar, 1);

        initRecyclerView();
        initListener();
    }

    private void initRecyclerView() {
        mRecyclerView = rootView.findViewById(R.id.main_t3_course_rl);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mMainActivity, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mainT3Adapter = new TipsCourseAdapter(mDatas);
        mRecyclerView.setAdapter(mainT3Adapter);
    }

    private void netTitleData() {


        CommonInfoHelper.getO(mMainActivity, "main1_Article_category", new TypeReference<List<CategoryArticleBean>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<CategoryArticleBean>>) o -> mCategoryArticleBeans = o);


        mLoveEngin.categoryArticle().subscribe(new DisposableObserver<ResultInfo<List<CategoryArticleBean>>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<CategoryArticleBean>> listAResultInfo) {
                if (listAResultInfo != null && listAResultInfo.code == HttpConfig.STATUS_OK) {
                    List<CategoryArticleBean> data = listAResultInfo.data;
                    if (data == null || data.size() == 0) {
                        return;
                    }
                    mCategoryArticleBeans = data;
//                mCacheWorker.setCache("main1_Article_category", mCategoryArticleBeans);
                    CommonInfoHelper.setO(mMainActivity, mCategoryArticleBeans, "main1_Article_category");
                }
            }


        });
    }

    private void netData() {


        CommonInfoHelper.getO(mMainActivity, "main3_example_ts_category", new TypeReference<List<MainT3Bean>>() {
        }.getType(), (CommonInfoHelper.onParseListener<List<MainT3Bean>>) o -> {
            mDatas = o;
            if (mDatas != null && mDatas.size() > 0) {
                mainT3Adapter.setNewData(mDatas);
            }
        });


        mLoadDialog = new LoadDialog(mMainActivity);
        mLoadDialog.showLoadingDialog();


        mLoveEngin.exampleTsCategory().subscribe(new DisposableObserver<ResultInfo<ExampleTsCategory>>() {
            @Override
            public void onComplete() {
                mLoadDialog.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mLoadDialog.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<ExampleTsCategory> exampleTsCategoryAResultInfo) {
                mLoadDialog.dismissLoadingDialog();
                if (exampleTsCategoryAResultInfo != null && exampleTsCategoryAResultInfo.code == HttpConfig.STATUS_OK && exampleTsCategoryAResultInfo.data != null) {
                    ExampleTsCategory exampleTsCategory = exampleTsCategoryAResultInfo.data;
                    createNewData(exampleTsCategory);
                }
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
        CommonInfoHelper.setO(mMainActivity, mDatas, "main3_example_ts_category");
        mainT3Adapter.setNewData(mDatas);
    }

    private void initListener() {
        mainT3Adapter.setOnItemClickListener((adapter, view, position) -> {

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
//                                startActivity(new Intent(mMainActivity, LoveHealingActivity.class));
                                break;
                        }
                    } else {
                        LoveIntroductionActivity.startLoveIntroductionActivity(mMainActivity, item.name, String.valueOf(id));
                    }
                }

            }
        });
        mainT3Adapter.setOnItemChildClickListener((adapter, view, position) -> {
            MainT3Bean item = mainT3Adapter.getItem(position);
            if (item != null) {
                if (MainT3Bean.LOVE_HEAL_TYPE_TITLE == item.type) {
                    switch (view.getId()) {
                        case R.id.item_t3title_iv_title://表白入口
                            startActivity(new Intent(mMainActivity, ExpressFragment.class));
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
