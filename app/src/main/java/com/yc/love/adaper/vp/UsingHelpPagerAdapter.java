package com.yc.love.adaper.vp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.love.factory.CollectFactory;
import com.yc.love.factory.UsingHelpFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class UsingHelpPagerAdapter extends FragmentPagerAdapter {
    private  int[] imageResId;
    //    private List<String> titleJobTypeList;
    private final FragmentManager fm;

    public UsingHelpPagerAdapter(FragmentManager fm, int[] imageResId) {
        super(fm);
        this.fm = fm;
        this.imageResId = imageResId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = UsingHelpFactory.createFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return imageResId.length;
    }


 /*   @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);//页卡标题
    }*/

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
        Fragment fragment = UsingHelpFactory.fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }
}
