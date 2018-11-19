package tianchi.com.risksourcecontrol2.activitiy.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.notice.ReadData;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by hairun.tian on 2018/3/7 0007.
 */

public class ReceiveNoticeListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<ReadData> m_list;


    public interface CallBack {
        void getData(String string);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_news);
        initView();
    }

    //初始化view
    private void initView() {
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        //依据接收者名字
        getNotice(UserSingleton.getUserInfo().getRealName(), new CallBack() {
            @Override
            public void getData(String string) {
                LogUtils.i("回调回来的数据", string);
                String msg = GsonUtils.getNodeJsonString(string, "msg");
                int status = GsonUtils.getIntNoteJsonString(string, "status");

                if (status == 0){
                    MyToast.showMyToast(ReceiveNoticeListActivity.this,msg, Toast.LENGTH_SHORT);
                }else if (status == -1){
                    MyToast.showMyToast(ReceiveNoticeListActivity.this,msg, Toast.LENGTH_SHORT);
                }else if (status == 1){
                    List<ReadData> _readDatas = GsonUtils.jsonToArrayBeans(string,"readData", ReadData.class);
                    List<ReadData> _unreadDatas = GsonUtils.jsonToArrayBeans(string,"unreadData", ReadData.class);


                    //如果两个为空，提示没有接收到任何消息
                    if (_readDatas.size() == 0 && _unreadDatas.size() ==0){
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);

                    }else { //否则把接收到的信息提取标题，放在list中
                        m_list = new ArrayList();
                        if (_unreadDatas.size() != 0){
                            for (int i = 0; i <_unreadDatas.size(); i++) {
                                m_list.add(_unreadDatas.get(i));
                            }

                        }
                        if (_readDatas.size() != 0){
                            for (int i = 0; i <_readDatas.size(); i++) {
                                m_list.add(_readDatas.get(i));
                            }
                        }

//                        //放通知标题的list
//                        List<ReadData> _list = new ArrayList<ReadData>();
//                        for (int i = 0; i < m_list.size(); i++) {
//                            _list.add(m_list.get(i).getTitle());
//                        }

//                        m_lvReceive.setAdapter(new ReceviceNoticeAdapter(ReceiveNoticeListActivity.this,m_list));

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


    //点击item,携带数据跳转到通知详情界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle  =  new Bundle();
        ReadData readData =m_list.get(position);
       String _s =  readData.getDataTime();
        _bundle.putSerializable("data",readData);

        Intent _intent = new Intent(ReceiveNoticeListActivity.this, ReceiveNoticeActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }


    //网络请求接收过的通知
    private void getNotice(String receiveMan, CallBack callBack) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("receiveMan", receiveMan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
        OkHttpUtils.postAsync(ServerConfig.URL_RECEIVE_NOTICE, str, new OkHttpUtils.InsertDataCallBack() {
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
