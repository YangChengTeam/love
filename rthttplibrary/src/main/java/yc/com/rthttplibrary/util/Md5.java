package yc.com.rthttplibrary.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5 {

	public static String md5(String name) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffers = md.digest(name.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte buffer : buffers) {
				String s = Integer.toHexString(0xff & buffer);
				if (s.length() == 1) {
					sb.append("0").append(s);
				}
				if (s.length() != 1) {
					sb.append(s);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final String TAG = "MD5";
	private static final String ALGORITHM = "MD5";

	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static MessageDigest sDigest;

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "Get MD5 Digest failed.");
		}
	}

}
