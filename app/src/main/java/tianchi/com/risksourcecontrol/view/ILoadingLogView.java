package tianchi.com.risksourcecontrol.view;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by Kevin on 2018/2/6.
 */

public interface ILoadingLogView {

    String getDownLoadURL();

    String getIdLoginName();

    String getPictureName();

    void showLoadingPiture(String msg);

    void hideLoadingPicture();

    void showLoadingSucceed(Bitmap bitmap);

    void showLoadingFailed(String msg);

    void showSubmitSucceedOrFailed(String msg);

    Map<String, Object> getModifyLogMap();
}
