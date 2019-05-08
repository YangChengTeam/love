package com.yc.love.model.error;

/**
 * Created by mayn on 2019/5/8.
 */

public class KkServeThrowable extends Throwable  {

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.out.println("服务器异常");
    }
}
