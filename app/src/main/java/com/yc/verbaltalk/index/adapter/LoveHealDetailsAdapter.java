package com.yc.verbaltalk.index.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.OpenApkPkgInfo;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.model.util.PackageUtils;
import com.yc.verbaltalk.base.view.OpenAkpDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2017/9/12.
 * 话术详情页视频器
 */

public class LoveHealDetailsAdapter extends BaseMultiItemQuickAdapter<LoveHealDetBean, BaseViewHolder> {

    private String mTitle;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public LoveHealDetailsAdapter(List<LoveHealDetBean> data, String title) {
        super(data);
        addItemType(LoveHealDetBean.VIEW_ITEM, R.layout.recycler_view_item_love_heal_det);
        addItemType(LoveHealDetBean.VIEW_VIP, R.layout.recycler_view_item_love_heal_det_vip);
        this.mTitle = title;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, final LoveHealDetBean item) {
        if (null != item) {
            List<LoveHealDetDetailsBean> details = item.details;
            switch (item.type) {
                case LoveHealDetBean.VIEW_ITEM:
                    RecyclerView recyclerView = helper.getView(R.id.item_love_heal_rv);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                    recyclerView.setLayoutManager(layoutManager);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);


                    if (details == null || details.size() == 0) {
                        details = item.detail;
                    }

                    final IndexVerbalDetAdapter loveHealDetAdapterNew = new IndexVerbalDetAdapter(details);

                    recyclerView.setAdapter(loveHealDetAdapterNew);
                    loveHealDetAdapterNew.setOnItemChildClickListener((adapter, view, position) -> {
                        LoveHealDetDetailsBean item1 = loveHealDetAdapterNew.getItem(position);
                        if (item1 != null) {
                            item1.setTitle(mTitle);
                            toCopy(item1);
                        }
                    });

                    loveHealDetAdapterNew.setOnItemClickListener((adapter, view, position) -> {
                        LoveHealDetDetailsBean item1 = loveHealDetAdapterNew.getItem(position);
                        if (item1 != null) {
                            item1.setTitle(mTitle);
                            toCopy(item1);
                        }
                    });

                    break;

                case LoveHealDetBean.VIEW_VIP:
                    if (details == null || details.size() == 0) {
                        details = item.detail;
                    }

                    if (details != null && details.size() > 0) {
                        if (details.size() > 1) {
                            LoveHealDetDetailsBean detailsBean = details.get(0);
                            helper.setText(R.id.item_love_heal_det_vip_tv_name, detailsBean.content);
                        } else {
                            helper.setText(R.id.item_love_heal_det_vip_tv_name, "*************");
                        }
//                        String ansSex = detailsBean.ans_sex;
//                        if (!TextUtils.isEmpty(ansSex)) {
                        helper.setImageDrawable(R.id.item_love_heal_det_vip_iv_sex, mContext.getResources().getDrawable(R.mipmap.icon_dialogue_women));
//                        }
                    }
                    helper.addOnClickListener(R.id.tv_look);

                    break;
            }
        }
    }

    private void toCopy(LoveHealDetDetailsBean content) {
        MobclickAgent.onEvent(mContext, ConstantKey.UM_COPY_DIALOGUE_HEAL);
        ClipboardManager myClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", content.content);
        myClipboard.setPrimaryClip(myClip);
        showOpenAkpDialog(content);
    }

    private void showOpenAkpDialog(LoveHealDetDetailsBean content) {
        List<OpenApkPkgInfo> openApkPkgInfos = new ArrayList<>();
        OpenApkPkgInfo qq = new OpenApkPkgInfo(1, "", "QQ", mContext.getResources().getDrawable(R.mipmap.icon_d_qq));
        OpenApkPkgInfo wx = new OpenApkPkgInfo(2, "", "微信", mContext.getResources().getDrawable(R.mipmap.icon_d_wx));
        OpenApkPkgInfo mm = new OpenApkPkgInfo(3, "", "陌陌", mContext.getResources().getDrawable(R.mipmap.icon_d_momo));
//        OpenApkPkgInfo tt = new OpenApkPkgInfo(4, "", "探探", getResources().getDrawable(R.mipmap.icon_d_tt));

        List<String> apkList = PackageUtils.getApkList(mContext);
        for (int i = 0; i < apkList.size(); i++) {
            String apkPkgName = apkList.get(i);
            if ("com.tencent.mobileqq".equals(apkPkgName)) {
                qq.pkg = apkPkgName;
            } else if ("com.tencent.mm".equals(apkPkgName)) {
                wx.pkg = apkPkgName;
            } else if ("com.immomo.momo".equals(apkPkgName)) {
                mm.pkg = apkPkgName;
            }/* else if ("com.p1.mobile.putong".equals(apkPkgName)) {
                tt.pkg = apkPkgName;
            }*/
        }

        openApkPkgInfos.add(qq);
        openApkPkgInfos.add(wx);
        openApkPkgInfos.add(mm);
//        openApkPkgInfos.add(tt);
        OpenAkpDialog openAkpDialog = new OpenAkpDialog(mContext, openApkPkgInfos, content, false);

        openAkpDialog.show();
    }


}