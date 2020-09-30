package com.yc.verbaltalk.base.model;

/**
 * Created by suns  on 2020/6/22 10:10.
 */
public class UserAccreditInfo {
    private String nickname;

    private String face;

    private String city;

    private String iconUrl;

    private String gender;

    private String province;

    private String openid;

    private String accessToken;

    private String uid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserAccreditInfo{" +
                "nickname='" + nickname + '\'' +
                ", face='" + face + '\'' +
                ", city='" + city + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", openid='" + openid + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

}
