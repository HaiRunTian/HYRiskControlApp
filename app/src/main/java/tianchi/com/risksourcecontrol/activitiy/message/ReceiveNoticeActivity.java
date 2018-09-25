package tianchi.com.risksourcecontrol.activitiy.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.QueryDisclosureActivity;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.notice.ReadData;
import tianchi.com.risksourcecontrol.bean.notice.SendNotice;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.util.OpenFile;

/**
 * Created by hairun.tian on 2018/3/21 0021.
 */

public class ReceiveNoticeActivity extends BaseActivity implements View.OnClickListener {

    private EditText m_edtTitle; //标题
    private EditText m_edtTime; //时间
    private EditText m_edtSendMan; //发送人
    private EditText m_edtContent; //内容
    private TextView m_tvClose;//
    private String m_fileName;//文件名字
    private TextView m_tvfileName;
    private File m_file;
    private Button m_btnSubmit; //提交
    private View m_view;
    private EditText m_edtReplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_receive);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        getNotice();
    }


    private void getNotice() {
        String idLoginName = UserSingleton.getUserInfo().getUserId() + UserSingleton.getUserInfo().getLoginName();
        m_file = new File(FoldersConfig.SD_FILE, m_fileName);
        //本地存在，打开
        if (m_file.exists()) {
            m_tvfileName.setText(m_fileName);

        } else { //本地不存在 ，网络下载跳转打开

            downlodaFile(m_fileName, idLoginName, new QueryDisclosureActivity.downloadListener() {
                @Override
                public void downloadSucceed() {
                    m_tvfileName.setText(m_fileName);
                    MyToast.showMyToast(ReceiveNoticeActivity.this, "下载成功", Toast.LENGTH_SHORT);
//                    Intent _intent = OpenFile.openFile(m_file.getAbsolutePath());
//                    startActivity(_intent);
                }

                @Override
                public void downloadFail() {

                    MyToast.showMyToast(ReceiveNoticeActivity.this, "下载失败", Toast.LENGTH_SHORT);
                }
            });
        }


    }

    private void initView() {
        m_edtTitle = (EditText) findViewById(R.id.edtTitle);
        m_edtTime = (EditText) findViewById(R.id.edtTime);
        m_edtSendMan = (EditText) findViewById(R.id.edtReceiveMan);
        m_edtContent = (EditText) findViewById(R.id.edtContent);
        m_tvfileName = (TextView) findViewById(R.id.tvFileName);
        m_btnSubmit = (Button) findViewById(R.id.btnSubmit);
        m_view =findViewById(R.id.scrollView);
        m_edtReplay = (EditText) findViewById(R.id.edtreplay);
        m_btnSubmit.setOnClickListener(this);

        m_tvfileName.setOnClickListener(this);

        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);

       ReadData _readData = (ReadData) getIntent().getSerializableExtra("data");
        m_fileName = _readData.getFileUrl();
        m_edtTitle.setText(_readData.getTitle());
//        String s = _readData.getDataTime();
        if (_readData.getDataTime().toString().length()>10) {
            m_edtTime.setText(_readData.getDataTime().toString().substring(0, 10));
        }else {
            m_edtTime.setText(" ");
        }
        m_edtSendMan.setText(_readData.getSendName());
        m_edtContent.setText(_readData.getNoticeContent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvFileName:
                Intent _intent = OpenFile.openFile(m_file.getAbsolutePath());
                startActivity(_intent);
                break;

            case R.id.btnSubmit:
                if (m_btnSubmit.getText().toString().equals("回复")){
                    m_view.setVisibility(View.VISIBLE);
                    m_btnSubmit.setText("发送");
                }else { //回复信息

                    sendNotice();

                }
        }
    }



    private void sendNotice() {
        SendNotice _sendNotice = getString();
        String _jsonObject = GsonUtils.objectToJson(_sendNotice);
        OkHttpUtils.postAsync(ServerConfig.URL_SEND_NOTICE, _jsonObject, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("发送通知", request.body().toString());
                MyToast.showMyToast(ReceiveNoticeActivity.this, "回复发送失败", Toast.LENGTH_SHORT);

            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                LogUtils.i("发送通知", result);
                int status = GsonUtils.getIntNoteJsonString(result, "status");
                String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                if (status == 1) {
                    MyToast.showMyToast(ReceiveNoticeActivity.this, msg, Toast.LENGTH_SHORT);
                    finish();
                } else if (status == 0) {
                    MyToast.showMyToast(ReceiveNoticeActivity.this, msg, Toast.LENGTH_SHORT);

                } else {
                    MyToast.showMyToast(ReceiveNoticeActivity.this, msg, Toast.LENGTH_SHORT);
                }
            }
        });


    }


    private SendNotice getString() {


        SendNotice _sendNotice = new SendNotice();
        _sendNotice.setTitleName(m_edtTitle.getText().toString());
        _sendNotice.setDataTime(m_edtTime.getText().toString() + " 00:00:00");
        _sendNotice.setReceiveMans(m_edtSendMan.getText().toString()); //接收人
        _sendNotice.setTextContent(m_edtContent.getText().toString()+"\n"+"——————回复内容："+m_edtReplay.getText().toString());
        _sendNotice.setSendName(UserSingleton.getUserInfo().getRealName());
//        if (m_edtAddFile.getText().toString().length() != 0){
//            _sendNotice.setFileType(fileType);
//            _sendNotice.setFileUrl(m_fileName);
//        }else {
            _sendNotice.setFileType(" ");
            _sendNotice.setFileUrl(" ");
//        }


        return _sendNotice;
    }


    private void downlodaFile(final String fileName, String idLoginName, QueryDisclosureActivity.downloadListener listener) {

        try {

            final String url = ServerConfig.URL_UPDATA_NOTICE_PIC + idLoginName + "/" + URLEncoder.encode(fileName, "UTF-8");
            URL _new = new URL(url);
//            final long startTime = System.currentTimeMillis();
//            LogUtils.i("DOWNLOAD", "startTime=" + startTime + ", url = " + url);
            Request request = new Request.Builder().url(_new).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    listener.downloadFail();
                    // 下载失败
                    e.printStackTrace();
//                    LogUtils.i("DOWNLOAD", "download failed");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Sink sink = null;
                    BufferedSink bufferedSink = null;
                    try {

                        File dest = new File(FoldersConfig.SD_FILE + "/" + fileName);
                        sink = Okio.sink(dest);
                        bufferedSink = Okio.buffer(sink);
                        bufferedSink.writeAll(response.body().source());
                        //bufferedSink.writeAll(response.getWriter().write());
                        bufferedSink.close();
                        listener.downloadSucceed();
                    } catch (Exception e) {
//                        listener.downloadFail();
                        e.printStackTrace();
                    } finally {
                        if (bufferedSink != null) {
                            bufferedSink.close();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
