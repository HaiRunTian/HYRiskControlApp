package tianchi.com.risksourcecontrol2.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import tianchi.com.risksourcecontrol2.R;

/**
 * 自定义日期选择器
 * Created by Kevin on 2017/11/6.
 */

public class MyDatePicker {

    static int year;
    static int month;
    static int day;
    static String monthStr;
    static String dayStr;

    /*
    * 弹出日期选择器
    * */
    public static void ShowDatePicker(Context context, final TextView tvDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择时间")
                .setIcon(R.mipmap.ic_calendar2);
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_dateselect, null);
        builder.setView(linearLayout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = (DatePicker) linearLayout.findViewById(R.id.datepicker1);
                year = datePicker.getYear();
                month = datePicker.getMonth() + 1;
                day = datePicker.getDayOfMonth();
                if (month < 10)
                    monthStr = "0" + month;
                else
                    monthStr = "" + month;

                if (day < 10)
                    dayStr = "0" + day;
                else
                    dayStr = "" + day;
                if (tvDate != null) {
                    tvDate.setText(year + "-" + monthStr + "-" + dayStr);
                }
            }
        })
                .setNegativeButton("取消", null)
                .create().show();
    }
}
