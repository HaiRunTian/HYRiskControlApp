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

import org.json.JSONArray;
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
import tianchi.com.risksourcecontrol2.bean.newnotice.ReplySupervisorInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-13 9:22.
 * 已审核整改通知单列表
 */

public class CheckedListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
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
        _textView.setText("已审核回复单列表");
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);

        m_progressDialog = new ProgressDialog(CheckedListActivity.this);
        m_progressDialog.setMessage("回复单列表正在加载中，请稍等…");
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
                    MyToast.showMyToast(CheckedListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
                    MyToast.showMyToast(CheckedListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "data");//解析数据
                    List<RectifyReplyInfo> _unReadDatas = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyReplyInfo.class);
//                    String _Sinfo = GsonUtils.getNodeJsonString(string,"supervisionInfos");
//                    String _Oinfo = GsonUtils.getNodeJsonString(string,"reciversOwnerInfos");
                    try {
                        JSONArray jObj = new JSONArray(beanListUnReply);
                        List<ReplySupervisorInfo> _sList = new ArrayList<ReplySupervisorInfo>(); //监理审批意见集合
                        List<ReplySupervisorInfo> _oList = new ArrayList<ReplySupervisorInfo>(); //业主审批意见集合

                        for (int i = 0; i < jObj.length(); i++) {
                            JSONObject _object = (JSONObject) jObj.get(0);
                            JSONArray _jsonArraySInfo = _object.getJSONArray("supervisionInfos");
                            JSONArray _jsonArryOInfo = _object.getJSONArray("reciversOwnerInfos");
                            //监理审批意见
                            ReplySupervisorInfo _sinfo = new ReplySupervisorInfo();
                            if (_jsonArraySInfo.length() == 1) {
                                JSONObject _objectSInfo = _jsonArraySInfo.getJSONObject(i);
                                _sinfo.setId(_objectSInfo.getInt("id"));
                                _sinfo.setHasReaded(_objectSInfo.getBoolean("hasReaded"));
                                _sinfo.setHasVerify(_objectSInfo.getInt("hasVerify"));
                                _sinfo.setReceiver(_objectSInfo.getString("receiver"));
                                _sinfo.setResult(_objectSInfo.getString("result"));
                                _sinfo.setRemark(_objectSInfo.getString("remark"));
                                _sinfo.setSubmitTime(_objectSInfo.getString("submitTime"));
                                _sList.add(_sinfo);

                            }

                            //业主审批意见
                            ReplySupervisorInfo _oinfo = new ReplySupervisorInfo();
                            if (_jsonArryOInfo.length() == 1) {
                                JSONObject _objectOinfo = _jsonArryOInfo.getJSONObject(0);
                                _oinfo.setId(_objectOinfo.getInt("id"));
                                _oinfo.setHasReaded(_objectOinfo.getBoolean("hasReaded"));
                                _oinfo.setHasVerify(_objectOinfo.getInt("hasVerify"));
                                _oinfo.setReceiver(_objectOinfo.getString("receiver"));
                                _oinfo.setResult(_objectOinfo.getString("result"));
                                _oinfo.setRemark(_objectOinfo.getString("remark"));
                                _oinfo.setSubmitTime(_objectOinfo.getString("submitTime"));
                                _oList.add(_oinfo);
                            }
                            _unReadDatas.get(i).setSupervisionInfos(_sList);
                            _unReadDatas.get(i).setReciversOwnerInfos(_oList);

                        }
//                        LogUtils.i("jObj size = ", jObj.length() + "");

                    } catch (JSONException e) {
                        e.printStackTrace();
//                        LogUtils.i("json解析异常",e.toString());
                    }
                    m_progressDialog.dismiss();
                    //如果两个为空，提示没有接收到任何消息
                    if (_unReadDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);
                        TextView tv = $(R.id.tv_no_notice);
                        tv.setText("没有已审核的整改回复单");

                    } else { //否则把接收到的信息提取标题，放在list中
                        m_list.clear();
                        if (_unReadDatas.size() != 0) {
                            for (int i = _unReadDatas.size() - 1; i >= 0; i--) {
//                                _unReadDatas.get(i).setRead(1);
                                m_list.add(_unReadDatas.get(i));
                            }
                        }
                        MyToast.showMyToast(CheckedListActivity.this, "共审核了" + m_list.size() + "条整改通知", Toast.LENGTH_SHORT);
                        m_adapter = new RectifyReplyAdapter(CheckedListActivity.this, m_list);
                        m_lvReceive.setAdapter(m_adapter);
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


    //点击item,携带数据跳转到通知详情界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();
        RectifyReplyInfo _rectifyNotifyInfo = m_list.get(position);
        List<ReplySupervisorInfo> _sInfoList = _rectifyNotifyInfo.getSupervisionInfos();
        List<ReplySupervisorInfo> _oInfoList = _rectifyNotifyInfo.getReciversOwnerInfos();

        _bundle.putSerializable("data", _rectifyNotifyInfo);
        if (_sInfoList.size() != 0 && _sInfoList != null) {
            _bundle.putSerializable("sInfo", _sInfoList.get(_sInfoList.size()-position-1));
        } else _bundle.putSerializable("sInfo", null);

        if (_oInfoList.size() != 0 &&_oInfoList != null) {
            _bundle.putSerializable("oInfo", _oInfoList.get(_oInfoList.size()-position-1));
        } else _bundle.putSerializable("oInfo", null);

        Intent _intent = new Intent(CheckedListActivity.this, ReadReplyInfoHasCheckActivity.class);
//        Intent _intent = new Intent(CheckedListActivity.this, ReadRectifyReplyInfoActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }


    //网络请求接收过的通知
    private void getNotice(String receiveMan, ReceiveNoticeListActivity.CallBack callBack) {
        m_progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("realName", receiveMan);
            jsonObject.put("startTime", "");
            jsonObject.put("endTime", "");
            jsonObject.put("replyStatus", 1); //0 未审核 1审核
//            jsonObject.put("replyState",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
        OkHttpUtils.postAsync(ServerConfig.URL_GET_MSG_CHECK_ONCHECK2, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("已审核接收失败", request.body().toString());
                m_progressDialog.setMessage("加载失败……");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                LogUtils.i("已审核接收成功", result);
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
