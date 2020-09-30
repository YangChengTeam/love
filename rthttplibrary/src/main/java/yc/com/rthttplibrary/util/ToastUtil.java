package yc.com.rthttplibrary.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by suns  on 2020/7/27 17:06.
 */
public class ToastUtil {

    private static Toast toast;

    public static void toast(Context context, String mess) {

        if (toast == null) {
            toast = Toast.makeText(context, mess, Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
