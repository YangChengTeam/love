package com.yc.verbaltalk.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.ui.activity.base.BaseSameActivity;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

/**
 * Created by suns  on 2020/1/16 10:27.
 */
public class ConsultDetailActivity extends BaseSameActivity {
    @Override
    protected String offerActivityTitle() {
        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        initView();
    }


    private void initView() {
        ImageView ivConsult = findViewById(R.id.iv_consult_teacher);

        ivConsult.setOnClickListener(v -> showToWxServiceDialog(null));

        float dis = ivConsult.getTranslationX();
        float disy = ivConsult.getTranslationY();


        ObjectAnimator translationx = ObjectAnimator.ofFloat(ivConsult, "translationX", dis, 30);
        ObjectAnimator translationy = ObjectAnimator.ofFloat(ivConsult, "translationY", disy, -30);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationx).with(translationy);
        translationx.setDuration(3000);
        translationx.setRepeatMode(ValueAnimator.REVERSE);
        translationx.setRepeatCount(ValueAnimator.INFINITE);

        translationy.setDuration(3000);
        translationy.setRepeatMode(ValueAnimator.REVERSE);
        translationy.setRepeatCount(ValueAnimator.INFINITE);


//        translationx.start();
//设置插值器
        animatorSet.setInterpolator(new LinearOutSlowInInterpolator());
        animatorSet.start();


//        animatorSet.addListener();
    }

}
