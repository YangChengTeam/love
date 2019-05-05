package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.MainT2Bean;
import com.yc.love.model.bean.StringBean;

/**
 * Created by mayn on 2019/4/26.
 */

public class MainT2TitleViewHolder extends BaseViewHolder<MainT2Bean> {

    private final Context context;
    private OnClickShareListent onClickShareListent;

    public MainT2TitleViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_title_t2_view, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(MainT2Bean mainT2Bean) {
       /* ImageView ivTitle = itemView.findViewById(R.id.item_title_view_iv_title);
        RelativeLayout rlShare = itemView.findViewById(R.id.item_title_view_rl_share);
        ImageView ivShare = itemView.findViewById(R.id.item_title_view_iv_share);
//        RelativeLayout rlShare = itemView.findViewById(R.id.item_title_view_view_temp);
        View viewTemp = itemView.findViewById(R.id.item_title_view_view_temp);
        *//*ViewGroup.LayoutParams layoutParams = rlShare.getLayoutParams();
        int heightShare = layoutParams.height;
        int heightShareHalf = heightShare / 2;*//*


         *//*ViewGroup.LayoutParams layoutParams1 = viewTemp.getLayoutParams();
        layoutParams1.height=heightShareHalf;
        viewTemp.setLayoutParams(layoutParams1);*//*


        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ivTitle.measure(w, w);
        rlShare.measure(w, w);
        int height = ivTitle.getMeasuredHeight();
        int width = ivTitle.getMeasuredWidth();
        Log.d("mylog", "bindData: ivTitle height " + height + " width " + width);
        int rlShareMeasuredHeight = rlShare.getMeasuredHeight();
        Log.d("mylog", "bindData: rlShare.measure  height " + rlShareMeasuredHeight + " width " + rlShare.getMeasuredWidth());
        RelativeLayout.LayoutParams layoutParamsRlShare = (RelativeLayout.LayoutParams) rlShare.getLayoutParams();
        int stateBar3 = getStateBar3();
        int rlShareMarginTop = height - rlShareMeasuredHeight - stateBar3;
        Log.d("mylog", "bindData: rlShareMarginTop " + rlShareMarginTop);
        layoutParamsRlShare.setMargins(0, rlShareMarginTop, 0, 0);

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickShareListent.clickShareListent();
            }
        });*/
    }

    public interface OnClickShareListent {
        void clickShareListent();
    }

    public void setOnClickShareListent(OnClickShareListent onClickShareListent) {
        this.onClickShareListent = onClickShareListent;
    }

    private int getStateBar3() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("mylog", "getStateBar3: result " + result);
        return result;
//        tv_result3.setText("方法3------》" + result);
    }


}
