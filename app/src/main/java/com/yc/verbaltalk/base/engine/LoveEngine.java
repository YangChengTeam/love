package com.yc.verbaltalk.base.engine;

import android.content.Context;
import android.text.TextUtils;

import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
import com.yc.verbaltalk.base.config.URLConfig;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.chat.bean.AudioDataWrapperInfo;
import com.yc.verbaltalk.chat.bean.BannerInfo;
import com.yc.verbaltalk.chat.bean.CategoryArticleBean;
import com.yc.verbaltalk.chat.bean.CommunityDetailInfo;
import com.yc.verbaltalk.chat.bean.CommunityInfoWrapper;
import com.yc.verbaltalk.chat.bean.CommunityTagInfoWrapper;
import com.yc.verbaltalk.chat.bean.CourseInfo;
import com.yc.verbaltalk.chat.bean.ExampDataBean;
import com.yc.verbaltalk.chat.bean.ExampListsBean;
import com.yc.verbaltalk.chat.bean.ExampleTsCategory;
import com.yc.verbaltalk.chat.bean.IndexHotInfoWrapper;
import com.yc.verbaltalk.chat.bean.LoveByStagesBean;
import com.yc.verbaltalk.chat.bean.LoveByStagesDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;
import com.yc.verbaltalk.chat.bean.MenuadvInfoBean;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.ShareInfo;
import com.yc.verbaltalk.chat.bean.TopTopicInfo;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.WetChatInfo;
import com.yc.verbaltalk.chat.bean.confession.ConfessionBean;
import com.yc.verbaltalk.index.bean.SmartChatItem;
import com.yc.verbaltalk.model.ModelApp;
import com.yc.verbaltalk.model.dao.LoveHealDetDetailsBeanDao;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import yc.com.rthttplibrary.bean.ResultInfo;


public class LoveEngine extends BaseEngine {
    private LoveHealDetDetailsBeanDao detailsBeanDao;

    public LoveEngine(Context context) {
        super(context);
        detailsBeanDao = ModelApp.getDaoSession().getLoveHealDetDetailsBeanDao();
    }

