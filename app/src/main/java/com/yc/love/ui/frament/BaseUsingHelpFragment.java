package com.yc.love.ui.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.love.ui.activity.UsingHelpActivity;
import com.yc.love.ui.activity.base.BaseActivity;
import com.yc.love.ui.frament.base.BaseLazyFragment;

/**
 * Created by mayn on 2019/5/24.
 */

public abstract class BaseUsingHelpFragment extends BaseLazyFragment {
    public BaseActivity mUsingHelpActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUsingHelpActivity = (BaseActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }

}
