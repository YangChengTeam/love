package com.yc.verbaltalk.mine.adapter;

import android.view.ViewGroup;

import com.yc.verbaltalk.mine.factory.UsingHelpFactory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Administrator on 2017/10/19.
 */

public class UsingHelpPagerAdapter extends FragmentPagerAdapter {
    private int[] imageResId;
    //    private List<String> titleJobTypeList;
    private final FragmentManager fm;

    public UsingHelpPagerAdapter(FragmentManager fm,int behavior, int[] imageResId) {
        super(fm,behavior);
        this.fm = fm;
        this.imageResId = imageResId;
    }

    @Override
    public Fragment getItem(int position) {
        return UsingHelpFactory.createFragment(position);
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
    public Fragment instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
//        fm.beginTransaction().show(fragment).commit();
        fm.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = UsingHelpFactory.fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }
}
