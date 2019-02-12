package tianchi.com.risksourcecontrol2.util;

import android.util.Log;

import com.supermap.data.LicenseManager;
import com.supermap.data.LicenseStatus;

import java.util.Date;

import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.custom.MyToast;

/**
 * Created by HaiRun on 2018/12/4.
 * //许可验证类
 */

public class LicenseUtils {

    private static LicenseUtils s_licenseUtils = null;

    public static LicenseUtils ins() {
        if (s_licenseUtils == null) {
            s_licenseUtils = new LicenseUtils();
        }
        return s_licenseUtils;
    }

    /**
     * 判断许可时间
     *
     * @author HaiRun
     * created at 2018/12/4 15:44
     */
    public boolean judgeLicese() {
        LicenseManager _manager = LicenseManager.getInstance();
        LicenseStatus _status = _manager.getLicenseStatus();
        if (!_status.isLicenseExsit()) {
            MyToast.showMyToast(AppInitialization.getInstance(), "许可不存在", 1);
            return false;

        } else {
            if (!_status.isLicenseValid()) {
                MyToast.showMyToast(AppInitialization.getInstance(), "许可已经过期，请更新许可", 1);
                return false;

            } else { //许可存在，判断时间
                Date _endDate = _status.getExpireDate(); //许可证最后一天时间
                long _endTime = _endDate.getTime();
                long _toadyTime = System.currentTimeMillis(); //当天时间
                int _day = (int) ((_endTime - _toadyTime) / 1000 / 60 / 60 / 24);
                if (_day < 3) { //如果软件试用时间低于三天，则跳出提示
                    MyToast.showMyToast(AppInitialization.getInstance(), "软件试用时间还有" + _day + "天,请跟技术员联系！", 3);
                }
//                LogUtills.i("当天时间  =  "+ day);
                return true;
            }
        }
    }
}
