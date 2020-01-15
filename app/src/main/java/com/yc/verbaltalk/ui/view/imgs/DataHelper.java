package com.yc.verbaltalk.ui.view.imgs;

import java.util.List;

/**
 * Created by mayn on 2019/5/7.
 */

public interface DataHelper<T> {

    boolean addAll(List<T> list);

    boolean addAll(int position, List<T> list);

    void add(T data);

    void add(int position, T data);

    void clear();

    boolean contains(T data);

    T getData(int index);

    void modify(T oldData, T newData);

    void modify(int index, T newData);

    boolean remove(T data);

    void remove(int index);

}
