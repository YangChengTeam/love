package com.yc.verbaltalk.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

/**
 * 模糊drawable
 */
public class BlurDrawable {

    public static Bitmap rsBlur(Context context, Bitmap source, int radius) {

        int width = Math.round(source.getWidth() * 1f/4);
        int height = Math.round(source.getHeight() * 1f/4);

        Bitmap inputBmp = Bitmap.createScaledBitmap(source,width,height,false);

        //(1)
        RenderScript renderScript = RenderScript.create(context);

//        Log.i("TAG", "scale size:" + source.getWidth() + "*" + source.getHeight());

        // Allocate memory for Renderscript to work with
        //(2)

        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);
        //(8)
        renderScript.destroy();

        return inputBmp;
    }
}