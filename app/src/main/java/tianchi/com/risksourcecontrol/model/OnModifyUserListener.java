package tianchi.com.risksourcecontrol.model;

/**
 * Created by Kevin on 2018/1/29.
 */

public interface OnModifyUserListener {

    void modifySucceed(String msg);

    void modifyFailed(String msg);

    void serverError(String msg);
}
