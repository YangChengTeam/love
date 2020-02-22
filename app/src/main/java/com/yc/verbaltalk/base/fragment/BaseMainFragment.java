package com.yc.verbaltalk.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.activity.MainActivity;

/**
 * Created by mayn on 2019/4/23.
 */

public abstract class BaseMainFragment extends BaseLazyFragment {

    public MainActivity mMainActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }


}
