package cn.edu.scujcc.downloadandupload.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Administrator
 */
public class ToastUtils {

    public ToastUtils() {

    }

    public static void shortToast(Context context, String name) {
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setText(name);
        toast.show();
    }
}