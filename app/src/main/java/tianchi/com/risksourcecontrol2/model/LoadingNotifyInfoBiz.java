package tianchi.com.risksourcecontrol2.model;

import tianchi.com.risksourcecontrol2.work.UpDownLoadFileWork;

/**
 * Created by Kevin on 2018/2/6.
 */

public class LoadingNotifyInfoBiz implements ILoadingNotifyInfoBiz{

    @Override
    public void downloadLogPicture(String url, String fileName, OnDownloadFileListener downloadFileListener) {
        UpDownLoadFileWork.downloadFileFromServer(url,fileName, downloadFileListener);
    }

}
