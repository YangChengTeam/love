package com.yc.love.ui.frament.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.love.ui.activity.LoveByStagesActivity;

/**
 * Created by mayn on 2019/5/5.
 */

public  abstract class BaseLoveByStagesFragment  extends BaseLazyFragment {

    public LoveByStagesActivity mLoveByStagesActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoveByStagesActivity = (LoveByStagesActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }


}

