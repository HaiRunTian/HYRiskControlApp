package tianchi.com.risksourcecontrol.base;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.supermap.data.Environment;

import java.util.HashMap;
import java.util.Map;

import tianchi.com.risksourcecontrol.config.DataManager;
import tianchi.com.risksourcecontrol.config.DefaultDataConfig;
import tianchi.com.risksourcecontrol.config.DefaultDataManager;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.location.LocationService;
import tianchi.com.risksourcecontrol.util.ActivityCollector;
import tianchi.com.risksourcecontrol.util.AssetsUtils;
import tianchi.com.risksourcecontrol.util.SharedPreferencesUtil;


public class MyApplication extends Application {
    public static String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/"; //手机根路径
    private static MyApplication sInstance = null;
    public static String KEY_IS_QUIT = "quit";
    public static String KEY_INTERVAL = "interval";

    private DefaultDataManager m_defaultDataManager = null;

    private DataManager m_dataManager = null;

    public LocationService locationService;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sInstance = this;

        //第一步就是设置环境参数，初始化好iMobile
        Environment.setLicensePath(DefaultDataConfig.LicPath);
        Environment.initialization(this);

        //初始化系统相关的类
        SharedPreferencesUtil.init(this);
        AssetsUtils.init(this);

        m_defaultDataManager = new DefaultDataManager();
        m_dataManager = new DataManager();

        //配置数据
        new DefaultDataConfig().autoConfig();


        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        //mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    /**
     * 获取当前application
     *
     * @return
     */
    public static MyApplication getInstance() {
        return sInstance;
    }

    /**
     * 获取默认数据对象
     *
     * @return
     */
    public DefaultDataManager getDefaultDataManager() {
        return m_defaultDataManager;
    }

    /**
     * 获取用户数据对象
     *
     * @return
     */
    public DataManager getUserDataManager() {
        return m_dataManager;
    }

    /**
     * 显示信息
     *
     * @param info 需要显示的信息
     */
    public void ShowInfo(String info) {
        Toast toast = Toast.makeText(sInstance, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * 显示错误信息
     *
     * @param err 需要显示的错误信息
     */
    public void ShowError(String err) {
        Toast toast = Toast.makeText(sInstance, "Error: " + err, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        //		toast.getView().setBackgroundResource(R.drawable.red_round_rect);
        toast.show();
        Log.e(this.getClass().getName(), err);
    }

    public static int dp2px(int dp) {
        return (int) (dp * sInstance.getResources().getDisplayMetrics().density);
    }

    /**
     * @param context      上下文
     * @param intervalTime 返回键时间间隔
     * @return Map<String, Object> isQuit true退出，false取消
     * @datetime 2018-08-06  10:59.
     */
    public static Map<String, Object> quitPrompt(Context context, long intervalTime) {
        Map<String, Object> _map = new HashMap<>();
        if (System.currentTimeMillis() - intervalTime >= 2000) {
            MyToast.showMyToast(context, "再按一次返回键退出", Toast.LENGTH_SHORT);
            intervalTime = System.currentTimeMillis();
            _map.put(KEY_IS_QUIT, false);
            _map.put(KEY_INTERVAL, intervalTime);
            return _map;
        } else {
            intervalTime = System.currentTimeMillis();
            ActivityCollector.finishAllActivity();
            _map.put(KEY_IS_QUIT, true);
            _map.put(KEY_INTERVAL, intervalTime);
            return _map;
        }
    }
}
