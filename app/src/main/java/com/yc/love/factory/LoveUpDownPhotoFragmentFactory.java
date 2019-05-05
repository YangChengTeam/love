package com.yc.love.factory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.yc.love.ui.frament.LoveByStagesFragment;
import com.yc.love.ui.frament.LoveUpDownPhotoFragment;
import com.yc.love.ui.frament.main.MainT1Fragment;
import com.yc.love.ui.frament.main.MainT2Fragment;
import com.yc.love.ui.frament.main.MainT3Fragment;
import com.yc.love.ui.frament.main.MainT4Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayn on 2019/4/23.
 */

public class LoveUpDownPhotoFragmentFactory {

    public static Map<Integer, Fragment> fragments = new HashMap<>();

    public static Fragment createFragment(int position, String dataString) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            return fragment;
        }
        fragment = createPositionFragment(position, dataString);
        fragments.put(position, fragment);
        return fragment;
    }

    private static Fragment createPositionFragment(int position, String dataString) {
        Log.d("mylog", "createPositionFragment: LoveUpDownPhotoFragment");
        Fragment fragment = new LoveUpDownPhotoFragment();
        Bundle args = new Bundle();
        args.putString("dataString", dataString);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }
}
