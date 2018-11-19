package tianchi.com.risksourcecontrol2.model;

import java.io.File;

import tianchi.com.risksourcecontrol2.bean.notice.SendNotice;

/**
 * Created by hairun.tian on 2018/3/21 0021.
 */

public interface ISendNotice {
    void submit(SendNotice baseLogInfo, OnSubmitLogListener submitLogListener);
    void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener );
}
