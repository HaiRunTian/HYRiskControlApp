package tianchi.com.risksourcecontrol2.model;

/**
 * Created by Kevin on 2018/2/5.
 */

public interface OnUploadFileListener {

    void uploadSucceed(String msg);

    void uploadFailed(String msg);

}
