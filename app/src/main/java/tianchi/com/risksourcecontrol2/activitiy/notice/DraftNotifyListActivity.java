package tianchi.com.risksourcecontrol2.activitiy.notice;

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
import tianchi.com.risksourcecontrol2.adapter.DraftNoticeAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-12 10:42.
 * 草稿列表
 */

public class DraftNotifyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<RectifyNotifyDraftInfo> m_list;
    private ProgressDialog m_progressDialog;
    private DraftNoticeAdapter m_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rectify_noticfy_list_activiaty);
        initView();
    }

    private void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitles);
        tvTitle.setText("草稿列表");
        m_list = new ArrayList();
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_mTvNoNotice = (TextView) findViewById(R.id.tv_no_notice);
        m_lvReceive = (ListView) findViewById(R.id.lvReceiveNews);
        m_progressDialog = new ProgressDialog(DraftNotifyListActivity.this);
        m_progressDialog.setMessage("草稿正在加载中，请稍等…");
        m_progressDialog.setCancelable(true);

        TextView _tvBack = (TextView) findViewById(R.id.tvClose);
        _tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        m_list.clear();
        //依据接收者名字
        getNotice(UserSingleton.getUserInfo().getRealName(), new ReceiveNoticeListActivity.CallBack() {
            @Override
            public void getData(String string) {
//                LogUtils.i("回调回来的数据", string);
                String msg = GsonUtils.getNodeJsonString(string, "msg");
                int status = GsonUtils.getIntNoteJsonString(string, "status");
                m_progressDialog.setMessage(msg);
                if (status == 0) {
                    MyToast.showMyToast(DraftNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
                    MyToast.showMyToast(DraftNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "data");//解析数据
                    List<RectifyNotifyDraftInfo> _unReplyDatas = GsonUtils.parserJsonToArrayBeans(beanListUnReply, RectifyNotifyDraftInfo.class);
                    RectifyNotifyDraftInfo _info = _unReplyDatas.get(0);
//                    如果两个为空，提示没有接收到任何消息
                    if (_unReplyDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);

                    } else { //否则把接收到的信息提取标题，放在list中

                        if (_unReplyDatas.size() != 0) {
                            for (int i = _unReplyDatas.size() -1; i >= 0; i--) {
                                m_list.add(_unReplyDatas.get(i));
                            }
                        }
                    }
                    m_progressDialog.dismiss();
//                    LogUtils.i("length = ", String.valueOf(m_list.size()));
                }
                m_adapter = new DraftNoticeAdapter(DraftNotifyListActivity.this, m_list);
                m_lvReceive.setAdapter(m_adapter);
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
        RectifyNotifyDraftInfo _rectifyNotifyInfo = m_list.get(position);
//        String _s =  readData.getDataTime();
        _bundle.putSerializable("data", _rectifyNotifyInfo);
        Intent _intent = new Intent(DraftNotifyListActivity.this, ReadDraftNotifyInfoActivity2.class);
//        _intent.putExtra("Type",);
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
            jsonObject.put("status", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();
//        LogUtils.i("json= ", str);
        OkHttpUtils.postAsync(ServerConfig.URL_QUERY_DRAFT_NOTICE, str, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("草稿查询失败", request.body().toString());
                m_progressDialog.setMessage("草稿加载失败");
                m_progressDialog.dismiss();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                callBack.getData(result);
            }
        });
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    //草稿列表，长按删除
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MyToast.showMyToast(DraftNotifyListActivity.this, "删除草稿", Toast.LENGTH_SHORT);

        MyAlertDialog.showAlertDialog(DraftNotifyListActivity.this, "温馨提示", "是否要删除草稿？", "确定", "取消", false, new DialogInterface.OnClickListener()    {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确认删除
                int _draftId = m_list.get(position).getId();
                deleteDraft(_draftId);
                m_list.remove(position);
                m_adapter.notifyDataSetChanged();
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

            OkHttpUtils.postAsync(ServerConfig.URL_DELETE_DRAFT_RECTIFY_NOTIFY, json.toString(), new OkHttpUtils.InsertDataCallBack() {
                @Override
                public void requestFailure(Request request, IOException e) {

                }
                @Override
                public void requestSuccess(String result) throws Exception {
                    int status = GsonUtils.getIntNoteJsonString(result, "status");
                    String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                    if (status == -1 || status == 0) {
                        MyToast.showMyToast(DraftNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                    } else {
                        MyToast.showMyToast(DraftNotifyListActivity.this, msg, Toast.LENGTH_SHORT);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
