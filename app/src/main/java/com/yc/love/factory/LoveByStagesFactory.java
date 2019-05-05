package com.yc.love.factory;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yc.love.ui.frament.LoveByStagesFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveByStagesFactory {


    public static Map<Integer, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(int position, String jobType) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        fragment = createJobTypePervasiveFragment(position, jobType);
        fragments.put(position, fragment);
        return fragment;
    }

    private static Fragment createJobTypePervasiveFragment(int position, String jobType) {
        Fragment fragment = new LoveByStagesFragment();
        Bundle args = new Bundle();
//        args.putString("jobType", jobType);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }
}
