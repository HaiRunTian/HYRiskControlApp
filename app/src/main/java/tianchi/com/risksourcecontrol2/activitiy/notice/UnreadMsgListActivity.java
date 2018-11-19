package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.ETC1;
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
import tianchi.com.risksourcecontrol2.adapter.UnReadListAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.NotifyMessagesInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.ReplySupervisorInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-15 15:43.
 * 未读消息列表
 */

public class UnreadMsgListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public ListView m_lvMsg;
    public List<NotifyMessagesInfo> m_list;
    public UnReadListAdapter m_adapter;
    private TextView m_notify;
    private ProgressDialog m_progressDialog;
    private TextView m_allRead;
    private List<NotifyMessagesInfo> m_msglist;
    private ReplySupervisorInfo _sInfo;
    private ReplySupervisorInfo _oInfo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAllRead:
                List<String> list = new ArrayList<>();  //回复单ID
                List<String> _listNotif = new ArrayList<>(); //整改单ID
                for (int i = 0; i < m_msglist.size(); i++) {
                    if (m_msglist.get(i).getId() != 0) {
                        list.add(m_msglist.get(i).getId() + "");
                    }
                }
                //整改回复单
                StringBuffer _buffer = new StringBuffer();
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        _buffer.append(list.get(i) + "#");
                    }
                    String str = _buffer.toString();
                    setReplyNotifyRead(str.substring(0, str.length() - 1));
                } else {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "没有消息", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }


    public interface CallBack {
        void getJsonFromNet(String string);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_unread_list);
        initView();
        initVale();

    }

    private void initVale() {
        m_list = new ArrayList<>();
        queryMsgfromNet(UserSingleton.getUserInfo().getRealName(), new CallBack() {
            @Override
            public void getJsonFromNet(String string) {
//                LogUtils.i("回调回来的数据", string);
                m_msglist = GsonUtils.jsonToArrayBeans(string, "data", NotifyMessagesInfo.class);
                m_progressDialog.setMessage("获取成功");

                if (m_msglist.size() > 0) {
                    for (int i = m_msglist.size()-1; i >= 0; i--) {
                        m_list.add(m_msglist.get(i));
                    }
//                    m_list.addAll(m_msglist);
//                    String s = m_list.get(0).getReceiverName();
//                    LogUtils.i("未读消息name", s + "----1111");
//                    LogUtils.i("未读消息list.size", m_list.size() + "----1111");
                    m_adapter = new UnReadListAdapter(UnreadMsgListActivity.this, m_list);
                    m_lvMsg.setAdapter(m_adapter);
                } else {
                    m_notify.setVisibility(View.VISIBLE);
                }
                m_progressDialog.dismiss();
            }
        });

//        LogUtils.i("未读消息list.size", m_list.size() + "11");
        m_lvMsg.setOnItemClickListener(this);
        m_allRead.setOnClickListener(this);
    }


    private void queryMsgfromNet(String realName, CallBack callback) {
        m_progressDialog.show();
        JSONObject _js = new JSONObject();
        try {
            _js.put("realName", realName);
            _js.put("readed", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_MSG, _js.toString(), new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast(UnreadMsgListActivity.this, "查询失败", Toast.LENGTH_SHORT);
                m_progressDialog.setMessage("加载失败");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
//                LogUtils.i("未读消息json", jsonString);
                int status = GsonUtils.getIntNoteJsonString(jsonString, "status");
                String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                if (status == -1) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "服务器异常", Toast.LENGTH_SHORT);
                } else if (status == 0) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "查询失败", Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    callback.getJsonFromNet(jsonString);

                }
            }
        });
    }

    private void initView() {
        m_lvMsg = $(R.id.lvUnReadMsg);
        m_notify = $(R.id.tv_no_notice);
        m_progressDialog = new ProgressDialog(UnreadMsgListActivity.this);
        m_progressDialog.setMessage("消息获取中，请稍等");
        m_progressDialog.setCancelable(true);
        m_allRead = (TextView) findViewById(R.id.btnAllRead);
        findViewById(R.id.tvQueryReformBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * @param parent
     * @param view
     * @param position
     * @param id       listView item 点击事件 弹出对话框
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NotifyMessagesInfo _info = (NotifyMessagesInfo) m_adapter.getItem(position);
        int _notifyId = _info.getNotifyID();
        int _replyId = _info.getReplyNotifyID();
        int pid = _info.getId();
        String _remark = _info.getRemark();

        MyAlertDialog.showAlertDialog(UnreadMsgListActivity.this, "消息提示", _info.getRemark(), R.mipmap.ic_msg, "查看整改单", "取消", "查看回复单", false, new DialogInterface.OnClickListener() {
            /**
             * 确认点击查看回复单信息
             * 依据id判断消息是   整改通知单 或者是  回复通知单  已审核通知单  未审核通知单
             *
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (_notifyId == 0) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "没有整改单", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //跳转到整改通知单
                    if (_remark.contains("需要处理") || _remark.contains("需要查看")) {
                        JSONObject _jsonObject = new JSONObject();
                        try {
                            _jsonObject.put("id", _notifyId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_NOTIFY_FOR_ID, _jsonObject.toString(), new OkHttpUtils.QueryDataCallBack() {
                            @Override
                            public void requestFailure(Request request, IOException e) {
                                MyToast.showMyToast(UnreadMsgListActivity.this, "查看失败", Toast.LENGTH_SHORT);

                            }
                            @Override
                            public void requestSuccess(String jsonString) throws Exception {
                                try {
                                    int status = GsonUtils.getIntNoteJsonString(jsonString, "status");
                                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                                    if (status == -1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 0) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                        String _json = GsonUtils.getNodeJsonString(jsonString, "data");
                                        RectifyNotifyInfo _info = GsonUtils.jsonToBean(_json, RectifyNotifyInfo.class);
                                        Intent _intent = new Intent(UnreadMsgListActivity.this, ReadRectifyNotifyInfoActivity.class);
                                        Bundle _bundle = new Bundle();
                                        _bundle.putSerializable("data", _info);
                                        _intent.putExtras(_bundle);
                                        startActivity(_intent);
                                        setReplyNotifyRead(pid);
                                    }
                                }catch (Exception e){
//                                    LogUtils.i("error = ",e.toString());
                                }
                            }
                        });
                    }
                }

            }
        }, new DialogInterface.OnClickListener() {
            /**
             *取消dialog
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }, new DialogInterface.OnClickListener() {
            /**
             * 查看整改回复单
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (_replyId == 0) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "没有回复单", Toast.LENGTH_SHORT);
                } else { //跳转到回复整改单
//                    MyToast.showMyToast(UnreadMsgListActivity.this, "有回复单", Toast.LENGTH_SHORT);
                    if (_remark.contains("待审批")) {
                        //跳转到审核表 业主待审核  监理待审核  施工方审批
                        JSONObject _jsonObject = new JSONObject();
                        try {
                            _jsonObject.put("id", _replyId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_REPLY_FOR_ID, _jsonObject.toString(), new OkHttpUtils.QueryDataCallBack() {
                            @Override
                            public void requestFailure(Request request, IOException e) {
                                MyToast.showMyToast(UnreadMsgListActivity.this, "查询失败，request", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void requestSuccess(String jsonString) throws Exception {
                                try {
                                    int status = GsonUtils.getIntNoteJsonString(jsonString, "status");
                                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                                    if (status == -1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 0) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                        String _json = GsonUtils.getNodeJsonString(jsonString, "data");
                                        RectifyReplyInfo _info = GsonUtils.jsonToBean(_json, RectifyReplyInfo.class);
                                        Intent _intent = new Intent(UnreadMsgListActivity.this, ReadRectifyReplyInfoActivity.class);
                                        Bundle _bundle = new Bundle();
                                        _bundle.putSerializable("data", _info);
                                        _intent.putExtras(_bundle);
                                        startActivity(_intent);
                                        setReplyNotifyRead(pid);
                                    }

                                } catch (Exception e) {
//                                    LogUtils.i("error=", e.toString());
                                }
                            }
                        });
                    } else {
                        //跳转到已经审核的表  业主已审批 监理已审批  施工方已审批
                        JSONObject _jsonObject = new JSONObject();
                        try {
                            _jsonObject.put("id", _replyId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_REPLY_FOR_ID, _jsonObject.toString(), new OkHttpUtils.QueryDataCallBack() {
                            @Override
                            public void requestFailure(Request request, IOException e) {

                            }

                            @Override
                            public void requestSuccess(String jsonString) throws Exception {
                                try {
                                    int status = GsonUtils.getIntNoteJsonString(jsonString, "status");
                                    String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");
                                    if (status == -1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 0) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                    } else if (status == 1) {
                                        MyToast.showMyToast(UnreadMsgListActivity.this, msg, Toast.LENGTH_SHORT);
                                        String _json = GsonUtils.getNodeJsonString(jsonString, "data");
                                        RectifyReplyInfo _info = GsonUtils.jsonToBean(_json, RectifyReplyInfo.class);
                                        String _jsSinfo = GsonUtils.getNodeJsonString(jsonString, "SInfo");
                                        String _jsOinfo = GsonUtils.getNodeJsonString(jsonString, "OInfo");
//                                    LogUtils.i("_jsSinfo",_jsSinfo);
//                                    LogUtils.i("_jsOinfo",_jsOinfo);
//                                    ReplySupervisorInfo _sInfo;
//                                    ReplySupervisorInfo _oInfo;
                                        if (_jsSinfo.length() > 4) {
                                            _sInfo = GsonUtils.jsonToBean(_jsSinfo, ReplySupervisorInfo.class);
                                        }
                                        if (_jsOinfo.length() > 4) {
                                            _oInfo = GsonUtils.jsonToBean(_jsOinfo, ReplySupervisorInfo.class);
                                        }
                                        Intent _intent = new Intent(UnreadMsgListActivity.this, ReadReplyInfoHasCheckActivity.class);
                                        Bundle _bundle = new Bundle();
                                        _bundle.putSerializable("data", _info);
                                        _bundle.putSerializable("sInfo", _sInfo);
                                        _bundle.putSerializable("oInfo", _oInfo);
                                        _intent.putExtras(_bundle);
                                        startActivity(_intent);
                                        setReplyNotifyRead(pid);
                                    }
                                } catch (Exception e) {
//                                    LogUtils.i("error", e.toString());
                                }
                            }
                        });
                    }
                }
                //设置整改通知单已读
            }
        });

//        setReplyNotifyRead(pid);

    }

    //设置单个消息已读
    private void setReplyNotifyRead(int replyId) {
        try {
            JSONObject _jsObj = new JSONObject();
            _jsObj.put("pids", replyId + "");
            _jsObj.put("realName", UserSingleton.getUserInfo().getRealName());
            String json = _jsObj.toString();
//            LogUtils.i("jsonMap = ", json);
            OkHttpUtils.postAsync(ServerConfig.URL_SET_MSG_READ, json, new OkHttpUtils.InsertDataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "修改通知状态失败", Toast.LENGTH_SHORT);
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    int status = GsonUtils.getIntNoteJsonString(result, "status");
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                    if (status == -1) {
//                        LogUtils.i("msg", msg + status);
                    } else if (status == 0) {
//                        LogUtils.i("msg", msg + status);
                    } else if (status == 1) {
//                        LogUtils.i("msg", msg + status);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setReplyNotifyRead(String str) {
        try {
            JSONObject _jsObj = new JSONObject();
            _jsObj.put("pids", str);
            _jsObj.put("realName", UserSingleton.getUserInfo().getRealName());
            String json = _jsObj.toString();
//            LogUtils.i("jsonMap = ", json);
            OkHttpUtils.postAsync(ServerConfig.URL_SET_MSG_READ, json, new OkHttpUtils.InsertDataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {
                    MyToast.showMyToast(UnreadMsgListActivity.this, "修改通知状态失败", Toast.LENGTH_SHORT);
                }

                @Override
                public void requestSuccess(String result) throws Exception {
                    int status = GsonUtils.getIntNoteJsonString(result, "status");
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                    if (status == -1) {
//                        LogUtils.i("msg", msg + status);
                    } else if (status == 0) {
//                        LogUtils.i("msg", msg + status);
                    } else if (status == 1) {
//                        LogUtils.i("msg", msg + status);
                        MyToast.showMyToast(UnreadMsgListActivity.this, "一键设置已读成功", Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
