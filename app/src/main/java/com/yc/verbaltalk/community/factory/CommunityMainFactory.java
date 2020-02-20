package com.yc.verbaltalk.community.factory;

import android.util.SparseArray;

import com.yc.verbaltalk.community.ui.fragment.CommunityFragment;
import com.yc.verbaltalk.community.ui.fragment.CommunityHotFragment;
import com.yc.verbaltalk.community.ui.fragment.CommunityMyFragment;

import androidx.fragment.app.Fragment;

/**
 * Created by suns  on 2019/8/29 10:09.
 */
public class CommunityMainFactory {

    private static final int FRAGMENT_0 = 0;
    private static final int FRAGMENT_1 = 1;
    private static final int FRAGMENT_2 = 2;
    public static SparseArray<Fragment> fragments = new SparseArray<>();

    public static Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case FRAGMENT_0:
                fragment = new CommunityFragment();
                fragments.put(FRAGMENT_0, fragment);
                break;
            case FRAGMENT_1:
                fragment = new CommunityHotFragment();
                fragments.put(FRAGMENT_1, fragment);
                break;
            case FRAGMENT_2:
                fragment = new CommunityMyFragment();
                fragments.put(FRAGMENT_2, fragment);
        }
        return fragment;
    }
}
