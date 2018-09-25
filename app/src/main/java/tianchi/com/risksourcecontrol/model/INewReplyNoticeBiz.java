package tianchi.com.risksourcecontrol.model;

import java.io.File;

import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyReplyInfo;

/**
 * Created by hairun.tian on 2018/6/14 0014.
 */

public interface INewReplyNoticeBiz {

    //提交
    void submit(RectifyReplyInfo rectifyNotifyInfo, OnSubmitLogListener submitLogListener);

    void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener);
}
