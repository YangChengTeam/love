package com.yc.love.factory;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yc.love.ui.frament.CollectArticleFragment;
import com.yc.love.ui.frament.CollectLoveHealingFragment;
import com.yc.love.ui.frament.LoveByStagesFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/5/5.
 */

public class CollectFactory {

    public static final int FRAGMENT_0 = 0;
    public static final int FRAGMENT_1 = 1;
    public static Map<Integer, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        switch (position) {
            case FRAGMENT_0:
                fragment = new CollectLoveHealingFragment();
                fragments.put(FRAGMENT_0, fragment);
                break;
            case FRAGMENT_1:
                fragment = new CollectArticleFragment();
               /* Bundle args = new Bundle();
                args.putInt("category_id", id);
                args.putInt("position", position);
                fragment.setArguments(args);*/
                fragments.put(FRAGMENT_1, fragment);
                break;
        }
        return fragment;
    }
}
