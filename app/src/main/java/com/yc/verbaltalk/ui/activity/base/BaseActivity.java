package com.yc.verbaltalk.ui.activity.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.model.base.MySubscriber;
import com.yc.verbaltalk.model.bean.AResultInfo;
import com.yc.verbaltalk.model.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.model.bean.WetChatInfo;
import com.yc.verbaltalk.model.bean.event.EventLoginState;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.model.data.BackfillSingle;
import com.yc.verbaltalk.model.engin.LoveEnginV2;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.single.YcSingle;
import com.yc.verbaltalk.model.util.SPUtils;
import com.yc.verbaltalk.ui.activity.IdCorrelationSlidingActivity;
import com.yc.verbaltalk.ui.activity.SpecializedActivity;
import com.yc.verbaltalk.ui.frament.AddWxFragment;
import com.yc.verbaltalk.ui.view.LoadDialog;
import com.yc.verbaltalk.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import rx.Subscriber;

/**
 * Created by mayn on 2019/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LoadDialog mLoadingDialog;
    protected LoveEngine mLoveEngine;
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ClassName", "onCreate: ClassName " + getClass().getName());
        mLoadingDialog = new LoadDialog(this);
        mLoveEngine = new LoveEngine(this);
        mHandler = new Handler();
        checkSingle();

    }

    private void checkSingle() {
        if (this instanceof SpecializedActivity) { //默认在闪屏页恢复数据
            return;
        }
        int id = YcSingle.getInstance().id;
        if (id <= 0) {
            String idInfo = (String) SPUtils.get(this, SPUtils.ID_INFO_BEAN, "");
            if (!TextUtils.isEmpty(idInfo)) {
//                MobclickAgent.onEvent(this, ConstantKey.UM_INFO_LOSE_ID);
                BackfillSingle.backfillLoginData(this, "");
            }
        }
    }

    public void showToastShort(String des) {
        Toast.makeText(this, des, Toast.LENGTH_SHORT).show();
    }

    /**
     * 侵入状态栏
     * 让背景图片可以利用系统状态栏的空间，从而能够让背景图和状态栏融为一体。
     */
    public void invadeStatusBar() {
        //5.0以后才有效果
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        StatusBarUtil.setMaterialStatus(this);
    }

    /**
     * 谷歌原生方式改变状态栏文字颜色
     *
     * @param dark 一旦用谷歌原生设置状态栏文字颜色的方法进行设置的话，因为一直会携带SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN这个flag，
     *             那么默认界面会变成全屏模式，需要在根布局中设置FitsSystemWindows属性为true
     */
    public void setAndroidNativeLightStatusBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void setAndroidNativeLightStatusBar() {
        StatusBarUtil.setStatusTextColor1(true, this);
//        setAndroidNativeLightStatusBar(true);
    }

    /**
     * 获取状态栏高度 直接获取属性，通过getResource
     *
     * @return
     */
    public void setStateBarHeight(View viewBar) {
        setStateBarHeight(viewBar, 0);
    }

    public void setStateBarHeight(View viewBar, int addHeight) {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        if (result <= 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewBar.getLayoutParams();
        layoutParams.height = result + addHeight;
        viewBar.setLayoutParams(layoutParams);
        Log.d("ClassName", "setStateBarHeight: layoutParams.height " + layoutParams.height);
    }


    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    protected void setDrawerLayoutFitSystemWindow() {
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            int statusBarHeight = getStatusHeight(this);
            if (contentViewGroup == null) {
                contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            }
            if (contentViewGroup instanceof DrawerLayout) {
                DrawerLayout drawerLayout = (DrawerLayout) contentViewGroup;
                drawerLayout.setClipToPadding(true);
                drawerLayout.setFitsSystemWindows(false);
                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
                    View child = drawerLayout.getChildAt(i);
                    child.setFitsSystemWindows(false);
                    child.setPadding(0, statusBarHeight, 0, 0);
                }

            }
        }
    }

    public int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public void hindKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showToLoginDialog() {
        netUserReg();
        if (3 > 0) {
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("提示");
        alertDialog.setMessage("您还未登录，请先登录");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialogInterface, i) -> IdCorrelationSlidingActivity.startIdCorrelationActivity(BaseActivity.this, IdCorrelationSlidingActivity.ID_CORRELATION_STATE_LOGIN));
        DialogInterface.OnClickListener listent = null;
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", listent);
        alertDialog.show();
    }


    private void netUserReg() {
        LoadDialog loadDialog = new LoadDialog(this);
        LoveEnginV2 loveEnginV2 = new LoveEnginV2(this);
        loveEnginV2.userReg("user/reg").subscribe(new MySubscriber<AResultInfo<IdCorrelationLoginBean>>(loadDialog) {
            @Override
            protected void onNetNext(AResultInfo<IdCorrelationLoginBean> idCorrelationLoginBeanAResultInfo) {
                IdCorrelationLoginBean data = idCorrelationLoginBeanAResultInfo.data;
                loginSuccess(data);
            }

            @Override
            protected void onNetError(Throwable e) {

            }

            @Override
            protected void onNetCompleted() {

            }
        });
    }

    private void loginSuccess(IdCorrelationLoginBean data) {
        //持久化存储登录信息
        String str = JSON.toJSONString(data);// java对象转为jsonString
        BackfillSingle.backfillLoginData(this, str);

        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
    }

    public void showToWxServiceDialog(onExtraListener listener) {
        showToWxServiceDialog("xuanfu", listener);
    }

    public void showToWxServiceDialog(String position, onExtraListener listener) {
        MobclickAgent.onEvent(this, ConstantKey.UM_CONTACT_US_CLICK_ID);
        final String[] mWechat = {"pai201807"};

        mLoveEngine.getWechatInfo(position).subscribe(new Subscriber<ResultInfo<WetChatInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<WetChatInfo> wetChatInfoResultInfo) {
                if (wetChatInfoResultInfo != null && wetChatInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    mWechat[0] = wetChatInfoResultInfo.data.weixin;
                    runOnUiThread(() -> {
                        showService(mWechat[0], listener);
//                            showServieDialog(mWechat[0]));
                    });


                }
            }
        });


    }

    private void showService(String wechat, BaseActivity.onExtraListener listener) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        Log.e("TAG", "showService: " + wechat);
        ClipData myClip = ClipData.newPlainText("text", wechat);
        myClipboard.setPrimaryClip(myClip);


        if (null != listener) {
            listener.onExtra();

        } else {
            AddWxFragment addWxFragment = new AddWxFragment();
            addWxFragment.setWX(wechat);
            addWxFragment.show(getSupportFragmentManager(), "");
            addWxFragment.setListener(() -> {
//            copySuccess();
                openWeiXin();

                addWxFragment.dismiss();

            });
        }

    }


    public void openWeiXin() {

        try {
            MobclickAgent.onEvent(this, ConstantKey.UM_CONTACT_US_TO_WECHAT_ID);
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (Exception exception) {
            showToastShort("未安装微信");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler)
            mHandler = null;
        if (null != mLoadingDialog)
            mLoadingDialog = null;
    }


    public onExtraListener onExtraListener;

    public void setOnExtraListener(BaseActivity.onExtraListener onExtraListener) {
        this.onExtraListener = onExtraListener;
    }

    public interface onExtraListener {
        void onExtra();
    }
}
