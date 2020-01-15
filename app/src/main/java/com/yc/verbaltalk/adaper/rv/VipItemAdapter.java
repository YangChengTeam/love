package com.yc.verbaltalk.adaper.rv;

import android.graphics.Paint;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.model.bean.IndexDoodsBean;
import com.yc.verbaltalk.model.util.DoubleToStringUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wanglin  on 2019/7/2 10:06.
 */
public class VipItemAdapter extends BaseQuickAdapter<IndexDoodsBean, BaseViewHolder> {


    private SparseArray<ConstraintLayout> constraintLayoutSparseArray;
    private SparseArray<TextView> payUnitSparseArray;
    private SparseArray<TextView> payMonSparseArray;
    private SparseArray<TextView> descSparseArray;
    private SparseArray<ImageView> imageViewSparseArray;


    public VipItemAdapter(@Nullable List<IndexDoodsBean> data) {
        super(R.layout.vip_item_view, data);
        constraintLayoutSparseArray = new SparseArray<>();
        payUnitSparseArray = new SparseArray<>();
        payMonSparseArray = new SparseArray<>();
        descSparseArray = new SparseArray<>();
        imageViewSparseArray = new SparseArray<>();

    }

    @Override
    protected void convert(BaseViewHolder helper, IndexDoodsBean item) {
        helper.setText(R.id.item_become_vip_tail_tv_pay_tit_01, item.name)
                .setText(R.id.item_become_vip_tail_tv_pay_mon_01, DoubleToStringUtils.doubleStringToString(item.m_price))
                .setText(R.id.item_become_vip_tail_tv_original_price_01, "原价".concat(DoubleToStringUtils.doubleStringToString(item.price)));

        TextView tvOrigin = helper.getView(R.id.item_become_vip_tail_tv_original_price_01);
        tvOrigin.setPaintFlags(tvOrigin.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        helper.setText(R.id.item_become_vip_tail_tv_pay_des_01, item.desp);


        int position = helper.getAdapterPosition();
        constraintLayoutSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_cl_con_01));
        payUnitSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_unit_01));
        payMonSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_mon_01));
        descSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_tv_pay_des_01));
        imageViewSparseArray.put(position, helper.getView(R.id.item_become_vip_tail_iv_pay_sel_01));

        setSelect(0);


    }

    public void setSelect(int position) {
        resetState();
        constraintLayoutSparseArray.get(position).setSelected(true);
        payUnitSparseArray.get(position).setSelected(true);
        payMonSparseArray.get(position).setSelected(true);
        descSparseArray.get(position).setSelected(true);
        imageViewSparseArray.get(position).setVisibility(View.VISIBLE);


    }

    private void resetState() {
        for (int i = 0; i < constraintLayoutSparseArray.size(); i++) {
            constraintLayoutSparseArray.get(i).setSelected(false);
            payUnitSparseArray.get(i).setSelected(false);
            payMonSparseArray.get(i).setSelected(false);
            descSparseArray.get(i).setSelected(false);
            imageViewSparseArray.get(i).setVisibility(View.GONE);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                IndexDoodsBean doodsBean = mData.get(position);
                if (doodsBean.id == 12) {
                    return 2;
                }
                return 1;
            }
        });

    }
}
