package tianchi.com.risksourcecontrol2.activitiy.log;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.presenter.NewReformLogPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.view.INewLogView;

/**
 * @描述 新建整改日志
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:09.
 */
public class NewReformActivity extends BaseActivity implements MyTakePicDialog.OnItemClickListener, INewLogView, View.OnClickListener, View.OnLongClickListener {

    private TextView tvBack;//返回
    private TextView tvDate;//日期
    private EditText edtLogID;//日志编号
    private EditText edtRiskID;//日志编号
    private EditText edtReformAccount;//整改账号
    private EditText edtRiskDetails;//日志详情
    private EditText edtTitle;//标题
    private EditText edtRecorder;//记录员
    private EditText edtBuildCharger;//记录员
    private EditText edtStakeNum;//桩号
    private Spinner spSection;//标段
    private Spinner spRiskType;//风险源类型
    private Button btnAddPic;//添加照片
    private Button btnSubmit;//提交
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//提交进度

    //整改日志控制器
    NewReformLogPresenter m_reformLogPresenter = new NewReformLogPresenter(this);

    //    private byte[] picBytes;//存放照片字节数组
    //    private Bitmap picBitmapForUser;//存放照片位图（供用户打开查看）
    //    private String picBase64String;//存放照片字符串
    private String pictureName = "";//存放照片字符串
    private Bitmap picBitmap;//存放照片位图
    private File resultImgFile;//存放照片位图
    private Uri fileUri;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reform);
        initView();
        initValues();
    }

    private void initValues() {
        setSaveTime();
        setRecorder();
        setLogId();
        setSection();
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvDate = (TextView) findViewById(R.id.tvDate);
        edtLogID = (EditText) findViewById(R.id.edtLogID);
        edtRiskID = (EditText) findViewById(R.id.edtRiskID);
        edtReformAccount = (EditText) findViewById(R.id.edtReformAccount);
        edtRiskDetails = (EditText) findViewById(R.id.edtRiskDetails);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtBuildCharger = (EditText) findViewById(R.id.edtLeader);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        spSection = (Spinner) findViewById(R.id.spSection);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        btnAddPic = (Button) findViewById(R.id.btnAddPic);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        tvBack.setOnClickListener(this);
        btnAddPic.setOnClickListener(this);
        btnAddPic.setOnLongClickListener(this);
        btnSubmit.setOnClickListener(this);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("提交日志中...");
        m_progressDialog.setCancelable(true);
    }

    @Override
    public void onBackPressed() {
        saveLogRemind();
    }

    private void saveLogRemind() {
        MyAlertDialog.showAlertDialog(this,
                "温馨提示", "数据将丢失，点击确定提交？", R.mipmap.ic_question,
                "确定", "直接退出", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkInput() == true) {
                            m_reformLogPresenter.submit();
                            finish();
                        } else {
                            MyToast.showMyToast(NewReformActivity.this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    //检查每项必须有内容
    private boolean checkInput() {
        boolean isOk = true;
        if (getLogID().length() == 0)
            isOk = false;
        if (getRiskID().length() == 0)
            isOk = false;
        if (getRiskType().length() == 0)
            isOk = false;
        if (getSection().length() == 0)
            isOk = false;
        if (getStakeNum().length() == 0)
            isOk = false;
        if (getReformAccount().length() == 0)
            isOk = false;
        if (getTitles().length() == 0)
            isOk = false;
        if (getRecorder().length() == 0)
            isOk = false;
        if (getLeader().length() == 0)
            isOk = false;
        if (getDetails().length() == 0)
            isOk = false;
        if (picBitmap == null)
            isOk = false;
        return isOk;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                saveLogRemind();
                break;
            case R.id.btnAddPic:
                MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                _takePicDialog.setOnItemClickListener(NewReformActivity.this);
                m_dialog = _takePicDialog.showTakePicDialog(NewReformActivity.this);
                m_dialog.show();
                break;
            case R.id.btnSubmit:
                if (checkInput()) {
                    m_reformLogPresenter.submit();
                } else {
                    MyToast.showMyToast(this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public String getLogID() {
        return edtLogID.getText().toString().trim();
    }


    @Override
    public String getStakeNum() {
        return edtStakeNum.getText().toString();
    }

    @Override
    public String getRiskID() {
        return edtRiskID.getText().toString();
    }

    @Override
    public String getAccount() {
        if (UserSingleton.getUserInfo().getLoginName() != null)
            return UserSingleton.getUserInfo().getLoginName();
        return "";
    }

    @Override
    public String getReformAccount() {
        return edtReformAccount.getText().toString();
    }

    @Override
    public String getWeather() {
        return null;//无用
    }

    @Override
    public void setSection() {
        if (UserSingleton.getSectionList() != null) {
            spSection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                    UserSingleton.getSectionList()));
        }
    }

    @Override
    public String getSection() {
        if (spSection.getSelectedItem() != null)
            return spSection.getSelectedItem().toString();
        return "";
    }

    @Override
    public String getEmergency() {
        return null;//无用
    }

    @Override
    public String getLeader() {
        return edtBuildCharger.getText().toString();
    }

    @Override
    public void setRecorder() {
        if (UserSingleton.getUserInfo().getRealName() != null)
            edtRecorder.setText(UserSingleton.getUserInfo().getRealName());
    }

    @Override
    public String getRecorder() {
        return edtRecorder.getText().toString();
    }

    @Override
    public String getRiskType() {
        String riskType = spRiskType.getSelectedItem().toString();
        if (riskType != null && riskType.length() > 0) {
            return riskType.substring(2, riskType.length());
        }
        return "";
    }

    @Override
    public void setAccount() {
        if (UserSingleton.getUserInfo().getLoginName() != null)
            edtRecorder.setText(UserSingleton.getUserInfo().getLoginName());
    }

    @Override
    public void setSaveTime() {
        tvDate.setText(DateTimeUtils.setCurrentTime());
    }

    @Override
    public void setProjectRole() {

    }

    @Override
    public String getProjectRole() {
        return null;
    }

    @Override
    public Date getSaveTime() {
        try {
            return DateTimeUtils.longToDate(System.currentTimeMillis(), DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDetails() {
        return edtRiskDetails.getText().toString();
    }

    @Override
    public String getTitles() {
        return edtTitle.getText().toString();
    }

    @Override
    public String getMDetails() {
        return null;//无用
    }

    @Override
    public String getTDetails() {
        return null;//无用
    }

    @Override
    public String getPicture() {
        if (pictureName.length() > 0) {
            return pictureName;
        }
        return "";
    }

    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public File getUploadFile(int position) {
        if (resultImgFile != null)
            return resultImgFile;
        return null;
    }


    @Override
    public void showInSubmiting(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage(msg);
            m_progressDialog.show();
        }
    }

    @Override
    public void hideInSubmiting() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
    }

    //    @Override
    //    public byte[] getPicture() {
    //        if (picBytes != null)
    //            return picBytes;
    //        return new byte[0];
    //    }

    @Override
    public void showSubmitSucceed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideInSubmiting();
            }
        });
        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void showSubmitFailed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideInSubmiting();
            }
        });
        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void uploadFileSucceed(String msg) {
        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                hideInSubmiting();
                btnAddPic.setBackground(new BitmapDrawable(picBitmap));
            }
        });
    }

    @Override
    public void uploadFileFailed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideInSubmiting();
            }
        });
        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        picBitmap = null;
    }

    @Override
    public void setLogId() {
        String tempDate = DateTimeUtils.dateToString(new Date(), "yyyyMMddHHmmss");
        int tempNum = Integer.parseInt(tempDate.substring(8, tempDate.length() - 1));
        int rand = new Random().nextInt(tempNum) / 8;
        edtLogID.setText("R_" + UserSingleton.getUserInfo().getUserId() + "_" +
                DateTimeUtils.dateToString(new Date(), "yyyyMMdd") + rand);
    }

    /**
     * @return 获取整改时间
     */
    @Override
    public String getExpireTime() {
        return null;
    }

    /**
     * @return 获取风险源编号
     */
    @Override
    public String getRiskIndex() {
        return null;
    }

    //    @Override
    //    public void initLoadingDataSucceed(List<String> list) {
    //        if (list.size()>0)
    //            spSection.setAdapter(new ArrayAdapter<String>
    //                    (this, android.R.layout.simple_list_item_1, list));
    //    }
    //
    //    @Override
    //    public void initLoadingDataFailed(String msg) {
    //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
    //    }

    @Override
    public void setOnItemClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakePicture:
                fileUri = Uri.fromFile(new File(CameraUtils.file, System.currentTimeMillis() + ".jpg"));
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnPickFromAlbum:
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CameraUtils.IMAGE_UNSPECIFIED);
                startActivityForResult(i, CameraUtils.PHOTO_REQUEST_GALLERY);
                break;
            case R.id.btnViewPicture:
                // 通过picBytes判断当前是否已经拍摄了照片或选择了照片
                if (picBitmap == null) {
                    MyToast.showMyToast(this, "还未拍摄或未选择!", Toast.LENGTH_SHORT);
                    return;
                }
                if (resultImgFile != null && resultImgFile.exists()) {
                    //打开照片查看
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(resultImgFile), CameraUtils.IMAGE_UNSPECIFIED);
                    startActivity(intent);
                }
                break;
            case R.id.btnCancel:
                if (m_dialog != null)
                    m_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        Bitmap   _picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                         picBitmap = CameraUtils.comp(_picBitmap);
                        if (picBitmap != null) {
                            resultImgFile = new File(new URI(uri.toString()));
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureName = resultImgFile.getName();
//                                    m_reformLogPresenter.uploadFile();
                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;

                case CameraUtils.PHOTO_REQUEST_GALLERY:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};//取媒体文件路径集合
                        Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);//取选中图片的cursor
                        c.moveToFirst();//移动游标
                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
                        String imgpath = c.getString(colindex);//取文件相对手机路径
                        c.close();
                        resultImgFile = new File(imgpath);
//                        picBitmap = BitmapFactory.decodeFile(imgpath);
                        picBitmap = CameraUtils.getimage(imgpath);
                        if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                            boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                            if (_overLimit) {
                                MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                            } else {
                                pictureName = resultImgFile.getName();
//                                m_reformLogPresenter.uploadFile();
                            }
                        } else {
                            MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            MyToast.showMyToast(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        }
        if (m_dialog != null)
            m_dialog.dismiss();
    }

    @Override
    public boolean onLongClick(View v) {
        MyAlertDialog.showAlertDialog(NewReformActivity.this, "删除提示",
                "确定清除已添加的照片？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (resultImgFile != null) {
                            resultImgFile = null;
                            pictureName = "";
                            picBitmap = null;
                            btnAddPic.setBackgroundResource(R.mipmap.ic_add);
                        } else {
                            MyToast.showMyToast(NewReformActivity.this, "您还没有添加照片", Toast.LENGTH_SHORT);
                        }
                    }
                });
        return false;
    }
}
