package tianchi.com.risksourcecontrol.model;

import java.io.File;
import java.util.Map;

/**
 * 修改用户信息接口
 * Created by Kevin on 2018/1/27.
 */

public interface IModifyUserBiz {
    public void modifyUser(Map<String, Object> objectMap, OnModifyUserListener modifyUserListener);

    public void modifyUserHead(File imgFile,String idLoginName,OnUploadFileListener uploadFileListener);

    public void downloadUserHead(String fileURL,String fileName,OnDownloadFileListener downloadFileListener);

}
