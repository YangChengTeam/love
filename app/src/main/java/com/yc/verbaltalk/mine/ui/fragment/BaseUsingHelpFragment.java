package com.yc.verbaltalk.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.activity.BaseActivity;
import com.yc.verbaltalk.base.fragment.BaseLazyFragment;

/**
 * Created by sunshey on 2019/5/24.
 */

public abstract class BaseUsingHelpFragment extends BaseLazyFragment {
    public BaseActivity mUsingHelpActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUsingHelpActivity = (BaseActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }

}
