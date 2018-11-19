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
import tianchi.com.risksourcecontrol2.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.QueryLogWork;

/**
 * Created by Kevin on 2017/12/22.
 */

public class QueryReformLogBiz implements IQueryLogBiz {

    @Override
    public void queryLog(Map<String, Object> queryCondition, OnLogQueryListener logQueryListener) {
//        String jsonString = GsonUtils.objectToJson(queryCondition);
        String jsonString = QueryLogWork.parseObjectMap2Json(queryCondition);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_RISK_REFORM_LOG, jsonString, new OkHttpUtils.InsertDataCallBack() {
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
                if (!result.contains("status") || !result.contains("msg")) {
                    if (logQueryListener != null) {
                        logQueryListener.onQueryFailed("服务器解析出错_error:"+result);
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
                        List<ReformLogInfo> reformLogList = new ArrayList<>();
                        JSONArray beanJsonArray = new JSONArray(beanListStr);
                        for (int i = 0; i < beanJsonArray.length();i++) {
                            JSONObject beanObject = beanJsonArray.getJSONObject(i);
                            ReformLogInfo reformLogInfo = GsonUtils.parseJson2Bean(beanObject, ReformLogInfo.class);
                            reformLogList.add(reformLogInfo);
                        }
                        if (reformLogList.size() > 0) {
                            if (logQueryListener != null) {
                                logQueryListener.onQuerySucceed(reformLogList);
                            }
                        }
                        break;
                    default:
                        logQueryListener.onQueryFailed(msg);
                        break;
                }
            }
        });
    }

    @Override
    public void queryAllLog(Map<String, Object> queryCondition, OnLogQueryListener logQueryListener) {
        String jsonString = GsonUtils.objectToJson(queryCondition);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_RISK_REFORM_LOG, jsonString, new OkHttpUtils.InsertDataCallBack() {
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
                        List<ReformLogInfo> reformLogList = new ArrayList<>();
                        JSONArray beanJsonArray = new JSONArray(beanListStr);
                        for (int i = 0; i < beanJsonArray.length();i++) {
                            JSONObject beanObject = beanJsonArray.getJSONObject(i);
                            ReformLogInfo reformLogInfo = GsonUtils.parseJson2Bean(beanObject, ReformLogInfo.class);
                            reformLogList.add(reformLogInfo);
                        }
                        if (reformLogList.size() > 0) {
                            if (logQueryListener != null) {
                                logQueryListener.onQuerySucceed(reformLogList);
                            }
                        }
                        break;
                    default:
                        logQueryListener.onQueryFailed(msg);
                        break;
                }
            }
        });
    }

    @Override
    public void pushLog(BaseLogInfo baseLogInfo, OnSubmitLogListener submitLogListener) {

    }
}
