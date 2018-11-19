package tianchi.com.risksourcecontrol2.model;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.UpDownLoadFileWork;

/**
 * Created by Kevin on 2018/2/6.
 */

public class LoadingLogInfoBiz implements ILoadingLogInfoBiz{

    @Override
    public void downloadLogPicture(String url, String fileName, OnDownloadFileListener downloadFileListener) {
        UpDownLoadFileWork.downloadFileFromServer(url,fileName, downloadFileListener);
    }

    @Override
    public void submitModifyLog(String url,Map<String, Object> objectMap,OnSubmitLogListener submitLogListener) {
        try {
            String jsonString = GsonUtils.objectToJson(objectMap);
            OkHttpUtils.postAsync(url, jsonString, new OkHttpUtils.InsertDataCallBack() {
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
                        case -1://提交修改失败
                        case -2://异常
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
            if (submitLogListener != null)
                submitLogListener.onSubmitFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
        }
    }
}
