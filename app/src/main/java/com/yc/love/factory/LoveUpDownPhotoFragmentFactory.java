package com.yc.love.factory;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yc.love.ui.frament.LoveUpDownPhotoFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/4/23.
 */

public class LoveUpDownPhotoFragmentFactory {

    public static Map<Integer, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(int position,String childUrl, String dataString) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        fragment = createPositionFragment(position,childUrl, dataString);
        fragments.put(position, fragment);
        return fragment;
    }

    private static Fragment createPositionFragment(int position,String childUrl, String dataString) {
        Fragment fragment = new LoveUpDownPhotoFragment();
        Bundle args = new Bundle();
        args.putString("dataString", dataString);
        args.putString("childUrl", childUrl);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }
}
