package com.yc.verbaltalk.base.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.music.player.lib.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.fragment.AddWxFragment;
import com.yc.verbaltalk.base.utils.StatusBarUtil;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.WetChatInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.model.constant.ConstantKey;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.observers.DisposableObserver;

import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by sunshey on 2019/4/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LoadDialog mLoadingDialog;
    protected LoveEngine mLoveEngine;
    protected Handler mHandler;
    private MyRunnable taskRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ClassName", "onCreate: ClassName " + getClass().getName());
        mLoadingDialog = new LoadDialog(this);
        mLoveEngine = new LoveEngine(this);
        mHandler = new Handler();
//        checkSingle();

    }

//    private void checkSingle() {
//        if (this instanceof SpecializedActivity) { //默认在闪屏页恢复数据
//            return;
//        }
//        int id = YcSingle.getInstance().id;
//        if (id <= 0) {
//            String idInfo = (String) SPUtils.get(this, SPUtils.ID_INFO_BEAN, "");
//            if (!TextUtils.isEmpty(idInfo)) {
////                MobclickAgent.onEvent(this, ConstantKey.UM_INFO_LOSE_ID);
//                BackfillSingle.backfillLoginData(this, "");
//            }
//        }
//    }



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

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, 255, 255, 255);
    }


    private void loginSuccess(UserInfo data) {
        //持久化存储登录信息
        String str = JSON.toJSONString(data);// java对象转为jsonString
//        BackfillSingle.backfillLoginData(this, str);

        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED));
    }

    public void showToWxServiceDialog(onExtraListener listener) {
        showToWxServiceDialog("xuanfu", listener);
    }

    public void showToWxServiceDialog(String position, onExtraListener listener) {
        MobclickAgent.onEvent(this, ConstantKey.UM_CONTACT_US_CLICK_ID);
        final String[] mWechat = {"pai201807"};

        mLoveEngine.getWechatInfo(position).subscribe(new DisposableObserver<ResultInfo<WetChatInfo>>() {
            @Override
            public void onComplete() {

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
            listener.onExtra(wechat);

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
            ToastUtils.showCenterToast("未安装微信");
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
        void onExtra(String wx);
    }


    /**
     * 改变获取验证码按钮状态
     */
    public void showGetCodeDisplay(TextView textView) {
        taskRunnable = new MyRunnable(textView);
        if (null != mHandler) {

            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
            totalTime = 60;
            textView.setClickable(false);
//            textView.setTextColor(ContextCompat.getColor(R.color.coment_color));
//            textView.setBackgroundResource(R.drawable.bg_btn_get_code);
            if (null != mHandler) mHandler.postDelayed(taskRunnable, 0);
        }
    }

    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime = 60;


    private class MyRunnable implements Runnable {
        TextView mTv;


        public MyRunnable(TextView textView) {
            this.mTv = textView;
        }

        @Override
        public void run() {
            mTv.setText(totalTime + "s");
            totalTime--;
            if (totalTime < 0) {
                //还原
                initGetCodeBtn(mTv);
                return;
            }
            if (null != mHandler) mHandler.postDelayed(this, 1000);
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn(TextView textView) {
        totalTime = 0;
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
        }
        textView.setText("重新获取");

        textView.setClickable(true);
//        textView.setTextColor(CommonUtils.getColor(R.color.white));
//        textView.setBackgroundResource(R.drawable.bg_btn_get_code_true);
    }
}
