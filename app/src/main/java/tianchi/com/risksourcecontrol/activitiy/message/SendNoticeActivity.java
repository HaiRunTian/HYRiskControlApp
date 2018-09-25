package tianchi.com.risksourcecontrol.activitiy.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
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
import tianchi.com.risksourcecontrol.bean.notice.SendNotice;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.util.OpenFile;

/**
 * Created by hairun.tian on 2018/3/21 0021.
 */

public class SendNoticeActivity extends BaseActivity implements View.OnClickListener {

    private EditText m_edtTitle; //标题
    private EditText m_edtTime; //时间
    private EditText m_edtSendMan; //接收人
    private EditText m_edtContent; //内容
    private String m_fileType; //文件类型
    private TextView m_tvClose;//
    private String m_fileName; //文件名字
    private TextView m_tvFileName;
    private File m_file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_send);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        SendNotice _sendNotice = (SendNotice) getIntent().getSerializableExtra("data1");
//            String i = _sendNotice.getReceiveMans();
        m_edtTitle.setText(_sendNotice.getTitleName().toString());
        m_edtSendMan.setText(_sendNotice.getReceiveMans().toString());
        if (_sendNotice.getDataTime().toString().length()>10) {
            m_edtTime.setText(_sendNotice.getDataTime().toString().substring(0, 10));
        }else {
            m_edtTime.setText(" ");
        }
        m_edtContent.setText(_sendNotice.getTextContent().toString());
        m_fileType = _sendNotice.getFileType();
        m_fileName = _sendNotice.getFileUrl();
        downLoadFile();


    }

    private void downLoadFile() {
        String idLoginName = UserSingleton.getUserInfo().getUserId() + UserSingleton.getUserInfo().getLoginName();
        m_file = new File(FoldersConfig.SD_FILE, m_fileName);
        //本地存在，打开
        if (m_file.exists()) {
            m_tvFileName.setText(m_fileName);

        } else { //本地不存在 ，网络下载跳转打开

            downlodaFile(m_fileName, idLoginName, new QueryDisclosureActivity.downloadListener() {
                @Override
                public void downloadSucceed() {
                    m_tvFileName.setText(m_fileName);
                    MyToast.showMyToast(SendNoticeActivity.this, "下载成功", Toast.LENGTH_SHORT);
//                    Intent _intent = OpenFile.openFile(m_file.getAbsolutePath());
//                    startActivity(_intent);
                }

                @Override
                public void downloadFail() {

                    MyToast.showMyToast(SendNoticeActivity.this, "下载失败", Toast.LENGTH_SHORT);
                }
            });
        }

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

    private void initView() {

        m_edtTitle = (EditText) findViewById(R.id.edtTitle);
        m_edtTime = (EditText) findViewById(R.id.edtTime);
        m_edtSendMan = (EditText) findViewById(R.id.edtReceiveMan);
        m_edtContent = (EditText) findViewById(R.id.edtContent);
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvFileName = (TextView) findViewById(R.id.tvFileName);
        m_tvFileName.setOnClickListener(this);
        m_tvClose.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvFileName:
                Intent _intent = OpenFile.openFile(m_file.getAbsolutePath());
                startActivity(_intent);
                break;
        }
    }
}
