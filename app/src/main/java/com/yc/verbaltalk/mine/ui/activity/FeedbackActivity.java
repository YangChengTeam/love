package com.yc.verbaltalk.mine.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.engine.UserInfoEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

public class FeedbackActivity extends BaseSameActivity {

    private TextView mTvQqNum;
    private EditText mEtWorkContent;
    private String mTrimEtWorkContent;
    private EditText mEtQq;
    private String mTrimEtQq;
    private EditText mEtWx;
    private UserInfoEngine mIdCorrelationEngin;
    private String mMTrimEtWx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mIdCorrelationEngin = new UserInfoEngine(this);
        initViews();
        initDatas();
    }

    private void initDatas() {

    }

    private void initViews() {


        mEtQq = findViewById(R.id.feedback_et_qq);
        mEtWx = findViewById(R.id.feedback_et_wx);
        mTvQqNum = findViewById(R.id.feedback_tv_qq_num);
        TextView tvCopyQq = findViewById(R.id.feedback_tv_cope);
        TextView tvNext = findViewById(R.id.feedback_tv_next);

        tvCopyQq.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        mEtWorkContent = findViewById(R.id.feedback_et_work_content);
        final TextView tv_num = findViewById(R.id.feedback_tv_num);

        mEtWorkContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_num.setText(String.valueOf(editable.length()));
            }
        });
    }

    private boolean checkInput() {
        mTrimEtWorkContent = mEtWorkContent.getText().toString().trim();
        if (TextUtils.isEmpty(mTrimEtWorkContent)) {
            ToastUtils.showCenterToast("内容不能为空");
            return false;
        }
        mTrimEtQq = mEtQq.getText().toString().trim();
        if (TextUtils.isEmpty(mTrimEtQq)) {
            ToastUtils.showCenterToast("QQ号不能为空");
            return false;
        }
        if (mTrimEtQq.length() < 6) {
            ToastUtils.showCenterToast("QQ号格式错误");
            return false;
        }
        if (mTrimEtWorkContent.length() < 6) {
            ToastUtils.showCenterToast("最少输入一句话反馈");
            return false;
        }
        mMTrimEtWx = mEtWx.getText().toString();
        if (!TextUtils.isEmpty(mMTrimEtWx) && !"null".equals(mMTrimEtWx)) {
            if (mTrimEtQq.length() < 2) {
                ToastUtils.showCenterToast("微信号格式错误");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.feedback_tv_cope:
                String trimQqNum = mTvQqNum.getText().toString().trim();
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", trimQqNum);
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showCenterToast("复制客服QQ号成功");
                break;
            case R.id.feedback_tv_next:

                if (checkInput()) {
                    Log.d("mylog", "onClick: mTrimEtQq qq号" + mTrimEtQq + " mTrimEtWorkContent 输入内容" + mTrimEtWorkContent);
                    if (UserInfoHelper.isLogin(this))
                        netNext();
                }
                break;
        }
    }

    private void netNext() {

        mIdCorrelationEngin.addSuggestion(UserInfoHelper.getUid(), mTrimEtWorkContent, mTrimEtQq, mMTrimEtWx)
                .subscribe(new DisposableObserver<ResultInfo<String>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultInfo<String> stringResultInfo) {
                        if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                            String message = stringResultInfo.message;
                            ToastUtils.showCenterToast(message);
                            finish();
                        }
                    }

                });
    }

    @Override
    protected String offerActivityTitle() {
        return "意见反馈";
    }
}
