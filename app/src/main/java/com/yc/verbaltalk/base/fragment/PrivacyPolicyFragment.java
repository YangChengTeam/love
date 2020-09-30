package com.yc.verbaltalk.base.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.utils.UIUtils;
import com.yc.verbaltalk.mine.ui.activity.PrivacyStatementActivity;
import com.yc.verbaltalk.mine.ui.activity.UserPolicyActivity;

/**
 * Created by suns  on 2020/9/9 14:31.
 */
public class PrivacyPolicyFragment extends BaseDialogFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_privacy_policy;
    }

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void init() {
        setCancelable(false);

        TextView tvServiceClause = rootView.findViewById(R.id.tv_service_clause);
        TextView tvPrivacy = rootView.findViewById(R.id.tv_privacy);
        TextView tvKnow = rootView.findViewById(R.id.tv_agree);
        TextView tvNotAgree = rootView.findViewById(R.id.tv_not_agree);
        TextView tvDesc = rootView.findViewById(R.id.tv_desc);
        tvDesc.setText(String.format(getString(R.string.privacy_desc), UIUtils.getAppName(getActivity())));
        tvServiceClause.setOnClickListener(v -> startActivity(new Intent(mContext, PrivacyStatementActivity.class)));
        tvPrivacy.setOnClickListener(v -> startActivity(new Intent(mContext, UserPolicyActivity.class)));
        tvKnow.setOnClickListener(v -> {
            if (onClickBtnListener != null) {
                onClickBtnListener.onBtnClick();
            }

            dismiss();
        });
        tvNotAgree.setOnClickListener(v -> {
            dismiss();

            if (getActivity() != null) {
                getActivity().finish();
            }
            System.exit(0);
        });
    }


    private OnClickBtnListener onClickBtnListener = null;

    public void setOnClickBtnListener(OnClickBtnListener listener) {
        this.onClickBtnListener = listener;
    }

    public interface OnClickBtnListener {
        void onBtnClick();
    }
}
