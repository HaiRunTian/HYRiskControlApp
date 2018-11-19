package tianchi.com.risksourcecontrol2.activitiy.mine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.LoginActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserProfileActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.LogUtils;

public class MinePageActivity extends BaseActivity implements View.OnClickListener {
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

    //下载文件接口
    public interface DownLoadFileListener {
        void downloadSucceed(Bitmap bitmap);
        void downloadFailed(String errorMsg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_page);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initValue();
    }

    @Override
    public void onBackPressed() {
        slideTheWindow2Top();
    }

    void slideTheWindow2Top() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
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
                        "/" + pictureName, new DownLoadFileListener() {
                    @Override
                    public void downloadSucceed(Bitmap bitmap) {
                        if (bitmap != null) {
                            runOnUiThread(new Runnable() {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.showMyToast(MinePageActivity.this, "下载头像失败_:" + errorMsg, Toast.LENGTH_SHORT);
                                if (m_progressDialog != null)
                                    m_progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    private void initView() {
        imgvUserProfileHead = (ImageView) findViewById(R.id.imgvUserProfileHead);
        imgvUserProfileHead.setOnClickListener(this);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("头像初始化...");
        findViewById(R.id.tvUserName).setOnClickListener(this);
        findViewById(R.id.imgvUserProfileHead).setOnClickListener(this);
        findViewById(R.id.linearUserProfile).setOnClickListener(this);
        findViewById(R.id.linearSystemConfigSetting).setOnClickListener(this);
        findViewById(R.id.linearDevelopmentInfo).setOnClickListener(this);
        findViewById(R.id.linearUpdateOnline).setOnClickListener(this);
        findViewById(R.id.linearAboutApp).setOnClickListener(this);
        findViewById(R.id.linearQRcode).setOnClickListener(this);
        findViewById(R.id.linearLogOut).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUserName:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.linearUserProfile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.linearSystemConfigSetting:
                startActivity(new Intent(this, ConfigurationActivity.class));
                break;
            case R.id.linearDevelopmentInfo:
                startActivity(new Intent(this, DevelopmentInfoActivity.class));
                break;
            case R.id.linearUpdateOnline:
                Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearAboutApp:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.linearQRcode:
                startActivity(new Intent(this, QRCodeActivity.class));
                break;
            case R.id.linearLogOut:
                m_dialog = MyAlertDialog.showAlertDialog(this, "提示", "" +
                        "退出当前账号？", R.mipmap.ic_question, "确定", "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UsersList.clearList();
                        Intent intent = new Intent().setClass(MinePageActivity.this, LoginActivity.class);
                        intent.putExtra("isFromLogOut", true);
                        startActivity(intent);
                        finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (m_dialog != null)
                            m_dialog.dismiss();
                    }
                });
                break;
        }
    }

    private void downLoadFile2Server(String url, DownLoadFileListener listener) {
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
                    LogUtils.i("inputstringsuccess!");
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
