package tianchi.com.risksourcecontrol2.model;

/**
 * 提交Log接口
 * Created by Kevin on 2017/12/20.
 */

public interface OnSubmitLogListener {

    public void onSubmitSucceed(String msg);

    public void onSubmitFailed(String msg);

}
