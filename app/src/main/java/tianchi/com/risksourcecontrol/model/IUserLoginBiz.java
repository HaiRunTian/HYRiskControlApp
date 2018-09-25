package tianchi.com.risksourcecontrol.model;

/**
 * 抽象登录业务
 * Created by Kevin on 2017/12/18.
 */

public interface IUserLoginBiz {
    public void login(String userName, String passWord, OnLoginListener onLoginListener);
    public void getRelationshipList(String section,LoadingRelationshipListener listener);
}
