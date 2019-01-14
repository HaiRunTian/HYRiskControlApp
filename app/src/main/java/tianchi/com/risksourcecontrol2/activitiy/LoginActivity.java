package tianchi.com.risksourcecontrol2.activitiy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supermap.data.LicenseManager;
import com.supermap.data.LicenseStatus;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.SelectListAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.HistoryInfo;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.config.SuperMapConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.presenter.UserLoginPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.ActivityCollector;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.util.GetVersionNum;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.view.IUserLoginView;

import static tianchi.com.risksourcecontrol2.R.id.edtUserName;

/**
 * @描述 登录
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:02.
 */
public class LoginActivity extends BaseActivity implements IUserLoginView, View.OnClickListener, PopupWindow.OnDismissListener, SelectListAdapter.OnItemClickListener, SelectListAdapter.OnDelBtnClickListener {

    //登录界面所有组件
    private EditText m_edtUserName;
    private EditText m_edtPassWord;
    //    private CheckBox m_cbPassWordVisible;
    //    private ImageView m_imgvUserNameClear;
    //    private TextView m_tvModifyPassWord;
    //    private TextView tvQA;
    //    private CheckBox m_cbRememberPassWord;
    private Button m_btLogin;
    //    private Button m_btReset;
    private ProgressDialog m_progressDialog;
    private AlertDialog m_dialog;
    //用户登录控制器
    private UserLoginPresenter m_userLoginPresenter = new UserLoginPresenter(this);

    //是否从注销返回
    boolean isFromLogOut;
    //是否记住密码
    boolean isPassWordRemembered;
    //返回键时间间隔
    private long intervalTime = 0;
    //    //当前账号
    //    static String s_CurrentAccount;
    private TextView m_tvVersion;
    private String m_updateMsg;
    private int m_serveVersionCode;
    private String m_serveVersionName;
    private String m_localVersionName;
    private int m_localVersionCode;
    private boolean downloadIsOk = false;
    private ImageView m_Arrow;

