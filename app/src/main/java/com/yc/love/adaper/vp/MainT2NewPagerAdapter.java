package com.yc.love.adaper.vp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yc.love.factory.MainT2NewFactory;

import java.util.List;

/**
 * Created by mayn on 2019/5/5.
 */

public class MainT2NewPagerAdapter extends FragmentPagerAdapter {
    private List<String> titleList;


    public MainT2NewPagerAdapter(FragmentManager fm, List<String> titleList) {
        super(fm);
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return MainT2NewFactory.createFragment(position);
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
