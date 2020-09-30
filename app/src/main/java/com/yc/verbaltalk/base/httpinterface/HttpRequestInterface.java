package com.yc.verbaltalk.base.httpinterface;


import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicInfoWrapper;
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
import com.yc.verbaltalk.chat.bean.IndexDoodsBean;
import com.yc.verbaltalk.chat.bean.IndexHotInfoWrapper;
import com.yc.verbaltalk.chat.bean.LoveByStagesBean;
import com.yc.verbaltalk.chat.bean.LoveByStagesDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealDateBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetBean;
import com.yc.verbaltalk.chat.bean.LoveHealDetDetailsBean;
import com.yc.verbaltalk.chat.bean.LoveHealingBean;
import com.yc.verbaltalk.chat.bean.MenuadvInfoBean;
import com.yc.verbaltalk.chat.bean.OrdersInitBean;
import com.yc.verbaltalk.chat.bean.OthersJoinNum;
import com.yc.verbaltalk.chat.bean.SearchDialogueBean;
import com.yc.verbaltalk.chat.bean.ShareInfo;
import com.yc.verbaltalk.chat.bean.TopTopicInfo;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.WetChatInfo;
import com.yc.verbaltalk.chat.bean.confession.ConfessionBean;
import com.yc.verbaltalk.index.bean.SmartChatItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by suns  on 2020/7/30 10:55.
 */
public interface HttpRequestInterface {
    //表白
    @FormUrlEncoded
    @POST
    Observable<ConfessionBean> getExpressData(@Url String url, @Field("id") String id, @Field("page") int page, @Field("isrsa") boolean isrsa, @Field("iszip") boolean iszip);

