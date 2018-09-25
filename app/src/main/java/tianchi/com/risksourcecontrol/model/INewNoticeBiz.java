package tianchi.com.risksourcecontrol.model;

import java.io.File;


import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol.model.OnUploadFileListener;

/**
 * Created by hairun.tian on 2018/6/14 0014.
 */

public interface INewNoticeBiz {

    //提交
    void submit(RectifyNotifyInfo rectifyNotifyInfo, OnSubmitLogListener submitLogListener);

    void saveToDraft(RectifyNotifyDraftInfo rectifyNotifyDraftInfo,OnSubmitLogListener submitLogListener);

    void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener );
}
