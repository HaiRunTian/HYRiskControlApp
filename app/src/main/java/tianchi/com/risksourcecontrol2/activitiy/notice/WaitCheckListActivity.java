package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.app.ProgressDialog;
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
import tianchi.com.risksourcecontrol2.activitiy.message.ReceiveNoticeListActivity;
import tianchi.com.risksourcecontrol2.adapter.RectifyReplyAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-13 9:22.
 * 待审核整改通知单列表
 * 11
 */

public class WaitCheckListActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<RectifyReplyInfo> m_list;
    public RectifyReplyAdapter m_adapter;
    private ProgressDialog m_progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rectify_noticfy_list_activiaty);
        initView();
    }

    private void initView() {
        TextView _textView = $(R.id.tvTitles);
        _textView.setText("待审核回复单列表");
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        m_progressDialog = new ProgressDialog(WaitCheckListActivity.this);
        m_progressDialog.setMessage("正在加载待回复单列表，请稍等…");
        m_progressDialog.setCancelable(true);
        //依据接收者名字
        getNotice(UserSingleton.getUserInfo().getRealName(), new ReceiveNoticeListActivity.CallBack() {
            @Override
            public void getData(String string) {
//                LogUtils.i("回调回来的数据", string);
                String msg = GsonUtils.getNodeJsonString(string, "msg");
                int status = GsonUtils.getIntNoteJsonString(string, "status");
                m_progressDialog.setMessage(msg);
                if (status == 0) {
                    MyToast.showMyToast(WaitCheckListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
                    MyToast.showMyToast(WaitCheckListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "data");//解析数据
//                    LogUtils.i("未读——————————————————————————————"+beanListUnReply);
                    List<RectifyReplyInfo> _unReadDatas = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyReplyInfo.class);
                    //如果两个为空，提示没有接收到任何消息
                    if (_unReadDatas.size() == 0 ) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);
                        TextView tv = $(R.id.tv_no_notice);
                        tv.setText("没有待审核的整改回复单");
                    } else { //否则把接收到的信息提取标题，放在list中
                        m_list.clear();
                        if (_unReadDatas.size() != 0) {
                            for (int i = _unReadDatas.size() - 1; i >= 0; i--) {
                                _unReadDatas.get(i).setRead(1);
                                m_list.add(_unReadDatas.get(i));
                            }
                        }
                        MyToast.showMyToast(WaitCheckListActivity.this,"共有"+m_list.size()+"条整改通知待审核", Toast.LENGTH_SHORT);
                        m_adapter = new RectifyReplyAdapter(WaitCheckListActivity.this, m_list);
                        m_lvReceive.setAdapter(m_adapter);
                    }
                }
                m_progressDialog.dismiss();
            }
        });
        m_lvReceive.setOnItemClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //点击item,携带数据跳转到通知详情界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();
        RectifyReplyInfo _rectifyNotifyInfo = m_list.get(position);
//        String _s =  readData.getDataTime();
        _bundle.putSerializable("data", _rectifyNotifyInfo);
        Intent _intent = new Intent(WaitCheckListActivity.this, ReadRectifyReplyInfoActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }


    //网络请求接收过的通知
    private void getNotice(String receiveMan, ReceiveNoticeListActivity.CallBack callBack) {
        m_progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("realName", receiveMan);
            jsonObject.put("startTime","");
            jsonObject.put("endTime","");
            jsonObject.put("replyStatus",0); //0 未审核 1审核
//            jsonObject.put("replyState",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
        OkHttpUtils.postAsync(ServerConfig.URL_GET_MSG_CHECK_ONCHECK2, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("待审核接收失败", request.body().toString());
                m_progressDialog.setMessage("加载失败");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                LogUtils.i("待审核接收成功", result);
                callBack.getData(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
//        m_adapter.notifyDataSetChanged();
    }
}
