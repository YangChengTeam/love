package com.yc.verbaltalk.ui.view.imgs;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.verbaltalk.R;
import com.yc.verbaltalk.utils.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ISListActivity extends AppCompatActivity implements View.OnClickListener, Callback {

    public static final String INTENT_RESULT = "result";
    private static final int IMAGE_CROP_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 1;

    private ISListConfig config;

    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private Button btnConfirm;
    private ImageView ivBack;
    private String cropImagePath;

    private ImgSelFragment fragment;

    private ArrayList<String> result = new ArrayList<>();

    public static void startForResult(Activity activity, ISListConfig config, int RequestCode) {
        Intent intent = new Intent(activity, ISListActivity.class);
        intent.putExtra("config", config);
        activity.startActivityForResult(intent, RequestCode);
    }

    public static void startForResult(Fragment fragment, ISListConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), ISListActivity.class);
        intent.putExtra("config", config);
        fragment.startActivityForResult(intent, RequestCode);
    }

    public static void startForResult(android.app.Fragment fragment, ISListConfig config, int RequestCode) {
        Intent intent = new Intent(fragment.getActivity(), ISListActivity.class);
        intent.putExtra("config", config);
        fragment.startActivityForResult(intent, RequestCode);
    }

    /**
     * 移除状态栏
     */
    public void removeStatusBar() {
        //顶掉系统状态栏的空间
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
        getSupportActionBar().hide();
    }

    /**
     * 获取状态栏高度 直接获取属性，通过getResource
     *
     * @return
     */
    public void setStateBarHeight(View viewBar) {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        if (result <= 0) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewBar.getLayoutParams();
        layoutParams.height = result;
        viewBar.setLayoutParams(layoutParams);
    }

    /**
     * 谷歌原生方式改变状态栏文字颜色
     *
     * @param dark 一旦用谷歌原生设置状态栏文字颜色的方法进行设置的话，因为一直会携带SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN这个flag，
     *             那么默认界面会变成全屏模式，需要在根布局中设置FitsSystemWindows属性为true
     */
    public void setAndroidNativeLightStatusBar(boolean dark) {
        StatusBarUtil.setStatusTextColor1(true, this);
      /*  View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_sel);
        removeStatusBar(); //侵入状态栏
        View viewBar = findViewById(R.id.img_sel_view_bar);
        setStateBarHeight(viewBar);
        setAndroidNativeLightStatusBar(true);

        config = (ISListConfig) getIntent().getSerializableExtra("config");

        // Android 6.0 checkSelfPermission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        } else {
            fragment = ImgSelFragment.instance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fmImageList, fragment, null)
                    .commit();
        }

        initView();
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, getString(R.string.sd_disable), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rlTitleBar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        if (config != null) {
            if (config.backResId != -1) {
                ivBack.setImageResource(config.backResId);
            }

            if (config.statusBarColor != -1) {
                StatusBarCompat.compat(this, config.statusBarColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            }
            rlTitleBar.setBackgroundColor(config.titleBgColor);
            tvTitle.setTextColor(config.titleColor);
            tvTitle.setText(config.title);
            btnConfirm.setBackgroundColor(config.btnBgColor);
            btnConfirm.setTextColor(config.btnTextColor);

            if (config.multiSelect) {
                if (!config.rememberSelected) {
                    Constant.imageList.clear();
                }
                btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
            } else {
                Constant.imageList.clear();
                btnConfirm.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnConfirm) {
            if (Constant.imageList != null && !Constant.imageList.isEmpty()) {
                exit();
            } else {
                Toast.makeText(this, getString(R.string.minnum), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.ivBack) {
            onBackPressed();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (config.needCrop) {
            crop(path);
        } else {
            Constant.imageList.add(path);
            exit();
        }
    }

    @Override
    public void onImageSelected(String path) {
        btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
    }

    @Override
    public void onImageUnselected(String path) {
        btnConfirm.setText(String.format(getString(R.string.confirm_format), config.btnText, Constant.imageList.size(), config.maxNum));
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (config.needCrop) {
                crop(imageFile.getAbsolutePath());
            } else {
                Constant.imageList.add(imageFile.getAbsolutePath());
                config.multiSelect = false; // 多选点击拍照，强制更改为单选
                exit();
            }
        }
    }

    @Override
    public void onPreviewChanged(int select, int sum, boolean visible) {
        if (visible) {
            tvTitle.setText(select + "/" + sum);
        } else {
            tvTitle.setText(config.title);
        }
    }

    private void crop(String imagePath) {
        File file = new File(FileUtils.createRootPath(this) + "/" + System.currentTimeMillis() + ".jpg");

        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public ISListConfig getConfig() {
        return config;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            Constant.imageList.add(cropImagePath);
            config.multiSelect = false; // 多选点击拍照，强制更改为单选
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void exit() {
        Intent intent = new Intent();
        result.clear();
        result.addAll(Constant.imageList);
        intent.putStringArrayListExtra(INTENT_RESULT, result);
        setResult(RESULT_OK, intent);

        if (!config.multiSelect) {
            Constant.imageList.clear();
        }

        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fmImageList, ImgSelFragment.instance(), null)
                            .commitAllowingStateLoss();
                } else {
                    Toast.makeText(this, getString(R.string.permission_storage_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("config", config);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        config = (ISListConfig) savedInstanceState.getSerializable("config");
    }

    @Override
    public void onBackPressed() {
        if (fragment == null || !fragment.hidePreview()) {
            Constant.imageList.clear();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
