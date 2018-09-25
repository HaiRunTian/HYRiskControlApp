package tianchi.com.risksourcecontrol.activitiy.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.adapter.SendNoticeAdapter;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.notice.ReadData;
import tianchi.com.risksourcecontrol.bean.notice.SendNotice;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;

/**
 * Created by hairun.tian on 2018/3/7 0007.
 */

public class SendNoticeListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public interface CallBack {
        void getData(String string);
    }

    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;

    private List<SendNotice> m_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);
        initView();

    }

    private void initView() {
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_lvReceive = (ListView) findViewById(R.id.lvSendNews);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);


        getNotice(UserSingleton.getUserInfo().getRealName(), new CallBack() {
            @Override
            public void getData(String string) {
                LogUtils.i("回调回来的数据", string);
                String msg = GsonUtils.getNodeJsonString(string, "msg");
                int status = GsonUtils.getIntNoteJsonString(string, "status");

                if (status == 0) {
                    MyToast.showMyToast(SendNoticeListActivity.this, msg, Toast.LENGTH_SHORT);

                } else if (status == -1) {
                    MyToast.showMyToast(SendNoticeListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    List<SendNotice> _readDatas = GsonUtils.jsonToArrayBeans(string, "data", SendNotice.class);


                    //如果两个为空，提示没有接收到任何消息
                    if (_readDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);

                    } else { //否则把接收到的信息提取标题，放在list中
                        m_list = new ArrayList();
                        if (_readDatas.size() != 0) {
                            for (int i = 0; i < _readDatas.size(); i++) {
                                m_list.add(_readDatas.get(i));
                            }
                        }

                        m_lvReceive.setAdapter(new SendNoticeAdapter(SendNoticeListActivity.this,m_list));

                    }

                }

            }

        });

        m_lvReceive.setOnItemClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();

        SendNotice _sendNotice = m_list.get(position);
        Intent _intent = new Intent(SendNoticeListActivity.this, SendNoticeActivity.class);
        _bundle.putSerializable("data1", _sendNotice);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }

    private void getNotice(String sendMan, CallBack callBack) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sendMan", sendMan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
        OkHttpUtils.postAsync(ServerConfig.URL_HAVE_SEND_NOTICE, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                LogUtils.i("接收通知失败", request.body().toString());
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtils.i("接收通知", result);
                callBack.getData(result);
            }
        });
    }
}
