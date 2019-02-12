package tianchi.com.risksourcecontrol2.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by HaiRun on 2019/1/17.
 */

public class GpsUtils {
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps && network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
//        Intent GPSIntent = new Intent();
//        GPSIntent.setClassName("com.android.settings",
//                "com.android.settings.widget.SettingsAppWidgetProvider");
//        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
//        GPSIntent.setData(Uri.parse("custom:3"));
//        try {
//            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }


        AlertDialog.Builder dialog = new AlertDialog.Builder(context); //            dialog.setTitle("要使用定位功能，请打开GPS连接");
        dialog.setMessage("要使用定位功能，请打开GPS连接");
        dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
               // 转到手机设置界面，用户设置GPS
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent); // 设置完成后返回到原来的界面
            }
        });
        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create(); //点击外面不消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();


    }


}
