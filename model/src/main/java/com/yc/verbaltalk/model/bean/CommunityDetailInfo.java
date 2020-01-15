package com.yc.verbaltalk.model.bean;

import java.util.List;

/**
 * Created by suns  on 2019/9/3 14:10.
 */
public class communityDetaiInfo {

    /**
     * topic_info : {"topic_id":40,"content":"快乐，不是拥有的多，而是计较的少。感谢那些看透了我，却还一直陪在我身边的人。等待，有时候并不是相信那个人还会回来，而是因为还有爱。别人说你变了，是因为你没有按照他的想法活罢了。别人怎么看你不重要，唯一重要的是你很喜欢真实的自己。别人怎么看你不重要，重要的是你很喜欢真实的自己。","digcount":1,"comcount":1,"create_time":1409835150,"nick_name":"大成和怡","face":"http://love.bshu.com/logo/2019022808114037961.jpg","sex":0,"is_dig":0}
     * comment_list : [{"comment_id":15,"content":"美女你好 jiaQQMA","digcount":0,"create_time":1567215335,"user_id":10,"nick_name":"武疆涵意","face":"http://love.bshu.com/logo/14824774843536848.jpg","sex":0,"is_dig":0}]
     */

    private CommunityInfo topic_info;
    private List<CommunityInfo> comment_list;

    public CommunityInfo getTopic_info() {
        return topic_info;
    }

    public void setTopic_info(CommunityInfo topic_info) {
        this.topic_info = topic_info;
    }

    public List<CommunityInfo> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommunityInfo> comment_list) {
        this.comment_list = comment_list;
    }
}
