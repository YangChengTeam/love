package com.yc.love.model.engin;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.love.model.base.BaseEngine;
import com.yc.love.model.bean.AResultInfo;
import com.yc.love.model.bean.CategoryArticleBean;
import com.yc.love.model.bean.ExampDataBean;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.model.bean.ExampleTsBean;
import com.yc.love.model.bean.ExampleTsCategory;
import com.yc.love.model.bean.ExampleTsListBean;
import com.yc.love.model.bean.LoveByStagesBean;
import com.yc.love.model.bean.LoveByStagesDetailsBean;
import com.yc.love.model.bean.LoveHealDateBean;
import com.yc.love.model.bean.LoveHealDetBean;
import com.yc.love.model.bean.LoveHealingBean;
import com.yc.love.model.bean.LoveUpDownPhotoBean;
import com.yc.love.model.domain.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveEngin extends BaseEngine {

    public LoveEngin(Context context) {
        super(context);
    }

    public Observable<AResultInfo<List<String>>> abc(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<String>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<String>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<String>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


    public Observable<AResultInfo<List<LoveHealDateBean>>> loveCategory(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDateBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDateBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDateBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<List<LoveHealDetBean>>> loveListCategory(String userId,String category_id, String page, String page_size, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("category_id", category_id);
        params.put("page", page);
        params.put("page_size", page_size);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDetBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDetBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDetBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

   /* public Observable<AResultInfo<List<LoveHealingBean>>> recommendLovewords(String page, String page_size, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", page_size);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealingBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealingBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealingBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }*/
    public Observable<AResultInfo<List<LoveHealingBean>>> recommendLovewords(String userId,String page, String page_size, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", page_size);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealingBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealingBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealingBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<String>> exampleCollectListList(String userId, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }



    public Observable<AResultInfo<List<ExampleTsBean>>> exampleTs(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<ExampleTsBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<ExampleTsBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<ExampleTsBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<ExampleTsCategory>> exampleTsCategory(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<ExampleTsCategory>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<ExampleTsCategory>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<ExampleTsCategory>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
   /* public Observable<AResultInfo<List<ExampleTsBean>>> indexExample(String page,String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<ExampleTsBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<ExampleTsBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<ExampleTsBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }*/
    public Observable<AResultInfo<ExampDataBean>> indexExample(String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<ExampDataBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<ExampDataBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<ExampDataBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<ExampDataBean>> exampLists(String userId,String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("user_id", userId);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<ExampDataBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<ExampDataBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<ExampDataBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<List<ExampListsBean>>> exampleCollectList(String userId,String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("user_id", userId);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<ExampListsBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<ExampListsBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<ExampListsBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<List<CategoryArticleBean>>> categoryArticle(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<CategoryArticleBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<CategoryArticleBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<CategoryArticleBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<List<LoveByStagesBean>>> listsArticle(String categoryId, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("category_id", categoryId);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveByStagesBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveByStagesBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveByStagesBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<List<ExampleTsListBean>>> collectListsArticle(String userId, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<ExampleTsListBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<ExampleTsListBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<ExampleTsListBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<List<LoveHealDetBean>>> searchDialogue(String userId,String searchType,String keyword,String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("search_type", searchType);
//        params.put("user_id", userId);
        params.put("page", page);
        params.put("keyword", keyword);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDetBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDetBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDetBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


    public Observable<AResultInfo<List<LoveHealingBean>>> listsCollectLovewords(String userId, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealingBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealingBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealingBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<LoveByStagesDetailsBean>> detailArticle(String id,String userId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("user_id", userId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<LoveByStagesDetailsBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<LoveByStagesDetailsBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<LoveByStagesDetailsBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<LoveByStagesDetailsBean>> detailExample(String id,String userId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("example_id", id);
        params.put("user_id", userId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<LoveByStagesDetailsBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<LoveByStagesDetailsBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<LoveByStagesDetailsBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<LoveUpDownPhotoBean>> detailLovewords(String lovewordsId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("lovewords_id", lovewordsId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<LoveUpDownPhotoBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<LoveUpDownPhotoBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<LoveUpDownPhotoBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }

    public Observable<AResultInfo<ExampDataBean>> exampleTsList(String id, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("category_id", id);
        params.put("page", page);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<ExampDataBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<ExampDataBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<ExampDataBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<String>> collectArticle(String userId,  String articleId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("article_id", articleId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<String>> likeExample(String userId,  String articleId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("article_id", articleId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<String>> collectLovewords(String userId,String lovewordsId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("lovewords_id", lovewordsId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }
    public Observable<AResultInfo<String>> collectExample(String userId,String exampleId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("example_id", exampleId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<String>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<String>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<String>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


}
