package com.yc.love.cache;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.yc.love.factory.ThreadPoolProxyFactory;
import com.yc.love.proxy.ThreadPoolProxy;
import com.yc.love.ui.activity.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by mayn on 2019/5/30.
 */

public abstract class BaseFileStrategy implements CacheStrategy {

    /**
     * 缓存文件的文件夹名称
     */
    private String mDirName;

    /**
     * 构造函数
     *
     * @param dirName 缓存文件的文件夹名称
     */
    public BaseFileStrategy(String dirName) {
        mDirName = dirName;

    }


    @Override
    public void setCache(final String fileName, final Object obj) {
        ThreadPoolProxy normalThreadPoolProxy = ThreadPoolProxyFactory.getNormalThreadPoolProxy();
        normalThreadPoolProxy.execute(new Runnable() {
            @Override
            public void run() {
                SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        //if you want do noting or no need all the callbacks you may use SimplePermissionAdapter instead
                        new CheckRequestPermissionListener() {
                            @Override
                            public void onPermissionOk(Permission permission) {
                                writeDataToFile(obj, fileName);
                            }

                            @Override
                            public void onPermissionDenied(Permission permission) {

                            }
                        });
            }
        });
    }

    private void writeDataToFile(Object obj, String fileName) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            //存入数据
            File file = new File(mDirName
                    + File.separator + fileName + ".data");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(file.toString());
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(obj);

            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            Object o = objectInputStream.readObject();
        } catch (Exception e) {
            Log.d("mylog", "writeDataToFile: 取出的数据 Exception " + e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getCache(final Context context, final String fileName) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return readDataToFile(fileName);
        }
        return null;
    }

    private Object readDataToFile(String fileName) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            //存入数据
            File file = new File(mDirName
                    + File.separator + fileName + ".data");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            //取出数据
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            Log.d("mylog", "writeDataToFile: 取出的数据 Exception " + e);
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}

