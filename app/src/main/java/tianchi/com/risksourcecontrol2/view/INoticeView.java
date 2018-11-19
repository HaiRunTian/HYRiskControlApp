package tianchi.com.risksourcecontrol2.view;

import java.io.File;
import java.util.Date;

/**
 * Created by hairun.tian on 2018/3/21 0021.
 */

public interface INoticeView {


    String getNoticeTitle(); //获取标题
    String getTime();  //获取时间
    String getReceiveMan();  //获取收件人
    String getMsgContent(); //获取内容

    void setAccount();//设置账号

    String getAccount();//获取当前账号

    String getReformAccount();//获取整改用户


    void setSaveTime();//设置时间

    Date getSaveTime();//获取时间

    String getPicture();//获取照片

    String getIdLoginName();

    File getUploadFile(int position);//获取上传的照片

    void showInSubmiting(String msg);//显示提交过程

    void hideInSubmiting();//隐藏提交过程

    void showSubmitSucceed(String msg);//提交成功

    void showSubmitFailed(String msg);//提交失败

    void uploadFileSucceed(String msg);//提交图片成功

    void uploadFileFailed(String msg);//提交图片失败


}
