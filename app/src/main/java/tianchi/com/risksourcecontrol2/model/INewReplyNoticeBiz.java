package tianchi.com.risksourcecontrol2.model;

import java.io.File;

import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;

/**
 * Created by hairun.tian on 2018/6/14 0014.
 */

public interface INewReplyNoticeBiz {

    //提交
    void submit(RectifyReplyInfo rectifyNotifyInfo, OnSubmitLogListener submitLogListener);

    void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener);
}
