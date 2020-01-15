package com.yc.verbaltalk.ui.frament.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.ui.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

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
