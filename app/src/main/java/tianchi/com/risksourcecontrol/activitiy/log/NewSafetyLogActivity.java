package tianchi.com.risksourcecontrol.activitiy.log;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyDatePicker;
import tianchi.com.risksourcecontrol.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.presenter.NewSafetyLogPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.CameraUtils;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.util.FileUtils;
import tianchi.com.risksourcecontrol.view.INewLogView;
import tianchi.com.risksourcecontrol.work.UserLoginWork;

/**
 * @描述 新建安全日志
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:09.
 */
public class NewSafetyLogActivity extends BaseActivity implements MyTakePicDialog.OnItemClickListener, INewLogView, View.OnClickListener, View.OnLongClickListener {

    private TextView tvBack;//返回
    //    private TextView tvDate;//日期
    private Spinner spRiskType;//风险源类型
    private EditText edtStakeNum;//桩号
    private EditText edtProjectRole;//提交方（业主，监理，施工）
    private Spinner spSection;//标段
    private Spinner spWeather;//天气
//    private EditText edtLogID;//日志编号
    private EditText edtProSafetyDetails;//日志详情
    private EditText edtEmergency;//突发事件
    private EditText edtLeader;//施工单位负责人
    private EditText edtRecorder;//记录员
    private Button btnAddPic;//添加照片
    private Button btnSubmit;//提交
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;
    private EditText edtTDetails;//建议
//    private EditText m_edtRiskIndex;

    //    private ArrayAdapter<String> adapter;
    //    private List<String> m_list = new ArrayList<String>();

    //拍照区域代码
    private GridView m_gridView;
    //    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器

    private NewSafetyLogPresenter m_safetyLogPresenter = new NewSafetyLogPresenter(this);//新建安全日志控制器
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件
    private Bitmap picBitmap;//存储用户修改头像
    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName = "";//照片全名xx.jpg

    private boolean canUpload = true;
    private EditText edtDeadlineTime; //整改期限
    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            uploadPictures();
            return false;
        }
    });

    private void uploadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(i).getName().equals(picNames.get(i))) {
                if (canUpload) {
                    canUpload = false;
                    m_safetyLogPresenter.uploadFile(picFiles.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
                break;
            }
        }
    }
    //    private byte[] picBytes;//存放照片字节数组
    //    private Bitmap picBitmapForUser;//存放照片位图（供用户打开查看）
    //    private String picBase64String = "";//存放照片字符串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_safety);
        initView();
        initValue();
    }


    private void initValue() {

//        setRiskIndex();
        //        setSaveTime();
        setRecorder();
//        setLogId();
        setSection();
        setProjectRole();
        initTakePicArea();
//        tempValueInit();
        //        m_safetyLogPresenter.loadSectionList();//加载标段
    }

