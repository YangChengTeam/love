package com.yc.love.model.bean;

import java.util.List;

/**
 * Created by mayn on 2019/5/17.
 */

public class UploadPhotoBean {

    /**
     * code : 1
     * msg : 上传成功
     * data : [{"path":"/uploads/images/20190517/eb50f507e8c2ddcd32b5edd9fe5cba3f.","url":"http://love.bshu.com/uploads/images/20190517/eb50f507e8c2ddcd32b5edd9fe5cba3f."}]
     */

    public int code;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * path : /uploads/images/20190517/eb50f507e8c2ddcd32b5edd9fe5cba3f.
         * url : http://love.bshu.com/uploads/images/20190517/eb50f507e8c2ddcd32b5edd9fe5cba3f.
         */

        public String path;
        public String url;
    }
}
