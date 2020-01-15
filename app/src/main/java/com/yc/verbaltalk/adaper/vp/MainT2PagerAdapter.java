package com.yc.verbaltalk.adaper.vp;

import com.yc.verbaltalk.factory.MainT2Factory;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by mayn on 2019/5/5.
 */

public class MainT2PagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;


    public MainT2PagerAdapter(FragmentManager fm, List<String> titleList) {
        super(fm);
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return MainT2Factory.createFragment(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);//页卡标题
    }

}
