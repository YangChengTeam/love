package com.yc.verbaltalk.skill.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.music.player.lib.util.ToastUtils;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.statistics.common.DeviceConfig;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.activity.BaseSameActivity;
import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.base.okhttp.presenter.NormalPresenter;
import com.yc.verbaltalk.base.okhttp.view.INormalUiView;
import com.yc.verbaltalk.base.okhttp.view.IUpFileUiView;
import com.yc.verbaltalk.base.utils.HeadImageUtils;
import com.yc.verbaltalk.base.utils.PhoneIMEIUtil;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.view.LoadDialog;
import com.yc.verbaltalk.base.view.imgs.Constant;
import com.yc.verbaltalk.chat.bean.confession.ConfessionDataBean;
import com.yc.verbaltalk.chat.bean.confession.ConfessionFieldBean;
import com.yc.verbaltalk.chat.bean.confession.ImageCreateBean;
import com.yc.verbaltalk.model.constant.ConstantKey;
import com.yc.verbaltalk.model.util.InputLenLimit;
import com.yc.verbaltalk.model.util.SPUtils;
import com.yc.verbaltalk.model.util.ScreenUtils;
import com.yc.verbaltalk.model.util.SizeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Request;
import yc.com.toutiao_adv.OnAdvStateListener;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

public class CreateBeforeActivity extends BaseSameActivity implements INormalUiView, IUpFileUiView, OnAdvStateListener {

    private ImageView mCreateBgImageView;
    private LinearLayout mCreateTypeLayout;
    private ConfessionDataBean mConfessionDataBean;
    private int cWidth;
    private int cHeight;

    private ImageView createSelectImageView;

    private int timeNum = 0;
    private boolean isChooseImage;
    private List<String> mSelectedImages;

    private NormalPresenter mNormalPresenter;
    private Map<String, String> netCompoundRequestData;


    public static void startCreateBeforeActivity(Context context, ConfessionDataBean confessionDataBean) {
        Intent intent = new Intent(context, CreateBeforeActivity.class);
        intent.putExtra("zb_data_info", confessionDataBean);
        context.startActivity(intent);
    }

