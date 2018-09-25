package tianchi.com.risksourcecontrol.view;

import java.io.File;
import java.util.Map;

/**
 * Created by Kevin on 2018-05-08.
 */

public interface IPatrolSignInView {

    String getPicture();//获取照片

    String getIdLoginName();

    File getUploadFile(int position);//获取上传的照片

    Map<String, Object> getMap();//获取巡查签到map

    void showInSubmiting(String msg);//显示提交过程

    void hideInSubmiting();//隐藏提交过程

    void showSubmitSucceed(String msg);//提交成功

    void showSubmitFailed(String msg);//提交失败

    void uploadFileSucceed(String msg);//提交图片成功

    void uploadFileFailed(String msg);//提交图片失败

}
