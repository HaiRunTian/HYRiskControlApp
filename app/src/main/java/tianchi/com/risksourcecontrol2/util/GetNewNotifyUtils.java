package tianchi.com.risksourcecontrol2.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.activitiy.message.ReceiveNoticeListActivity;

/**
 * Created by hairun.tian on 2018-06-30.
 */

public class GetNewNotifyUtils {


    //网络请求接收过的通知
    public static void getNoticeForNotify(String url, String receiveMan, ReceiveNoticeListActivity.CallBack callBack) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("realName", receiveMan);
            jsonObject.put("readed", 0);
//            jsonObject.put("replyState",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();

        OkHttpUtils.postAsync(url, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("接收通知失败", request.body().toString());

            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                LogUtils.i("接收通知", result);
                callBack.getData(result);

            }
        });
    }
}
