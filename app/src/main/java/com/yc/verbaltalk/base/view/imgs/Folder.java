package com.yc.verbaltalk.base.view.imgs;

import java.io.Serializable;
import java.util.List;



public class Folder implements Serializable {

    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    public Folder() {

    }
    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}