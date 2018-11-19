package tianchi.com.risksourcecontrol2.custom;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/** 
 *  @描述 自定义吐司
 *  @作者  Kevin.
 *  @创建日期 2017/12/18  14:59.
 */

public class MyToast extends Toast {
    private static Toast s_toast;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
    }

    /*
        * 弹出toast
        * */
    public static void showMyToast(Context context, String message, int duration) {
        if (s_toast == null) {
            s_toast = Toast.makeText(context, message, duration);
        } else {
            s_toast.setText(message+"");
        }
        s_toast.show();
    }

}

