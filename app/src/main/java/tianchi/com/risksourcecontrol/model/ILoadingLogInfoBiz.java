package tianchi.com.risksourcecontrol.model;

import java.util.Map;

/**
 * Created by Kevin on 2018/2/6.
 */

public interface ILoadingLogInfoBiz {

    public void downloadLogPicture(String url,String fileName,OnDownloadFileListener downloadFileListener);

    public void submitModifyLog(String url,Map<String,Object> objectMap,OnSubmitLogListener submitLogListener);
}
