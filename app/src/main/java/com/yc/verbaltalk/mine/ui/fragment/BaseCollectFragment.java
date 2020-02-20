package com.yc.verbaltalk.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.verbaltalk.base.fragment.BaseLazyFragment;
import com.yc.verbaltalk.mine.ui.activity.CollectActivity;

/**
 * Created by mayn on 2019/5/5.
 */

public  abstract class BaseCollectFragment extends BaseLazyFragment {

    public CollectActivity mCollectActivity;

    @Override
    protected View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCollectActivity = (CollectActivity) getActivity();
        return super.onFragmentCreateView(inflater, container, savedInstanceState);
    }


}

