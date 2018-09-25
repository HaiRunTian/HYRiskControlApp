package tianchi.com.risksourcecontrol.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 窗口工具类
 */

public class ScreenUtils {

    private ScreenUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    /*
    * 获取当前窗体宽度
    * */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /*
    * 获取当前窗体高度
    * */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * @param activity 上下文
     * @param width 原始窗口宽的倍数
     * @param height 原始窗口高的倍数
     * @param alpha 透明度
     * @param dimAmount 黑暗度
     * @return null
     * @datetime 2018/2/8  9:45.
     */
    public static   void setWindowsSize(Activity activity, double width, double height, double alpha, double dimAmount) {
        //固定窗口大小
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = activity.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.85);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        activity.getWindow().setAttributes(p);
    }

}
