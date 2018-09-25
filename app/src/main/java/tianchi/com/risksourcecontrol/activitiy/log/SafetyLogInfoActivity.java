package tianchi.com.risksourcecontrol.activitiy.log;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol.bean.login.UserInfo;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.model.OnUserListListener;
import tianchi.com.risksourcecontrol.presenter.LoadingLogInfoPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.view.ILoadingLogView;
import tianchi.com.risksourcecontrol.work.QueryUserListWork;

/*
* 安全日志详情界面
* */
public class SafetyLogInfoActivity extends BaseActivity implements ILoadingLogView, View.OnClickListener {
    private TextView tvBack;//返回
    //    private TextView tvDate;//日期
    private EditText edtRiskType;//风险源类型
    private EditText edtStakeNum;//桩号
    private EditText edtSection;//标段
    private EditText edtWeather;//天气
    private EditText edtLogID;//日志编号
    private EditText edtRiskDetails;//日志详情
    private EditText edtEmergency;//突发事件
    private EditText edtLeader;//施工单位负责人
    private EditText edtRecorder;//记录员
    private EditText edtProjectRole;//提交方（业主，监理，施工）
    private TextView tvEdit;//编辑按钮
    private TextView tvSubmit;//提交按钮
    //    private ImageView imgvPicture;//照片
    //    private Button btnPushLog;//推送日志
    private List<String> accountList;
    private Bitmap picBitmap;
    private AlertDialog _dialog;
    private ProgressDialog m_progressDialog;

    private EditText edtTDetails;//日志详情
    private EditText edtDeadlineTime;//整改期限

    //照片展示区代码
    private GridView m_gridView;             //图片展示区控件
    private List<String> _list;              //图片文件名数组
    private List<String> picNames;           //临时图片文件名数组
    private String pictureName = "";//多图片拼接文件名
    private String itemPicName = "";//子图片文件名
    private String userRealName = "";//用于取日志图片的用户真实姓名
    private int userId;
    private int id;//日志唯一索引id
    private String userLoginName = "";
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器

    private LoadingLogInfoPresenter m_logInfoPresenter = new LoadingLogInfoPresenter(this);

