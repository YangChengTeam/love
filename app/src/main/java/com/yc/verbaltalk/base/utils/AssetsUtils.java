package com.yc.verbaltalk.base.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
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
}
