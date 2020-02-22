package com.yc.verbaltalk.base.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.OpenApkPkgInfo;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.base.engine.LoveEngine;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import rx.Subscriber;

/**
 * Created by mayn on 2019/5/21.
 */

public class OpenAkpDialog extends AlertDialog {
    private final Context context;
    private final List<OpenApkPkgInfo> openApkPkgInfos;
    private LoveEngine loveEngin;
    private LoveHealDetDetailsBean loveHealDetDetailsBean;
    private boolean mIsCollect;

    public OpenAkpDialog(@NonNull Context context, List<OpenApkPkgInfo> openApkPkgInfos, LoveHealDetDetailsBean content, boolean isCollect) {
        super(context, 0);
        this.context = context;
        this.openApkPkgInfos = openApkPkgInfos;
        this.loveHealDetDetailsBean = content;
        this.mIsCollect = isCollect;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceDialog();
        loveEngin = new LoveEngine(context);
    }

    private void instanceDialog() {
        setContentView(R.layout.dialog_open_akp);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvQQ = window.findViewById(R.id.dialog_open_akp_tv_qq);
        TextView tvWx = window.findViewById(R.id.dialog_open_akp_tv_wx);
        TextView tvMm = window.findViewById(R.id.dialog_open_akp_tv_mm);
        TextView tvTt = window.findViewById(R.id.dialog_open_akp_tv_tt);
        TextView tvCollect = window.findViewById(R.id.dialog_open_collect);

        if (mIsCollect) {
            tvCollect.setText("取消收藏");
        } else {
            tvCollect.setText(context.getString(R.string.collect));
        }

        tvQQ.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(0).pkg));
        tvWx.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(1).pkg));
        tvMm.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(2).pkg));
        tvCollect.setOnClickListener(v -> {
            handleCollect();
            MobclickAgent.onEvent(context, "collection_id", "收藏话术");

        });
//        tvTt.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(3).pkg));


    }

    private void handleCollect() {
        if (mIsCollect) {
            loveEngin.deleteCollectLoveHeals(loveHealDetDetailsBean).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String s) {
                    if (TextUtils.equals("success", s)) {
                        ToastUtil.toast2(context, "已取消收藏！");
                    }
                    EventBus.getDefault().post("collect_cancel");
                    dismiss();
                }
            });
        } else {
            loveEngin.collectLoveHeal(loveHealDetDetailsBean).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String s) {
                    if (TextUtils.equals("success", s)) {
                        ToastUtil.toast2(context, "已经收藏成功，快去查看吧！");
                    }
                    dismiss();
                }
            });
        }
    }


    private View.OnClickListener clickToOpenApk(final String pkg) {
        return v -> {
            if (TextUtils.isEmpty(pkg)) {
                Toast.makeText(context, "未安装该应用", Toast.LENGTH_SHORT).show();
            } else {
                openApk(pkg);

                if ("com.tencent.mobileqq".equals(pkg)) {
                    MobclickAgent.onEvent(context, ConstantKey.UM_OPEN_DIALOGUE_QQ);
                } else if ("com.tencent.mm".equals(pkg)) {
                    MobclickAgent.onEvent(context, ConstantKey.UM_OPEN_DIALOGUE_WX);
                } else if ("com.immomo.momo".equals(pkg)) {
                    MobclickAgent.onEvent(context, ConstantKey.UM_OPEN_DIALOGUE_MOMO);
                }
            }
            if (isShowing()) {
                dismiss();
            }
        };
    }


    private void openApk(String name) {

        List<String> list = new ArrayList<>();
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
            if (pkg.contains(name)) {
                ComponentName componentName = new ComponentName(pkg, cls);
                Intent intent1 = new Intent();
                intent1.setComponent(componentName);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
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


}
