package com.yc.love.adaper.rv.holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.adaper.rv.base.RecyclerViewItemListener;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealDetDetailsBean;
import com.yc.love.model.bean.StringBean;
import com.yc.love.model.util.PackageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DetailsBeanViewHolder extends BaseViewHolder<LoveHealDetDetailsBean> {

    private final Context context;
    private OnClickCopyListent onClickCopyListent;

    public DetailsBeanViewHolder(Context context, RecyclerViewItemListener listener, ViewGroup parent) {
        super(context, parent, R.layout.recycler_view_item_details_bean, listener);   //一个类对应一个布局文件
        this.context = context;
    }

    @Override
    public void bindData(final LoveHealDetDetailsBean detailsBean) {
        ImageView ivSex = itemView.findViewById(R.id.item_details_bean_iv_sex);
        TextView tvName = itemView.findViewById(R.id.item_details_bean_tv_name);
        ImageView ivCopy = itemView.findViewById(R.id.item_details_bean_iv_copy);

        tvName.setText(detailsBean.content);
        String ansSex = detailsBean.ans_sex;
        if (!TextUtils.isEmpty(ansSex)) {
            if ("1".equals(ansSex)) { //1男2女0bi'a
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_men));
            } else if ("2".equals(ansSex)) {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_women));
            } else {
                ivSex.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_dialogue_nothing));
            }
        }
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCopyListent.onClickCopy(detailsBean);
               /* List<String> apkList = PackageUtils.getApkList(context);
                for (int i = 0; i < apkList.size(); i++) {
                    Log.d("mylog", "getMapApk: " + apkList.get(i).toString());
                }
                openWeiXin();*/
            }
        });

    }

    private void openQQ() {

        List<String> list=new ArrayList<>();
//        list.add()
        //同AndroidManifest中主入口Activity一样
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        //得到一个PackageManager的对象
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        //获取到主入口的Activity集合
        List<ResolveInfo> mlist = packageManager.queryIntentActivities(intent, 0);

        Collections.sort(mlist, new ResolveInfo.DisplayNameComparator(packageManager));

        for (ResolveInfo res : mlist) {
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
            if (pkg.contains("com.tencent.mm")) {
                ComponentName componentName = new ComponentName(pkg, cls);
                Intent intent1 = new Intent();
                intent1.setComponent(componentName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }
    }

    private void openWeiXin() {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
    }

    public interface OnClickCopyListent {
        void onClickCopy(LoveHealDetDetailsBean detailsBean);
    }

    public void setOnClickCopyListent(OnClickCopyListent onClickCopyListent) {
        this.onClickCopyListent = onClickCopyListent;
    }

}