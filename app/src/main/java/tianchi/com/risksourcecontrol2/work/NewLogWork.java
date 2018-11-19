package tianchi.com.risksourcecontrol2.work;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.model.OnLoadingDataListener;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * 新建日志的一些后台工作
 * Created by Kevin on 2018/1/24.
 */

public class NewLogWork {
    //加载标段
    public static void loadSection(OnLoadingDataListener loadingDataListener) {
        JSONObject _object = new JSONObject();
        try {
            _object.put("userId", UserSingleton.getUserInfo().getUserId());
            OkHttpUtils.postAsync(ServerConfig.URL_GET_SECTION_LIST, _object.toString(), new OkHttpUtils.InsertDataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    if (loadingDataListener != null)
                        loadingDataListener.loadFailed("初始化数据异常");
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    int resultCode = GsonUtils.getIntNoteJsonString(result, "status");
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                    String data = "";//节点jsonstring
                    switch (resultCode) {
                        case 0:
                        case -1://无标段,加载标段失败
                            if (loadingDataListener != null)
                                loadingDataListener.loadFailed(msg);
                            break;
                        case 1://成功加载标段
                            data = GsonUtils.getNodeJsonString(result, "data");
                            List<String> list = GsonUtils.jsonToList(data);
                            if (loadingDataListener != null)
                                loadingDataListener.loadSuccess(list);
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
//            LogUtils.i("LoadSectionError!", e.getMessage());
        }
    }
}
