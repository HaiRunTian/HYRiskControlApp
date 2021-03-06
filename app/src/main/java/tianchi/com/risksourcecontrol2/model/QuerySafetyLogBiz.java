package tianchi.com.risksourcecontrol2.model;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.QueryLogWork;

/**
 * 生产安全日志查询详细业务逻辑
 * Created by Kevin on 2017/12/22.
 */

public class QuerySafetyLogBiz implements IQueryLogBiz {

    @Override
    public void queryLog(Map<String, Object> queryCondition, OnLogQueryListener logQueryListener) {
        //        String jsonString = GsonUtils.objectToJson(queryCondition);
        String jsonString = QueryLogWork.parseObjectMap2Json(queryCondition);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_PRO_SAFETY_LOG, jsonString, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                if (logQueryListener != null) {
                    if (e instanceof SocketTimeoutException) {//超时异常
                        logQueryListener.onQueryFailed("登录超时");
                    } else if (e instanceof ConnectException) {//连接异常
                        logQueryListener.onQueryFailed("连接服务器异常");
                    } else {
                        logQueryListener.onQueryFailed("服务器异常");
                    }
                }
            }

            @Override
            public void requestSuccess(String result) throws Exception {

//                LogUtils.i("result=",result);
                try {
                    if (!result.contains("status") || !result.contains("msg")) {
                        if (logQueryListener != null) {
                            logQueryListener.onQueryFailed("服务器解析出错_error:" + result);
                        }
                    }
                    int resultCode = GsonUtils.getIntNoteJsonString(result, "status");//状态码
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");//附带信息
                    switch (resultCode) {
                        case 0:
                            if (logQueryListener != null) {
                                logQueryListener.onQueryFailed(msg);
                            }
                            break;
                        case 1:

                            String beanListStr = GsonUtils.getNodeJsonString(result, "data");//解析数据
                            List<SafetyLogInfo> safetyList = new ArrayList<>();
                            JSONArray beanJsonArray = new JSONArray(beanListStr);

                            for (int i = 0; i < beanJsonArray.length(); i++) {

                                JSONObject beanObject = beanJsonArray.getJSONObject(i);
                                SafetyLogInfo safetyLogInfo = GsonUtils.parseJson2Bean(beanObject, SafetyLogInfo.class);

//                                String section = safetyLogInfo.getSection();
//                                String _picture = safetyLogInfo.getPicture();
                                safetyList.add(safetyLogInfo);
                            }

                            if (safetyList.size() > 0) {
                                if (logQueryListener != null) {
                                    logQueryListener.onQuerySucceed(safetyList);
                                }
                            }
                            break;
                        default:
                            logQueryListener.onQueryFailed(msg);
                            break;
                    }
                } catch (Exception e) {
                    logQueryListener.onQueryFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void queryAllLog(Map<String, Object> queryCondition, OnLogQueryListener logQueryListener) {
        String jsonString = GsonUtils.objectToJson(queryCondition);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_PRO_SAFETY_LOG, jsonString, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                if (logQueryListener != null) {
                    if (e instanceof SocketTimeoutException) {//超时异常
                        logQueryListener.onQueryFailed("查询超时");
                    } else if (e instanceof ConnectException) {//连接异常
                        logQueryListener.onQueryFailed("连接服务器异常");
                    } else {
                        logQueryListener.onQueryFailed("服务器异常");
                    }
                }
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                try {
                    int resultCode = GsonUtils.getIntNoteJsonString(result, "status");//状态码
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");//附带信息
                    switch (resultCode) {
                        case 0:
                            if (logQueryListener != null) {
                                logQueryListener.onQueryFailed(msg);
                            }
                            break;
                        case 1:
                            String beanListStr = GsonUtils.getNodeJsonString(result, "data");//解析数据
                            List<SafetyLogInfo> safetyList = new ArrayList<>();
                            JSONArray beanJsonArray = new JSONArray(beanListStr);
                            for (int i = 0; i < beanJsonArray.length(); i++) {
                                JSONObject beanObject = beanJsonArray.getJSONObject(i);
                                SafetyLogInfo safetyLogInfo = GsonUtils.parseJson2Bean(beanObject, SafetyLogInfo.class);
                                safetyList.add(safetyLogInfo);
                            }
                            if (safetyList.size() > 0) {
                                if (logQueryListener != null) {
                                    logQueryListener.onQuerySucceed(safetyList);
                                }
                            }
                            break;
                        default:
                            logQueryListener.onQueryFailed(msg);
                            break;
                    }
                } catch (Exception e) {
                    logQueryListener.onQueryFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void pushLog(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener) {

    }

    //    @Override
    //    public void downloadLogPicture(String fileURL, OnDownloadFileListener downloadFileListener) {
    //
    //    }
}

