package tianchi.com.risksourcecontrol2.view;

import java.io.File;
import java.util.Date;

/**
 * 新建日志通用view接口
 * Created by Kevin on 2017/12/20.
 */

public interface INewLogView {

    String getLogID();//获取日志编号

    String getStakeNum();//获取桩号

    String getRiskID();//获取风险源编号

    void setAccount();//设置账号

    String getAccount();//获取当前账号

    String getReformAccount();//获取整改用户

    String getWeather();//获取天气

    void setSection();//初始化标段列表

    String getSection();//获取标段

    String getEmergency();//获取突发事件

    String getLeader();//获取施工单位负责人

    void setRecorder();//设置记录员

    String getRecorder();//获取记录员

    String getRiskType();//获取风险点类型

    void setSaveTime();//设置时间

    void setProjectRole();//设置提交方

    String getProjectRole();//获取提交方

    Date getSaveTime();//获取时间

    String getDetails();//获取详情

    String getTitles();//获取标题

    String getMDetails();//获取生产详情

    String getTDetails();//获取技术详情

//    byte[] getPicture();//获取照片

    String getPicture();//获取照片

    String getIdLoginName();

    File getUploadFile(int position);//获取上传的照片

    void showInSubmiting(String msg);//显示提交过程

    void hideInSubmiting();//隐藏提交过程

    void showSubmitSucceed(String msg);//提交成功

    void showSubmitFailed(String msg);//提交失败

    void uploadFileSucceed(String msg);//提交图片成功

    void uploadFileFailed(String msg);//提交图片失败

    void setLogId();

    /**
     *
     * @return 获取整改时间
     */
    String getExpireTime();

    /**
     *
     * @return 获取风险源编号
     */
    String getRiskIndex();
//    void initLoadingDataSucceed(List<String> list);//显示加载标段成功
//
//    void initLoadingDataFailed(String msg);//显示加载标段失败
}
