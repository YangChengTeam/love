package com.yc.verbaltalk.model.bean;

import java.util.List;

/**
 * Created by suns  on 2019/9/3 16:19.
 */
public class CommunityTagInfoWrapper {


    /**
     * list : [{"cat_id":2,"cat_name":"热恋期"},{"cat_id":3,"cat_name":"磨合期"},{"cat_id":1,"cat_name":"交友期"}]
     * image : http://love.bshu.com/static/community_hot_top_bg.jpg
     */

    private String image;
    private List<CommunityTagInfo> list;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CommunityTagInfo> getList() {
        return list;
    }

    public void setList(List<CommunityTagInfo> list) {
        this.list = list;
    }
}
