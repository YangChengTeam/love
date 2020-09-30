package yc.com.rthttplibrary.converter;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.exception.ApiException;
import yc.com.rthttplibrary.view.BaseLoadingView;


/**
 * Created by suns  on 2020/7/18 11:11.
 */
public abstract class BaseObserver<T, V extends BaseLoadingView> extends DisposableObserver<ResultInfo<T>> {

    protected V view;

    public BaseObserver(V view) {
        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null && isShow()) {
            view.showLoading();
        }
    }

    /**
     * 额外控制是否显示loading
     * <p>
     * 比如分页加载第一页显示loading，剩余的不需要显示loading加载
     *
     * @return
     */
    protected boolean isShow() {
        return true;
    }

    @Override
    public void onNext(ResultInfo<T> value) {
        if (view != null && isShow()) {
            view.hideLoading();
        }
        if (value != null) {
            if (value.code == HttpConfig.STATUS_OK) {
                onSuccess(value.data, value.message);
            } else {
                onFailure(value.code, value.message);
            }
        } else {
            onFailure(-1, HttpConfig.SERVICE_ERROR);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null && isShow()) {
            view.hideLoading();
        }
//        e.printStackTrace();
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            onFailure(exception.getErrorCode(),exception.getErrorMsg());
            return;
        }
        onFailure(-1, HttpConfig.NET_ERROR);
    }

    @Override
    public void onComplete() {
        if (view != null && isShow()) {
            view.hideLoading();
        }
        onRequestComplete();
    }


    public abstract void onSuccess(T data, String message);

    public abstract void onFailure(int code, String errorMsg);

    public abstract void onRequestComplete();


}
