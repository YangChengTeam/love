package com.yc.verbaltalk.chat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.chat.ui.activity.LoveByStagesActivity;

/**
 * Created by sunshey on 2019/5/5.
 */

public  abstract class BaseLoveByStagesFragment  extends BaseLazyFragment {

    public LoveByStagesActivity mLoveByStagesActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoveByStagesActivity = (LoveByStagesActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }


}

