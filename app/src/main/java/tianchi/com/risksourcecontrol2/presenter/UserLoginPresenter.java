package tianchi.com.risksourcecontrol2.presenter;

import android.os.Handler;

import tianchi.com.risksourcecontrol2.model.IUserLoginBiz;
import tianchi.com.risksourcecontrol2.model.LoadingRelationshipListener;
import tianchi.com.risksourcecontrol2.model.OnLoginListener;
import tianchi.com.risksourcecontrol2.model.UserLoginBiz;
import tianchi.com.risksourcecontrol2.view.IUserLoginView;

/**
 * 用户登录控制器
 * Created by Kevin on 2017/12/18.
 */

public class UserLoginPresenter {
    private IUserLoginBiz m_userBiz;
    private IUserLoginView m_userLoginView;
    private Handler m_handler = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.m_userLoginView = userLoginView;
        m_userBiz = new UserLoginBiz();//←←←业务逻辑入口
    }


    public void login(final boolean isSaveInfo) {
        m_userLoginView.showLoading("账号登录中...");
        m_userBiz.login(m_userLoginView.getUserName(), m_userLoginView.getPassWord(), new OnLoginListener() {
            @Override
            public void loginSuccess() {//登录成功
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_userLoginView.hideLoading();
                        m_userLoginView.toHomeActivity();
                        m_userLoginView.saveUserInfo2Local(isSaveInfo);
                    }
                });
            }

            @Override
            public void loginFailed(String msg) {//账号密码不匹配
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_userLoginView.hideLoading();
                        m_userLoginView.showLoginFailed(msg);
                    }
                });
            }

            @Override
            public void accessNetWorkError(String msg) {//访问网络出错
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_userLoginView.hideLoading();
                        m_userLoginView.accessNetWorkError(msg);
                    }
                });
            }

            @Override
            public void onUserNameEmpty() {//用户名为空
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_userLoginView.hideLoading();
                        m_userLoginView.showUserNameEmptyError();
                    }
                });
            }

            @Override
            public void onPasswordEmpty() {//密码为空
                m_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_userLoginView.hideLoading();
                        m_userLoginView.showPasswordEmptyError();
                    }
                });
            }
        });
    }

    //重置
    public void reset() {
        m_userLoginView.clearUserName();
        m_userLoginView.clearPassword();
    }

    //获取人员名单
    public void getRelationshipList(String section) {
        m_userLoginView.showLoading("获取人员关系列表...");
        m_userBiz.getRelationshipList(section, new LoadingRelationshipListener() {
            @Override
            public void loadingSucceed() {
                m_userLoginView.hideLoading();
                m_userLoginView.showLoadingFinish("初始化人员关系列表成功");
            }

            @Override
            public void loadingFailed(String msg) {
                m_userLoginView.hideLoading();
                m_userLoginView.showLoginFailed(msg);
            }
        });
    }
}
