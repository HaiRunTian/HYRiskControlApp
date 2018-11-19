package tianchi.com.risksourcecontrol2.model;

import java.io.File;


import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;

/**
 * Created by hairun.tian on 2018/6/14 0014.
 */

public interface INewNoticeBiz {

    //提交
    void submit(RectifyNotifyInfo rectifyNotifyInfo, OnSubmitLogListener submitLogListener);

    void saveToDraft(RectifyNotifyDraftInfo rectifyNotifyDraftInfo,OnSubmitLogListener submitLogListener);

    void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener );
}
