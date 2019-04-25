package com.yc.love.ui.frament;

import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.love.R;
import com.yc.love.cont.gank.MainT1FragGank;
import com.yc.love.cont.http.RequestImpl;
import com.yc.love.model.bean.BannerItemBean;
import com.yc.love.model.bean.FrontpageBean;
import com.yc.love.model.bean.JobBean;
import com.yc.love.ui.frament.base.BaseMainFragment;
import com.yc.love.ui.view.LoadingDialog;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by mayn on 2019/4/23.
 */

public class MainT1Fragment extends BaseMainFragment {

    private TextView tvName;


    @Override
    protected int setContentView() {
        return R.layout.fragment_main_t1;
    }

    @Override
    protected void initViews() {
        tvName = rootView.findViewById(R.id.main_t1_tv_name);
    }

    @Override
    protected void lazyLoad() {
        /*tvName.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);*/
        isCanLoadData();
    }

    private void isCanLoadData() {
        final LoadingDialog loadingView = new LoadingDialog(mMainActivity);
        loadingView.showLoading();

        MainT1FragGank mainT1FragGank = new MainT1FragGank();
        RequestImpl request = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                FrontpageBean bean = (FrontpageBean) object;
                if (bean != null && bean.getResult() != null && bean.getResult().getFocus() != null && bean.getResult().getFocus().getResult() != null) {
                    final ArrayList<BannerItemBean> result = (ArrayList<BannerItemBean>) bean.getResult().getFocus().getResult();
                    ArrayList<String> mBannerImages = new ArrayList<String>();
                    LinearLayout ll = rootView.findViewById(R.id.main_t1_ll);
                    if (result != null && result.size() > 0) {
                        for (int i = 0; i < result.size(); i++) {
                            //获取所有图片
                            mBannerImages.add(result.get(i).getRandpic());
                            Log.d("mylog", i + " MainT1Fragment loadSuccess: result.get(i) " + result.get(i).getRandpic());
                            ImageView imageView = new ImageView(mMainActivity);
                            imageView.setImageDrawable(mMainActivity.getResources().getDrawable(R.mipmap.tab_home));
//                            Glide.with(mMainActivity).load(result.get(i).getRandpic()).into(imageView);
                           /* Picasso.with(mMainActivity).load(result.get(i).getRandpic())
                                    .into(mBannerImagess.get(i));*/

                            TextView textView = new TextView(mMainActivity);
                            textView.setText(mBannerImages.get(i));
                            ll.addView(textView);
                        }
                        tvName.setText(getClass().getName());
                        loadingView.dismissLoading();
                      /*  maCache.remove(Constants.BANNER_PIC);
                        maCache.put(Constants.BANNER_PIC, mBannerImages);
                        maCache.remove(Constants.BANNER_PIC_DATA);
                        maCache.put(Constants.BANNER_PIC_DATA, result);
                        bannerDataBean.setData(mBannerImages, result);

                        bannerData.setValue(bannerDataBean);*/
                    }
                }
            }

            @Override
            public void loadFailed(Throwable throwable) {
                Log.d("mylog", "MainT1Fragment loadFailed: throwable "+throwable);
                loadingView.dismissLoading();
            }


            @Override
            public void addSubscription(Disposable subscription) {
                Log.d("mylog", "MainT1Fragment addSubscription: " + subscription.toString());
                subscription.isDisposed();
            }
        };

        mainT1FragGank.showData(request);


        mainT1FragGank.showJobData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                JobBean jobBean = (JobBean) object;
                jobBean.toString();

                Log.d("mylog", "loadSuccess: jobBean.toString() " + jobBean.toString());
            }

            @Override
            public void loadFailed(Throwable throwable) {
                Log.d("mylog", "loadFailed: showJobData throwable "+throwable);
            }


            @Override
            public void addSubscription(Disposable subscription) {
                Log.d("mylog", "addSubscription: subscription " + subscription.toString());
            }
        });
    }


}
