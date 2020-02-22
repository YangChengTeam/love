package com.yc.verbaltalk.skill.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.base.activity.MainActivity;

/**
 * Created by mayn on 2019/4/23.
 */

public abstract class BaseMainT2ChildFragment extends BaseLazyFragment {

    public MainActivity mMainActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }


}
