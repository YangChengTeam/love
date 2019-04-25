package com.yc.love.factory;

import android.support.v4.app.Fragment;

import com.yc.love.ui.frament.MainT1Fragment;
import com.yc.love.ui.frament.MainT2Fragment;
import com.yc.love.ui.frament.MainT3Fragment;
import com.yc.love.ui.frament.MainT4Fragment;
import com.yc.love.ui.frament.MainT5Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainFragmentFactory {
    public static final int MAIN_FRAGMENT_0 = 0;
    public static final int MAIN_FRAGMENT_1 = 1;
    public static final int MAIN_FRAGMENT_2 = 2;
    public static final int MAIN_FRAGMENT_3 = 3;
    public static final int MAIN_FRAGMENT_4 = 4;


    public static Map<Integer, Fragment> fragments = new HashMap<>();


    public static Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case MAIN_FRAGMENT_0:
                fragment = new MainT1Fragment();
                fragments.put(MAIN_FRAGMENT_0, fragment);
                break;
            case MAIN_FRAGMENT_1:
                fragment = new MainT2Fragment();
                fragments.put(MAIN_FRAGMENT_1, fragment);
                break;
            case MAIN_FRAGMENT_2:
                fragment = new MainT3Fragment();
                fragments.put(MAIN_FRAGMENT_2, fragment);
                break;
            case MAIN_FRAGMENT_3:
                fragment = new MainT4Fragment();
                fragments.put(MAIN_FRAGMENT_3, fragment);
                break;
            case MAIN_FRAGMENT_4:
                fragment = new MainT5Fragment();
                fragments.put(MAIN_FRAGMENT_4, fragment);
                break;
        }
        return fragment;
    }
}