    public Observable<ResultInfo<UserInfo>> userInfo(String userId) {


        return request.userInfo(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealDateBean>>> loveCategory(String sence) {


        return request.loveCategory(sence).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealDetBean>>> loveListCategory(String userId, String category_id, String page, String page_size) {


        return request.loveListCategory(userId, category_id, page, page_size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealingBean>>> recommendLovewords(String userId, String page, String page_size, String url) {

        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return request.recommendLovewords(userId, page, page_size, url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<ExampleTsCategory>> exampleTsCategory() {

        return request.exampleTsCategory().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<MenuadvInfoBean>> menuadvInfo() {

        return request.menuAdvInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<ExampDataBean>> exampLists(String userId, String page, String pageSize) {


        return request.exampLists(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<List<ExampListsBean>>> exampleCollectList(String userId, String page, String pageSize) {

        return request.exampleCollectList(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<List<CategoryArticleBean>>> categoryArticle() {

        return request.categoryArticle().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<List<LoveByStagesBean>>> listsArticle(String categoryId, String page, String pageSize) {

        return request.listsArticle(categoryId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealDetBean>>> searchDialogue(String userId, String searchType, String keyword, String page, String pageSize) {


        return request.searchDialogue(userId, searchType, keyword, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<SearchDialogueBean>> searchDialogue2(String userId, String keyword, String page, String pageSize) {

        return request.searchDialogue2(userId, keyword, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealingBean>>> listsCollectLovewords(String userId, String page, String pageSize) {


        return request.listsCollectLovewords(userId, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<LoveByStagesDetailsBean>> detailArticle(String id, String userId) {

        return request.detailArticle(id, userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<LoveByStagesDetailsBean>> detailExample(String id, String userId) {

        return request.detailExample(id, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<ResultInfo<ExampDataBean>> exampleTsList(String id, String page, String pageSize) {


        return request.exampleTsList(id, page, pageSize).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> collectArticle(String userId, String articleId, String url) {


        return request.collectArticle(userId, articleId, url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<String>> collectLovewords(String userId, String lovewordsId, String url) {

        return request.collectLovewords(userId, lovewordsId, url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> collectExample(String userId, String exampleId, String url) {


        return request.collectExample(userId, exampleId, url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ConfessionBean> getExpressData(int page) {


        return request.getExpressData(URLConfig.CATEGORY_LIST_URL, "1", page, false, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<String> collectLoveHeal(final LoveHealDetDetailsBean detDetailsBean) {

        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(s -> {
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
        });


    }

    public Observable<List<LoveHealDetDetailsBean>> getCollectLoveHeals(final int limit, final int offset) {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(s -> detailsBeanDao.queryBuilder().offset(offset * limit).limit(limit).orderDesc(LoveHealDetDetailsBeanDao.Properties.SaveTime).list());
    }

    private LoveHealDetDetailsBean getCollectLoveHealById(String content) {
        return detailsBeanDao.queryBuilder().where(LoveHealDetDetailsBeanDao.Properties.Content.eq(content)).unique();
    }


    public Observable<String> deleteCollectLoveHeals(final LoveHealDetDetailsBean detDetailsBean) {
        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(s -> {
            if (detDetailsBean == null) return "";
            detailsBeanDao.delete(detDetailsBean);
            return "success";
        });
    }

    public Observable<ResultInfo<ShareInfo>> getShareInfo() {
        return request.getShareInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //分类
    public Observable<ResultInfo<AudioDataWrapperInfo>> getAudioDataInfo() {

        return request.getAudioDataInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    //列表
    //order  排序   id 按照时间倒序排列   listen_times 按照点击次数排序
    public Observable<ResultInfo<MusicInfoWrapper>> getLoveItemList(String userId, String typeId, int page, int limit, int orderInt) {


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


        return request.getLoveItemList(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public Observable<ResultInfo<List<MusicInfo>>> randomSpaInfo(String type_id) {


        return request.randomSpaInfo("", type_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> audioPlay(String spa_id) {


        return request.audioPlay(spa_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //user_id: 用户ID
    //spa_id: SPAID
    //音频收藏
    public Observable<ResultInfo<String>> collectAudio(String user_id, String music_id) {


        return request.collectAudio(user_id, music_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 首页热点
     *
     * @return
     */
    public Observable<ResultInfo<IndexHotInfoWrapper>> getIndexHotInfo() {

        return request.getIndexHotInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取音频收藏列表
     *
     * @param userId
     * @return
     */

    public Observable<ResultInfo<MusicInfoWrapper>> getCollectAudioList(String userId, int page, int page_size) {


        return request.getCollectAudioList(userId, page, page_size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 统计搜索次数
     *
     * @param userId
     * @param keyword
     * @return
     */
    public Observable<ResultInfo<String>> searchCount(String userId, String keyword) {


        return request.searchCount(userId, keyword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 首页下拉热词
     *
     * @return
     */

    public Observable<ResultInfo<IndexHotInfoWrapper>> getIndexDropInfos(String keyword) {


        return request.getIndexDropInfos(keyword).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 音频详情页
     *
     * @param userId
     * @param id
     * @return
     */

    public Observable<ResultInfo<MusicInfoWrapper>> getMusicDetailInfo(String userId, String id) {

        return request.getMusicDetailInfo(userId, id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 分享得会员
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<String>> shareReward(String userId) {

        return request.shareReward(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取微信号
     *
     * @param position xuanfu  shizhan  miji  audio  lesson
     * @return
     */
    public Observable<ResultInfo<WetChatInfo>> getWechatInfo(String position) {


        return request.getWechatInfo(position).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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


        return request.getCommunityNewstInfos(userId, page, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 帖子点赞
     *
     * @param uesr_id
     * @param topic_id
     * @return
     */
    public io.reactivex.Observable<yc.com.rthttplibrary.bean.ResultInfo<String>> likeTopic(String uesr_id, String topic_id) {


        return request.likeTopic(uesr_id, topic_id).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread());
    }

    /**
     * 帖子详情
     *
     * @param topicId
     * @return
     */
    public Observable<ResultInfo<CommunityDetailInfo>> getCommunityDetailInfo(String userId, String topicId) {


        return request.getCommunityDetailInfo(userId, topicId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
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


        return request.createComment(user_id, topicId, content).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 评论点赞
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<String>> commentLike(String userId, String commentId) {

        return request.commentLike(userId, commentId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 获取热门标签
     *
     * @return
     */
    public Observable<ResultInfo<CommunityTagInfoWrapper>> getCommunityTagInfos() {

        return request.getCommunityTagInfos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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


        return request.getCommunityHotList(userId, page, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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


        return request.publishCommunityInfo(userId, cat_id, content).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 我的发帖
     *
     * @param userId
     * @return
     */
    public Observable<ResultInfo<CommunityInfoWrapper>> getMyCommunityInfos(String userId, int page, int pageSize) {


        return request.getMyCommunityInfos(userId, page, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 公告
     *
     * @return
     */

    public Observable<ResultInfo<TopTopicInfo>> getTopTopicInfos() {


        return request.getTopTopicInfos().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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


        return request.getCommunityTagListInfo(userId, catId, page, pageSize).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /***
     * 获取首页banner
     * @return
     */
    public Observable<ResultInfo<List<BannerInfo>>> getIndexBanner() {

        return request.getIndexBanner().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取撩吧课程数据
     *
     * @param page
     * @param page_size
     * @return
     */

    public Observable<ResultInfo<List<CourseInfo>>> getChatCourseInfos(int page, int page_size) {


        return request.getChatCourseInfos(page, page_size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取撩吧课程详情数据
     *
     * @param id
     * @return
     */
    public Observable<ResultInfo<CourseInfo>> getChatCourseDetailInfo(String id) {

        return request.getChatCourseDetailInfo(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 用户进入统计
     *
     * @return
     */
    public Observable<ResultInfo<String>> userActive() {

        return request.userActive(URLConfig.USER_ACTIVATE_URL).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 第三方登录
     *
     * @param access_token 登录token
     * @param account_type 登录类型 1 qq 2 微信
     * @param face         图像
     * @param nick_name    昵称
     * @return
     */

    public Observable<ResultInfo<UserInfo>> thirdLogin(String access_token, int account_type, String face, String sex, String nick_name) {


        return request.thirdLogin(access_token, account_type, face, sex, nick_name).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<String> connectUpFileNet(Map<String, String> params, File file) {


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {

            requestBodyMap.put(entry.getKey(), RequestBody.create(MediaType.parse("text/plain"), entry.getValue()));
        }

        String fileName = URLConfig.BASE_NORMAL_FILE_DIR + File.separator + System.currentTimeMillis() + (int) (Math.random() * 10000) + ".jpg";

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("img", fileName, photoRequestBody);

        return request.connectUpFileNet(URLConfig.URL_IMAGE_CREATE, requestBodyMap, photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> connectNet(Map<String, String> params) {
        return request.connectNet(URLConfig.URL_IMAGE_CREATE, params, false, false).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<SmartChatItem>> smartSearchVerbal(String keyword, int section) {
        return request.smartSearchVerbal(keyword, UserInfoHelper.getUid(), section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> aiCollect(String id) {
        return request.aiCollect(id, UserInfoHelper.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> aiPraise(String id) {
        return request.aiPraise(id, UserInfoHelper.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<List<LoveHealDetDetailsBean>>> getVerbalList(int page, int page_size) {
        return request.getVerbalList(UserInfoHelper.getUid(), page, page_size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
