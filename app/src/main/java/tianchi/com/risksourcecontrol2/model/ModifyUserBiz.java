package tianchi.com.risksourcecontrol2.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.UpDownLoadFileWork;
import tianchi.com.risksourcecontrol2.work.UserLoginWork;

/**
 * 修改用户资料具体业务逻辑
 * Created by Kevin on 2018/1/29.
 */

public class ModifyUserBiz implements IModifyUserBiz {

    @Override
    public void modifyUser(Map<String, Object> objectMap, OnModifyUserListener modifyUserListener) {
        Map<String, Object> mdfUserInfo = objectMap;
//        String jsonString = GsonUtils.objectToJson(mdfUserInfo);
        String jsonString = UserLoginWork.mapToJsonString(mdfUserInfo);
        OkHttpUtils.postAsync(ServerConfig.URL_MODIFY_USERINFO,
                jsonString, new OkHttpUtils.InsertDataCallBack() {
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        if (modifyUserListener != null)
                            modifyUserListener.serverError("同步至服务器出错~");
                    }

                    @Override
                    public void requestSuccess(String result) throws Exception {
                        if (!result.contains("status") || !result.contains("msg")) {
                            if (modifyUserListener != null) {
                                modifyUserListener.modifyFailed("服务器解析错误_jsonString:"+result);
                                return;
                            }
                        }
                        int resultCode = GsonUtils.getIntNoteJsonString(result, "status");
                        String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                        switch (resultCode) {
                            case 0://修改失败
                                if (modifyUserListener != null)
                                    modifyUserListener.modifyFailed(msg);
                                break;
                            case 1://修改成功
                                if (modifyUserListener != null)
                                    modifyUserListener.modifySucceed(msg);
                                break;
                            case -1:
                                if (modifyUserListener != null)
                                    modifyUserListener.modifyFailed(msg);
                                break;
                        }
//                        LogUtils.i("modifyUser e=", result);
                    }
                });
    }

    @Override
    public void modifyUserHead(File imgFile,String idLoginName, OnUploadFileListener uploadFileListener) {
        UpDownLoadFileWork.upLoadFile2Server(imgFile,ServerConfig.URL_UPLOAD_FILE,"user",idLoginName,uploadFileListener);
    }

    @Override
    public void downloadUserHead(String fileURL,String fileName, OnDownloadFileListener downloadFileListener) {
        UpDownLoadFileWork.downloadFileFromServer(fileURL, fileName,downloadFileListener);
    }

}
