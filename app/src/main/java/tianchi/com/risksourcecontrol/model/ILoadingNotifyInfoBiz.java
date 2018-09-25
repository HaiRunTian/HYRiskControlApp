package tianchi.com.risksourcecontrol.model;

import java.util.Map;

/**
 * Created by hairun on 2018/6/26.
 */

public interface ILoadingNotifyInfoBiz {

    public void downloadLogPicture(String url, String fileName, OnDownloadFileListener downloadFileListener);


}
