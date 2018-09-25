package tianchi.com.risksourcecontrol.view;

import java.util.Date;
import java.util.List;

import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;

/**
 * 查询日志通用view接口
 * Created by Kevin on 2017/12/22.
 */

public interface IQueryLogView {

    void setLogId();

    String getSection();//获取标段

    void setSection();//设置标段

    String getRiskType();//获取风险源类型

    String getStakeNum();//获取桩号

    Date getStartDate();//获取起始日期

    Date getEndDate();//获取终止日期

    String getRecorder();//获取记录者

    String getProjectRole();//获取记录者

    String getRiskId();//获取风险源编号

    String getLogId();//获取日志id

//    String getIdLoginName();//获取id+登录名拼接字符串

//    String getDownloadUrl();

    void setRecorder();//设置记录员

    void showQuerying(String msg);//显示查询进度

    void hideQuerying();//隐藏查询进度

    void showQuerySucceed(List<? extends BaseLogInfo> logInfoList);

    void showQueryFailed(String msg);

}
