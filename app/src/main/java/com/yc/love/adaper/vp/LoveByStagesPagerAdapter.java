package com.yc.love.adaper.vp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.yc.love.factory.LoveByStagesFactory;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveByStagesPagerAdapter extends FragmentPagerAdapter {
    private final List<Integer> mIdLists;
    private List<String> titleList;
//    private List<String> titleJobTypeList;
    private final FragmentManager fm;

    public LoveByStagesPagerAdapter(FragmentManager fm, List<String> titleList,List<Integer> idLists) {
        super(fm);
        this.fm = fm;
        this.titleList = titleList;
        this.mIdLists = idLists;
//        this.titleJobTypeList = titleJobTypeList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = LoveByStagesFactory.createFragment(position, mIdLists.get(position));
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

//    @Override
//    public Fragment instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container,
//                position);
//        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
////        fm.beginTransaction().show(fragment).commit();
//        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
//        return fragment;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        Fragment fragment = LoveByStagesFactory.fragments.get(position);
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        if (fragment != null) {
//            fragmentTransaction.hide(fragment);
//        }
//        fragmentTransaction.commit();
//    }
}