    //    private Lock m_lock = new ReentrantLock();
    private boolean canDownLoad;
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    downloadPictures();
                    break;
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_progressDialog.dismiss();
                        }
                    });
                    break;
                case 2:
                    /*提交修改代码片*/
                    m_logInfoPresenter.submitModifyLog(ServerConfig.URL_SUBMIT_MODIFY_PRO_SAFETY_LOG,getModifyLogMap());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_log_info);
        initView();
        initValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initValue() {
        initShowPicArea();
        SafetyLogInfo _logInfo = (SafetyLogInfo) getIntent().getSerializableExtra("logInfo");
        if (_logInfo != null) {
            id = _logInfo.getId();
            edtRiskType.setText(_logInfo.getRiskType());
            if (_logInfo.getStakeNum().length() != 0){
                edtStakeNum.setText(_logInfo.getStakeNum());
            }
            edtSection.setText(_logInfo.getSection());
            edtWeather.setText(_logInfo.getWeather());
            edtLogID.setText(_logInfo.getLogId());
            edtRiskDetails.setText(_logInfo.getBuildDetails());
            edtEmergency.setText(_logInfo.getEmergency());
            edtLeader.setText(_logInfo.getChargeBuilder());
            edtRecorder.setText(_logInfo.getRecorder());

            if (_logInfo.getTechnoDetails() != null) {
                edtTDetails.setText(_logInfo.getTechnoDetails());
            }

            if (_logInfo.getExpireTime() != null) {
                edtDeadlineTime.setText(_logInfo.getExpireTime().substring(0, 10));
            }
            if (_logInfo.getRecorder().equals(UserSingleton.getUserInfo().getRealName())) {//判断是否是自己提交的日志
                tvEdit.setVisibility(View.VISIBLE);
                tvSubmit.setVisibility(View.VISIBLE);
            }
            edtProjectRole.setText(_logInfo.getProjectRole());
            userRealName = _logInfo.getRecorder();//获取当前日志记录员

            QueryUserListWork.queryUserInfo(userRealName, new OnUserListListener() {
                @Override
                public void onQuerySucceed(UserInfo userInfo) {
                    userId = userInfo.getUserId();
                    userLoginName = userInfo.getLoginName();
                    pictureName = _logInfo.getPicture();
                    if (pictureName != null && pictureName.length() > 0) {
                        _list = new ArrayList<>();
                        _list.addAll(Arrays.asList(pictureName.split("#")));
                        picNames.addAll(_list);
                        if (picNames.size() > 0) {//第一次下载第一张照片
                            downloadFirstPicture();
                        }
                    } else {
                        MyToast.showMyToast(SafetyLogInfoActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                        //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(SafetyLogInfoActivity.this, msg, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void downloadFirstPicture() {
        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
        if (!picFile.exists()) {
            itemPicName = picNames.get(0);
            canDownLoad = false;
            m_logInfoPresenter.downloadLogPicture(picNames.size(), 0);
            picNames.set(0, "");
        } else {
            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.PRO_SAFETY_PIC_PATH + picNames.get(0));
            HashMap<String, Object> _map = new HashMap<>();
            _map.put("itemImage", _bitmap);
            imageItem.add(_map);
            refreshGridviewAdapter();
            picNames.set(0, "");
            canDownLoad = true;
            Message msg = new Message();
            msg.what = 0;
            m_handler.sendMessage(msg);
        }
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvEdit.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
        //        tvDate = (TextView) findViewById(R.id.tvDate);
        edtRiskType = (EditText) findViewById(R.id.edtRiskType);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        edtSection = (EditText) findViewById(R.id.edtSection);
        edtWeather = (EditText) findViewById(R.id.edtWeather);
        edtLogID = (EditText) findViewById(R.id.edtlogid);
        edtRiskDetails = (EditText) findViewById(R.id.edtDetails);
        edtEmergency = (EditText) findViewById(R.id.edtEmergency);
        edtLeader = (EditText) findViewById(R.id.edtLeader);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtProjectRole = (EditText) findViewById(R.id.edtProjectRole);
        edtTDetails = (EditText) findViewById(R.id.edttDetails);
        //        imgvPicture = (ImageView) findViewById(R.id.imgvPic);
        //        btnPushLog = (Button) findViewById(R.id.btnPushLog);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);

        m_gridView = (GridView) findViewById(R.id.gridView1);

        edtDeadlineTime = $(R.id.edtLogRectifyDate);
        //        imgvPicture.setOnClickListener(this);
        //        btnPushLog.setOnClickListener(this);
    }

    private void downloadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (!picFile.exists()) {
                if (canDownLoad) {
                    itemPicName = picNames.get(i);
                    canDownLoad = false;
                    m_logInfoPresenter.downloadLogPicture(picNames.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.PRO_SAFETY_PIC_PATH + picNames.get(i));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                imageItem.add(_map);
                //                picFiles.add(picFile);
                refreshGridviewAdapter();
                picNames.set(i, "");
                canDownLoad = true;
                Message msg = new Message();
                m_handler.sendMessage(msg);
                break;
                //                                m_lock.unlock();
            }
        }
    }

    private void initShowPicArea() {
        picNames = new ArrayList<>();
        imageItem = new ArrayList<>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                if (picFiles.get(position) != null) {
                //                    //打开照片查看
                //                    Intent intent = new Intent();
                //                    intent.setAction(Intent.ACTION_VIEW);
                //                    intent.setDataAndType(Uri.fromFile(picFiles.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
                //                    startActivity(intent);
                //                }
                if (_list.get(position) != null) {
                    //打开照片查看
                    File _file = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, _list.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;

            case R.id.tvEdit:
                setAllEdittextEnable();
                break;

            case R.id.tvSubmit:
                MyAlertDialog.showAlertDialog(this, "日志更改提示", "保存当前更改过的日志？",
                        "确定", "取消", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Message _message = new Message();
                                _message.what = 2;
                                m_handler.sendMessage(_message);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                break;
            //            case R.id.imgvPic:
            //                if (picBitmap == null) {
            //                    MyToast.showMyToast(this, "该日志无照片", Toast.LENGTH_SHORT);
            //                    return;
            //                }
            //                if (picBitmap != null) {
            //                    //打开照片查看
            //                    File _file = new File(CameraUtils.file, pictureName);
            //                    Intent intent = new Intent();
            //                    if (_file.exists()) {
            //                        intent.setAction(Intent.ACTION_VIEW);
            //                        intent.setDataAndType(Uri.fromFile(_file), CameraUtils.IMAGE_UNSPECIFIED);
            //                        startActivity(intent);
            //                    } else {
            //                        intent.setAction(Intent.ACTION_VIEW);
            //                        intent.setDataAndType(Uri.fromFile(CameraUtils.bitMap2File(picBitmap)),
            //                                CameraUtils.IMAGE_UNSPECIFIED);
            //                        startActivity(intent);
            //                    }
            //                }
            //                break;
            case R.id.btnPushLog:
                _dialog = MyAlertDialog.editTextDialog(SafetyLogInfoActivity.this,
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

    private void setAllEdittextEnable() {//设置所有框框可编辑
        edtRiskType.setEnabled(true);
        edtStakeNum.setEnabled(true);
        edtSection.setEnabled(true);
        edtWeather.setEnabled(true);
        edtRiskDetails.setEnabled(true);
        edtEmergency.setEnabled(true);
        edtLeader.setEnabled(true);
    }

    // TODO: 2018/5/3  需重新获取对应的URL
    @Override
    public String getDownLoadURL() {
        return ServerConfig.URL_DOWNLOAD_PRO_SAFETY_FILE +
                getIdLoginName() + "/" + itemPicName;
    }

    // TODO: 2018/5/3 需动态获取对应用户id及用户名
    @Override
    public String getIdLoginName() {
        /*return UserSingleton.getUserInfo().getUserId() +
                UserSingleton.getUserInfo().getLoginName();*/
        return userId + userLoginName;
    }

    @Override
    public String getPictureName() {
        if (itemPicName != null && itemPicName.length() > 0)
            return itemPicName;
        return "";
    }

    @Override
    public void showLoadingPiture(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage(msg);
            m_progressDialog.show();
        }
    }

    @Override
    public void hideLoadingPicture() {
        if (m_progressDialog != null) {
            Message msg = new Message();
            msg.what = 1;
            m_handler.sendMessage(msg);
        }
    }

    @Override
    public void showLoadingSucceed(Bitmap bitmap) {
        if (bitmap != null) {
            picBitmap = bitmap;
            //            picFiles.add(CameraUtils.bitMapToFile(picBitmap,
            //                    FoldersConfig.PRO_SAFETY_PIC_PATH, itemPicName));
            HashMap<String, Object> _map = new HashMap<>();
            _map.put("itemImage", picBitmap);
            imageItem.add(_map);
            refreshGridviewAdapter();
            //            MyToast.showMyToast(this, "成功加载" + itemPicName, Toast.LENGTH_SHORT);
            canDownLoad = true;
        }
        boolean isFinish = false;
        for (String _picName : picNames) {
            if (!_picName.equals("")) {
                isFinish = false;
            } else {
                isFinish = true;
            }
        }
        if (isFinish) {
            hideLoadingPicture();
        } else {
            Message msg = new Message();
            msg.what = 0;
            m_handler.sendMessageDelayed(msg, 500);
        }
        //        m_lock.unlock();
    }


    @Override
    public void showLoadingFailed(String msg) {
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        Message _message = new Message();
        _message.what = 1;
        m_handler.sendMessage(_message);
    }

    @Override
    public void showSubmitSucceedOrFailed(String msg) {
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        Message _message = new Message();
        _message.what = 1;
        m_handler.sendMessage(_message);
    }

    @Override
    public Map<String, Object> getModifyLogMap() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("id", id);
        _map.put("riskType", edtRiskType.getText().toString());
        _map.put("weather", edtWeather.getText().toString());
        _map.put("section", edtSection.getText().toString());
        _map.put("stakeNum", edtStakeNum.getText().toString());
        _map.put("emergency", edtEmergency.getText().toString());
        _map.put("buildDetails", edtRiskDetails.getText().toString());
        _map.put("chargeBuilder", edtLeader.getText().toString());
        _map.put("recorder", edtRecorder.getText().toString());
        return _map;
    }
}
