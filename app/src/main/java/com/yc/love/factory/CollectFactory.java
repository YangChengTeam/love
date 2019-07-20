package com.yc.love.factory;

import android.support.v4.app.Fragment;

import com.yc.love.ui.frament.CollectArticleFragment;
import com.yc.love.ui.frament.CollectAudioListFragment;
import com.yc.love.ui.frament.CollectExampleFragment;
import com.yc.love.ui.frament.CollectLoveHealDetailFragment;
import com.yc.love.ui.frament.CollectLoveHealingFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/5/5.
 */

public class CollectFactory {

    public static final int FRAGMENT_0 = 0;
    public static final int FRAGMENT_1 = 1;
    public static final int FRAGMENT_2 = 2;
    public static final int FRAGMENT_3 = 3;
    public static Map<Integer, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case FRAGMENT_0:
                fragment = new CollectLoveHealDetailFragment();
                fragments.put(FRAGMENT_0, fragment);

                break;
            case FRAGMENT_1:
                fragment = new CollectAudioListFragment();
                fragments.put(FRAGMENT_1, fragment);
                break;
            case FRAGMENT_2:
                fragment = new CollectLoveHealingFragment();
               /* Bundle args = new Bundle();
                args.putInt("category_id", love_id);
                args.putInt("position", position);
                fragment.setArguments(args);*/
                fragments.put(FRAGMENT_2, fragment);
                break;
            case FRAGMENT_3:
                fragment = new CollectExampleFragment();
               /* Bundle args = new Bundle();
                args.putInt("category_id", love_id);
                args.putInt("position", position);
                fragment.setArguments(args);*/
                fragments.put(FRAGMENT_3, fragment);
                break;

        }
        return fragment;
    }
}
