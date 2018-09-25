package tianchi.com.risksourcecontrol.activitiy.notice;

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
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.message.ReceiveNoticeListActivity;
import tianchi.com.risksourcecontrol.adapter.DraftNoticeAdapter;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;

/**
 * Created by HaiRun on 2018-09-12 10:42.
 * 草稿列表
 */

public class DraftNotifyListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView m_tvClose;
    private ListView m_lvReceive;
    private TextView m_mTvNoNotice;
    private List<RectifyNotifyDraftInfo> m_list;
    private ProgressDialog m_progressDialog;

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
//                    LogUtils.i("id=", "==" + _info.getId());
//                    LogUtils.i("id=", "==" + _info.getLogState());
//                    如果两个为空，提示没有接收到任何消息
                    if (_unReplyDatas.size() == 0) {
                        m_mTvNoNotice.setVisibility(View.VISIBLE);
                        m_lvReceive.setVisibility(View.GONE);

                    } else { //否则把接收到的信息提取标题，放在list中

                        if (_unReplyDatas.size() != 0) {
                            for (int i = 0; i < _unReplyDatas.size(); i++) {
                                m_list.add(_unReplyDatas.get(i));
                            }
                        }
                    }
                    m_progressDialog.dismiss();
//                    LogUtils.i("length = ", String.valueOf(m_list.size()));
                }
                m_lvReceive.setAdapter(new DraftNoticeAdapter(DraftNotifyListActivity.this, m_list));
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
            jsonObject.put("logState", -1);
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
//                LogUtils.i("草稿查询成功", result);
                callBack.getData(result);

            }
        });
    }


    @Override
    public void onClick(View v) {
        finish();
    }

}
