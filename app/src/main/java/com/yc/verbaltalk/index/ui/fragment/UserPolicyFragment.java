package com.yc.verbaltalk.index.ui.fragment;

import android.widget.ImageView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.fragment.BaseDialogFragment;

import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by suns  on 2020/4/17 16:28.
 */
public class UserPolicyFragment extends BaseDialogFragment {

    ImageView ivClose;

    @Override
    protected float getWidth() {
        return 0.7f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 3 / 5;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_policy;
    }

    @Override
    public void init() {

        ivClose= rootView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> dismiss());

    }
}
