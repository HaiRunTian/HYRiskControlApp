package tianchi.com.risksourcecontrol2.activitiy.log;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.notice.NewRectifyReplyInfoActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyDatePicker;
import tianchi.com.risksourcecontrol2.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.presenter.NewPatrolLogPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.view.INewLogView;
import tianchi.com.risksourcecontrol2.work.UserLoginWork;

/**
 * @描述 新建重大风险源日志
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:09.
 */
public class NewPatrolActivity extends BaseActivity implements MyTakePicDialog.OnItemClickListener, INewLogView, View.OnClickListener, View.OnLongClickListener {
    private TextView tvBack;//返回
    //    private TextView tvDate;//日期
//    private EditText edtLogID;//日志编号
    //    private EditText edtAccount;//账号
    private EditText edtMDetails;//生产安全详情
    private EditText edtTDetails;//技术质量安全详情
    private EditText edtEmergency;//突发事件
    private EditText edtLeader;//施工单位负责人
    private EditText edtRecorder;//记录员
    private EditText edtProjectRole;//提交方（业主，监理，施工）
    private EditText edtStakeNum;//桩号
    private Spinner spSection;//标段
    private Spinner spRiskType;//风险源类型
    private Spinner spWeather;//天气
    private Button btnAddPic;//添加照片
    private Button btnSubmit;//提交
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//提交进度
    //巡查日志控制器
    NewPatrolLogPresenter m_patrolLogPresenter = new NewPatrolLogPresenter(this);

