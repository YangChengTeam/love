package com.yc.love.adaper.vp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.yc.love.factory.LoveUpDownPhotoFragmentFactory;

import java.util.List;

import cn.youngkaaa.yviewpager.YFragmentPagerAdapter;
import cn.youngkaaa.yviewpager.YPagerAdapter;

/**
 * Created by mayn on 2019/4/23.
 */

public class LoveUpDownPhotoPagerAdapter extends YFragmentPagerAdapter  {

    private final FragmentManager fm;
    private final List<String> datas;
    private final String childUrl;

    public LoveUpDownPhotoPagerAdapter(FragmentManager fm,String childUrl, List<String> datas) {
        super(fm);
        this.fm = fm;
        this.datas = datas;
        this.childUrl = childUrl;
    }

    @Override
    public Fragment getItem(int position) {//指定Fragmemt
        Fragment fragment = LoveUpDownPhotoFragmentFactory.createFragment(position, childUrl,datas.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = LoveUpDownPhotoFragmentFactory.fragments.get(position);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
