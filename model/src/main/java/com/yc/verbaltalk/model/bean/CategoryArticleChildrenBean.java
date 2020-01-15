package com.yc.love.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/10.
 */

public class CategoryArticleChildrenBean implements Parcelable,Serializable {
    public String _level;
    public String name;
    public int id;
    public int parent_id;

    public CategoryArticleChildrenBean() {
    }

  /*  public CategoryArticleChildrenBean(String _level, String name, int love_id, int parent_id) {
        this._level = _level;
        this.name = name;
        this.love_id = love_id;
        this.parent_id = parent_id;
    }*/

    // 读数据进行恢复
    private CategoryArticleChildrenBean(Parcel in) {
        _level = in.readString();
        name = in.readString();
        id = in.readInt();
        parent_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // 写数据进行保存
    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(_level);
        out.writeString(name);
        out.writeInt(id);
        out.writeInt(parent_id);
    }

    // 用来创建自定义的Parcelable的对象
    public static final Creator<CategoryArticleChildrenBean> CREATOR = new Creator<CategoryArticleChildrenBean>() {

        @Override
        public CategoryArticleChildrenBean createFromParcel(Parcel parcel) {
            return new CategoryArticleChildrenBean(parcel);
        }

        @Override
        public CategoryArticleChildrenBean[] newArray(int i) {
            return new CategoryArticleChildrenBean[i];
        }
    };

    @Override
    public String toString() {
        return "CategoryArticleChildrenBean{" +
                "_level='" + _level + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }
}
