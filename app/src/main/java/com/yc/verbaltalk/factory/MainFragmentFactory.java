package com.yc.verbaltalk.factory;

import android.util.SparseArray;

import com.yc.verbaltalk.ui.frament.ChatFragment;
import com.yc.verbaltalk.ui.frament.main.CommunityMainFragment;
import com.yc.verbaltalk.ui.frament.main.IndexFragment;
import com.yc.verbaltalk.ui.frament.main.MainT2NewFragment;
import com.yc.verbaltalk.ui.frament.main.MineFragment;
import com.yc.verbaltalk.ui.frament.main.TipsCourseFragment;

import androidx.fragment.app.Fragment;

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

                fragment = new MainT2NewFragment();

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
