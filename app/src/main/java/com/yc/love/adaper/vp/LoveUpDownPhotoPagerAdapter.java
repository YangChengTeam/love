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

    public LoveUpDownPhotoPagerAdapter(FragmentManager fm, List<String> datas) {
        super(fm);
        Log.d("mylog", "LoveUpDownPhotoPagerAdapter: LoveUpDownPhotoFragment");
        this.fm = fm;
        this.datas = datas;
    }

    @Override
    public Fragment getItem(int position) {//指定Fragmemt
        Log.d("mylog", "getItem: LoveUpDownPhotoFragment");
        Fragment fragment = LoveUpDownPhotoFragmentFactory.createFragment(position, datas.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        Log.d("mylog", "getCount: LoveUpDownPhotoFragment "+datas.size());
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
