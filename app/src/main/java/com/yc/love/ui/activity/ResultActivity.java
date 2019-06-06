package com.yc.love.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yc.love.R;
import com.yc.love.ui.activity.base.BaseSameActivity;

public class ResultActivity extends BaseSameActivity {


    private String mCreateTitle;
    private String mResImagePath;

    public static void startResultActivity(Context context, String resImagePath, String createTitle) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("resImagePath", resImagePath);
        intent.putExtra("createTitle", createTitle);
        context.startActivity(intent);
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        mResImagePath = intent.getStringExtra("resImagePath");
        mCreateTitle = intent.getStringExtra("createTitle");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (TextUtils.isEmpty(mResImagePath)) {
            showToastShort("获取图片失败");
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        ImageView imageView = findViewById(R.id.result_iv_img);

        Picasso.with(this).load(mResImagePath).into(imageView);
    }

    @Override
    protected String offerActivityTitle() {
        if (TextUtils.isEmpty(mCreateTitle)) {
            mCreateTitle = "合成成功";
        }
        return mCreateTitle;
    }
}