//    private void setRiskIndex() {
//        String riskIndex = getIntent().getStringExtra("riskIndex");
//        if (riskIndex != null){
//            m_edtRiskIndex.setText(riskIndex);
//        }
//    }

    private void tempValueInit() {
        edtStakeNum.setText("1");
        edtEmergency.setText("1");
        edtLeader.setText("1");
        edtProSafetyDetails.setText("1");
    }

    //***新增*****
    //初始化拍照区域
    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPicture(position);
            }
        });
        m_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyAlertDialog.showAlertDialog(NewSafetyLogActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageItem.remove(position);
                                picNames.remove(position);
                                picFiles.remove(position);
                                refreshGridviewAdapter();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                return true;
            }
        });
    }

    //查看图片
    private void viewPicture(int position) {
        if (picFiles.get(position) != null) {
            //打开照片查看
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(picFiles.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
            startActivity(intent);
        }
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tvBack);
        //        tvDate = (TextView) findViewById(R.id.tvDate);
//        edtLogID = (EditText) findViewById(R.id.edtLogID);
        edtProSafetyDetails = (EditText) findViewById(R.id.edtProSafetyDetails);
        edtEmergency = (EditText) findViewById(R.id.edtEmergency);
        edtLeader = (EditText) findViewById(R.id.edtLeader);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtProjectRole = (EditText) findViewById(R.id.edtProjectRole);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        spSection = (Spinner) findViewById(R.id.spSection);
        spWeather = (Spinner) findViewById(R.id.spWeather);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        btnAddPic = (Button) findViewById(R.id.btnAddPic);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        m_gridView = (GridView) findViewById(R.id.gridView1);

        edtTDetails = (EditText) findViewById(R.id.edttDetails);

        edtDeadlineTime = $(R.id.edtLogRectifyDate);


        edtDeadlineTime.setOnClickListener(this);

//        m_edtRiskIndex = $(R.id.edtLogRiskIndex);
        tvBack.setOnClickListener(this);
        btnAddPic.setOnClickListener(this);
        //        btnAddPic.setOnLongClickListener(this);
        btnSubmit.setOnClickListener(this);
        edtProSafetyDetails.setFilters(new InputFilter[]{m_filter});
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("日志提交中...");
        m_progressDialog.setCancelable(true);
    }

    @Override
    public void onBackPressed() {
        saveLogRemind();
    }

    private InputFilter m_filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (edtProSafetyDetails.getText().length() > 160)
                return "";
            else
                return null;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                saveLogRemind();
                break;

            case R.id.btnAddPic:
                if (imageItem.size() == 5) {
                    MyToast.showMyToast(NewSafetyLogActivity.this, "最多支持上传5张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }
                break;
            case R.id.btnSubmit:
                if (checkInput()) {
                    uploadFirstPicture();
                    //                    m_safetyLogPresenter.submit();
                } else {
                    MyToast.showMyToast(this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
                }
                break;

            case R.id.edtLogRectifyDate:
                MyDatePicker.ShowDatePicker(this, edtDeadlineTime);

                break;

            default:break;


        }
    }

    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            if (canUpload) {
                m_safetyLogPresenter.uploadFile(picFiles.size(), 0);
                picNames.set(0, "");
                canUpload = false;
            }
        } else {
            MyToast.showMyToast(this, "请检查要上传的图片是否已被手动删除！", Toast.LENGTH_SHORT);
        }
    }

    private void saveLogRemind() {
        MyAlertDialog.showAlertDialog(this,
                "温馨提示", "数据将丢失，点击确定提交？", R.mipmap.ic_question,
                "确定", "直接退出", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkInput() == true) {
                            m_safetyLogPresenter.submit();
                            finish();
                        } else {
                            MyToast.showMyToast(NewSafetyLogActivity.this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
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
//        if (getLogID().length() == 0) {
//            edtLogID.setError("日志编号不能为空");
//            isOk = false;
//        }
        if (getSection().length() == 0) {
            spSection.setPrompt("标段不能为空");
            isOk = false;
        }
        if (getRiskType().length() == 0) {
            spRiskType.setPrompt("请选择风险源类型");
            isOk = false;
        }
        if (getStakeNum().length() == 0) {
            edtStakeNum.setError("桩号不能为空");
            isOk = false;
        }
        if (getWeather().length() == 0) {
            spWeather.setPrompt("请选择天气");
            isOk = false;
        }
        if (getEmergency().length() == 0) {
            edtEmergency.setError("安全隐患不能为空");
            isOk = false;
        }
        if (getRecorder().length() == 0) {
            edtRecorder.setError("安全员不能为空");
            isOk = false;
        }
        if (getLeader().length() == 0) {
            edtLeader.setError("施工单位负责人不能为空");
            isOk = false;
        }
        if (getDetails().length() == 0) {
            edtProSafetyDetails.setError("日志详情不能为空");
            isOk = false;
        }
        if (getPicture().length() == 0) {
            Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
            isOk = false;
        }
        return isOk;
    }

    @Override
    public String getLogID() {
        return " ";
    }

    @Override
    public String getStakeNum() {
        return edtStakeNum.getText().toString();
    }

    @Override
    public String getRiskID() {
        return null;//无用
    }

    @Override
    public String getAccount() {
        return UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public String getReformAccount() {
        return null;//无用
    }

    @Override
    public String getWeather() {
        if (spWeather.getSelectedItem() != null)
            return spWeather.getSelectedItem().toString();
        return "";
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
        return edtEmergency.getText().toString();
    }

    @Override
    public String getLeader() {
        return edtLeader.getText().toString();
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
        //不用
    }

    @Override
    public void setSaveTime() {
        //        tvDate.setText(DateTimeUtils.setCurrentTime());
    }

    @Override
    public void setProjectRole() {
        String submitter = UserLoginWork.getSubmitter(UserSingleton.getUserInfo().getRoleId());
        edtProjectRole.setText(submitter);
    }

    @Override
    public String getProjectRole() {
        return UserLoginWork.getSubmitter(UserSingleton.getUserInfo().getRoleId());
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
        return edtProSafetyDetails.getText().toString();
    }

    @Override
    public String getTitles() {
        return null;//无用
    }

    @Override
    public String getMDetails() {
        return null;//不用
    }

    @Override
    public String getTDetails() {
        return edtTDetails.getText().toString();
    }

    @Override
    public String getPicture() {
        String jointPictureName = "";
        if (picFiles.size() == 0) {
            jointPictureName = "";
        } else if (picFiles.size() == 1) {
            jointPictureName = picFiles.get(0).getName();
        } else {
            for (File _picFile : picFiles) {
                jointPictureName += _picFile.getName() + "#";
            }
            jointPictureName = jointPictureName.substring(0, jointPictureName.length() - 1);
        }
        return jointPictureName;
    }

    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public File getUploadFile(int position) {
        //        if (resultImgFile != null)
        //            return resultImgFile;
        //        return null;
        if (picFiles.size() > 0) {
            return picFiles.get(position);
        }
        return null;
    }

    @Override
    public void showInSubmiting(String msg) {
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
    public void hideInSubmiting() {
        if (m_progressDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_progressDialog.dismiss();
                }
            });
        }
    }

    //    @Override
    //    public byte[] getPicture() {
    //        if (picBytes != null)
    //            return picBytes;
    //        return new byte[0];
    //    }

    @Override
    public void showSubmitSucceed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        finish();
    }

    @Override
    public void showSubmitFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void uploadFileSucceed(String msg) {
        //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        canUpload = true;//可上传标记
        boolean isFinish = false;//标记整个上传过程是否完成
        for (String _picName : picNames) {
            if (!_picName.equals("")) {
                isFinish = false;
            } else {
                isFinish = true;
            }
        }
        if (!isFinish) {//上传未完成，继续发送下载指令
            Message _message = new Message();
            m_handler.sendMessageDelayed(_message, 500);
        } else {//下载完成，提交日志
            m_safetyLogPresenter.submit();
        }
        //        resetParams();
    }

    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        resetParams();
    }

    /**
     * 重置所有图片对象
     *
     * @datetime 2018/3/14  14:43.
     */
    public void resetParams() {
        picBitmap = null;
        resultImgFile = null;
        pictureName = "";
        fileUri = null;
        uri = null;
    }

    @Override
    public void setLogId() {
        String tempDate = DateTimeUtils.dateToString(new Date(), "yyyyMMddHHmmss");
//        int tempNum = Integer.parseInt(tempDate.substring(8, tempDate.length() - 1));
//        int rand = new Random().nextInt(tempNum) / 8;
//        edtLogID.setText("S_" + UserSingleton.getUserInfo().getUserId() + "_" + tempDate.substring(2));
    }

    /**
     * @return 获取整改时间
     */
    @Override
    public String getExpireTime() {
        return edtDeadlineTime.getText().toString()+" 00:00:00";
    }

    /**
     * @return 获取风险源编号
     */
    @Override
    public String getRiskIndex() {
        return null;
    }

    //    @Override
    //    public void initLoadingData(List<String> list) {//初始化标段成功
    //        if (list.size()>0)
    //            spSection.setAdapter(new ArrayAdapter<String>
    //                    (this, android.R.layout.simple_list_item_1, list));
    //    }
    //
    //    @Override
    //    public void initLoadingDataFailed(String msg) {
    //        MyToast.showMyToast(this,msg,Toast.LENGTH_SHORT);
    //    }

    @Override
    public void setOnItemClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnTakePicture:
                takPicFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, System.currentTimeMillis() + ".jpg");
                fileUri = Uri.fromFile(takPicFile);
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnPickFromAlbum:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CameraUtils.IMAGE_UNSPECIFIED);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_GALLERY);
                break;
            //            case R.id.btnViewPicture:
            //                // 通过picBytes判断当前是否已经拍摄了照片或选择了照片
            //                if (picBitmap == null) {
            //                    MyToast.showMyToast(this, "还未拍摄或未选择!", Toast.LENGTH_SHORT);
            //                    return;
            //                }
            //                if (resultImgFile != null && resultImgFile.exists()) {
            //                    //打开照片查看
            //                    intent = new Intent();
            //                    intent.setAction(Intent.ACTION_VIEW);
            //                    intent.setDataAndType(Uri.fromFile(resultImgFile), CameraUtils.IMAGE_UNSPECIFIED);
            //                    startActivity(intent);
            //                    if (m_dialog != null)
            //                        m_dialog.dismiss();
            //                }
            //                break;
            case R.id.btnCancel:
                if (m_dialog != null)
                    m_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /*拍照、选取相册结果返回*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {  //拍照
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//拍摄返回的bitmap
                        if (picBitmap != null) {
                            resultImgFile = new File(new URI(uri.toString()));//拍摄返回的图片file
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureName = resultImgFile.getName();//拍摄返回的图片name
                                    picNames.add(pictureName);
                                    picFiles.add(resultImgFile);
                                    HashMap<String, Object> _map = new HashMap<>();
                                    _map.put("itemImage", picBitmap);
                                    imageItem.add(_map);
                                    refreshGridviewAdapter();
                                    //                                    m_safetyLogPresenter.uploadFile();
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
                        c.moveToFirst();
                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
                        String imgpath = c.getString(colindex);//取文件相对手机路径
                        c.close();
                        picBitmap = BitmapFactory.decodeFile(imgpath);//拍摄返回的bitmap
                        if (picBitmap != null) {
                            resultImgFile = new File(imgpath);//拍摄返回的图片file
                            if (picNames.contains(resultImgFile.getName())) {
                                MyToast.showMyToast(this, "请勿上传重复图片", Toast.LENGTH_SHORT);
                                return;
                            }
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureName = resultImgFile.getName();//拍摄返回的图片name
                                    picNames.add(pictureName);
                                    picFiles.add(resultImgFile);
                                    HashMap<String, Object> _map = new HashMap<>();
                                    _map.put("itemImage", picBitmap);
                                    imageItem.add(_map);
                                    refreshGridviewAdapter();
                                    //                                    m_safetyLogPresenter.uploadFile();
                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            MyToast.showMyToast(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
        }
        if (m_dialog != null)
            m_dialog.dismiss();
        //        if (requestCode == CameraUtils.PHOTO_REQUEST_CUT) {
        //            picBitmap = CameraUtils.getBitmapFromCG(this, requestCode, resultCode, data, fileUri, 400, 300);
        //            if (picBitmap != null) {
        //                picBitmapForUser = picBitmap;
        //                picBytes = CameraUtils.Bitmap2Bytes(picBitmap);
        //                picBase64String = CameraUtils.bitmap2StrByBase64(picBitmap);
        //                btnAddPic.setBackground(new BitmapDrawable(picBitmap));
        //                if (m_dialog != null)
        //                    m_dialog.dismiss();
        //            }
        //        } else {
        //            CameraUtils.getBitmapFromCG(this, requestCode, resultCode, data, fileUri, 400, 300);
        //        }
    }

    //刷新图片区域gridview
    private void refreshGridviewAdapter() {
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic, new String[]{"itemImage"}, new int[]{R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {//绑定视图
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                        }
                    });
                    return true;
                }
                return false;
            }
        });
        runOnUiThread(new Runnable() {//主线程绑定adapter刷新数据
            @Override
            public void run() {
                m_gridView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        MyAlertDialog.showAlertDialog(this, "删除提示",
                "确定清除已添加的照片？", "确定", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //                        if (picBitmap != null) {
                        //                            //                            picBytes = null;
                        //                            //                            picBitmapForUser = null;
                        //                            picBitmap = null;
                        //                            btnAddPic.setBackgroundResource(R.mipmap.ic_add);
                        //                        } else {
                        //                            MyToast.showMyToast(NewSafetyLogActivity.this, "您还没有添加照片", Toast.LENGTH_SHORT);
                        //                        }
                    }
                });
        return false;
    }
}
