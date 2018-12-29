package tianchi.com.risksourcecontrol2.activitiy.notice.myself;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import tianchi.com.risksourcecontrol2.adapter.ReceviceNoticeAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.MyTime;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by hairun.tian on 2018/6/19 0019.
 * <p>
 * 整改通知单列表
 * 查询自己发出去的整改通知单列表
 * 发起人是自己
 */

public class MyselfSendNotifyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener{
    public TextView m_tvClose;
    public ListView m_lvReceive;
    public TextView m_mTvNoNotice;
    public List<RectifyNotifyInfo> m_list;
    private ProgressDialog m_progressDialog;
    private ReceviceNoticeAdapter _adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rectify_noticfy_list_activiaty);
        initView();
    }

    private void initView() {
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        m_progressDialog = new ProgressDialog(MyselfSendNotifyListActivity.this);
        m_progressDialog.setMessage("整改通知单正在加载中，请稍等…");
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
                    MyToast.showMyToast(MyselfSendNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                    m_mTvNoNotice.setVisibility(View.VISIBLE);
                    m_lvReceive.setVisibility(View.GONE);
                } else if (status == -1) {
                    MyToast.showMyToast(MyselfSendNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
//
                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "data");//解析数据
                    List<RectifyNotifyInfo> _unReplyDatas = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyNotifyInfo.class);
//                    如果两个为空，提示没有接收到任何消息
                    if (_unReplyDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);
                        m_progressDialog.dismiss();

                    } else { //否则把接收到的信息提取标题，放在list中
                        if (_unReplyDatas.size() != 0) {
                            for (int i = _unReplyDatas.size() - 1; i >= 0; i--) {
                                int _logState = _unReplyDatas.get(i).getLogState();
                                if (_logState!=5) {
                                    m_list.add(_unReplyDatas.get(i));
                                }
                            }
                        }
                          MyToast.showMyToast(MyselfSendNotifyListActivity.this, "您已发送了" + m_list.size() + "条整改通知", Toast.LENGTH_SHORT);
                        _adapter = new ReceviceNoticeAdapter(MyselfSendNotifyListActivity.this, m_list);
                        m_lvReceive.setAdapter(_adapter);
                    }
                }
                m_progressDialog.dismiss();
            }
        });
        m_lvReceive.setOnItemClickListener(this);
        m_lvReceive.setOnItemLongClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //点击item,携带数据跳转到通知详情界面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle _bundle = new Bundle();
        RectifyNotifyInfo _rectifyNotifyInfo = m_list.get(position);
        String _inspectorSigns = _rectifyNotifyInfo.getInspectorSigns();
        LogUtils.i("_inspectorSigns"+_inspectorSigns);
        String time = _rectifyNotifyInfo.getCheckedTime();
//        LogUtils.i("time", time);
        _bundle.putSerializable("data", _rectifyNotifyInfo);
        Intent _intent = new Intent(MyselfSendNotifyListActivity.this, ReadMyselfNotifyInfoActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }

    //网络请求接收过的通知
    private void getNotice(String receiveMan, ReceiveNoticeListActivity.CallBack callBack) {
        m_progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("realNames", receiveMan);
            jsonObject.put("startTime", "");
            jsonObject.put("endTime", "");
            jsonObject.put("logState", -1);

//            jsonObject.put("replyState",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
//        LogUtils.i("url=", ServerConfig.URL_QUERY_MYSELF_SEND_NOTIFY);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_MYSELF_SEND_NOTIFY, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("接收通知失败", request.body().toString());
                m_progressDialog.setMessage("加载失败");
                m_progressDialog.dismiss();

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtils.i("...接收通知", result);
                if (result == null) {
                    m_progressDialog.setMessage("加载失败");
                    m_progressDialog.dismiss();
                } else
                    callBack.getData(result);

            }
        });
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MyAlertDialog.showAlertDialog(MyselfSendNotifyListActivity.this, "温馨提示", "是否要删除当前整改通知单？", "确定", "取消", false, new DialogInterface.OnClickListener()    {
            @Override
            public void onClick(DialogInterface dialog,int which) {
                int _logState = m_list.get(position).getLogState();
                //                String _inspectorSign = m_list.get(position).getInspectorSign();
                //                String _realName = UserSingleton.getUserInfo().getRealName();

                String _time = m_list.get(position).getSubmitTime();
                String _tme1 = MyTime.getTime();
                LogUtils.i("i=" + _time + "---" + _tme1);
                boolean _b = MyTime.getTimeDifference(_time, _tme1);
                String m_time = "0:0:30";
                if (_logState != 1) {
                    Toast.makeText(MyselfSendNotifyListActivity.this, "通知单已受理不可删除", Toast.LENGTH_SHORT).show();

                }else{
                    if (_b) {
                        //确认删除
                        int _draftId = m_list.get(position).getId();
                        deleteDraft(_draftId);
                        m_list.remove(position);
                        _adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(MyselfSendNotifyListActivity.this, "时间已超过半小时不可删除", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消删除
            }
        });

        return true;
    }

    private void deleteDraft(int draftId) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", draftId);

            OkHttpUtils.postAsync(ServerConfig.URL_DELETE_NOTIFY_FOR_ID, json.toString(), new OkHttpUtils.InsertDataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {

                }
                @Override
                public void requestSuccess(String result) throws Exception {
                    int status = GsonUtils.getIntNoteJsonString(result, "status");
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                    if (status == -1 || status == 0) {
                        MyToast.showMyToast(MyselfSendNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                    } else {
                        MyToast.showMyToast(MyselfSendNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        finish();
    }


}
