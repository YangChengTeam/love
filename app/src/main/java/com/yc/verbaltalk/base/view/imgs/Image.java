package com.yc.verbaltalk.base.view.imgs;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/7.
 */

public class Image  implements Serializable {

    public String path;
    public String name;

    public Image(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Image() {

    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}