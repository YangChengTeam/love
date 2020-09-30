package com.yc.verbaltalk.model.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by sunshey on 2019/5/15.
 */

public class UploadUtils {
    private static String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
    private static String PREFIX = "--", LINE_END = "\r\n";
    private static String CONTENT_TYPE = "multipart/form-data";   //内容类型
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    private static  int state;
    /**
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @author ylbf_dev
     */
    public static boolean uploadFile(final ArrayList<File> imagefiles, final ArrayList<File> videofiles, final String uploadUrl, final ArrayList<String> name, final ArrayList<Object> values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(uploadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(TIME_OUT);
                    conn.setConnectTimeout(TIME_OUT);
                    conn.setDoInput(true);  //允许输入流
                    conn.setDoOutput(true); //允许输出流
                    conn.setUseCaches(false);  //不允许使用缓存
                    conn.setRequestMethod("POST");  //请求方式
                    conn.setRequestProperty("Charset", CHARSET);  //设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    StringBuffer sb = new StringBuffer();

                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);
                    /**
                     * 这里重点注意：
                     * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的   比如:abc.png
                     */
                    if (name!=null&&name.size()>0)
                        for (int i=0;i<name.size();i++){
                            addText(name.get(i),values.get(i)+"",dos);
                        }
                    addImage(imagefiles,dos);
                    addVedio(videofiles,dos);

                    // 读取服务器返回结果
                    InputStream isBack = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(isBack, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String result = br.readLine();
                    Log.d("Jun",result);
                    dos.close();
                    JSONObject jsonObject =new JSONObject(result);
                    state=jsonObject.optInt("State");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        if (state==1){
            return true;
        }else{
            return false;
        }

    }
    private static void addText(String name,Object value,DataOutputStream output) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX  + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\""+name+"\"" + LINE_END);
        sb.append(LINE_END);
        sb.append(value + LINE_END);
        sb.append(PREFIX + BOUNDARY + LINE_END);
        try {
            output.writeBytes(sb.toString());// 发送表单字段数据
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addImage(ArrayList<File> files, DataOutputStream output) {
        if (files.size()<1||files==null) return;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX + BOUNDARY + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + files.get(0).getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            output.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(files.get(0));
            byte[] bytes = new byte[1024];
            int len = 0;
            Log.d("Jun","开始上传");
            while ((len = is.read(bytes)) != -1) {
                output.write(bytes, 0, len);
            }
            is.close();
            output.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY  + LINE_END).getBytes();
            output.write(end_data);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private static void addVedio(ArrayList<File> files, DataOutputStream output) {
        if (files==null||files.size()<1) return;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX + BOUNDARY + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"video\"; filename=\"" + files.get(0).getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            output.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(files.get(0));
            byte[] bytes = new byte[1024];
            int len = 0;
            final int headerSize = sb.length();
            Log.d("Jun","开始上传");
            while ((len = is.read(bytes)) != -1) {
                output.write(bytes, 0, len);
            }
            is.close();
            output.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            output.write(end_data);
            Log.d("Jun",output+"");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
