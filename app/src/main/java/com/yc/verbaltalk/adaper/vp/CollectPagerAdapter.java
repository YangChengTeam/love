package com.yc.verbaltalk.adaper.vp;

import android.view.ViewGroup;

import com.yc.verbaltalk.factory.CollectFactory;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by mayn on 2019/5/5.
 */

public class CollectPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
//    private List<String> titleJobTypeList;
    private final FragmentManager fm;

    public CollectPagerAdapter(FragmentManager fm, List<String> titleList) {
        super(fm);
        this.fm = fm;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = CollectFactory.createFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);//页卡标题
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
//        fm.beginTransaction().show(fragment).commit();
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = CollectFactory.fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }
}
