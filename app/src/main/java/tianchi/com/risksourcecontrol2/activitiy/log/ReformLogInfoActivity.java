package tianchi.com.risksourcecontrol2.activitiy.log;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.presenter.LoadingLogInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.view.ILoadingLogView;

public class ReformLogInfoActivity extends BaseActivity implements ILoadingLogView, View.OnClickListener {
    private TextView tvBack;//返回
    private TextView tvDate;//日期
    private EditText edtRiskType;//风险源类型
    private EditText edtStakeNum;//桩号
    private EditText edtLogID;//日志编号
    private EditText edtRiskDetails;//日志详情
    private EditText edtRecorder;//记录员
    private EditText edtAccount;//整改账户
    private EditText edtTitles;//标题
    private ImageView imgvPicture;//照片
    private Button btnPushLog;//推送
//    private byte[] pictureBytes;
    private String pictureName = "";
    private Bitmap picBitmap;
    private ProgressDialog m_progressDialog;
    private List<String> accountList;
    AlertDialog _dialog;

    private LoadingLogInfoPresenter m_logInfoPresenter = new LoadingLogInfoPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reform_log_info);
        initView();
        initValue();
    }

    @Override
    public void onBackPressed() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
        super.onBackPressed();
    }

    private void initValue() {
        ReformLogInfo _logInfo = (ReformLogInfo) getIntent().getSerializableExtra("logInfo");
        if (_logInfo != null) {
            edtLogID.setText(_logInfo.getLogId());
            edtRiskType.setText(_logInfo.getRiskType());
            edtStakeNum.setText(_logInfo.getStakeNum());
            edtAccount.setText(_logInfo.getReformAccount());
            edtTitles.setText(_logInfo.getTitle());
            edtRecorder.setText(_logInfo.getRecorder());
            tvDate.setText(DateTimeUtils.dateToString(_logInfo.getSaveTime(), "yyyy-MM-dd"));
            edtRiskDetails.setText(_logInfo.getDetails());
            pictureName = _logInfo.getPicture();
            if (pictureName != null && pictureName.length() > 0) {
                File img = new File(FoldersConfig.RISK_REFORM_PIC_PATH, pictureName);
                if (img.exists())
                    picBitmap = BitmapFactory.decodeFile(img.getPath());
//                else
//                    m_logInfoPresenter.downloadLogPicture();
            }else {
                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
            }
        }
//        accountList = UserSingleton.getAccountList();
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvDate = (TextView) findViewById(R.id.tvDate);
        edtRiskType = (EditText) findViewById(R.id.edtRiskType);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        edtLogID = (EditText) findViewById(R.id.edtLogID);
        edtRiskDetails = (EditText) findViewById(R.id.edtDetails);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtAccount = (EditText) findViewById(R.id.edtReformAccount);
        edtTitles = (EditText) findViewById(R.id.edtTitles);
        imgvPicture = (ImageView) findViewById(R.id.imgvPic);
        btnPushLog = (Button) findViewById(R.id.btnPushLog);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
        tvBack.setOnClickListener(this);
        imgvPicture.setOnClickListener(this);
        btnPushLog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.imgvPic:
                if (picBitmap == null) {
                    MyToast.showMyToast(this, "该日志无照片", Toast.LENGTH_SHORT);
                    return;
                }
                if (picBitmap != null) {
                    //打开照片查看
                    File _file = new File(CameraUtils.file, pictureName);
                    Intent intent = new Intent();
                    if (_file.exists()) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(_file), CameraUtils.IMAGE_UNSPECIFIED);
                        startActivity(intent);
                    } else {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(CameraUtils.bitMap2File(picBitmap)),
                                CameraUtils.IMAGE_UNSPECIFIED);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.btnPushLog:
                _dialog = MyAlertDialog.editTextDialog(ReformLogInfoActivity.this,
                        "日志推送", "推送到指定账号", accountList, R.mipmap.ic_push_log_32px, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _dialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                _dialog.dismiss();
                            }
                        });
                break;
        }
    }

    @Override
    public String getDownLoadURL() {
        return ServerConfig.URL_DOWNLOAD_REFORM_FILE +
                getIdLoginName() + "/" + pictureName;
    }

    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId() +
                UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public String getPictureName() {
        if (pictureName != null && pictureName.length() > 0)
            return pictureName;
        return "";
    }

    @Override
    public void showLoadingPiture(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage("加载图片中...");
            m_progressDialog.show();
        }
    }

    @Override
    public void hideLoadingPicture() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
    }

    @Override
    public void showLoadingSucceed(Bitmap bitmap) {
        if (bitmap != null)
            picBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(ReformLogInfoActivity.this, "初始化图片成功", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void showLoadingFailed(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                MyToast.showMyToast(ReformLogInfoActivity.this, "初始化图片失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void showSubmitSucceedOrFailed(String msg) {

    }

    @Override
    public Map<String, Object> getModifyLogMap() {
        return null;
    }
}
