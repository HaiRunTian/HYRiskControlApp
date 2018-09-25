package tianchi.com.risksourcecontrol.view;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by hairun on 2018/2/6.
 */

public interface ILoadingNotifyView {

    /**
     *
     * @return 获取下载URL
     */
    String getDownLoadURL();

    /**
     *
     * @return 获取登录名 id
     */
    String getIdLoginName();

    /**
     *
     * @return 获取照片名字
     */
    String getPictureName();

    /**
     *
     * @param msg 展示下载照片
     */
    void showLoadingPiture(String msg);

    /**
     *  隐藏进度条
     */
    void hideLoadingPicture();

    /**
     *
     * @param bitmap 下载成功
     */
    void showLoadingSucceed(Bitmap bitmap);

    /**
     *
     * @param msg 下载失败
     */
    void showLoadingFailed(String msg);

//    void showSubmitSucceedOrFailed(String msg);
//
//    Map<String, Object> getModifyLogMap();
}
