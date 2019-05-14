package com.yc.love.adaper.rv.holder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.BecomeVipBean;
import com.yc.love.model.bean.BecomeVipPayBean;
import com.yc.love.model.bean.IndexDoodsBean;

import java.util.ArrayList;
import java.util.List;


public class BecomeVipTailViewHolder extends BaseViewHolder<BecomeVipBean> {

    private final Context context;
    private int mSelectPosition;
    private int mSelectPayType;
    public int PAY_TYPE_ZFB=0;
    public int PAY_TYPE_WX=1;
    private ConstraintLayout[] clCons;
    private ImageView[] ivSels;
    private TextView[] tvUnits;
    private TextView[] tvMoneys;
    private ImageView mIvPayZfb;
    private ImageView mIvPayWx;
    private TextView mTvNext;
//    private List<BecomeVipPayBean> mPayBeans;
    private List<IndexDoodsBean>  mPayBeans;

    private OnClickTailListener onClickTailListener;

    public BecomeVipTailViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_become_vip_tail, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(BecomeVipBean becomeVipBean) {

        ConstraintLayout clCon01 = itemView.findViewById(R.id.item_become_vip_tail_cl_con_01);
        ConstraintLayout clCon02 = itemView.findViewById(R.id.item_become_vip_tail_cl_con_02);
        ConstraintLayout clCon03 = itemView.findViewById(R.id.item_become_vip_tail_cl_con_03);
        ConstraintLayout clCon04 = itemView.findViewById(R.id.item_become_vip_tail_cl_con_04);

        TextView tvTit01 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_tit_01);
        TextView tvTit02 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_tit_02);
        TextView tvTit03 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_tit_03);
        TextView tvTit04 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_tit_04);

        TextView tvUnit01 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_unit_01);
        TextView tvUnit02 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_unit_02);
        TextView tvUnit03 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_unit_03);
        TextView tvUnit04 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_unit_04);

        TextView tvMoney01 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_mon_01);
        TextView tvMoney02 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_mon_02);
        TextView tvMoney03 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_mon_03);
        TextView tvMoney04 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_mon_04);

        TextView tvDes01 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_des_01);
        TextView tvDes02 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_des_02);
        TextView tvDes03 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_des_03);
        TextView tvDes04 = itemView.findViewById(R.id.item_become_vip_tail_tv_pay_des_04);

        ImageView ivSel01 = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_sel_01);
        ImageView ivSel02 = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_sel_02);
        ImageView ivSel03 = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_sel_03);
        ImageView ivSel04 = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_sel_04);

        mIvPayZfb = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_zfb);
        mIvPayWx = itemView.findViewById(R.id.item_become_vip_tail_iv_pay_wx);
        mTvNext = itemView.findViewById(R.id.item_become_vip_tv_next);

