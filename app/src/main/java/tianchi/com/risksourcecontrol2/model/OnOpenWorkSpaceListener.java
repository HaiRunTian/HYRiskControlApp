package tianchi.com.risksourcecontrol2.model;

/**
 * Created by Kevin on 2017/12/19.
 */

public interface OnOpenWorkSpaceListener {

    public void onOpenSucceed();

    public void onOpenFailed(String errorMsg);
}