    @Override
    protected void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle)
            mConfessionDataBean = (ConfessionDataBean) bundle.getSerializable("zb_data_info");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_before);

        mNormalPresenter = new NormalPresenter(this, this);
        initViews();
    }

    private void initViews() {
        mCreateBgImageView = findViewById(R.id.iv_create_bg_iv);
        mCreateTypeLayout = findViewById(R.id.layout_create_type);

        initData();
    }

    private void initData() {

        Log.d("mylog", "initData: mConfessionDataBean " + mConfessionDataBean);

        final int maxWidth = ScreenUtils.getScreenWidth(this);
        final int maxHeight = ScreenUtils.getScreenHeight(this) / 2;

        if (mConfessionDataBean != null) {
            Glide.with(this).asBitmap().load(mConfessionDataBean.front_img).into(new BitmapImageViewTarget(mCreateBgImageView) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                    double rWidth = resource.getWidth();
                    double rHeight = resource.getHeight();

                    //实际的宽高
                    double inputHeight = maxHeight - 100;
                    double scale = rWidth / rHeight;
                    double inputWidth = inputHeight * scale;

                    if (inputWidth > maxWidth) {
                        inputWidth = maxWidth - 100;
                        inputHeight = inputWidth / scale;
                    }

                    ViewGroup.LayoutParams layoutParams = mCreateBgImageView.getLayoutParams();
                    layoutParams.width = (int) inputWidth;
                    layoutParams.height = (int) inputHeight;
                    mCreateBgImageView.setLayoutParams(layoutParams);
                    mCreateBgImageView.setImageBitmap(resource);

//                    mLoadingLayout.setVisibility(View.GONE);
                    Log.d("mylog", "onResourceReady: " + "w-->" + resource.getWidth() + "---h-->" + resource.getHeight());
                    createInputView();
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.d("mylog", "onLoadFailed: ");
                }

            });

        }
    }

    private void createInputView() {

        if (mConfessionDataBean != null && mConfessionDataBean.field != null) {

            final int paddingLeft = SizeUtils.dp2px(this, 10);
            final int textSize = SizeUtils.sp2px(this, 6);
            final int tHeight = SizeUtils.dp2px(this, 38);
            final int tMargin = SizeUtils.dp2px(this, 42);

            Map<String, String> requestData = new HashMap<String, String>();
            Log.d("mylog", "createInputView: " + "create field --->" + mConfessionDataBean.field.size());
            for (int i = 0; i < mConfessionDataBean.field.size(); i++) {
//                ZBDataFieldInfo zField = mConfessionDataBean.field.get(i);
                ConfessionFieldBean confessionFieldBean = mConfessionDataBean.field.get(i);
                if ("0".equals(confessionFieldBean.is_hide)) {

                    if ("0".equals(confessionFieldBean.input_type)) {
                        EditText wordTv = new EditText(this);
                        wordTv.setHint(confessionFieldBean.def_val);
                        wordTv.setBackgroundResource(R.drawable.input_bg);
                        wordTv.setPadding(paddingLeft, 0, 0, 0);
                        wordTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                        //限制是能输入中文
                        if ("2".equals(confessionFieldBean.restrain)) {
                            InputLenLimit.lengthFilter(parseInt(confessionFieldBean.text_len_limit), this, wordTv);
                        } else {
                            wordTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(parseInt(confessionFieldBean.text_len_limit))});
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
                        params.gravity = Gravity.CENTER;
                        params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10));
                        mCreateTypeLayout.addView(wordTv, params);
                    }
                    if ("1".equals(confessionFieldBean.input_type)) {

                        final LinearLayout customLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        customLayout.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams sParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        sParams.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(this, 10));
                        sParams.gravity = Gravity.CENTER;
                        final Spinner niceSpinner = new Spinner(this);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
                        adapter.setDropDownViewResource(R.layout.spinner_item_text);
                        final List<String> dataSet = new LinkedList<String>();

                        if (confessionFieldBean.select != null && confessionFieldBean.select.size() > 0) {
                            for (int j = 0; j < confessionFieldBean.select.size(); j++) {
                                //if (!zField.select.get(j).opt_text.equals("自定义文字")) {
                                dataSet.add(confessionFieldBean.select.get(j).opt_text);
                                //}
                            }
                        }
                        adapter.addAll(dataSet);
                        niceSpinner.setAdapter(adapter);
                        customLayout.addView(niceSpinner, sParams);

                        EditText customTv = new EditText(CreateBeforeActivity.this);
                        customTv.setHint("请输入文字");
                        customTv.setBackgroundResource(R.drawable.input_bg);
                        customTv.setPadding(paddingLeft, 0, 0, 0);
                        customTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        customTv.setVisibility(View.GONE);
                        customTv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(parseInt(confessionFieldBean.text_len_limit))});

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight);
                        params.gravity = Gravity.CENTER;
                        params.setMargins(tMargin, 0, tMargin, SizeUtils.dp2px(CreateBeforeActivity.this, 10));
                        customLayout.addView(customTv, params);

                        mCreateTypeLayout.addView(customLayout, cParams);

                        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                niceSpinner.setContentDescription(dataSet.get(position));

                                if (dataSet.get(position).contains("自定义")) {
                                    for (int i = 0; i < customLayout.getChildCount(); i++) {
                                        if (customLayout.getChildAt(i) instanceof EditText) {
                                            customLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < customLayout.getChildCount(); i++) {
                                        if (customLayout.getChildAt(i) instanceof EditText) {
                                            customLayout.getChildAt(i).setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    if ("2".equals(confessionFieldBean.input_type) || "3".equals(confessionFieldBean.input_type) || "4".equals(confessionFieldBean.input_type)) {


                        cWidth = parseInt(confessionFieldBean.x2) - parseInt(confessionFieldBean.x1);
                        cHeight = parseInt(confessionFieldBean.y2) - parseInt(confessionFieldBean.y1);

                        RelativeLayout imageLayout = new RelativeLayout(this);
                        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                        TextView imageText = new TextView(this);
                        imageText.setText("选择图片：");
                        imageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        leftParams.setMargins(tMargin, SizeUtils.dp2px(this, 12), 0, 0);
                        imageLayout.addView(imageText, leftParams);

                        createSelectImageView = new ImageView(this);
                        createSelectImageView.setBackgroundResource(R.mipmap.create_select_img_icon);
                        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        rightParams.setMargins(0, SizeUtils.dp2px(this, 12), tMargin, 0);
                        imageLayout.addView(createSelectImageView, rightParams);

                        mCreateTypeLayout.addView(imageLayout, ivParams);

                        createSelectImageView.setOnClickListener(v -> {
//                                checkCameraPermissions();
                            checkSdPermissions();
                        });
                    }
                } else {
                    EditText hideEv = new EditText(this);
                    hideEv.setVisibility(View.GONE);
                    LinearLayout.LayoutParams hParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    hParams.gravity = Gravity.CENTER;
                    mCreateTypeLayout.addView(hideEv, hParams);
                }
            }

            //生成按钮
            final Button createBtn = new Button(this);
            createBtn.setBackgroundResource(R.drawable.selectot_btn_brim_red_crimson);
            createBtn.setText("一键生成");
            createBtn.setTextColor(ContextCompat.getColor(this, R.color.white));

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tHeight + 6);
            btnParams.gravity = Gravity.CENTER;
            btnParams.setMargins(tMargin, SizeUtils.dp2px(this, 10), tMargin, SizeUtils.dp2px(this, 30));
            mCreateTypeLayout.addView(createBtn, btnParams);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            createBtn.setOnClickListener(v -> {
                MobclickAgent.onEvent(CreateBeforeActivity.this, ConstantKey.UM_CREAT_ID);
//                    checkSdPermissions();
                checkCameraPermissions();
            });
        }
    }

    private void checkCameraPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CAMERA,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {


                        String brand = Build.BRAND.toLowerCase();
//                        TextUtils.equals("huawei", brand) || TextUtils.equals("honor", brand)
                        if (UserInfoHelper.isVip()) {
                            createImage();
                        } else {

                            showAdDialog();
                        }


                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
//                                Activity activity = SoulPermission.getInstance().getTopActivity();
                                /*if (null == activity) {
                                    return;
                                }*/
                        //绿色框中的流程
                        //用户第一次拒绝了权限、并且没有勾选"不再提示"这个值为true，此时告诉用户为什么需要这个权限。
                        if (permission.shouldRationale()) {
                            ToastUtils.showCenterToast("未获取到相机权限");
                        } else {
                            //此时请求权限会直接报未授予，需要用户手动去权限设置页，所以弹框引导用户跳转去设置页
                            String permissionDesc = permission.getPermissionNameDesc();
                            new AlertDialog.Builder(CreateBeforeActivity.this)
                                    .setTitle("提示")
                                    .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                                    .setPositiveButton("去设置", (dialogInterface, i) -> {
                                        //去设置页
                                        SoulPermission.getInstance().goPermissionSettings();
                                    }).create().show();
                        }
                    }

                });
    }


    private void showAdDialog() {
        TTAdDispatchManager.getManager().init(CreateBeforeActivity.this, TTAdType.REWARD_VIDEO, null, Constant.TOUTIAO_REWARD_ID, 0, "一键生成免费使用", 1, UserInfoHelper.getUid(), TTAdConstant.VERTICAL, CreateBeforeActivity.this);


    }

    private void checkSdPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        PhotoPicker.builder()
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .setShowGif(true)
                                .setPreviewEnabled(false)
                                .start(CreateBeforeActivity.this, PhotoPicker.REQUEST_CODE);

                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
