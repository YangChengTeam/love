package com.yc.verbaltalk.ui.view;

import android.app.Activity;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.adaper.rv.AudioFilterAdapter;
import com.yc.verbaltalk.model.bean.AudioDataInfo;
import com.yc.verbaltalk.model.bean.AudioDataWrapperInfo;
import com.yc.verbaltalk.model.engin.LoveEngine;
import com.yc.verbaltalk.model.util.SPUtils;
import com.yc.verbaltalk.ui.activity.base.BasePopwindow;
import com.yc.verbaltalk.utils.CommonInfoHelper;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.Subscriber;

/**
 * Created by wanglin  on 2019/7/22 08:49.
 */
public class AudioFilterPopwindow extends BasePopwindow {
    private RecyclerView filterRecyclerview;
    private AudioFilterAdapter filterAdapter;
    private LoveEngine loveEngin;
//    private CacheWorker mWorker;
    private static final String TAG = "AudioFilterPopwindow";

    public AudioFilterPopwindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_audio_filter;
    }

    @Override
    public void init() {
        loveEngin = new LoveEngine(mContext);
        filterRecyclerview = rootView.findViewById(R.id.filter_recyclerView);
        filterRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));

        filterAdapter = new AudioFilterAdapter(null);

        filterRecyclerview.setAdapter(filterAdapter);

        initData();
        initListener();
    }

    private void initData() {
//        List<AudioDataInfo> filterInfos = (List<AudioDataInfo>) mWorker.getCache(mContext, "audio_filter_data");
        CommonInfoHelper.getO(mContext, "audio_filter_data", new TypeReference<List<AudioDataInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<AudioDataInfo>>() {
            @Override
            public void onParse(List<AudioDataInfo> filterInfos) {
                if (filterInfos != null) {
                    filterAdapter.setNewData(filterInfos);
                }
            }
        });


        loveEngin.getAudioDataInfo().subscribe(new Subscriber<ResultInfo<AudioDataWrapperInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<AudioDataWrapperInfo> audioDataWrapperInfoResultInfo) {
                if (audioDataWrapperInfoResultInfo != null && audioDataWrapperInfoResultInfo.code == HttpConfig.STATUS_OK && audioDataWrapperInfoResultInfo.data != null) {
                    filterAdapter.setNewData(audioDataWrapperInfoResultInfo.data.getList());
//                    mWorker.setCache("audio_filter_data", audioDataWrapperInfoResultInfo.data.getList());
                    CommonInfoHelper.setO(mContext,audioDataWrapperInfoResultInfo.data.getList(),"audio_filter_data");
                }
            }
        });


    }

    private void initListener() {
        filterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                if (clickListener != null) {
                    clickListener.onItemClick(filterAdapter.getItem(position));
                }

                SPUtils.put(mContext, SPUtils.FILTER_POS, position);
            }
        });
    }


    private onItemClickListener clickListener;

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface onItemClickListener {
        void onItemClick(AudioDataInfo audioDataInfo);
    }
}
