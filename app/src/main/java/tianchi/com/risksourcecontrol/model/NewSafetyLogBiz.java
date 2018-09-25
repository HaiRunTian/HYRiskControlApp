package tianchi.com.risksourcecontrol.model;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Request;
import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.work.UpDownLoadFileWork;

/**
 * 新建生产安全日志详细业务逻辑
 * Created by Kevin on 2017/12/20.
 */

public class NewSafetyLogBiz implements INewLogBiz {

    @Override
    public void submit(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener) {//提交新建日志
        try {
            SafetyLogInfo _logInfo = (SafetyLogInfo) baseLogInfo;
            String jsonString = GsonUtils.objectToJson(_logInfo);//实体转json
            OkHttpUtils.postAsync(ServerConfig.URL_SUBMIT_PRO_SAFETY_LOG,//向服务器提交请求
                    jsonString, new OkHttpUtils.InsertDataCallBack() {
                        @Override
                        public void requestFailure(Request request, IOException e) {
                            if (submitLogListener != null) {
                                if (e instanceof SocketTimeoutException) {//超时异常
                                    submitLogListener.onSubmitFailed("提交超时");
                                } else if (e instanceof ConnectException) {//连接异常
                                    submitLogListener.onSubmitFailed("连接服务器异常");
                                } else {
                                    submitLogListener.onSubmitFailed("服务器异常");
                                }
                            }
                        }

                        @Override
                        public void requestSuccess(String result) throws Exception {
                            String jsonString = result;
                            if (!jsonString.contains("status") || !jsonString.contains("msg")) {
                                if (submitLogListener != null) {
                                    submitLogListener.onSubmitFailed("服务器解析错误_jsonString:" + jsonString);
                                    return;
                                }
                            }
                            int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");
                            String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                            switch (resultCode) {
                                case 0://提交失败
                                case -1://该编号日志已存在勿重复提交
                                    if (submitLogListener != null)
                                        submitLogListener.onSubmitFailed(msg);
                                    break;
                                case 1://提交成功
                                    if (submitLogListener != null)
                                        submitLogListener.onSubmitSucceed(msg);
                                    break;
                            }
                        }
                    });
        } catch (Exception e) {
            LogUtils.i("NewSafetyLogBizError!", e.getMessage());
            if (submitLogListener != null)
                submitLogListener.onSubmitFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
        }
    }

    @Override
    public void uploadFile(File uploadFile, String idLoginName, OnUploadFileListener uploadFileListener) {
        if (uploadFile != null) {
            UpDownLoadFileWork.upLoadFile2Server(uploadFile, ServerConfig.URL_UPLOAD_FILE,
                    "safeproduce", idLoginName, new OnUploadFileListener() {
                        @Override
                        public void uploadSucceed(String msg) {
                            if (uploadFileListener != null) {
                                uploadFileListener.uploadSucceed(msg);
                            }
                        }

                        @Override
                        public void uploadFailed(String msg) {
                            if (uploadFileListener != null) {
                                uploadFileListener.uploadFailed(msg);
                            }
                        }
                    });
        }
    }


    //    @Override
    //    public void loadSection(OnLoadingDataListener loadingDataListener) {//加载标段
    //        NewLogWork.loadSection(loadingDataListener);
    //    }
}
