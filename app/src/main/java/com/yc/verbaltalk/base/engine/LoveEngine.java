package com.yc.verbaltalk.base.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.yc.verbaltalk.model.ModelApp;
import com.yc.verbaltalk.chat.bean.AResultInfo;
import com.yc.verbaltalk.chat.bean.AudioDataWrapperInfo;
import com.yc.verbaltalk.chat.bean.BannerInfo;
import com.yc.verbaltalk.chat.bean.CategoryArticleBean;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.chat.bean.CommunityTagInfoWrapper;
import com.yc.verbaltalk.chat.bean.CourseInfo;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.chat.bean.ExampleTsCategory;
import com.yc.verbaltalk.chat.bean.IdCorrelationLoginBean;
import com.yc.verbaltalk.chat.bean.IndexHotInfoWrapper;
import com.yc.verbaltalk.chat.bean.LoveByStagesBean;
import com.yc.verbaltalk.chat.bean.LoveByStagesDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;
import com.yc.verbaltalk.chat.bean.LoveUpDownPhotoBean;
import com.yc.verbaltalk.chat.bean.MenuadvInfoBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.ShareInfo;
import com.yc.verbaltalk.chat.bean.TopTopicInfo;
import com.yc.verbaltalk.chat.bean.WetChatInfo;
import com.yc.verbaltalk.chat.bean.CommunityDetailInfo;
import com.yc.verbaltalk.chat.bean.confession.ConfessionBean;
import com.yc.verbaltalk.model.dao.LoveHealDetDetailsBeanDao;
import com.yc.verbaltalk.base.config.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by mayn on 2019/5/9.
 */

public class LoveEngine extends BaseEngine {
    private LoveHealDetDetailsBeanDao detailsBeanDao;

    public LoveEngine(Context context) {
        super(context);
        detailsBeanDao = ModelApp.getDaoSession().getLoveHealDetDetailsBeanDao();
    }

