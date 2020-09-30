package com.yc.verbaltalk.skill.adapter;

import android.view.ViewGroup;

import com.yc.verbaltalk.skill.factory.LoveUpDownPhotoFragmentFactory;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cn.youngkaaa.yviewpager.YFragmentPagerAdapter;

/**
 * Created by sunshey on 2019/4/23.
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
        return LoveUpDownPhotoFragmentFactory.createFragment(position, childUrl,datas.get(position));
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
