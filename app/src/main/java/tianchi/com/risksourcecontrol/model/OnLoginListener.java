package tianchi.com.risksourcecontrol.model;

/**
 * 查询接口
 * Created by Kevin on 2017/12/18.
 */

public interface OnLoginListener {

    void loginSuccess();

    void loginFailed(String msg);

    void accessNetWorkError(String msg);

    void onUserNameEmpty();

    void onPasswordEmpty();

}
