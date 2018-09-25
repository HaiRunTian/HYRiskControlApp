package tianchi.com.risksourcecontrol.view;

import android.graphics.Bitmap;

import java.io.File;
import java.util.Map;

/**
 * Created by Kevin on 2018/1/29.
 */

public interface IModifyUserView {

    String getLoginName();

    String getRealName();

    String getProjectRole();

    String getPhoneNum();

    String getEmail();

    String getQQNum();

    String getBirthday();

    String getRegDate();

    String getAddress();

    String getPicture();

    String getOldPassword();

    String getNewPassword();

    String getReNewPassword();

    String getSectionList();

    String getDownloadFileUrl();

    String getIdLoginName();

    void initValues();

    void saveTempInfo();

    void updateUserInfoBean();

    boolean isModified();

    boolean isNotEmpty();

    boolean checkValidity();

    Map<String, Object> getMdfUserInfo();

    File getMdfUserHeadImgFile();

    Bitmap getDownloadImgFile();

    void alertMessage(String msg);

    void showModifiedSucceed(String msg);

    void showModifiedFailed(String msg);

    void showModifiedUserHeadSucceed(String msg);

    void showModifiedUserHeadFailed(String msg);

    void showDownloadUserHeadSucceed(Bitmap bitmap);

    void showProgress(String msg);

    void hideProgress();

    void showServerError(String msg);
}
