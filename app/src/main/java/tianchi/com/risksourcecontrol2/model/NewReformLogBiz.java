package tianchi.com.risksourcecontrol2.model;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.UpDownLoadFileWork;

/**
 * 新建整改日志详细业务逻辑
 * Created by Kevin on 2017/12/20.
 */

public class NewReformLogBiz implements INewLogBiz{

    @Override
    public void submit(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener) {
        try {
            ReformLogInfo _logInfo = (ReformLogInfo) baseLogInfo;
            String jsonString = GsonUtils.objectToJson(_logInfo);
            OkHttpUtils.postAsync(ServerConfig.URL_SUBMIT_RISK_REFORM_LOG,//向服务器提交请求
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
                            int resultCode = GsonUtils.getIntNoteJsonString(result, "status");
                            String msg = GsonUtils.getStringNodeJsonString(result, "msg");
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
//            LogUtils.i("NewReformLogBizError!",e.getMessage());
        }
    }

    @Override
    public void uploadFile(File uploadFile,String idLoginName, OnUploadFileListener uploadFileListener) {
        if (uploadFile != null) {
            UpDownLoadFileWork.upLoadFile2Server(uploadFile, ServerConfig.URL_UPLOAD_FILE,
                    "riskreform",idLoginName, new OnUploadFileListener() {
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
//    public void loadSection(OnLoadingDataListener loadingDataListener) {
//        NewLogWork.loadSection(loadingDataListener);
//    }
}
