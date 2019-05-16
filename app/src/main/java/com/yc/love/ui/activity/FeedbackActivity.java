package com.yc.love.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.love.R;
import com.yc.love.model.base.MySubscriber;
import com.yc.love.model.bean.IdCorrelationLoginBean;
import com.yc.love.model.engin.IdCorrelationEngin;
import com.yc.love.model.single.YcSingle;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class FeedbackActivity extends BaseSameActivity {

    private TextView mTvQqNum;
    private EditText mEtWorkContent;
    private String mTrimEtWorkContent;
    private EditText mEtQq;
    private String mTrimEtQq;
    private EditText mEtWx;
    private IdCorrelationEngin mIdCorrelationEngin;
    private String mMTrimEtWx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mIdCorrelationEngin = new IdCorrelationEngin(this);
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
            showToastShort("内容不能为空");
            return false;
        }
        mTrimEtQq = mEtQq.getText().toString().trim();
        if (TextUtils.isEmpty(mTrimEtQq)) {
            showToastShort("QQ号不能为空");
            return false;
        }
        if (mTrimEtQq.length() < 6) {
            showToastShort("QQ号格式错误");
            return false;
        }
        if (mTrimEtWorkContent.length() < 6) {
            showToastShort("最少输入一句话反馈");
            return false;
        }
        mMTrimEtWx = mEtWx.getText().toString();
        if (!TextUtils.isEmpty(mMTrimEtWx) && !"null".equals(mMTrimEtWx)) {
            if (mTrimEtQq.length() < 2) {
                showToastShort("微信号格式错误");
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
                showToastShort("复制客服QQ号成功");
                break;
            case R.id.feedback_tv_next:
                if (checkInput()) {
                    Log.d("mylog", "onClick: mTrimEtQq qq号" + mTrimEtQq + " mTrimEtWorkContent 输入内容" + mTrimEtWorkContent);
                    netNext();
                }
                break;
        }
    }

    private void netNext() {
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            showToLoginDialog();
            return;
        }
        mIdCorrelationEngin.addSuggestion(String.valueOf(id), mTrimEtWorkContent, mTrimEtQq, mMTrimEtWx, "Suggestion/add").subscribe(new MySubscriber<ResultInfo<String>>(mLoadingDialog) {
            @Override
            protected void onNetNext(ResultInfo<String> stringResultInfo) {
                String message = stringResultInfo.message;
                showToastShort(message);
                finish();
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    @Override
    protected String offerActivityTitle() {
        return "意见反馈";
    }
}
