package com.yc.verbaltalk.ui.frament.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by mayn on 2019/4/23.
 */

public abstract class BaseFragment extends Fragment {


//    public String LOG_TAG_FRAGMENT_NAME;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LOG_TAG_FRAGMENT_NAME = getClass().getName() + " ";
        Log.d("ClassName", "onCreate: ------Fragment :" + getClass().getName());
        return onFragmentCreateView(inflater, container, savedInstanceState);
    }

    protected abstract View onFragmentCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}