    private File resultImgFile;//最终生成的img文件
    private Bitmap picBitmap;//存储用户修改头像
    private Uri fileUri;//用户头像拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    private String pictureName = "";//照片全名xx.jpg
    //拍照区域代码
    private File takPicFile;//用户头像拍照文件
    private GridView m_gridView;
    private Bitmap bmp;                      //导入临时图片
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private EditText edtRiskIndex; ///风险源编号
    private EditText edtDeadlineTime; //整改期限
    private boolean canUpload = true;
    private int m_picIndex = 0; //照片下标
    private String m_regEx; //风险源编号正则表达式
    private int uploadImgIndex = 0;//上传照片时的数量
    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            uploadPictures();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patrol);
        initView();
        initValues();
    }

    private void initValues() {
        //  setSaveTime();
        setRecorder();
//        setLogId();
        setSection();
        setProjectRole();
        initTakePicArea();
        setRiskIndex();
        //        tempValueInit();
    }

    private void setRiskIndex() {
        String riskIndex = getIntent().getStringExtra("riskIndex");
        if (riskIndex != null) {
            edtRiskIndex.setText(riskIndex);
        }
    }

    private void tempValueInit() {
        edtStakeNum.setText("1");
        edtEmergency.setText("1");
        edtLeader.setText("1");
        edtMDetails.setText("1");
        edtTDetails.setText("1");
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
                MyAlertDialog.showAlertDialog(NewPatrolActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
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
        //    tvDate = (TextView) findViewById(R.id.tvDate);
        //    edtLogID = (EditText) findViewById(R.id.edtLogID);
        //    edtAccount = (EditText) findViewById(R.id.edtReformAccount);
        edtMDetails = (EditText) findViewById(R.id.edtmDetails);
        edtTDetails = (EditText) findViewById(R.id.edttDetails);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtProjectRole = (EditText) findViewById(R.id.edtProjectRole);
        edtEmergency = (EditText) findViewById(R.id.edtEmergency);
        edtLeader = (EditText) findViewById(R.id.edtLeader);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        spSection = (Spinner) findViewById(R.id.spSection);
        spWeather = (Spinner) findViewById(R.id.spWeather);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        btnAddPic = (Button) findViewById(R.id.btnAddPic);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        m_gridView = (GridView) findViewById(R.id.gridView1);
        edtDeadlineTime = $(R.id.edtLogRectifyDate);
        edtRiskIndex = $(R.id.edtLogRiskIndex);
        edtDeadlineTime.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        btnAddPic.setOnClickListener(this);
        btnAddPic.setOnLongClickListener(this);
        btnSubmit.setOnClickListener(this);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("提交日志中...");
        m_progressDialog.setCancelable(true);

    }

    //上传多张图片
    private void uploadPictures() {
//        for (int i = 0; i < picNames.size(); i++) {
//            if (picNames.get(i).equals("")) {
//                continue;
//            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(uploadImgIndex).getName().equals(picNames.get(uploadImgIndex))) {
                if (canUpload) {
                    canUpload = false;
                    m_picIndex = uploadImgIndex;
                    m_patrolLogPresenter.uploadFile(picFiles.size(), uploadImgIndex);

//                    picNames.set(i, "");
//                    break;
                }
//            } else {
//                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
//                break;
//            }
        }
    }

    @Override
    public void onBackPressed() {
        saveLogRemind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                saveLogRemind();
                break;
            case R.id.btnAddPic:
                if (imageItem.size() == 5) {
                    MyToast.showMyToast(NewPatrolActivity.this, "最多支持上传5张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }
                break;
            case R.id.btnSubmit:
                //风险源编号填写正确后才可以提交
                if ( isRiskIndex()) {
                   if (picFiles.size() != 0) {
                       uploadFirstPicture();
                   }else m_patrolLogPresenter.submit();
                    //                    m_safetyLogPresenter.submit();
                } else {
                    if (!isRiskIndex()){
                        MyToast.showMyToast(this, "请输入正确的风险源编号", Toast.LENGTH_SHORT);
                    }
                }
                //必填项必填后才可以提交
//                if (checkInput() && isRiskIndex()) {
//
//                    uploadFirstPicture();
//                    //                    m_safetyLogPresenter.submit();
//                } else {
//                    if (!checkInput()){
//                        MyToast.showMyToast(this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
//                    }else {
//                        MyToast.showMyToast(this, "请输入正确的风险源编号", Toast.LENGTH_SHORT);
//                    }
//
//                }
                break;

            case R.id.edtLogRectifyDate:
                MyDatePicker.ShowDatePicker(this, edtDeadlineTime);

                break;
            default:
                break;
        }
    }

    //校验风险源编号
    private boolean isRiskIndex() {
        m_regEx = "^[0-1][0-9][A-Z][0-9]{2}$";
        Pattern _pattern = Pattern.compile(m_regEx);
        Matcher _matcher = _pattern.matcher(getRiskIndex());
        boolean re = _matcher.matches();
        if (!re) {
            MyToast.showMyToast(NewPatrolActivity.this, "请输入正确的风险源编号", Toast.LENGTH_SHORT);
        }


        return re;
    }

    //上传第一张照片
    private void uploadFirstPicture() {
//        String _name1 = picFiles.get(0).getName();
//        String _name2 = picNames.get(0);
        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            while (true) {
                if (canUpload) {
                    m_patrolLogPresenter.uploadFile(picFiles.size(), 0);
//                    m_picIndex = 0;
//                    picNames.set(0, "");
                    canUpload = false;
                    break;
                }
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
                            m_patrolLogPresenter.submit();
                            finish();
                        } else {
                            MyToast.showMyToast(NewPatrolActivity.this, "请完善每项信息再提交", Toast.LENGTH_SHORT);
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
        if (getRiskIndex().length() == 0) {
            edtRiskIndex.setError("风险源编号不能为空");
            isOk = false;
        }
        if (getSection().length() == 0) {
            spSection.setPrompt("标段不能为空");
            isOk = false;
        }
        if (getRiskType().length() == 0) {
            spRiskType.setPrompt("请选择风险源类型");
            isOk = false;
        }
        if (getStakeNum().length() == 0) {
            edtStakeNum.setError("检查单位不能为空");
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
            edtLeader.setError("受检单位不能为空");
            isOk = false;
        }
        if (getMDetails().length() == 0) {
            edtMDetails.setError("检查发现问题不能为空");
            isOk = false;
        }
        if (getTDetails().length() == 0) {
            edtTDetails.setError("建议不能为空");
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
        return edtStakeNum.getText().toString()+"";
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
        return edtEmergency.getText().toString()+"";
    }

    @Override
    public String getLeader() {
        return edtLeader.getText().toString()+"";
    }

    @Override
    public void setRecorder() {
        if (UserSingleton.getUserInfo().getRealName() != null)
            edtRecorder.setText(UserSingleton.getUserInfo().getRealName());
    }

    @Override
    public String getRecorder() {
        return edtRecorder.getText().toString()+"";
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
        //无用
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
        return null;//无用
    }

    @Override
    public String getTitles() {
        return null;//无用
    }

    @Override
    public String getMDetails() {
        return edtMDetails.getText().toString()+"";
    }

    @Override
    public String getTDetails() {
        return edtTDetails.getText().toString()+"";
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(NewPatrolActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
        //        runOnUiThread(new Runnable() {
        //            @Override
        //            public void run() {
        //                hideInSubmiting();
        //            }
        //        });
        finish();
    }

    @Override
    public void showSubmitFailed(String msg) {
        hideInSubmiting();
        uploadImgIndex = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(NewPatrolActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
        finish();
    }


    @Override
    public void uploadFileSucceed(String msg) {
        //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
//        picNames.set(m_picIndex, "");
        canUpload = true;
        boolean isFinish = false;
        ++uploadImgIndex;
//        for (String _picName : picNames) {
//            if (!_picName.equals("")) {
//                isFinish = false;
//            } else {
//                isFinish = t rue;
//            }
//        }
        if (picNames.size() > uploadImgIndex){  //如果集合中的照片还有未上传的，继续上传
            isFinish = false;
        }else {
            isFinish = true;
        }

        if (!isFinish) {
            Message _message = new Message();
            m_handler.sendMessageDelayed(_message, 500);
        } else {
            m_patrolLogPresenter.submit();
        }
    }


    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        uploadImgIndex = 0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(NewPatrolActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
        picBitmap = null;
    }

    @Override
    public void setLogId() {
        String tempDate = DateTimeUtils.dateToString(new Date(), "yyyyMMddHHmmss");
        //        int tempNum = Integer.parseInt(tempDate.substring(8, tempDate.length() - 1));
        //        int rand = new Random().nextInt(tempNum) / 8;
//        edtLogID.setText("X_" + UserSingleton.getUserInfo().getUserId() + "_" + tempDate.substring(2));
    }

    /**
     * @return 获取整改时间
     */
    @Override
    public String getExpireTime() {
        return edtDeadlineTime.getText().toString() + " 00:00:00";
    }

    /**
     * @return 获取风险源编号
     */
    @Override
    public String getRiskIndex() {
        return edtRiskIndex.getText().toString().trim().toUpperCase();
    }

    //    @Override
    //    public void initLoadingDataSucceed(List<String> list) {
    //        if (list.size() > 0)
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
                takPicFile = new File(FoldersConfig.SAFETY_PATROL_PIC_PATH, System.currentTimeMillis() + ".jpg");
                fileUri = Uri.fromFile(takPicFile);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnPickFromAlbum:
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CameraUtils.IMAGE_UNSPECIFIED);
                startActivityForResult(i, CameraUtils.PHOTO_REQUEST_GALLERY);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap _bitmap = CameraUtils.comp(picBitmap);
                        if (_bitmap != null) {
                            resultImgFile = new File(new URI(uri.toString()));
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureName = resultImgFile.getName();//拍摄返回的图片name
                                    picNames.add(pictureName);
                                    picFiles.add(resultImgFile);
                                    HashMap<String, Object> _map = new HashMap<>();
                                    _map.put("itemImage", _bitmap);
                                    imageItem.add(_map);
                                    refreshGridviewAdapter();
                                    //                                    m_patrolLogPresenter.uploadFile();
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
                        if (picBitmap != null) {
                            //                            pictureName = new File(new URI(uri.toString())).getName();
                            //                            resultImgFile = CameraUtils.bitMap2File(picBitmap);
                            //                            resultImgFile = new File(new URI(uri.toString()));
                            resultImgFile = new File(imgpath);
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
                                    //                                    m_patrolLogPresenter.uploadFile();
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
                        //                            picBitmap = null;
                        //                            btnAddPic.setBackgroundResource(R.mipmap.ic_add);
                        //                        } else {
                        //                            MyToast.showMyToast(NewPatrolActivity.this, "您还没有添加照片", Toast.LENGTH_SHORT);
                        //                        }
                    }
                });
        return false;
    }
}
