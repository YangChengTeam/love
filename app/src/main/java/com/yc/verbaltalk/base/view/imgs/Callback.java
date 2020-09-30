package com.yc.verbaltalk.base.view.imgs;

import java.io.File;
import java.io.Serializable;



public interface Callback extends Serializable {

    void onSingleImageSelected(String path);

    void onImageSelected(String path);

    void onImageUnselected(String path);

    void onCameraShot(File imageFile);

    void onPreviewChanged(int select, int sum, boolean visible);
}
