package com.yc.verbaltalk.base.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.yc.verbaltalk.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by suns  on 2019/8/30 15:31.
 */
public class AssetsUtils {

    public static String getJson(Context context, String fileName) {
        AssetManager assets = context.getAssets();
        StringBuilder buffer = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assets.open(fileName)));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    public static String readAsset(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder result = new StringBuilder();


            while ((line = br.readLine()) != null)
                result.append(line.replace("XX", context.getString(R.string.app_name))).append("\n");
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
