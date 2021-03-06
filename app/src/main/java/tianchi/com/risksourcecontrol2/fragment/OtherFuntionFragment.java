package tianchi.com.risksourcecontrol2.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.LoginActivity;
import tianchi.com.risksourcecontrol2.activitiy.mine.AboutActivity;
import tianchi.com.risksourcecontrol2.activitiy.mine.ConfigurationActivity;
import tianchi.com.risksourcecontrol2.activitiy.mine.DevelopmentInfoActivity;
import tianchi.com.risksourcecontrol2.activitiy.mine.MinePageActivity;
import tianchi.com.risksourcecontrol2.activitiy.mine.QRCodeActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserProfileActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * Created by Kevin on 2018-08-29.
 */

public class OtherFuntionFragment extends Fragment implements View.OnClickListener {
    private ImageView imgvUserProfileHead;
    private TextView tvUserName;
    private AlertDialog m_dialog;
    private ProgressDialog m_progressDialog;
    private Bitmap picBitmap;
    private RoundedBitmapDrawable m_roundedBitmapDrawable;
    private AlertDialog takePicDialog;
    private Uri fileUri;//拍照时传入的uri
    private Uri uri;//系统返回的uri
    private String downloadURL;
    private String pictureName;
    //    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    final MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private int m_serveVersionCode;
    private String m_updateMsg;
    private String m_serveVersionName;
    private String m_localVersionName;
    private int m_localVersionCode;
    public boolean isOk = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUserName:
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                break;
            case R.id.linearUserProfile:
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
                break;
            case R.id.linearSystemConfigSetting:
                startActivity(new Intent(getActivity(), ConfigurationActivity.class));
                break;
            case R.id.linearDevelopmentInfo:
                startActivity(new Intent(getActivity(), DevelopmentInfoActivity.class));
                break;
            case R.id.linearUpdateOnline:
                Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearAboutApp:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.linearQRcode:
                startActivity(new Intent(getActivity(), QRCodeActivity.class));
                break;
            case R.id.linearLogOut:
                m_dialog = MyAlertDialog.showAlertDialog(getActivity(), "提示", "" +
                        "退出当前账号？", R.mipmap.ic_question, "确定", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UsersList.clearList();
                        Intent intent = new Intent().setClass(getActivity(), LoginActivity.class);
                        intent.putExtra("isFromLogOut", true);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_dialog != null)
                            m_dialog.dismiss();
                    }
                });
                break;
            case R.id.upData:
                if (ping()) {
                    checkVersionCode();//测试版本号
                }
                break;
            default:break;
        }
    }

    private void checkVersionCode() {
        OkHttpUtils.getAsync(ServerConfig.URL_GET_VERSION_CODE, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                m_serveVersionCode = 0;
                MyToast.showMyToast(getActivity(), "获取版本失败", Toast.LENGTH_SHORT);
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
        MyToast.showMyToast(getActivity(), "请查看网络是否打开", Toast.LENGTH_SHORT);
        return false;
    }
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());
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
    private void downLoadApk() {

        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(getActivity());
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
    private void installApk(File file) {

        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);


    }
    //下载文件接口
    public interface DownLoadFileListener {
        void downloadSucceed(Bitmap bitmap);

        void downloadFailed(String errorMsg);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.activity_mine_page, null);
        imgvUserProfileHead = (ImageView) _view.findViewById(R.id.imgvUserProfileHead);
        imgvUserProfileHead.setOnClickListener(this);
        tvUserName = (TextView) _view.findViewById(R.id.tvUserName);
        m_progressDialog = new ProgressDialog(getActivity());
        m_progressDialog.setMessage("头像初始化...");
        _view.findViewById(R.id.tvUserName).setOnClickListener(this);
        _view.findViewById(R.id.imgvUserProfileHead).setOnClickListener(this);
        _view.findViewById(R.id.linearUserProfile).setOnClickListener(this);
        _view.findViewById(R.id.linearSystemConfigSetting).setOnClickListener(this);
        _view.findViewById(R.id.linearDevelopmentInfo).setOnClickListener(this);
        _view.findViewById(R.id.linearUpdateOnline).setOnClickListener(this);
        _view.findViewById(R.id.linearAboutApp).setOnClickListener(this);
        _view.findViewById(R.id.linearQRcode).setOnClickListener(this);
        _view.findViewById(R.id.linearLogOut).setOnClickListener(this);
        _view.findViewById(R.id.upData).setOnClickListener(this);
        initValue();
        return _view;
    }

    private void initValue() {
        String userRealName = UserSingleton.getUserInfo().getRealName();
        pictureName = UserSingleton.getUserInfo().getPicture();
        if (userRealName != null && userRealName.length() > 0)
            tvUserName.setText(UserSingleton.getUserInfo().getRealName());
        if (pictureName != null && pictureName.length() > 0) {
            //            picBitmap = CameraUtils.base64ToBitmap(userHeadPic);
            File file = new File(FoldersConfig.USER_HEAD_PATH, pictureName);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    picBitmap = BitmapFactory.decodeStream(fis);
                    setRoundedBitmapDrawable(picBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                if (m_progressDialog != null)
                    m_progressDialog.show();
                downLoadFile2Server(ServerConfig.URL_DOWNLOAD_USER_FILE +
                        UserSingleton.getUserInfo().getUserId() +
                        UserSingleton.getUserInfo().getLoginName() +
                        "/" + pictureName, new MinePageActivity.DownLoadFileListener() {
                    @Override
                    public void downloadSucceed(Bitmap bitmap) {
                        if (bitmap != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setRoundedBitmapDrawable(bitmap);
                                    if (m_progressDialog != null)
                                        m_progressDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void downloadFailed(String errorMsg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showMyToast(getActivity(), "下载头像失败_:" + errorMsg, Toast.LENGTH_SHORT);
                                if (m_progressDialog != null)
                                    m_progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    private void setRoundedBitmapDrawable(Bitmap src) {
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()) {
            dst = Bitmap.createBitmap(src, src.getWidth() / 2 - src.getHeight() / 2, 0, src.getHeight(), src.getHeight());
        } else {
            dst = Bitmap.createBitmap(src, 0, src.getHeight() / 2 - src.getWidth() / 2, src.getWidth(), src.getWidth());
        }
        m_roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), dst);
        m_roundedBitmapDrawable.setCornerRadius(dst.getWidth() / 2);
        m_roundedBitmapDrawable.setAntiAlias(true);
        imgvUserProfileHead.setImageDrawable(m_roundedBitmapDrawable);
    }

    private void downLoadFile2Server(String url, MinePageActivity.DownLoadFileListener listener) {
        downloadURL = url;
        new Thread() {
            @Override
            public void run() {
                //TODOAuto-generatedmethodstub
                try {
                    //创建一个url对象
                    URL url = new URL(downloadURL);
                    //打开URL对应的资源输入流
                    InputStream is = url.openStream();
                    //把InputStream转化成ByteArrayOutputStream
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    is.close();//关闭输入流
                    //将ByteArrayOutputStream转化成InputStream
                    is = new ByteArrayInputStream(baos.toByteArray());
                    //将InputStream解析成Bitmap
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    //再次将ByteArrayOutputStream转化成InputStream
                    is = new ByteArrayInputStream(baos.toByteArray());
                    baos.close();
                    //打开手机文件对应的输出流
                    File myCaptureFile = new File(FoldersConfig.USER_HEAD_PATH, UserSingleton.getUserInfo().getPicture());
                    if (!myCaptureFile.exists()) {
                        myCaptureFile.createNewFile();
                    }
                    FileOutputStream os = new FileOutputStream(myCaptureFile);
                    byte[] buff = new byte[1024];
                    int count = 0;
                    //将URL对应的资源下载到本地
                    while ((count = is.read(buff)) > 0) {
                        os.write(buff, 0, count);
                    }
                    os.flush();
                    //关闭输入输出流
                    is.close();
                    os.close();
//                    LogUtils.i("inputstringsuccess!");
                    if (bmp != null) {
                        if (listener != null) {
                            listener.downloadSucceed(bmp);
                        }
                    } else {
                        if (listener != null) {
                            listener.downloadFailed("解析头像失败");
                        }
                    }
                } catch (Exception e) {
                    //TODOAuto-generatedcatchblock
                    e.printStackTrace();
                    listener.downloadFailed(e.getClass().getName() + " error detail:" + e.getMessage());
                }
            }
        }.start();
    }
}
