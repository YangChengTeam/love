package com.yc.love.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yc.love.R;
import com.yc.love.model.bean.IdCorrelationSmsBean;
import com.yc.love.model.engin.IdCorrelationEngin;

import rx.Subscriber;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

      /*  IdCorrelationEngin idCorrelationEngin = new IdCorrelationEngin(this);
        idCorrelationEngin.idCorrelationSms("0","15927678095").subscribe(new Subscriber<IdCorrelationSmsBean>() {
            @Override
            public void onCompleted() {
                Log.d("mylog", "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mylog", "onError: Throwable "+e);
            }

            @Override
            public void onNext(IdCorrelationSmsBean idCorrelationSmsBean) {
                Log.d("mylog", "onNext: idCorrelationSmsBean "+idCorrelationSmsBean.toString());
            }
        });*/
    }
}
