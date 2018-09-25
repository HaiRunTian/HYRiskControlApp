package tianchi.com.risksourcecontrol.util;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理类
 */

public class ActivityCollector {

    public static List<AppCompatActivity> s_activities =  new ArrayList<AppCompatActivity>();

    public static void addActivity(AppCompatActivity activity){
        s_activities.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        s_activities.remove(activity);
    }

    public static void finishAllActivity() {
        if (s_activities.size()>0) {
            for (AppCompatActivity _activity : s_activities) {
                if (!_activity.isFinishing()) {
                    _activity.finish();
                }
            }
        }
    }
}