//       List<TextView> tvTits= new ArrayList<>();
        TextView[] tvTits = {tvTit01, tvTit02, tvTit03, tvTit04};
        tvMoneys = new TextView[]{tvMoney01, tvMoney02, tvMoney03, tvMoney04};
        TextView[] tvDess = {tvDes01, tvDes02, tvDes03, tvDes04};
        clCons = new ConstraintLayout[]{clCon01, clCon02, clCon03, clCon04};
        ivSels = new ImageView[]{ivSel01, ivSel02, ivSel03, ivSel04};
        tvUnits = new TextView[]{tvUnit01, tvUnit02, tvUnit03, tvUnit04};

        mPayBeans = becomeVipBean.payBeans;
        for (int i = 0; i < mPayBeans.size(); i++) {
            IndexDoodsBean indexDoodsBean = mPayBeans.get(i);
            tvTits[i].setText(indexDoodsBean.name);
            tvMoneys[i].setText(String.valueOf(indexDoodsBean.m_price));
            tvDess[i].setText(indexDoodsBean.desp);
        }

        /*clCon01.setBackground(context.getResources().getDrawable(R.mipmap.become_vip_bg_pay_s));
        ivSel01.setVisibility(View.VISIBLE);
        tvUnit01.setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon_s));
        tvMoney01.setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon_s));*/
        mSelectPosition = 0;
        cancelAllSelectPosition(mSelectPosition);

        clCon01.setOnClickListener(this);
        clCon02.setOnClickListener(this);
        clCon03.setOnClickListener(this);
        clCon04.setOnClickListener(this);
        mIvPayZfb.setOnClickListener(this);
        mIvPayWx.setOnClickListener(this);
        mTvNext.setOnClickListener(this);

        mSelectPayType = PAY_TYPE_ZFB;
        selectPayType(mSelectPayType);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int clickPosition;
        switch (v.getId()) {
            case R.id.item_become_vip_tail_cl_con_01:
                clickPosition = 0;
                if (clickPosition != mSelectPosition) {
                    mSelectPosition = clickPosition;
                    cancelAllSelectPosition(clickPosition);
                }
                break;
            case R.id.item_become_vip_tail_cl_con_02:
                clickPosition = 1;
                if (clickPosition != mSelectPosition) {
                    mSelectPosition = clickPosition;
                    cancelAllSelectPosition(clickPosition);
                }
                break;
            case R.id.item_become_vip_tail_cl_con_03:
                clickPosition = 2;
                if (clickPosition != mSelectPosition) {
                    mSelectPosition = clickPosition;
                    cancelAllSelectPosition(clickPosition);
                }
                break;
            case R.id.item_become_vip_tail_cl_con_04:
                clickPosition = 3;
                if (clickPosition != mSelectPosition) {
                    mSelectPosition = clickPosition;
                    cancelAllSelectPosition(clickPosition);
                }
                break;
            case R.id.item_become_vip_tail_iv_pay_zfb:
                clickPosition = PAY_TYPE_ZFB;
                if (clickPosition != mSelectPayType) {
                    mSelectPayType = clickPosition;
                    selectPayType(clickPosition);
                }
                break;
            case R.id.item_become_vip_tail_iv_pay_wx:
                clickPosition = PAY_TYPE_WX;
                if (clickPosition != mSelectPayType) {
                    mSelectPayType = clickPosition;
                    selectPayType(clickPosition);
                }
                break;
            case R.id.item_become_vip_tv_next:
                onClickTailListener.onClickTailNext(mSelectPayType, mSelectPosition);
                break;
        }
    }

    private void cancelAllSelectPosition(int selPosition) {
        for (int i = 0; i < clCons.length; i++) {
            if (selPosition == i) {
                clCons[i].setBackground(context.getResources().getDrawable(R.mipmap.become_vip_bg_pay_s));
                ivSels[i].setVisibility(View.VISIBLE);
                tvUnits[i].setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon_s));
                tvMoneys[i].setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon_s));
            } else {
                clCons[i].setBackground(context.getResources().getDrawable(R.mipmap.become_vip_bg_pay));
                ivSels[i].setVisibility(View.GONE);
                tvUnits[i].setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon));
                tvMoneys[i].setTextColor(context.getResources().getColor(R.color.yellow_vip_text_mon));
            }
        }
        setNextDes();
    }

    private void setNextDes() {
        StringBuffer nextDes = new StringBuffer("使用");
//        String nextDes="使用";
        if (mSelectPayType == 1) {
            nextDes.append("微信支付");
        } else {
            nextDes.append("支付宝");
        }
        nextDes.append("支付").append(mPayBeans.get(mSelectPosition).m_price).append("元");
        mTvNext.setText(nextDes.toString());
    }

    private void selectPayType(int selectPayType) {
        if (selectPayType == PAY_TYPE_ZFB) {
            mIvPayZfb.setImageDrawable(context.getResources().getDrawable(R.mipmap.become_vip_icon_pay_zfb_s));
            mIvPayWx.setImageDrawable(context.getResources().getDrawable(R.mipmap.become_vip_icon_pay_wx));
        } else {
            mIvPayZfb.setImageDrawable(context.getResources().getDrawable(R.mipmap.become_vip_icon_pay_zfb));
            mIvPayWx.setImageDrawable(context.getResources().getDrawable(R.mipmap.become_vip_icon_pay_wx_s));
        }
        setNextDes();
    }

    public interface OnClickTailListener {
        void onClickTailNext(int payType, int selectPosition);
    }

    public void setOnClickTailListener(OnClickTailListener onClickTailListener) {
        this.onClickTailListener = onClickTailListener;
    }

}