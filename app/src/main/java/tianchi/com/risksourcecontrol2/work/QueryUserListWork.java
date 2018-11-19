package tianchi.com.risksourcecontrol2.work;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by Kevin on 2018/5/3.
 */

public class QueryUserListWork {
    //查询用户列表
    public static void queryUserInfo(String realName, OnUserListListener listener) {
        String jsonString = "";
        try {
            JSONObject _jsonObject = new JSONObject();
            _jsonObject.put("realName", realName);
            jsonString = _jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onQueryFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
        }
        OkHttpUtils.postAsync(ServerConfig.URL_GET_USERINFO, jsonString, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                if (listener != null) {
                    if (e instanceof SocketTimeoutException) {//超时异常
                        listener.onQueryFailed("登录超时");
                    } else if (e instanceof ConnectException) {//连接异常
                        listener.onQueryFailed("连接服务器异常");
                    } else {
                        listener.onQueryFailed("服务器异常");
                    }
                }
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                try {
                    int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");//状态码
                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");//附带信息
                    switch (resultCode) {
                        case 0:
                            if (listener != null) {
                                listener.onQueryFailed(msg);
                            }
                            break;
                        case 1:
                            String beanStr = GsonUtils.getNodeJsonString(jsonString, "Data");//解析数据
                            UserInfo _userInfo = UserLoginWork.jsonToUserInfoBean(beanStr);
                            if (_userInfo != null) {
                                if (listener != null) {
                                    listener.onQuerySucceed(_userInfo);
                                }
                            }
                            break;
                        default:
                            listener.onQueryFailed(msg);
                            break;
                    }
                } catch (Exception e) {
                    listener.onQueryFailed(e.getClass().getSimpleName() + " error detail: " + e.getMessage());
                }
            }
        });
    }
}
