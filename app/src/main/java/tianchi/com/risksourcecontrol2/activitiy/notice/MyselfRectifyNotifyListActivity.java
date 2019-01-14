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
import tianchi.com.risksourcecontrol2.adapter.ReceviceNoticeAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by hairun.tian on 2018/6/19 0019.
 * <p>
 * 整改通知单列表
 * /未读整改通知单 个人收到
 * 接收人是自己
 */

public class MyselfRectifyNotifyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public TextView m_tvClose;
    public ListView m_lvReceive;
    public TextView m_mTvNoNotice;
    public List<RectifyNotifyInfo> m_list;
    private ProgressDialog m_progressDialog;
    private ReceviceNoticeAdapter m_receviceNoticeAdapter;

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

        m_progressDialog = new ProgressDialog(MyselfRectifyNotifyListActivity.this);
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
                    MyToast.showMyToast(MyselfRectifyNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
                    MyToast.showMyToast(MyselfRectifyNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "UnReplys");//解析数据

                    List<RectifyNotifyInfo> _unReplyDatas = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyNotifyInfo.class);

                    List<RectifyReplyInfo> _rectifyReplyInfos = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyReplyInfo.class);

                    //                    如果两个为空，提示没有接收到任何消息
                    if (_unReplyDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);
                        m_progressDialog.dismiss();

                    } else { //否则把接收到的信息提取标题，放在list中
                        if (_unReplyDatas.size() != 0) {
                            for (int i = _unReplyDatas.size() - 1; i >= 0; i--) {
                                int _logState = _unReplyDatas.get(i).getLogState();

                                if (_logState!=6){
                                    m_list.add(_unReplyDatas.get(i));
                                }

                            }
                        }

//                        LogUtils.i("length = ", String.valueOf(m_list.size()));
                        m_receviceNoticeAdapter = new ReceviceNoticeAdapter(MyselfRectifyNotifyListActivity.this, m_list);
                        m_lvReceive.setAdapter(m_receviceNoticeAdapter);
                        m_progressDialog.dismiss();
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
        RectifyNotifyInfo _rectifyNotifyInfo = m_list.get(position);

//        String time = _rectifyNotifyInfo.getCheckedTime();
//        LogUtils.i("time" ,time);
//        _bundle.putSerializable("data", _rectifyNotifyInfo);
//        Intent _intent = new Intent(MyselfRectifyNotifyListActivity.this, ReadRectifyNotifyInfoActivity.class);
        int _id = _rectifyNotifyInfo.getId();
        _bundle.putInt("id",_id);
        Intent _intent = new Intent(MyselfRectifyNotifyListActivity.this, QueryNotfiyReplyActivity.class);
        _intent.putExtras(_bundle);
        startActivity(_intent);

    }



    //网络请求接收过的通知
    private void getNotice(String receiveMan, ReceiveNoticeListActivity.CallBack callBack) {
        m_progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("realName", receiveMan);
//            jsonObject.put("replyState",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
        OkHttpUtils.postAsync(ServerConfig.URL_QUERYRECTIFYNOTIFY, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("接收通知失败", request.body().toString());
                m_progressDialog.setMessage("加载失败");
                m_progressDialog.dismiss();
            }
            @Override
            public void requestSuccess(String result) throws Exception {
                LogUtils.i("接收通知", result);
                callBack.getData(result);

            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//        MyAlertDialog.showAlertDialog(MyselfRectifyNotifyListActivity.this, "温馨提示", "是否要删除当前整改通知单？", "确定", "取消", false, new DialogInterface.OnClickListener()       {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                int _logState = m_list.get(position).getLogState();
//                if (_logState) {
//                    //确认删除
//                    int _draftId = m_list.get(position).getId();
//                    deleteDraft(_draftId);
//                    m_list.remove(position);
//                    m_receviceNoticeAdapter.notifyDataSetChanged();
//                }
//
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //取消删除
//            }
//        });
//        return true;
//
////    }
//    private void deleteDraft(int draftId) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("id", draftId);
//            OkHttpUtils.postAsync(ServerConfig.URL_DELETE_NOTIFY_FOR_ID, json.toString(), new OkHttpUtils.InsertDataCallBack() {
//                @Override
//                public void requestFailure(Request request, IOException e) {
//                }
//                @Override
//                public void requestSuccess(String result) throws Exception {
//                    int status = GsonUtils.getIntNoteJsonString(result, "status");
//                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
//
//                    if (status == -1 || status == 0) {
//                        MyToast.showMyToast(MyselfRectifyNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
//                    } else {
//                        MyToast.showMyToast(MyselfRectifyNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
//                    }
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
