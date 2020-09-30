package com.yc.verbaltalk.base.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.BaseFragment;

/**
 * Created by sunshey on 2019/4/24.
 */

public abstract class BaseLazyFragment extends BaseFragment {

    protected View rootView;
    private boolean isInitView = false;
    private boolean isVisible = false;
    protected LoveEngine mLoveEngine;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(setContentView(), container, false);
        }
        mLoveEngine = new LoveEngine(getActivity());
        initBundle();
        initViews();
        isInitView = true;
        isCanLoadData();
        return rootView;
    }

    protected void initBundle() {

    }


    @Override
    public void onResume() {
        super.onResume();
        //新版本
        if (!isHidden() && isResumed()) {
            isVisible = true;
            isCanLoadData();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    private void isCanLoadData() {
        //所以条件是view初始化完成并且对用户可见
        if (isInitView && isVisible) {
            lazyLoad();
            //防止重复加载数据
            isInitView = false;
//            isVisible = false;
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.e("TAG", "setUserVisibleHint: " + isVisibleToUser + "  class:  " + getClass().getSimpleName());
//        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见，获取该标志记录下来
//        if (isVisibleToUser) {
//            isVisible = true;
//            isCanLoadData();
//        } else {
//            isVisible = false;
//        }
//    }


    /**
     * 加载页面布局文件
     */
    protected abstract int setContentView();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initViews();

    /**
     * 加载要显示的数据
     */
    protected abstract void lazyLoad();


}