//                                Activity activity = SoulPermission.getInstance().getTopActivity();
                                /*if (null == activity) {
                                    return;
                                }*/
                        //绿色框中的流程
                        //用户第一次拒绝了权限、并且没有勾选"不再提示"这个值为true，此时告诉用户为什么需要这个权限。
                        if (permission.shouldRationale()) {
                            ToastUtils.showCenterToast("未获取到相机权限");
                        } else {
                            //此时请求权限会直接报未授予，需要用户手动去权限设置页，所以弹框引导用户跳转去设置页
                            String permissionDesc = permission.getPermissionNameDesc();
                            new AlertDialog.Builder(CreateBeforeActivity.this)
                                    .setTitle("提示")
                                    .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //去设置页
                                            SoulPermission.getInstance().goPermissionSettings();
                                        }
                                    }).create().show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        computeTime();

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                mSelectedImages = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (mSelectedImages != null && mSelectedImages.size() > 0) {
                    //Glide.with(this).load(mSelectedImages.get(0)).into(createSelectImageView);
                    HeadImageUtils.imgPath = mSelectedImages.get(0);
                    Intent intent = new Intent(CreateBeforeActivity.this, ImageCropActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("xcrop", cWidth);
                    bundle.putInt("ycrop", cHeight);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, HeadImageUtils.FREE_CUT);
                }
            }
        }

        if (requestCode == HeadImageUtils.FREE_CUT && HeadImageUtils.cropBitmap != null) {

            String fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";
            File fileDir = new File(URLConfig.BASE_NORMAL_FILE_DIR);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File tempFile = new File(fileName);
            try {
                if (!tempFile.exists()) {
                    tempFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    HeadImageUtils.cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isChooseImage = true;
            HeadImageUtils.imgResultPath = tempFile.getAbsolutePath();
            Log.d("mylog", "onActivityResult: " + "tempfile path--->" + HeadImageUtils.imgResultPath);
            Glide.with(this).load(tempFile).apply(RequestOptions.circleCropTransform()).into(createSelectImageView);
        }
    }

    public void computeTime() {
        if (timeNum > 12) {
            //TODO
//            String sourceIdsKey = App.loginUser != null ? App.loginUser.love_id + "_ids" : App.ANDROID_ID + "_ids";
            String sourceIdsKey = "123456";
            String ssss = (String) SPUtils.get(CreateBeforeActivity.this, sourceIdsKey, "");
            StringBuffer sourceIds = new StringBuffer(ssss);
            if (!TextUtils.isEmpty(sourceIds.toString())) {
                sourceIds.append(",");
            }

            sourceIds.append(mConfessionDataBean.id);
            SPUtils.put(CreateBeforeActivity.this, sourceIdsKey, sourceIds.toString());

            SPUtils.put(CreateBeforeActivity.this, "is_comment", true);
        }
    }

    private void createImage() {  //一键生成

        Map<String, String> requestData = new HashMap<String, String>();
        if (mCreateTypeLayout != null) {
            boolean isValidate = true;
            for (int i = 0; i < mCreateTypeLayout.getChildCount(); i++) {
                if (mCreateTypeLayout.getChildAt(i) instanceof EditText) {
                    EditText iEditText = (EditText) mCreateTypeLayout.getChildAt(i);
                    if (TextUtils.isEmpty(iEditText.getText()) && iEditText.getVisibility() == View.VISIBLE) {
                        ToastUtils.showCenterToast("请输入值");
                        isValidate = false;
                        break;
                    } else {
                        requestData.put(i + "", iEditText.getText().toString());
                    }
                    continue;
                }

                if (mCreateTypeLayout.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout tempLinearLayout = (LinearLayout) mCreateTypeLayout.getChildAt(i);
                    for (int m = 0; m < tempLinearLayout.getChildCount(); m++) {
                        if (tempLinearLayout.getChildAt(m) instanceof EditText) {
                            EditText iEditText = (EditText) tempLinearLayout.getChildAt(m);
                            if (TextUtils.isEmpty(iEditText.getText()) && iEditText.getVisibility() == View.VISIBLE) {
                                ToastUtils.showCenterToast("请输入值");
                                isValidate = false;
                                break;
                            } else {
                                if (iEditText.getVisibility() == View.VISIBLE) {
                                    requestData.put(i + "", iEditText.getText().toString());
                                }
                            }
                            continue;
                        }

                        if (tempLinearLayout.getChildAt(m) instanceof Spinner) {
                            Spinner iSpinner = (Spinner) tempLinearLayout.getChildAt(m);
                            if (TextUtils.isEmpty(iSpinner.getContentDescription())) {
                                ToastUtils.showCenterToast("请选择值");
                                isValidate = false;
                                break;
                            } else {
                                requestData.put(i + "", iSpinner.getContentDescription().toString());
                            }
                            continue;
                        }
                    }
                }

                if (mCreateTypeLayout.getChildAt(i) instanceof RelativeLayout) {
                    RelativeLayout tempLayout = (RelativeLayout) mCreateTypeLayout.getChildAt(i);
                    for (int j = 0; j < tempLayout.getChildCount(); j++) {
                        if (tempLayout.getChildAt(j) instanceof ImageView) {
                            requestData.put(i + "", "");
                            break;
                        }
                    }
                    continue;
                }
            }

            // 自定义事件,统计次数
//            MobclickAgent.onEvent(CreateBeforeActivity.this, "create_click", SystemTool.getAppVersionName(this));

            if (isValidate) {
//                String mime="";

                this.netCompoundRequestData = requestData;
                checkIMEIPermissions();
            }
        }
    }

    private void netCompoundData() {  //一键生成 请求网络
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadDialog(this);
        }
//        mLoadingDialog.showLoadingDialog();
        mLoadingDialog.showLoadingDialog();
//        String ANDROID_ID = getPhoneIMEI(getApplicationContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mConfessionDataBean != null ? mConfessionDataBean.id : "");
//                params.put("mime", ANDROID_ID);
        String phoneIMEI = PhoneIMEIUtil.getPhoneIMEI(CreateBeforeActivity.this);
        if (TextUtils.isEmpty(phoneIMEI)) {
            phoneIMEI = "99000854223779";
        }


        String deviceIdForGeneral = DeviceConfig.getDeviceIdForGeneral(CreateBeforeActivity.this);

//        phoneIMEI  ="99000854223779";
        params.put("mime", phoneIMEI);  //99000854223779   99001140644954  356615505655247

        Log.d("mylog", "netCompoundData: mime " + phoneIMEI);
        Log.d("mylog", "netCompoundData: deviceIdForGeneral " + deviceIdForGeneral);

        if (netCompoundRequestData.size() > 0) {
            params.put("requestData", JSON.toJSONString(netCompoundRequestData));
        }
        if (isChooseImage) {
            mNormalPresenter.netUpFileNet(params, new File(HeadImageUtils.imgResultPath));


        } else {
            mNormalPresenter.netNormalData(params);
        }
    }

    public String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "123456";
        }
        return tm.getDeviceId();
    }


    private void checkIMEIPermissions() {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_PHONE_STATE,
                //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                new CheckRequestPermissionListener() {
                    @Override
                    public void onPermissionOk(Permission permission) {
                        netCompoundData();
                    }

                    @Override
                    public void onPermissionDenied(Permission permission) {
                        netCompoundData();
                    }
                });
    }

    @Override
    protected String offerActivityTitle() {
        String title = "";
        if (null != mConfessionDataBean)
            title = mConfessionDataBean.title;
        if (TextUtils.isEmpty(title)) {
            title = "合成图片";
        }
        return title;
    }

    private int parseInt(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    //图片合成
    @Override
    public void onUpFileSuccess(String jsonData) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismissLoadingDialog();
        }
        doResult(jsonData);
    }


    @Override
    public void onSuccess(String jsonData) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismissLoadingDialog();
        }
        doResult(jsonData);
    }

    public void doResult(String response) {

        Log.d("mylog", "doResult: response " + response);
//        Log.d("securityhttp", "doResult: response " + response);

        if (response != null) {
//            Logger.e("create result --- >" + response);
            try {
                ImageCreateBean res = JSON.parseObject(response, ImageCreateBean.class);
                String data = res.data;
                /*ImageCreateBean res = Contants.gson.fromJson(response, new TypeToken<ImageCreateBean>() {
                }.getType());
                 Bundle bundle = new Bundle();
                bundle.putString("imagePath", data);
                bundle.putString("createTitle", mConfessionDataBean != null ? mConfessionDataBean.title : "");
                Intent intent = new Intent(CreateBeforeActivity.this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);*/

                ResultActivity.startResultActivity(CreateBeforeActivity.this, data, mConfessionDataBean != null ? mConfessionDataBean.title : "");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CreateBeforeActivity.this, "生成失败,请稍后重试!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailed(String msg) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismissLoadingDialog();
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        /*if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismissLoadingDialog();
        }*/
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFailed() {

    }

    @Override
    public void clickAD() {
//        if (!rewardComplete) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("关闭广告可能无法使用神器功能！！");
//            builder.setPositiveButton("确定", (dialog, which) -> createImage()).create().show();
//        } else {
//
//        }
//
        createImage();
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }


    @Override
    public void onDrawFeedAd(List<TTFeedAd> feedAdList) {

    }

    @Override
    public void removeNativeAd(TTNativeExpressAd ad, int position) {

    }

    boolean rewardComplete = false;

    @Override
    public void rewardComplete() {
        rewardComplete = true;
    }
}
