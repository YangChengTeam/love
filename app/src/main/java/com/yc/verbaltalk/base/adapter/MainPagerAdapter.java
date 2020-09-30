package com.yc.verbaltalk.base.adapter;

import android.view.ViewGroup;

import com.yc.verbaltalk.index.factory.MainFragmentFactory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by sunshey on 2019/4/23.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager fm;

    public MainPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {//指定Fragmemt
        return MainFragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.show(fragment);
        /**
         * 使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
         * onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
         * 再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
         * 了，其效果是一样的。
         */
//        fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView(object);
        Fragment fragment = MainFragmentFactory.fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
