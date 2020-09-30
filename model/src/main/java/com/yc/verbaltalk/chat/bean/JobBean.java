package com.yc.verbaltalk.chat.bean;

/**
 * Created by sunshey on 2019/4/24.
 */

public class JobBean {
    public String message;
//    public DataBean data;
    public String code;

    public static class DataBean {
        public String studentIdentState;
        public String bankCardIdentState;
        public String idIdentState;
        public String licenseIdentState;
        public String isFlagComp;

        @Override
        public String toString() {
            return "DataBean{" +
                    "studentIdentState='" + studentIdentState + '\'' +
                    ", bankCardIdentState='" + bankCardIdentState + '\'' +
                    ", idIdentState='" + idIdentState + '\'' +
                    ", licenseIdentState='" + licenseIdentState + '\'' +
                    ", isFlagComp='" + isFlagComp + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JobBean{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
