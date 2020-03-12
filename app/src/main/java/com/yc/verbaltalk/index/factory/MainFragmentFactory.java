package com.yc.verbaltalk.index.factory;

import android.util.SparseArray;

import androidx.fragment.app.Fragment;

import com.yc.verbaltalk.chat.ui.fragment.TipsCourseFragment;
import com.yc.verbaltalk.community.ui.fragment.CommunityMainFragment;
import com.yc.verbaltalk.index.ui.fragment.IndexFragment;
import com.yc.verbaltalk.mine.ui.fragment.MineFragment;
import com.yc.verbaltalk.skill.ui.fragment.SkillFragment;


/**
 * Created by mayn on 2019/4/23.
 */

public class MainFragmentFactory {
    public static final int MAIN_FRAGMENT_0 = 0;
    public static final int MAIN_FRAGMENT_1 = 1;
    public static final int MAIN_FRAGMENT_2 = 2;
    public static final int MAIN_FRAGMENT_3 = 3;
    public static final int MAIN_FRAGMENT_4 = 4;


    public static SparseArray<Fragment> fragments = new SparseArray<>();


    public static Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case MAIN_FRAGMENT_0:
                fragment = new IndexFragment();
                fragments.put(MAIN_FRAGMENT_0, fragment);
                break;
            case MAIN_FRAGMENT_1:

                fragment = new SkillFragment();

                fragments.put(MAIN_FRAGMENT_1, fragment);
                break;
            case MAIN_FRAGMENT_2:

                fragment = new CommunityMainFragment();

                fragments.put(MAIN_FRAGMENT_2, fragment);
                break;
            case MAIN_FRAGMENT_3:
//                fragment = new WealFragment();

                fragment = new TipsCourseFragment();
//                fragment = new ChatFragment();


                fragments.put(MAIN_FRAGMENT_3, fragment);
                break;
            case MAIN_FRAGMENT_4:
                fragment = new MineFragment();
                fragments.put(MAIN_FRAGMENT_4, fragment);
                break;
        }
        return fragment;
    }
}
