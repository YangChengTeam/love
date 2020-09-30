package com.yc.verbaltalk.community.adapter;

import com.yc.verbaltalk.community.factory.CommunityMainFactory;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by suns  on 2019/8/29 10:06.
 */
public class CommunityMainPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTitleList;

    public CommunityMainPagerAdapter(FragmentManager fm, int behavior, List<String> titleList) {
        super(fm, behavior);

        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int i) {

        return CommunityMainFactory.createFragment(i);
    }

    @Override
    public int getCount() {
        return mTitleList != null ? mTitleList.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