    public Observable<AResultInfo<IdCorrelationLoginBean>> userInfo(String userId, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        requestParams(params);
        HttpCoreEngin<AResultInfo<IdCorrelationLoginBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<IdCorrelationLoginBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<IdCorrelationLoginBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


    public Observable<AResultInfo<List<LoveHealDateBean>>> loveCategory(String url, String sence) {
        Map<String, String> params = new HashMap<>();
        params.put("sence", sence);
        requestParams(params);
        HttpCoreEngin<AResultInfo<List<LoveHealDateBean>>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<List<LoveHealDateBean>>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<List<LoveHealDateBean>>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


    public Observable<AResultInfo<List<LoveHealDetBean>>> loveListCategory(String userId, String category_id, String page, String page_size, String url) {
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


    public Observable<AResultInfo<List<LoveHealingBean>>> recommendLovewords(String userId, String page, String page_size, String url) {
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

    public Observable<AResultInfo<MenuadvInfoBean>> menuadvInfo(String url) {
        Map<String, String> params = new HashMap<>();
//        params.put("password", password);
        requestParams(params);
        HttpCoreEngin<AResultInfo<MenuadvInfoBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<MenuadvInfoBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<MenuadvInfoBean>>() {
                }.getType(),
                params,
                true,
                true, true);
        return rxpost;
    }


    public Observable<AResultInfo<ExampDataBean>> exampLists(String userId, String page, String pageSize, String url) {
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

    public Observable<AResultInfo<List<ExampListsBean>>> exampleCollectList(String userId, String page, String pageSize, String url) {
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


    public Observable<AResultInfo<List<LoveHealDetBean>>> searchDialogue(String userId, String searchType, String keyword, String page, String pageSize, String url) {
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

    public Observable<AResultInfo<List<LoveHealDetBean>>> searchDialogue(String userId, String keyword, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
//        params.put("search_type", searchType);
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

    public Observable<AResultInfo<SearchDialogueBean>> searchDialogue2(String userId, String keyword, String page, String pageSize, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
//        params.put("search_type", searchType);
//        params.put("user_id", userId);
        params.put("page", page);
        params.put("keyword", keyword);
        params.put("page_size", pageSize);
        requestParams(params);
        HttpCoreEngin<AResultInfo<SearchDialogueBean>> httpCoreEngin = HttpCoreEngin.get(mContext);
        Observable<AResultInfo<SearchDialogueBean>> rxpost = httpCoreEngin.rxpost(URLConfig.getUrl(url), new TypeReference<AResultInfo<SearchDialogueBean>>() {
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

    public Observable<AResultInfo<LoveByStagesDetailsBean>> detailArticle(String id, String userId, String url) {
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

    public Observable<AResultInfo<LoveByStagesDetailsBean>> detailExample(String id, String userId, String url) {
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

    public Observable<AResultInfo<String>> collectArticle(String userId, String articleId, String url) {
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

    public Observable<AResultInfo<String>> likeExample(String userId, String articleId, String url) {
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

    public Observable<AResultInfo<String>> collectLovewords(String userId, String lovewordsId, String url) {
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

    public Observable<AResultInfo<String>> collectExample(String userId, String exampleId, String url) {
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


    public Observable<ConfessionBean> geteExpressData(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("page", page + "");

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.CATEGORY_LIST_URL, new TypeReference<ConfessionBean>() {
                }.getType(), params,
                false, false, false);

    }


    public Observable<String> collectLoveHeal(final LoveHealDetDetailsBean detDetailsBean) {

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                if (detDetailsBean == null) return "";

                LoveHealDetDetailsBean queryBean = getCollectLoveHealById(detDetailsBean.content);
                if (queryBean == null) {
                    detDetailsBean.saveTime = System.currentTimeMillis();
                    detailsBeanDao.insert(detDetailsBean);
                } else {
                    queryBean.saveTime = System.currentTimeMillis();
                    detailsBeanDao.update(queryBean);
                }

                return "success";
            }
        });


    }

    public Observable<List<LoveHealDetDetailsBean>> getCollectLoveHeals(final int limit, final int offset) {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<LoveHealDetDetailsBean>>() {
            @Override
            public List<LoveHealDetDetailsBean> call(String s) {

                return detailsBeanDao.queryBuilder().offset(offset * limit).limit(limit).orderDesc(LoveHealDetDetailsBeanDao.Properties.SaveTime).list();
            }
        });
    }

    private LoveHealDetDetailsBean getCollectLoveHealById(String content) {
        return detailsBeanDao.queryBuilder().where(LoveHealDetDetailsBeanDao.Properties.Content.eq(content)).unique();
    }


    public Observable<String> deleteCollectLoveHeals(final LoveHealDetDetailsBean detDetailsBean) {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                if (detDetailsBean == null) return "";
                detailsBeanDao.delete(detDetailsBean);
                return "success";
            }
        });
    }

    public Observable<ResultInfo<ShareInfo>> getShareInfo(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.get_share_info, new TypeReference<ResultInfo<ShareInfo>>() {
        }.getType(), null, true, true, true);
    }


    //分类
    public Observable<ResultInfo<AudioDataWrapperInfo>> getAudioDataInfo() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_DATA_LIST_URL, new TypeReference<ResultInfo<AudioDataWrapperInfo>>() {
        }.getType(), null, true, true, true);
    }


    //列表
    //order  排序   id 按照时间倒序排列   listen_times 按照点击次数排序
    public Observable<ResultInfo<MusicInfoWrapper>> getLoveItemList(String userId, String typeId, int page, int limit, int orderInt) {


        //page=1&page_size=10&user_id=2&order=listen_times
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(typeId))
            params.put("cat_id", typeId);
        params.put("page", page + "");
        params.put("page_size", limit + "");
        params.put("user_id", userId);
        String order = "";
        if (orderInt == 1) {
            order = "id";
        } else if (orderInt == 2) {
            order = "listen_times";
        }
        params.put("order", order);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_ITEM_LIST_URL, new TypeReference<ResultInfo<MusicInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }


    public Observable<ResultInfo<List<MusicInfo>>> randomSpaInfo(String type_id) {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", "");
        params.put("type_id", type_id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SPA_RANDOM_URL, new TypeReference<ResultInfo<List<MusicInfo>>>() {
        }.getType(), params, true, true, true);

    }

    public Observable<ResultInfo<String>> audioPlay(String spa_id) {
        Map<String, String> params = new HashMap<>();

        params.put("music_id", spa_id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_PLAY_URL, new TypeReference<ResultInfo<MusicInfo>>() {
        }.getType(), params, true, true, true);

    }

    //user_id: 用户ID
    //spa_id: SPAID
    //音频收藏
    public Observable<ResultInfo<String>> collectAudio(String user_id, String music_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("music_id", music_id);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_COLLECT_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 首页热点
     *
     * @return
     */
    public Observable<ResultInfo<IndexHotInfoWrapper>> getIndexHotInfo() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.LOVE_INDEX_URL, new TypeReference<ResultInfo<IndexHotInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }

    /**
     * 获取音频收藏列表
     *
     * @param userId
     * @return
     */

    public Observable<ResultInfo<MusicInfoWrapper>> getCollectAudioList(String userId, int page, int page_size) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", page + "");
        params.put("page_size", page_size + "");

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_COLLECT_LIST_URL, new TypeReference<ResultInfo<MusicInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 统计搜索次数
     *
     * @param userId
     * @param keyword
     * @return
     */
    public Observable<ResultInfo<String>> searchCount(String userId, String keyword) {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("keyword", keyword);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SEARCH_COUNT_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 首页下拉热词
     *
     * @return
     */

    public Observable<ResultInfo<IndexHotInfoWrapper>> getIndexDropInfos(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.INDEX_DROP_URL, new TypeReference<ResultInfo<IndexHotInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 音频详情页
     *
     * @param userId
     * @param id
     * @return
     */

    public Observable<ResultInfo<MusicInfoWrapper>> getMusicDetailInfo(String userId, String id) {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("id", id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.AUDIO_DETAIL_URL, new TypeReference<ResultInfo<MusicInfoWrapper>>() {
                }.getType(),
                params, true, true, true);
    }


    /**
     * 分享得会员
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<String>> shareReward(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SHARE_REWARD_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 获取微信号
     * @param position  xuanfu  shizhan  miji  audio  lesson
     * @return
     */
    public Observable<ResultInfo<WetChatInfo>> getWechatInfo(String position) {
        Map<String,String> params = new HashMap<>();
        params.put("position",position);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.WECHAT_INFO_URL, new TypeReference<ResultInfo<WetChatInfo>>() {
        }.getType(), params, true, true, true);

    }


    /**
     * 获取社区最新发帖
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<ResultInfo<CommunityInfoWrapper>> getCommunityNewstInfos(String userId, int page, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(pageSize));

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMUNITY_NEWEST_LIST_URL, new TypeReference<ResultInfo<CommunityInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 帖子点赞
     *
     * @param uesr_id
     * @param topic_id
     * @return
     */
    public Observable<ResultInfo<String>> likeTopic(String uesr_id, String topic_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", uesr_id);
        params.put("topic_id", topic_id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.TOPIC_LIKE_URL, new TypeReference<ResultInfo<String>>() {
                }.getType(),
                params, true, true, true);

    }

    /**
     * 帖子详情
     *
     * @param topicId
     * @return
     */
    public Observable<ResultInfo<CommunityDetailInfo>> getCommunityDetailInfo(String userId, String topicId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);

        params.put("topic_id", topicId);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.TOPIC_DETAIL_URL, new TypeReference<ResultInfo<CommunityDetailInfo>>() {
                }.getType(),
                params, true, true, true);
    }

    /**
     * 创建评论
     *
     * @param user_id
     * @param topicId
     * @param content
     * @return
     */
    public Observable<ResultInfo<String>> createComment(String user_id, String topicId, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("topic_id", topicId);
        params.put("content", content);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.CREATE_COMMENT_URL, new TypeReference<ResultInfo<String>>() {
                }.getType(),
                params, true, true, true);
    }


    /**
     * 评论点赞
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<String>> commentLike(String userId, String commentId) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("comment_id", commentId);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMENT_LIKE_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 获取热门标签
     *
     * @return
     */
    public Observable<ResultInfo<CommunityTagInfoWrapper>> getCommunityTagInfos() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMUNITY_TAG_URL, new TypeReference<ResultInfo<CommunityTagInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }

    /**
     * 获取热门帖子
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */

    public Observable<ResultInfo<CommunityInfoWrapper>> getCommunityHotList(String userId, int page, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);

        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(pageSize));

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMUNITY_HOT_LIST_URL, new TypeReference<ResultInfo<CommunityInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 发帖
     *
     * @param userId
     * @param cat_id
     * @param content
     * @return
     */
    public Observable<ResultInfo<String>> publishCommunityInfo(String userId, String cat_id, String content) {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("cat_id", cat_id);
        params.put("content", content);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.PUBLISH_COMMUNITY_URL, new TypeReference<ResultInfo<String>>() {
                }.getType(),
                params, true, true, true);
    }


    /**
     * 我的发帖
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<CommunityInfoWrapper>> getMyCommunityInfos(String userId, int page, int pageSize) {

        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(pageSize));
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMUNITY_MY_URL, new TypeReference<ResultInfo<CommunityInfoWrapper>>() {
                }.getType(),
                params, true, true, true);

    }

    /**
     * 公告
     *
     * @return
     */

    public Observable<ResultInfo<TopTopicInfo>> getTopTopicInfos() {

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.TOP_TOPIC_URL, new TypeReference<ResultInfo<TopTopicInfo>>() {
        }.getType(), null, true, true, true);
    }


    /**
     * 热门标签列表
     *
     * @param userId
     * @param catId
     * @param page
     * @param pageSize
     * @return
     */
    public Observable<ResultInfo<CommunityInfoWrapper>> getCommunityTagListInfo(String userId, String catId, int page, int pageSize) {

        Map<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("cat_id", catId);
        params.put("page", String.valueOf(page));
        params.put("page_size", String.valueOf(pageSize));

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.COMMUNITY_TAG_LIST_URL, new TypeReference<ResultInfo<CommunityInfoWrapper>>() {
                }.getType(),
                params, true, true, true);

    }


    /***
     * 获取首页banner
     * @return
     */
    public Observable<ResultInfo<List<BannerInfo>>> getIndexBanner() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.INDEX_BANNER_URL, new TypeReference<ResultInfo<List<BannerInfo>>>() {
        }.getType(), null, true, true, true);
    }

    /**
     * 获取撩吧课程数据
     *
     * @param page
     * @param page_size
     * @return
     */

    public Observable<ResultInfo<List<CourseInfo>>> getChatCourseInfos(int page, int page_size) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("page_size", page_size + "");
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.CHAT_LESSONS_URL, new TypeReference<ResultInfo<List<CourseInfo>>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 获取撩吧课程详情数据
     *
     * @param id
     * @return
     */
    public Observable<ResultInfo<CourseInfo>> getChatCourseDetailInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.CHAT_LESSON_DETAIL_URL, new TypeReference<ResultInfo<CourseInfo>>() {
        }.getType(), params, true, true, true);

    }


}
