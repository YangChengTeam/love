package yc.com.rthttplibrary.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class FileUtil {

    private static String uuid = "";

    public static void setUuid(String uuid) {
        FileUtil.uuid = uuid;
    }

    private static String getFileName(String name) {
        return Md5.md5(uuid + name);
    }

    ///< 读取输入流
    public static String readString(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    ///< 写入字符到文件
    public static void writeInfoToFile(String result, String dir, String name) {
        String tmpName = getFileName(name);
        String tmpResult = Base64.encode(Encrypt.encode(result).getBytes());
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File gpxfile = new File(dir, tmpName);
        try {
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(tmpResult);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LogUtil.msg("file w->" + e.getMessage(), LogUtil.W);
        }
        LogUtil.msg("file w ->" + tmpName + "->" + result);
    }

    ///< 写入图片到文件
    public static void writeImageToFile(Bitmap bitmap, String dir, String name) {
        String tmpName = getFileName(name);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File logoFile = new File(dir, tmpName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logoFile);
            fos.write(bitmapdata);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.msg(tmpName + "->" + e.getMessage(), LogUtil.W);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.msg(tmpName + "->" + e.getMessage(), LogUtil.W);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.msg("file w logo->" + logoFile.getAbsolutePath());
    }

    ///< 从文件获取bitmap
    public static Bitmap getBitmapFromFile(String dir, String name) {
        String tmpName = getFileName(name);
        String filePath = dir + "/" + tmpName;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        LogUtil.msg("file r logo->" + filePath);
        return bitmap;
    }

    ///< 从文件获取字符串
    public static String readInfoFromFile(String dir, String name) {
        String tmpName = getFileName(name);
        File file = new File(dir, tmpName);
        if (file.exists()) {
            //Read text from file
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
                String reuslt = Encrypt.decode(new String(Base64.decode(text.toString())));
                LogUtil.msg("file r ->" + tmpName + "->" + reuslt);
                return reuslt;
            } catch (IOException e) {
                //You'll need to add proper aerror handling here
                e.printStackTrace();
                LogUtil.msg("file r->" + e.getMessage(), LogUtil.W);
            }
        }
        return null;
    }

    ///< 将流写入文件
    public static void writeInputStreamToFile(InputStream in, String path) {
        OutputStream output = null;
        try {
            File file = new File(path);
            output = new FileOutputStream(file);

            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;
            while ((read = in.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
