package tianchi.com.risksourcecontrol2.activitiy.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-13 16:41.
 */

public class UsersListActivity extends BaseActivity {

//    private int m_roleId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       String section =  UserSingleton.getUserInfo().getManagerSection();
        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("id",206);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = _jsonObject.toString();
        LogUtils.i("jsonString ==" ,json);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_REPLY_FOR_ID,json , new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                LogUtils.i("jsonString ==" ,"请求失败");
            }
            @Override
            public void requestSuccess(String jsonString) throws Exception {
                String s = new String("2");
                LogUtils.i("jsonString ==" ,jsonString);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
