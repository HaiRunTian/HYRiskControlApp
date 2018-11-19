package tianchi.com.risksourcecontrol2.activitiy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.adapter.TecDisFromNetAdapter;
import tianchi.com.risksourcecontrol2.adapter.TecDisFromSDAdapter;
import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.DisclosureInfoData;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.util.OpenFile;

/**
 * 技术交底列表
 */
public class QueryDisclosureActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnLongClickListener {
    private Button btnBack;
    private Button btnQuerySection;
    private Button btnQueryStake;
    private ListView m_lvResult;
    private List<String> m_listData;
    private TecDisFromNetAdapter m_adapter;
    private List<String> m_list;
    private List<DisclosureInfoData> m_data;
    private String m_result;
    private boolean isFromSd = false;
    private Spinner m_spinner;
    private ProgressDialog m_progressDialog;
    private String section;
    private String recorders;
    private TextView m_tvPerson;
    private final int GET_RELATIONSHIP = 0;
    public interface CallBack {
        void getData(List<DisclosureInfoData> list);
    }

    public interface downloadListener {
        void downloadSucceed();
        void downloadFail();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_disclosure);
        initView();
        initValue();

    }

    private void initValue() {

    }

    private void initView() {
        m_list = new ArrayList<>();
        btnBack = (Button) findViewById(R.id.tvQueryDisclosureBack);
        btnQuerySection = (Button) findViewById(R.id.btQuerySection);
        btnQueryStake = (Button) findViewById(R.id.btQueryStake);
        m_lvResult = (ListView) findViewById(R.id.lvResult);
        m_spinner = (Spinner) findViewById(R.id.spSection);
        m_tvPerson = (TextView) findViewById(R.id.tv_person);
        String str = UserSingleton.getUserInfo().getSectionList();
        m_tvPerson.setOnClickListener(this);
        m_tvPerson.setOnLongClickListener(this);
        if (str.contains("0")) {
            section = str.substring(2);
        }
        String[] m_arrSection = section.split("#");  //字符串转成数组

        String[] _spData = new String[m_arrSection.length];
        for (int _i = 0; _i < m_arrSection.length; _i++) {
            _spData[_i] = ServerConfig.getMapSection().get(m_arrSection[_i]);
        }


        m_spinner.setAdapter(new ArrayAdapter<String>(QueryDisclosureActivity.this, android.R.layout.simple_spinner_dropdown_item, _spData));
        btnBack.setOnClickListener(this);
        btnQuerySection.setOnClickListener(this);
        btnQueryStake.setOnClickListener(this);

        m_lvResult.setOnItemClickListener(this);

        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("文件正在下载中...");
        m_progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvQueryDisclosureBack:
                finish();
                break;

            //连接服务器，根据标段查询技术交底
            case R.id.btQuerySection:
                isFromSd = false;
                String _secton = m_spinner.getSelectedItem().toString();
                recorders = m_tvPerson.getText().toString();
                if (recorders.length() == 0) {
                    MyToast.showMyToast(this, "请输人员再查询", Toast.LENGTH_SHORT);
                } else {
                    //输入标段，从服务器中查询
                    getRiskAsynByOkHttp3(_secton, recorders, new CallBack() {
                        @Override
                        public void getData(List<DisclosureInfoData> list) {

//                            LogUtils.i("接口回调回来的数据————————————————————————————————", String.valueOf(list.size()));
                            m_lvResult.setAdapter(new TecDisFromNetAdapter(list));
                        }
                    });
                }

                break;


            //查询本地全部技术交底
            case R.id.btQueryStake:
                isFromSd = true;
                //遍历文件夹
                File _file = new File(FoldersConfig.DEFAULT_TECDIS_FILE_PATH);
                m_list = recursionFile(_file);
                if (m_list.size() == 0) {
                    MyToast.showMyToast(this, "本地没有任何技术交底文件，请下载", Toast.LENGTH_SHORT);
                    return;
                } else {

                    m_lvResult.setAdapter(new TecDisFromSDAdapter(m_list));
                }

                break;


            case R.id.tv_person:
                startActivityForResult(new Intent(this, RelationshipListActivity.class), GET_RELATIONSHIP);
                break;

            default:
                break;

        }

    }


    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.tv_person) {
            MyAlertDialog.showAlertDialog(this, "已选人员列表", getReceiveMan());
        }
        return false;
    }

    private String getReceiveMan() {

        return m_tvPerson.getText().toString();
    }


    //遍历手机所有文件 并将路径名存入集合中 参数需要 路径和集合
    public List<String> recursionFile(File dir) {
        List<String> fileName = new ArrayList<>();
        //得到某个文件夹下所有的文件
        File[] files = dir.listFiles();
        //文件为空
        if (files == null) {
            return null;
        }
        //遍历当前文件下的所有文件
        for (File file : files) {
            if (file.isFile()) {
                fileName.add(file.getName());
            }
        }
        return fileName;
    }

    private void getRiskAsynByOkHttp3(String section, String person, CallBack callBack) {

        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("section", section);
            _jsonObject.put("recorders", person);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = _jsonObject.toString();
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_QUERY_DISCLOSURE_FROM_SECTION, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);

            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                LogUtils.i("数组长度", _response);
                int status = GsonUtils.getIntNoteJsonString(_response, "status");
                String msg = GsonUtils.getStringNodeJsonString(_response, "msg");
                try {
                    if (status == 0) {
                        MyToast.showMyToast(QueryDisclosureActivity.this, msg, Toast.LENGTH_SHORT);
                        return;
                    } else if (status == -2) {
                        MyToast.showMyToast(QueryDisclosureActivity.this, msg, Toast.LENGTH_SHORT);
                        return;
                    } else if (status == 1) {

                        m_data = GsonUtils.jsonToArrayBeans(_response, "data", DisclosureInfoData.class);
                        LogUtils.i("数组长度", String.valueOf(m_data.size()));
                        callBack.getData(m_data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //点击item,获取列表名字，判断本地是否存在，若存在，打开本地，若不存在，从服务器下载后再打开
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isFromSd) {
            String fileName = m_list.get(position);
            File _file = new File(FoldersConfig.DEFAULT_TECDIS_FILE_PATH, fileName);
            Intent _intent = OpenFile.openFile(_file.getAbsolutePath());
            startActivity(_intent);
        } else {

            String fileName = m_data.get(position).getFileName(); //获取点击安全交底的名称
            String idLoginName = UserSingleton.getUserInfo().getUserId() + UserSingleton.getUserInfo().getLoginName();
            File _file = new File(FoldersConfig.DEFAULT_TECDIS_FILE_PATH, fileName);
            //本地存在，打开
            if (_file.exists()) {
                Intent _intent = OpenFile.openFile(_file.getAbsolutePath());
                startActivity(_intent);
            } else { //本地不存在 ，网络下载跳转打开

                downlodaFile(fileName, idLoginName, new downloadListener() {
                    @Override
                    public void downloadSucceed() {
                        m_progressDialog.dismiss();
                        MyToast.showMyToast(QueryDisclosureActivity.this, "下载成功", Toast.LENGTH_SHORT);
                        Intent _intent = OpenFile.openFile(_file.getAbsolutePath());
                        startActivity(_intent);
                    }

                    @Override
                    public void downloadFail() {
                        m_progressDialog.dismiss();
                        MyToast.showMyToast(QueryDisclosureActivity.this, "下载失败", Toast.LENGTH_SHORT);
                    }
                });
            }
        }

    }

    private void downlodaFile(final String fileName, String idLoginName, downloadListener listener) {

        try {

            final String url = ServerConfig.URL_DOWNLOAD_DIS + idLoginName + "/" + URLEncoder.encode(fileName, "UTF-8");
            URL _new = new URL(url);
//            final long startTime = System.currentTimeMillis();
//            LogUtils.i("DOWNLOAD", "startTime=" + startTime + ", url = " + url);
            m_progressDialog.show();
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

                        File dest = new File(FoldersConfig.DEFAULT_TECDIS_FILE_PATH + "/" + fileName);
                        sink = Okio.sink(dest);
                        bufferedSink = Okio.buffer(sink);
                        bufferedSink.writeAll(response.body().source());
                        //bufferedSink.writeAll(response.getWriter().write());
                        bufferedSink.close();
                        listener.downloadSucceed();
                    } catch (Exception e) {
                        listener.downloadFail();
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


//
//        File _file = new File(SuperMapConfig.DEFAULT_TECDIS_FILE_PATH,_fileName);
//        //本地存在，打开
//        if (_file.exists()){
//           Intent _intent =  OpenFile.openFile(_file.getAbsolutePath());
//            startActivity(_intent);
//        }else { //本地不存在 ，网络下载跳转打开
//        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_RELATIONSHIP:
                if (resultCode == RESULT_OK) {
                    String allNameList = "";
                    for (String name : UsersList.getList()) {
                        allNameList += name + "#";
                    }
                    m_tvPerson.setText(allNameList.substring(0, allNameList.length() - 1));
                    UsersList.clearList();

                }
        }
    }
}



