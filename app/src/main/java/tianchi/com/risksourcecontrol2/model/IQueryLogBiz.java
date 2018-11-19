package tianchi.com.risksourcecontrol2.model;

import java.util.Map;

import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;

/**
 * Created by Kevin on 2018/1/15.
 */

public interface IQueryLogBiz {

    //根据条件查询日志
    void queryLog(Map<String,Object> queryCondition, OnLogQueryListener logQueryListener);
    //浏览日志
    void queryAllLog(Map<String,Object> queryCondition,OnLogQueryListener logQueryListener);
    //推送日志
    void pushLog(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener);

//    void downloadLogPicture(String fileURL,OnDownloadFileListener downloadFileListener);

}
