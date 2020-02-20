package com.yc.verbaltalk.mine.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.pay.adapter.VipItemAdapter;
import com.yc.verbaltalk.chat.bean.BecomeVipBean;
import com.yc.verbaltalk.chat.bean.IndexDoodsBean;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BecomeVipAdapter extends BaseMultiItemQuickAdapter<BecomeVipBean, BaseViewHolder> {


    private int mNumber;


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BecomeVipAdapter(List<BecomeVipBean> data) {
        super(data);
        addItemType(BecomeVipBean.VIEW_TITLE, R.layout.recycler_view_item_become_vip_title);
        addItemType(BecomeVipBean.VIEW_ITEM, R.layout.recycler_view_item_become_vip_new);
        addItemType(BecomeVipBean.VIEW_TAIL, R.layout.recycler_view_item_become_vip_tail);
        addItemType(BecomeVipBean.VIEW_VIP_TAG, R.layout.recycler_view_item_become_vip_tag);
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, BecomeVipBean item) {
        if (item != null) {
            switch (item.type) {
                case BecomeVipBean.VIEW_TITLE:
                    break;
                case BecomeVipBean.VIEW_ITEM:
//                    helper.setImageResource(R.love_id.item_become_vip_iv_icon, item.imgResId);
//                    helper.setText(R.love_id.item_become_vip_tv_name, item.name)
//                            .setText(R.love_id.item_become_vip_tv_des, item.subName);
                    break;
                case BecomeVipBean.VIEW_TAIL:
                    RecyclerView recyclerView = helper.getView(R.id.pay_item_recyclerView);
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                    final VipItemAdapter vipItemAdapter = new VipItemAdapter(item.payBeans);
                    recyclerView.setAdapter(vipItemAdapter);
                    vipItemAdapter.setOnItemClickListener((adapter, view, position) -> {
                        vipItemAdapter.setSelect(position);
                        IndexDoodsBean doodsBean = vipItemAdapter.getItem(position);
                        if (onPayClickListener != null) {
                            onPayClickListener.onPayClick(doodsBean);
                        }
                    });

                    if (mNumber <= 0) {
                        mNumber = 156592;
                    }
                    helper.setText(R.id.item_become_vip_tv_pay_num, String.valueOf(mNumber))
                            .addOnClickListener(R.id.item_become_vip_rl_btn_wx)
                            .addOnClickListener(R.id.item_become_vip_rl_btn_zfb)
                            .addOnClickListener(R.id.item_become_vip_rl_btn_share);

                    break;
                case BecomeVipBean.VIEW_VIP_TAG:
                    break;
            }
        }
    }

    private OnPayClickListener onPayClickListener;

    public void setOnPayClickListener(OnPayClickListener onPayClickListener) {
        this.onPayClickListener = onPayClickListener;
    }

    public interface OnPayClickListener {
        void onPayClick(IndexDoodsBean doodsBean);
    }


}