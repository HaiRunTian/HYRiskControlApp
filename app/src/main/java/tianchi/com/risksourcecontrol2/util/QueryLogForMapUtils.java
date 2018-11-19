package tianchi.com.risksourcecontrol2.util;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Request;
import tianchi.com.risksourcecontrol2.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.model.OnQueryRiskListener;


/**
 * Created by hairun.tian on 2018-06-29.
 */

public class QueryLogForMapUtils {

    public PatrolLogInfo m_patrolLogInfo;
    public SafetyLogInfo m_safetyLogInfo;
    /**
     *
     * @return 查询安全巡查日志
     */
    public static void getPatrlLog(String str, OnQueryRiskListener listener){

        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("riskName",str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_NEW_PATROL_LOG, _jsonObject.toString(), new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                listener.onQueryFailed();
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                listener.onQuerySucceed(jsonString);
            }
        });
    }
    /**
     *
     * @return 查询生产安全日志
     */
    public static void getSafetyLog(String str,OnQueryRiskListener listener){
        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("riskName",str);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_NEW_PRO_LOG, _jsonObject.toString(), new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                listener.onQueryFailed();
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                    listener.onQuerySucceed(jsonString);
            }
        });


    }
}
