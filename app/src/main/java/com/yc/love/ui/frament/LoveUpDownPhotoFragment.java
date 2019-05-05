package com.yc.love.ui.frament;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.ui.frament.base.BaseLazyFragment;
import com.yc.love.ui.frament.base.BaseLoveByStagesFragment;

/**
 * Created by mayn on 2019/5/5.
 */

public class LoveUpDownPhotoFragment extends BaseLazyFragment {

    private RecyclerView mRecyclerView;
    private String mDataString;

    @Override
    protected int setContentView() {
        initBundle();
        Log.d("mylog", "setContentView: LoveUpDownPhotoFragment ");
        return R.layout.fragment_love_up_down_photo;
    }

    @Override
    protected void initViews() {
        TextView tv = rootView.findViewById(R.id.fragment_love_up_down_photo_tv);
        tv.setText(mDataString);
    }



    private void initBundle() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position");
            mDataString = arguments.getString("dataString", "-1");
        }
    }

    @Override
    protected void lazyLoad() {
      /*  List<StringBean> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            StringBean stringBean = new StringBean("name " + i);
            datas.add(stringBean);
        }
        NoThingAdapter<StringBean> adapter = new NoThingAdapter<StringBean>(datas, mRecyclerView) {
            @Override
            public BaseViewHolder getHolder(ViewGroup parent) {
                return new StringBeanViewHolder(mLoveByStagesActivity, null, parent);
            }
        };
        mRecyclerView.setAdapter(adapter);*/
    }
}