    // 图片合成
    @Multipart
    @POST
    Observable<String> connectUpFileNet(@Url String requestUrl, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST
    Observable<String> connectNet(@Url String requestUrl, @FieldMap Map<String, String> params, @Field("isrsa") boolean isrsa, @Field("iszip") boolean iszip);

    //音频分类
    @POST("music/cats")
    Observable<ResultInfo<AudioDataWrapperInfo>> getAudioDataInfo();

    //音频列表
    @FormUrlEncoded
    @POST("music/lists")
    Observable<ResultInfo<MusicInfoWrapper>> getLoveItemList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Lesson/lists")
    Observable<ResultInfo<List<CourseInfo>>> getChatCourseInfos(@Field("page") int page, @Field("page_size") int page_size);

    @POST("Hotsearch/index")
    Observable<ResultInfo<IndexHotInfoWrapper>> getIndexHotInfo();

    @POST("banner/index")
    Observable<ResultInfo<List<BannerInfo>>> getIndexBanner();

    //首页搜索框下拉热词
    @FormUrlEncoded
    @POST("hotsearch/dropdown")
    Observable<ResultInfo<IndexHotInfoWrapper>> getIndexDropInfos(@Field("keyword") String keyword);

    //搜索次数统计
    @FormUrlEncoded
    @POST("dialogue/searchlog")
    Observable<ResultInfo<String>> searchCount(@Field("user_id") String userId, @Field("keyword") String keyword);

    //音频收藏接口
    @FormUrlEncoded
    @POST("music/collect")
    Observable<ResultInfo<String>> collectAudio(@Field("user_id") String user_id, @Field("music_id") String music_id);

    //音频详情
    @FormUrlEncoded
    @POST("music/info")
    Observable<ResultInfo<MusicInfoWrapper>> getMusicDetailInfo(@Field("user_id") String userId, @Field("id") String id);

    //spa随便听听
    @FormUrlEncoded
    @POST("spa/random")
    Observable<ResultInfo<List<MusicInfo>>> randomSpaInfo(@Field("user_id") String user_id, @Field("type_id") String type_id);

    //音频播放次数
    @FormUrlEncoded
    @POST("music/play")
    Observable<ResultInfo<String>> audioPlay(@Field("music_id") String spa_id);

    //音频收藏列表
    @FormUrlEncoded
    @POST("music/collect_list")
    Observable<ResultInfo<MusicInfoWrapper>> getCollectAudioList(@Field("user_id") String userId, @Field("page") int page, @Field("page_size") int page_size);

    //分享得会员
    @FormUrlEncoded
    @POST("share/reward")
    Observable<ResultInfo<String>> shareReward(@Field("user_id") String userId);

    @Multipart
    @POST("common/upload")
    Observable<ResponseBody> uploadPhoto(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("user/code")
    Observable<ResultInfo<String>> sendCode(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("user/reset")
    Observable<ResultInfo<String>> resetPassword(@Field("code") String code, @Field("mobile") String mobile, @Field("new_password") String password);

    @FormUrlEncoded
    @POST("user/reg")
    Observable<ResultInfo<UserInfo>> register(@Field("code") String code, @Field("mobile") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/login")
    Observable<ResultInfo<UserInfo>> login(@Field("mobile") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<UserInfo>> codeLogin(@Url String url, @Field("mobile") String mobile, @Field("code") String code);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<UserInfo>> modifyPwd(@Url String url, @Field("user_id") String user_id, @Field("password") String pwd, @Field("new_password") String newPwd);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<String>> setPwd(@Url String url, @Field("user_id") String user_id, @Field("password") String pwd);


    @POST("Share/info")
    Observable<ResultInfo<ShareInfo>> getShareInfo();

    @FormUrlEncoded
    @POST("config/index")
    Observable<ResultInfo<WetChatInfo>> getWechatInfo(@Field("position") String position);

    @FormUrlEncoded
    @POST("topic/newlist")
    Observable<ResultInfo<CommunityInfoWrapper>> getCommunityNewstInfos(@Field("user_id") String userId, @Field("page") int page, @Field("page_size") int pageSize);

    @FormUrlEncoded
    @POST("topic/dig")
    Observable<ResultInfo<String>> likeTopic(@Field("user_id") String user_id, @Field("topic_id") String topic_id);

    @FormUrlEncoded
    @POST("Topic/catlist")
    Observable<ResultInfo<CommunityInfoWrapper>> getCommunityTagListInfo(@Field("user_id") String userId, @Field("cat_id") String catId, @Field("page") int page, @Field("page_size") int pageSize);

    @POST("TopicCat/all")
    Observable<ResultInfo<CommunityTagInfoWrapper>> getCommunityTagInfos();

    @FormUrlEncoded
    @POST("topic/hotlist")
    Observable<ResultInfo<CommunityInfoWrapper>> getCommunityHotList(@Field("user_id") String userId, @Field("page") int page, @Field("page_size") int pageSize);

    @FormUrlEncoded
    @POST("topic/post")
    Observable<ResultInfo<String>> publishCommunityInfo(@Field("user_id") String userId, @Field("cat_id") String cat_id, @Field("content") String content);

    @FormUrlEncoded
    @POST("Topic/mylist")
    Observable<ResultInfo<CommunityInfoWrapper>> getMyCommunityInfos(@Field("user_id") String userId, @Field("page") int page, @Field("page_size") int pageSize);

    @POST("Topic/top")
    Observable<ResultInfo<TopTopicInfo>> getTopTopicInfos();

    @FormUrlEncoded
    @POST("Lesson/detail")
    Observable<ResultInfo<CourseInfo>> getChatCourseDetailInfo(@Field("id") String id);


    @POST()
    Observable<ResultInfo<String>> userActive(@Url String url);

    @FormUrlEncoded
    @POST("user/sns")
    Observable<ResultInfo<UserInfo>> thirdLogin(@Field("token") String access_token, @Field("sns") int account_type, @Field("face") String face, @Field("sex") String sex, @Field("nick_name") String nick_name);

    @FormUrlEncoded
    @POST("user/info")
    Observable<ResultInfo<UserInfo>> userInfo(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("TopicComment/dig")
    Observable<ResultInfo<String>> commentLike(@Field("user_id") String userId, @Field("comment_id") String commentId);

    @POST("topic/detail")
    @FormUrlEncoded
    Observable<ResultInfo<CommunityDetailInfo>> getCommunityDetailInfo(@Field("user_id") String userId, @Field("topic_id") String topicId);

    @FormUrlEncoded
    @POST("TopicComment/create")
    Observable<ResultInfo<String>> createComment(@Field("user_id") String user_id, @Field("topic_id") String topicId, @Field("content") String content);

    @FormUrlEncoded
    @POST("Dialogue/category")
    Observable<ResultInfo<List<LoveHealDateBean>>> loveCategory(@Field("sence") String sence);

    @FormUrlEncoded
    @POST("Dialogue/search")
    Observable<ResultInfo<SearchDialogueBean>> searchDialogue2(@Field("user_id") String userId, @Field("keyword") String keyword, @Field("page") String page, @Field("page_size") String pageSize);

    @FormUrlEncoded
    @POST("Dialogue/search")
    Observable<ResultInfo<List<LoveHealDetBean>>> searchDialogue(@Field("user_id") String userId, @Field("search_type") String searchType, @Field("keyword") String keyword, @Field("page") String page, @Field("page_size") String pageSize);

    @FormUrlEncoded
    @POST("Article/lists")
    Observable<ResultInfo<List<LoveByStagesBean>>> listsArticle(@Field("category_id") String categoryId, @Field("page") String page, @Field("page_size") String pageSize);

    @POST("Article/category")
    Observable<ResultInfo<List<CategoryArticleBean>>> categoryArticle();

    @POST("example/ts_category")
    Observable<ResultInfo<ExampleTsCategory>> exampleTsCategory();

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<List<LoveHealingBean>>> recommendLovewords(@Field("user_id") String userId, @Field("page") String page, @Field("page_size") String page_size, @Url String url);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<String>> collectLovewords(@Field("user_id") String userId, @Field("lovewords_id") String lovewordsId, @Url String url);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<String>> collectArticle(@Field("user_id") String userId, @Field("article_id") String articleId, @Url String url);

    @FormUrlEncoded
    @POST("Article/detail")
    Observable<ResultInfo<LoveByStagesDetailsBean>> detailArticle(@Field("id") String id, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("example/detail")
    Observable<ResultInfo<LoveByStagesDetailsBean>> detailExample(@Field("example_id") String id, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST
    Observable<ResultInfo<String>> collectExample(@Field("user_id") String userId, @Field("example_id") String exampleId, @Url String url);

    @FormUrlEncoded
    @POST("Dialogue/lists")
    Observable<ResultInfo<List<LoveHealDetBean>>> loveListCategory(@Field("user_id") String userId, @Field("category_id") String category_id, @Field("page") String page, @Field("page_size") String page_size);


    @POST("menuadv/info")
    Observable<ResultInfo<MenuadvInfoBean>> menuAdvInfo();


    @FormUrlEncoded
    @POST("Example/collect_list")
    Observable<ResultInfo<List<ExampListsBean>>> exampleCollectList(@Field("user_id") String userId, @Field("page") String page, @Field("page_size") String pageSize);

    @FormUrlEncoded
    @POST("example/lists")
    Observable<ResultInfo<ExampDataBean>> exampLists(@Field("user_id") String userId, @Field("page") String page, @Field("page_size") String pageSize);


    @FormUrlEncoded
    @POST("Lovewords/collect_list")
    Observable<ResultInfo<List<LoveHealingBean>>> listsCollectLovewords(@Field("user_id") String userId, @Field("page") String page, @Field("page_size") String pageSize);

    @FormUrlEncoded
    @POST("example/ts_lists")
    Observable<ResultInfo<ExampDataBean>> exampleTsList(@Field("category_id") String id, @Field("page") String page, @Field("page_size") String pageSize);


    @POST("goods/index")
    Observable<ResultInfo<List<IndexDoodsBean>>> indexGoods();

    @POST("Others/join_num")
    Observable<ResultInfo<OthersJoinNum>> othersJoinNum();

    @FormUrlEncoded
    @POST("orders/init")
    Observable<ResultInfo<OrdersInitBean>> initOrders(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Suggestion/add")
    Observable<ResultInfo<String>> addSuggestion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/update")
    Observable<ResultInfo<UserInfo>> updateInfo(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Search/newa")
    Observable<ResultInfo<SmartChatItem>> smartSearchVerbal(@Field("s_key") String keyword, @Field("user_id") String userId, @Field("section") int section);


    @FormUrlEncoded
    @POST("Search/collect")
    Observable<ResultInfo<String>> aiCollect(@Field("id") String id, @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("Search/favour")
    Observable<ResultInfo<String>> aiPraise(@Field("id") String id, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("Search/collectList")
    Observable<ResultInfo<List<LoveHealDetDetailsBean>>> getVerbalList(@Field("user_id") String userId, @Field("page") int page, @Field("page_size") int page_size);
}
