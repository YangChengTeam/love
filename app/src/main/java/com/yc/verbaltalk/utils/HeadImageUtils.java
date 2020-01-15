/**
 * 
 */
package com.yc.verbaltalk.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;

/**
 * @author HBX
 * 
 */
public class HeadImageUtils 
{
	/**拍照返回请求吗码*/
	public static final int FROM_CRAMA=0;
	/**图库选择照片返回码*/
	public static final int FROM_LOCAL=1;
	/**裁剪后返回码*/
	public static final int FROM_CUT=2;
	/**自由裁剪后返回码*/
	public static final int FREE_CUT=3;
	/**拍照后照片地址*/
	public static Uri photoCamare=null;
	/**裁剪后照片地址*/
	public static Uri cutPhoto=null;
	//裁剪后的图片的真实路径
	public static String imgPath=null;
	//裁剪后的bitmap
	public static Bitmap cropBitmap = null;
	//裁剪完成之后的图片路径
	public static String imgResultPath=null;

	public static void getFromCamara(final Activity activity)
	{
		HeadImageUtils.photoCamare=setCutUriImage(activity);
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,HeadImageUtils.photoCamare);
		activity.startActivityForResult(intent,HeadImageUtils.FROM_CRAMA);
	}
	
	/**
	 * @category 从图库获得照片
	 * */
	public static void getFromLocation(final Activity context)
	{
		Intent intent=new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		context.startActivityForResult(intent,HeadImageUtils.FROM_LOCAL);
		
		/*Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		context.startActivityForResult(pickIntent, HeadImageUtils.FROM_LOCAL);*/
		
	}
	
	/**
	 * @category 从图库获得照片
	 * */
	public static void getFromLocation1(final Fragment context)
	{
		Intent intent=new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		context.startActivityForResult(intent,HeadImageUtils.FROM_LOCAL);
		
		/*Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		// 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		context.startActivityForResult(pickIntent, HeadImageUtils.FROM_LOCAL);*/
		
	}

	public static void cutCorePhoto(Activity activity, Uri uri)
	{
		HeadImageUtils.cutPhoto=setCutUriImage(activity);
		
		//----获取图片的真实路径
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		if(filePathColumn != null && filePathColumn.length > 0){
			Cursor cursor = activity.getContentResolver().query(HeadImageUtils.cutPhoto, filePathColumn, null, null,null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				imgPath = cursor.getString(columnIndex);
			}
		}else{
			imgPath = null;
		}
		
		//-----
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX",1);
		intent.putExtra("aspectY",1);
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, HeadImageUtils.cutPhoto);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, FROM_CUT);
		
	}
	
	public static Uri setCutUriImage(Context context)
	{
		Uri imagePath=null;
		String status= Environment.getExternalStorageState();
		SimpleDateFormat fomater=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
		long time= System.currentTimeMillis();
		String timeString=fomater.format(new Date(time));
		ContentValues values=new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, timeString);
		values.put(MediaStore.Images.Media.DATE_TAKEN,time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		if(status.equals(Environment.MEDIA_MOUNTED))
		{
			imagePath=context.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		}
		else
		{
			imagePath=context.getContentResolver().insert(
					MediaStore.Images.Media.INTERNAL_CONTENT_URI,values);
		}
		
		return imagePath;
	}

}
