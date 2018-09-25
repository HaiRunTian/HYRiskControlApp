package tianchi.com.risksourcecontrol.activitiy.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Request;
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.login.UserInfo;
import tianchi.com.risksourcecontrol.bean.login.UsersList;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.fragment.ConstructionListFragment;
import tianchi.com.risksourcecontrol.fragment.OwnerListFragment;
import tianchi.com.risksourcecontrol.fragment.RelationshipListFragment;
import tianchi.com.risksourcecontrol.fragment.SupervisorListFragment;
import tianchi.com.risksourcecontrol.model.OnUserListListener;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.work.QueryUserListWork;

/**
 * Created by HaiRun on 2018-09-13 16:41.
 */

public class UsersListActivity extends BaseActivity {

    private int m_roleId;

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
