package tianchi.com.risksourcecontrol.model;

import java.io.File;

import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;

/**
 * Created by Kevin on 2018/1/15.
 */

public interface INewLogBiz {
    //提交
    void submit(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener);

    void uploadFile(File uploadFile,String idLoginName,OnUploadFileListener uploadFileListener );
//    //初始化标段列表
//    void loadSection(OnLoadingDataListener loadingDataListener);
}
