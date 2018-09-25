package tianchi.com.risksourcecontrol.model;

/**
 * Created by Kevin on 2018/2/5.
 */

public interface OnUploadFileListener {

    void uploadSucceed(String msg);

    void uploadFailed(String msg);

}
