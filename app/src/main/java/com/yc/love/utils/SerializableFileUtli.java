package com.yc.love.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionListener;
import com.yc.love.factory.ThreadPoolProxyFactory;
import com.yc.love.model.bean.ExampListsBean;
import com.yc.love.proxy.ThreadPoolProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by mayn on 2019/5/29.
 */

public class SerializableFileUtli {

    public static void checkPermissionWriteData(final Object obj, final String fileName) {
        if (obj == null || TextUtils.isEmpty(fileName)) {
            return;
        }
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

    public static Object checkReadPermission(Context context,final String fileName) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
                    return readDataToFile(fileName);
                }
        return null;
    }


    private static void writeDataToFile(Object obj, String fileName) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            //存入数据
            File file = new File(Environment.getExternalStorageDirectory().toString()
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

            /*//取出数据
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            List<ExampListsBean> savedArrayList = (List<ExampListsBean>) objectInputStream.readObject();

            for (int i = 0; i < savedArrayList.size(); i++) {
                System.out.println("取出的数据:" + savedArrayList.get(i).toString());
                Log.d("mylog", "writeDataToFile: " + "取出的数据:" + savedArrayList.get(i).toString());
            }*/

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
           /* if (objectInputStream != null) {
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
            }*/
        }
    }

    private static Object readDataToFile(String fileName) {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            //存入数据
            File file = new File(Environment.getExternalStorageDirectory().toString()
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
