package tianchi.com.risksourcecontrol2.view;//

/**
 * 用户登录view接口
 * Created by Kevin on 2017/12/18.
 */

public interface IUserLoginView {

    String getUserName();//获取登录账号

    String getPassWord();//获取密码

    boolean isRemembered();//是否记住密码

    void clearUserName();//清除用户名

    void clearPassword();//清除密码

    void showLoading(String msg);//显示登录进度

    void hideLoading();//隐藏登录进度

    boolean getLocalUserInfo();//获取本地保存的用户信息

    void saveUserInfo2Local(boolean isSaveInfo);//保存用户信息到本地

    void toHomeActivity();//跳转到主页面

    void showLoginFailed(String msg);//显示登录失败

    void showLoadingFinish(String msg);//显示加载完成

    void accessNetWorkError(String msg);//访问网络错误

    void showUserNameEmptyError();//提示账户不为空

    void showPasswordEmptyError();//提示密码不为空

}