    private ArrayList<HistoryInfo> mAccounts;
    private PopupWindow mSelectWindow;
    private RelativeLayout mInputLayout;
    private SelectListAdapter adapter;
    private SharedPreferences sharedPreferences;
    private final String KEY = "Set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*防止按下home键后从app图标进入时出现再次将app开市页置顶*/
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        initView();
        if (!licenseStatus()) { //判断许可证是否过期，许可证过期，重新从服务器下载
            try {
                Boolean isDelet = FileUtils.getInstance().deleteFile(SuperMapConfig.FULL_LIC_PATH);
                if (isDelet) {
//                    LogUtils.i("许可删除成功", "许可删除成功");
                    downLoadLIcense();
                    if (!licenseStatus()) { //如果刚下载的许可证也过期，就联系技术员
                        MyToast.showMyToast(LoginActivity.this, "许可过期，请联系技术员", Toast.LENGTH_SHORT);
                    }
                }
            } catch (Exception e) {
                MyToast.showMyToast(LoginActivity.this, "许可下载异常", Toast.LENGTH_SHORT);
            }

        }
        autoLogin();
        if (ping()) {
            checkVersionCode();//测试版本号
        }
    }


    private void downLoadLIcense() {
        //        String idLoginName = UserSingleton.getUserInfo().getUserId() + UserSingleton.getUserInfo().getLoginName();
        downlodaFile(SuperMapConfig.LIC_NAME, new QueryDisclosureActivity.downloadListener() {
            @Override
            public void downloadSucceed() {
                m_progressDialog.setMessage("许可更新成功");
                m_progressDialog.dismiss();
//                LogUtils.i("111111111", "许可下载更新成功");
                downloadIsOk = true;
                //
            }

            @Override
            public void downloadFail() {
                m_progressDialog.dismiss();
//                LogUtils.i("111111111", "许可下载更新失败");
                downloadIsOk = false;
            }
        });

    }


    private void downlodaFile(final String fileName, QueryDisclosureActivity.downloadListener listener) {
//        LogUtils.i("下载许可中");
        try {
            final String url = ServerConfig.URl_DOWNLOAD_LICENSE + fileName;
//            LogUtils.i("许可下载路径 = ",url);
            URL _new = new URL(url);
            m_progressDialog.show();
            m_progressDialog.setMessage("许可更新中");
            Request request = new Request.Builder().url(_new).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    listener.downloadFail();
                    // 下载失败
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Sink sink = null;
                    BufferedSink bufferedSink = null;
                    try {
                        File dest = new File(SuperMapConfig.LIC_PATH + fileName);
                        if (!dest.exists()) dest.createNewFile();
                        sink = Okio.sink(dest);
                        bufferedSink = Okio.buffer(sink);
                        bufferedSink.writeAll(response.body().source());
//                        bufferedSink.writeAll(response.getWriter().write());
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


    //判断许可证是否过期、有效
    public boolean licenseStatus() {
        LicenseManager _licenseManager = LicenseManager.getInstance();
        LicenseStatus _licenseStatus = _licenseManager.getLicenseStatus();
        boolean isUse = _licenseStatus.isLicenseValid(); //是否有效
        return isUse;


    }

    private void downLoadApk() {

        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(ServerConfig.URL_UPDATE_APP + m_serveVersionName, pd);

                    //安装APK
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {

                }
            }
        }.start();
    }

    private void installApk(File file) {

        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);


    }

    private File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(FoldersConfig.APK_FLODE, m_serveVersionName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }


    }

    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(m_updateMsg);
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", (dialog, which) -> downLoadApk());
        //当点取消按钮时不做任何举动
        builer.setNegativeButton("取消", (dialogInterface, i) -> {
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    public boolean isOk = false;

    //从服务器中获取版本号
    private void
    checkVersionCode() {
        OkHttpUtils.getAsync(ServerConfig.URL_GET_VERSION_CODE, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                m_serveVersionCode = 0;
                MyToast.showMyToast(LoginActivity.this, "获取版本失败", Toast.LENGTH_SHORT);
                isOk = false;

            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject _jsonObject = new JSONObject(result);
                int status = _jsonObject.getInt("status");
                String msg = _jsonObject.getString("msg");
                if (status == 1) {
                    m_updateMsg = _jsonObject.getString("Message");
                    m_serveVersionCode = _jsonObject.getInt("VersionCode");
                    m_serveVersionName = _jsonObject.getString("VersionName");
//                    LogUtils.i("VersionName=",m_serveVersionName);
//                    LogUtils.i("URL=",ServerConfig.URL_UPDATE_APP+m_serveVersionName);
                    //                    MyToast.showMyToast(LoginActivity.this, msg, Toast.LENGTH_SHORT);
                    if (m_serveVersionCode > m_localVersionCode) { //服务器版本号大于本地版本号，则下载更新
                        /**
                         * 弹出对话框
                         */
                        showUpdataDialog();

                    } else {
                        //                        MyToast.showMyToast(LoginActivity.this, "已经是最版本", Toast.LENGTH_SHORT);
                    }
                } else {
                    //                    MyToast.showMyToast(LoginActivity.this, msg, Toast.LENGTH_SHORT);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - intervalTime >= 2000) {
            MyToast.showMyToast(this, "再按一次返回键退出", Toast.LENGTH_SHORT);
            intervalTime = System.currentTimeMillis();
            return;
        } else {
            intervalTime = System.currentTimeMillis();
            //            Intent i = new Intent();
            //            i.putExtra("backFromLogin", 1);
            //            setResult(99,i);
            ActivityCollector.finishAllActivity();
            super.onBackPressed();
        }
    }

    //自动登录
    private void autoLogin() {
        if (getLocalUserInfo()) {
            //            m_userLoginPresenter.login(isRemembered());
        }
    }

    public void onDownArrowClicked(View view)
    {
        if (mAccounts.size() != 0)
        {
            m_Arrow.setBackgroundResource(R.drawable.arrow_up);
            showAccountChoiceWindow();
        }
    }



    private void showAccountChoiceWindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.down_account, null, false);
        LinearLayout contentview = (LinearLayout) view.findViewById(R.id.input_select_listlayout);
        ListView userlist = (ListView) view.findViewById(R.id.input_select_list);
        userlist.setDividerHeight(0);
        adapter = new SelectListAdapter(this, mAccounts);
        adapter.setOnItemClickListener(this);
        adapter.setOnDelBtnClickListener(this);
        userlist.setAdapter(adapter);
        mSelectWindow = new PopupWindow(contentview, mInputLayout.getMeasuredWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mSelectWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mSelectWindow.setOutsideTouchable(true);
        mSelectWindow.setFocusable(true);
        mSelectWindow.setOnDismissListener(this);
        mSelectWindow.showAsDropDown(mInputLayout, 0, 0);
        m_Arrow.setBackgroundResource(R.drawable.arrow_up);
    }


    /*
    * 初始化布局
    * */
    private void initView() {

        mAccounts = new ArrayList<>();
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        m_tvVersion = (TextView) findViewById(R.id.tvVersionNum);
        m_Arrow = ((ImageView) findViewById(R.id.input_arrow));

        mInputLayout = ((RelativeLayout) findViewById(R.id.input_layout));

        //获取版本号
        m_localVersionCode = GetVersionNum.getLocalVersion(LoginActivity.this);
        //获取版本名字
        m_localVersionName = GetVersionNum.getLocalVersionName(LoginActivity.this);
        m_tvVersion.setText("版本：" + String.valueOf(m_localVersionName));
        m_edtUserName = (EditText) findViewById(edtUserName);
        m_edtPassWord = (EditText) findViewById(R.id.edtPassWord);
        //        m_cbPassWordVisible = (CheckBox) findViewById(R.id.cbPasswordVisible);
        //        m_imgvUserNameClear = (ImageView) findViewById(R.id.imgvUsernameClear);
        //        m_tvModifyPassWord = (TextView) findViewById(R.id.tvModifyPassWord);
        //        tvQA = (TextView) findViewById(R.id.tvQA);
        m_btLogin = (Button) findViewById(R.id.btLogin);
        //        m_btReset = (Button) findViewById(R.id.btReset);
        //        m_cbRememberPassWord = (CheckBox) findViewById(R.id.cbRememberPassWord);
        m_btLogin.setOnClickListener(this);
        //        m_btReset.setOnClickListener(this);
        //        m_imgvUserNameClear.setOnClickListener(this);
        //        m_tvModifyPassWord.setOnClickListener(this);
        //        tvQA.setOnClickListener(this);
        m_edtUserName.setFilters(new InputFilter[]{m_filter});
        m_edtPassWord.setFilters(new InputFilter[]{m_filter});
        m_edtUserName.addTextChangedListener(m_watcher);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(false);
        /*账号名输入框文本改变事件*/
        //        CustomClearButton.setClearButtonVisible(m_edtUserName, m_imgvUserNameClear);

        /*
        * 密码明文显示事件
        * */
        //        CustomClearButton.setPasswordVisible(m_edtPassWord, m_cbPassWordVisible);
    }

    //文本框过滤
    private InputFilter m_filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" "))
                return "";
            else
                return null;
        }
    };
    TextWatcher m_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (m_edtUserName.getText().toString().length() > 0) {
                m_btLogin.setBackgroundColor(Color.parseColor("#80b3f1"));
            } else {
                m_btLogin.setBackgroundColor(Color.parseColor("#cfe2fa"));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLogin://登录
                //                s_CurrentAccount = getUserName();

                m_userLoginPresenter.login(isRemembered());




                break;
            //            case R.id.btReset://重置
            //                m_userLoginPresenter.reset();
            //                break;
            //            case R.id.imgvUsernameClear://清空账号
            //                clearUserName();
            //                break;
            //            case R.id.tvQA:
            //                m_dialog = MyAlertDialog.showAlertDialog(this, "温馨提示", "请检查当前网络是否可用，如果当前网络正常，仍然无法登陆，请联系技术人员。");
            //                break;
            //            case R.id.tvModifyPassWord://修改密码
            //                toModifyPasswordActivity();
            //                break;
            default:
                break;
        }
    }

    @Override
    public String getUserName() {
        return m_edtUserName.getText().toString();
    }

    @Override
    public String getPassWord() {
        return m_edtPassWord.getText().toString();
    }

    @Override
    public boolean isRemembered() {
        return true;
    }

    //    @Override
    //    public boolean isRemembered() {
    //        return m_cbRememberPassWord.isChecked();
    //    }

    @Override
    public void clearUserName() {
        m_edtUserName.setText("");
    }

    @Override
    public void clearPassword() {
        m_edtPassWord.setText("");
    }

    @Override
    public void showLoading(String msg) {
        if (m_progressDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_progressDialog.setMessage(msg);
                    m_progressDialog.show();
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        if (m_progressDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_progressDialog.dismiss();
                }
            });
        }
    }

    //获取本地保存的用户信息
    @Override
    public boolean getLocalUserInfo() {
        Intent intent = getIntent();

        isFromLogOut = intent.getBooleanExtra("isFromLogOut", false);
        Set<String> _set = sharedPreferences.getStringSet(KEY, new HashSet<>());

        Iterator<String> _iterator1 = _set.iterator();
        while (_iterator1.hasNext()){
            String _next = _iterator1.next();
            LogUtils.i("next",_next);
        }
        isPassWordRemembered = sharedPreferences.getBoolean("isPassWordRemembered", false);

        if (isPassWordRemembered) {

//            m_edtUserName.setText(sharedPreferences.getString("userName", ""));
//            m_edtPassWord.setText(sharedPreferences.getString("passWord", ""));


            if (_set != null && _set.size() != 0){

                Iterator<String> _iterator = _set.iterator();
                String _s = "";
                while (_iterator.hasNext()){
                     _s = _iterator.next();
                    String[] _login = _s.split("#");
                    mAccounts.add(new HistoryInfo(_login[0],_login[1]));
                    m_edtUserName.setText(_login[0]);
                    m_edtPassWord.setText(_login[1]);
                }
//                m_edtUserName.setText(_login[0]);
//                m_edtPassWord.setText(_login[1]);

            }


            if (!isFromLogOut) {
                return true;
            } else {
                return false;
            }
        } else {
            m_edtUserName.setText(sharedPreferences.getString("userName", ""));
        }
        return false;
    }

    //保存用户信息到本地
    @Override
    public void saveUserInfo2Local(boolean isSave) {
        if (isSave) {

//            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//            sharedPreferences.edit().putBoolean("isPassWordRemembered", true)
//                    .putString("userName", getUserName())
//                    .putString("passWord", getPassWord())
//                    .commit();
            Set<String> _set1 = sharedPreferences.getStringSet(KEY, new HashSet<>());
            Iterator<String> _iterator1 = _set1.iterator();
            while (_iterator1.hasNext()){
                String _next = _iterator1.next();
                LogUtils.i("next",_next);
            }
            HashSet<String> _set = new HashSet<>(_set1);
            _set.add(getUserName()+"#" + getPassWord());

           sharedPreferences.edit().putStringSet(KEY,_set)
                    .putBoolean("isPassWordRemembered", true).commit();

//            SharedPreferences sharedPreferencesUP = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//            sharedPreferencesUP.edit().putBoolean("isPassWordRemembered", true)
//                    .putStringSet("user_pass", up)
//                    .apply();

        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

            sharedPreferences.edit().putString(KEY, getUserName())
                    .putBoolean("isPassWordRemembered", false).commit();
        }
    }

    //跳转至主界面
    @Override
    public void toHomeActivity() {
        //        MyToast.showMyToast(this, "登录成功", Toast.LENGTH_SHORT);
        Intent _intent = new Intent(LoginActivity.this, DrawerActivity.class);
        //        _intent.putExtra("userName", getUserName());
        //        _intent.putExtra("password", getPassWord());
        startActivity(_intent);

        String section = UserSingleton.getUserInfo().getManagerSection();
        if (section != null && section.length() > 0) {
            m_userLoginPresenter.getRelationshipList(section);
        }
        //        finish();
    }

    @Override
    public void showLoginFailed(String msg) {
//        LogUtils.i("showLoginfailed",msg);
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoadingFinish(String msg) {
        //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void accessNetWorkError(String msg) {
//        LogUtils.i("accessNetWorkerror",msg);
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
    }

    @Override
    public void showUserNameEmptyError() {
        m_edtUserName.setError("账号不能为空");
    }

    @Override
    public void showPasswordEmptyError() {
        m_edtPassWord.setError("密码不能为空");
    }
    //    @Override
    //    protected void onStart() {
    //        super.onStart();
    //        LogUtils.i("LoginActivity----------", "onStart()");
    //    }
    //
    //    @Override1245
    //    protected void onRestart() {
    //        super.onRestart();
    //        LogUtils.i("LoginActivity----------", "onRestart()");
    //    }
    //
    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //        LogUtils.i("LoginActivity----------", "onResume()");
    //    }
    //
    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //        LogUtils.i("LoginActivity----------", "onPause()");
    //    }
    //
    //    @Override
    //    protected void onDestroy() {
    //        super.onDestroy();
    //        LogUtils.i("LoginActivity----------", "onDestroy()");
    //    }

    /**
     * 通过ping判断服务器是否可用
     *
     * @return
     */
    public boolean ping() {
        try {
            //服务器ip地址
            //            String ip = "192.168.0.21";
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ServerConfig.IP);
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            int status = p.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (IOException e) {
//            LogUtils.i(e.toString());
        } catch (InterruptedException e) {
//            LogUtils.i(e.toString());
        }
        MyToast.showMyToast(this, "请查看网络是否打开", Toast.LENGTH_SHORT);
        return false;
    }

    @Override
    public void onDismiss() {
        m_Arrow.setBackgroundResource(R.drawable.arrow_up);
    }


    @Override
    public void onItemClicked(int position) {
        mSelectWindow.dismiss();
        m_edtUserName.setText(mAccounts.get(position) .getName());
        m_edtPassWord.setText(mAccounts.get(position).getPassword());

    }

    @Override
    public void onDelBtnClicked(int position) {
        mSelectWindow.dismiss();
//        String inputAccount = m_edtUserName.getText().toString();
//
//        if (inputAccount == mAccounts.remove(position).getName())
//        {
            Set<String> _set1 = sharedPreferences.getStringSet(KEY, new HashSet<>());


            String _s = mAccounts.get(position).getName() + "#" + mAccounts.get(position).getPassword();
            Iterator<String> _iterator = _set1.iterator();
            while (_iterator.hasNext()){
                String _next = _iterator.next();
                LogUtils.i("abc",_next);

                if (_next.equals(_s)){
                    _iterator.remove();
                    mAccounts.remove(position);
                    adapter.notifyDataSetChanged();
                    sharedPreferences.edit().clear().commit();
                }
            }
            HashSet<String> _set = new HashSet<>(_set1);
            sharedPreferences.edit().putStringSet(KEY,_set)
                    .putBoolean("isPassWordRemembered", true).commit();
//            String nextAccount = "";
//            if (mAccounts.size() != 0)
//                nextAccount = mAccounts.get(0) + "";
//
//            m_edtUserName.setText(nextAccount);
//        }
        Toast.makeText(this, "删除账号成功", Toast.LENGTH_SHORT).show();


    }
}
