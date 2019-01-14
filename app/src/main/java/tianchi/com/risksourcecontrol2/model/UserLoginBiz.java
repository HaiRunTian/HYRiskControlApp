package tianchi.com.risksourcecontrol2.model;

import android.text.TextUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.bean.UserBean;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.UserLoginWork;

/**
 * 具体登录业务逻辑
 * Created by Kevin on 2017/12/18.
 */

public class UserLoginBiz implements IUserLoginBiz {
    private String pwd;//临时变量，存密码

    @Override
    public void login(final String userName, final String passWord, final OnLoginListener onLoginListener) {
        //登录
        try {
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                if (TextUtils.isEmpty(userName)) {
                    onLoginListener.onUserNameEmpty();
                }
                if (TextUtils.isEmpty(passWord)) {
                    onLoginListener.onPasswordEmpty();
                }
            } else {
                //封装键入的账号密码
                Map<String, String> userInfo = new HashMap<String, String>();
                userInfo.put("loginName", userName);
                userInfo.put("password", passWord);
                pwd = passWord;
                String jsonString = GsonUtils.objectToJson(userInfo);//转json
                OkHttpUtils.postAsync(ServerConfig.URL_CHECK_LOGIN,//向服务器提交请求
                        jsonString, new OkHttpUtils.QueryDataCallBack() {
                            @Override
                            public void requestFailure(Request request, IOException e) {
                                if (onLoginListener != null) {
                                    if (e instanceof SocketTimeoutException) {//超时异常
                                        onLoginListener.accessNetWorkError("登录超时");
                                    } else if (e instanceof ConnectException) {//连接异常
                                        onLoginListener.accessNetWorkError("连接服务器异常");
                                    } else {
                                        onLoginListener.accessNetWorkError("服务器异常");
                                    }
                                }
                            }
                            @Override
                            public void requestSuccess(String jsonString) throws Exception {
                                if (!jsonString.contains("status") || !jsonString.contains("msg")) {
                                    if (onLoginListener != null) {
                                        onLoginListener.loginFailed("服务器解析出错_error:" + jsonString);
                                        return;
                                    }
                                }
                                int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");//状态码
                                String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");//附带信息
                                switch (resultCode) {
                                    case -1:
                                        if (onLoginListener != null) {
                                            onLoginListener.loginFailed(msg);
                                        }
                                        break;
                                    case 0:
                                        if (onLoginListener != null) {
                                            onLoginListener.loginFailed(msg);
                                        }
                                        break;
                                    case 1:
                                        String beanString = GsonUtils.getNodeJsonString(jsonString, "data");//解析数据
                                        UserInfo _userInfo = UserLoginWork.jsonToUserInfoBean(beanString);
                                        UserSingleton.setUserInfo(_userInfo);
                                        UserSingleton.getUserInfo().setPassword(pwd);//存明文密码
                                        if (onLoginListener != null) {
                                            onLoginListener.loginSuccess();
                                        }
                                        break;
                                    default:
                                        onLoginListener.loginFailed("未知错误");
                                        break;
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
            onLoginListener.loginFailed(e.getClass().getName() + " error detail:" + e.getMessage());
//            LogUtils.i("UpdateUserError", "登录用户到服务器失败" + e.getMessage());
        }
    }

    @Override
    public void getRelationshipList(String section, LoadingRelationshipListener listener) {
        Map<String, String> sectionInfo = new HashMap<>();
        sectionInfo.put("section", section);
        String jsonString = GsonUtils.objectToJson(sectionInfo);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_USER_LIST2, jsonString, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                if (listener != null) {
                    if (e instanceof SocketTimeoutException) {//超时异常
                        listener.loadingFailed("获取人员关系列表超时");
                    } else if (e instanceof ConnectException) {//连接异常
                        listener.loadingFailed("连接时异常");
                    } else {
                        listener.loadingFailed("服务器请求未知错误，请联系相关技术人员");
                    }
                }
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                try {
                    if (!jsonString.contains("status") || !jsonString.contains("msg")) {
                        if (listener != null) {
                            listener.loadingFailed("服务器解析出错_error:" + jsonString);
                        }
                    }
                    int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");//状态码
                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");//附带信息
                    switch (resultCode) {
                        case -1:
                            if (listener != null) {
                                listener.loadingFailed("服务器异常_error:" + jsonString);
                            }
                            break;
                        case 0:
                            //                        if (listener != null) {
                            //                            listener.loadingFailed("初始化人员关系列表失败");
                            //                        }
                            break;
                        case 1: ////人员列表

                            String beanString = GsonUtils.getNodeJsonString(jsonString, "Data");//解析数据
                            LogUtils.i("人员列表",beanString);
//                            UserBean _userBeen = GsonUtils.jsonToBean(beanString,UserBean.class);
//
//                            if (_userBeen!=null){
//                                UserLoginWork.distributeUserList(_userBeen);
//                            }
                            Map<String, List<String>> relationshipMap = GsonUtils.gsonToMaps(beanString);

                            if (relationshipMap != null) {

                                UserLoginWork.distributeRelationshipList(relationshipMap);//初始化用户列表
                            }
                            if (listener != null) {
                                listener.loadingSucceed();
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    listener.loadingFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                }
            }
        });
    }

}
