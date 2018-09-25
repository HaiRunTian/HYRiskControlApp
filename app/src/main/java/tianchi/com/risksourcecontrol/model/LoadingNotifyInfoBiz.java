package tianchi.com.risksourcecontrol.model;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.Request;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.work.UpDownLoadFileWork;

/**
 * Created by Kevin on 2018/2/6.
 */

public class LoadingNotifyInfoBiz implements ILoadingNotifyInfoBiz{

    @Override
    public void downloadLogPicture(String url, String fileName, OnDownloadFileListener downloadFileListener) {
        UpDownLoadFileWork.downloadFileFromServer(url,fileName, downloadFileListener);
    }

}
