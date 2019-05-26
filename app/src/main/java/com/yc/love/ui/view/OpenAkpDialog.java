package com.yc.love.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.love.R;
import com.yc.love.model.bean.OpenApkPkgInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mayn on 2019/5/21.
 */

public class OpenAkpDialog extends AlertDialog {
    private final Context context;
    private final List<OpenApkPkgInfo> openApkPkgInfos;

    public OpenAkpDialog(@NonNull Context context, List<OpenApkPkgInfo> openApkPkgInfos) {
        super(context, 0);
        this.context = context;
        this.openApkPkgInfos = openApkPkgInfos;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceDialog();
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

        tvQQ.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(0).pkg));
        tvWx.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(1).pkg));
        tvMm.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(2).pkg));
//        tvTt.setOnClickListener(clickToOpenApk(openApkPkgInfos.get(3).pkg));


    }

    private View.OnClickListener clickToOpenApk(final String pkg) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pkg)) {
                    Toast.makeText(context, "未安装该应用", Toast.LENGTH_SHORT).show();
                } else {
                    openApk(pkg);
                }
                if (isShowing()) {
                    dismiss();
                }
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
