package tianchi.com.risksourcecontrol.activitiy.log;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;
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

/**
 * Created by Kevin on 2018/1/8.
 * 巡查日志详细界面
 */

public class PatrolLogInfoActivity extends BaseActivity implements ILoadingLogView, View.OnClickListener {
    private TextView tvBack;//返回
    //    private TextView tvDate;//日期
    private EditText edtRiskType;//风险源类型
    private EditText edtStakeNum;//桩号
    private EditText edtSection;//标段
    private EditText edtWeather;//天气
    private EditText edtLogID;//日志编号
    private EditText edtMDetails;//日志详情
    private EditText edtTDetails;//日志详情

    private EditText edtEmergency;//突发事件
    private EditText edtLeader;//施工单位负责人
    private EditText edtRecorder;//记录员
    private EditText edtProjectRole;//提交方（业主，监理，施工）
    private TextView tvEdit;
    private TextView tvSubmit;
    private EditText edtRiskIndex;//风险源编号
    private EditText edtDeadlineTime;//整改期限
    //    private ImageView imgvPicture;//照片
//    private Button btnPushLog;//推送
    //    private byte[] pictureBytes;
    private Bitmap picBitmap;
    private ProgressDialog m_progressDialog;
    private List<String> accountList;
    AlertDialog _dialog;

    //照片展示区代码
    private GridView m_gridView;
    private List<String> _list;
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private String pictureName = "";//多图片拼接文件名
    private String itemPicName = "";//子图片文件名
    private String userRealName = "";//用于取日志图片的用户真实姓名
    private int userId;
    private int id;//日志唯一索引id
    private String userLoginName = "";

    private LoadingLogInfoPresenter m_logInfoPresenter = new LoadingLogInfoPresenter(this);

    private boolean canDownLoad;//标记是否允许开启下载线程
    private Handler m_handler = new Handler() {//用于在主线程开启耗时子线程
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
                    m_logInfoPresenter.submitModifyLog(ServerConfig.URL_SUBMIT_MODIFY_SAFETY_PATROL_LOG, getModifyLogMap());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_log_info);
        initView();
        initValue();
    }

    @Override
    public void onBackPressed() {
        if (m_progressDialog != null)
            m_progressDialog.dismiss();
        super.onBackPressed();
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tvBack);
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
        edtMDetails = (EditText) findViewById(R.id.edtmDetails);
        edtTDetails = (EditText) findViewById(R.id.edttDetails);
        edtEmergency = (EditText) findViewById(R.id.edtEmergency);
        edtLeader = (EditText) findViewById(R.id.edtLeader);
        edtRecorder = (EditText) findViewById(R.id.edtRecorder);
        edtProjectRole = (EditText) findViewById(R.id.edtProjectRole);
        //        imgvPicture = (ImageView) findViewById(R.id.imgvPic);
//        btnPushLog = (Button) findViewById(R.id.btnPushLog);


        edtDeadlineTime = $(R.id.edtLogRectifyDate);
        edtRiskIndex = $(R.id.edtLogRiskIndex);

        m_gridView = (GridView) findViewById(R.id.gridView1);

        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);

        tvBack.setOnClickListener(this);
        //        imgvPicture.setOnClickListener(this);
//        btnPushLog.setOnClickListener(this);
    }

    private void initValue() {
        initShowPicArea();
        PatrolLogInfo _logInfo = (PatrolLogInfo) getIntent().getSerializableExtra("logInfo");
        if (_logInfo != null) {
            id = _logInfo.getId();
//            String id = _logInfo.getLogId();
            edtLogID.setText(_logInfo.getLogId()+"");
            edtSection.setText(_logInfo.getSection()+"");
            edtRiskType.setText(_logInfo.getRiskType()+"");
            edtStakeNum.setText(_logInfo.getStakeNum()+"");
            edtWeather.setText(_logInfo.getWeather()+"");
            edtEmergency.setText(_logInfo.getEmergency()+"");
            edtRecorder.setText(_logInfo.getRecorder()+"");
            if (_logInfo.getRiskIndex().length() != 0) {
                edtRiskIndex.setText(_logInfo.getRiskIndex());
            }
            if (_logInfo.getExpireTime().length() != 0) {
                edtDeadlineTime.setText(_logInfo.getExpireTime().substring(0, 10));
            }
            if (_logInfo.getRecorder().equals(UserSingleton.getUserInfo().getRealName())) {
                tvEdit.setVisibility(View.VISIBLE);
                tvSubmit.setVisibility(View.VISIBLE);
            }
            edtProjectRole.setText(_logInfo.getProjectRole()+"");
            edtLeader.setText(_logInfo.getChargeBuilder()+"");
            //            tvDate.setText(DateTimeUtils.dateToString(_logInfo.getSaveTime(), "yyyy-MM-dd"));
            edtMDetails.setText(_logInfo.getBuildDetails()+"");
            edtTDetails.setText(_logInfo.getTechnoDetails()+"");
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
                        MyToast.showMyToast(PatrolLogInfoActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(PatrolLogInfoActivity.this, msg, Toast.LENGTH_SHORT);
                }
            });
        }
//        accountList = UserSingleton.getAccountList();
    }

    private void downloadFirstPicture() {
        File picFile = new File(FoldersConfig.SAFETY_PATROL_PIC_PATH, picNames.get(0));
        if (!picFile.exists()) {
            itemPicName = picNames.get(0);
            canDownLoad = false;
            m_logInfoPresenter.downloadLogPicture(picNames.size(), 0);
            picNames.set(0, "");
        } else {
            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.SAFETY_PATROL_PIC_PATH + picNames.get(0));
            HashMap<String, Object> _map = new HashMap<>();
            _map.put("itemImage", _bitmap);
            imageItem.add(_map);
            refreshGridviewAdapter();
            picNames.set(0, "");
            canDownLoad = true;
            Message msg = new Message();
            m_handler.sendMessage(msg);
        }
    }

    private void initShowPicArea() {
        picNames = new ArrayList<>();
        imageItem = new ArrayList<>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (_list.get(position) != null) {
                    //打开照片查看
                    File _file = new File(FoldersConfig.SAFETY_PATROL_PIC_PATH, _list.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    private void downloadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            File picFile = new File(FoldersConfig.SAFETY_PATROL_PIC_PATH, picNames.get(i));
            if (!picFile.exists()) {
                if (canDownLoad) {
                    itemPicName = picNames.get(i);
                    canDownLoad = false;
                    m_logInfoPresenter.downloadLogPicture(picNames.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.SAFETY_PATROL_PIC_PATH + picNames.get(i));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                imageItem.add(_map);
                refreshGridviewAdapter();
                picNames.set(i, "");
                canDownLoad = true;
                Message msg = new Message();
                m_handler.sendMessage(msg);
                break;
            }
        }
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
                Message _message = new Message();
                _message.what = 2;
                m_handler.sendMessage(_message);
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
                _dialog = MyAlertDialog.editTextDialog(PatrolLogInfoActivity.this,
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
        edtMDetails.setEnabled(true);
        edtTDetails.setEnabled(true);
        edtEmergency.setEnabled(true);
        edtLeader.setEnabled(true);
    }

    @Override
    public String getDownLoadURL() {
        return ServerConfig.URL_DOWNLOAD_SAFETY_PATROL_FILE +
                getIdLoginName() + "/" + itemPicName;
    }

    @Override
    public String getIdLoginName() {
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
        _map.put("chargeBuilder", edtLeader.getText().toString());
        _map.put("recorder", edtRecorder.getText().toString());
        _map.put("buildDetails", edtMDetails.getText().toString());
        _map.put("technoDetails", edtTDetails.getText().toString());
        return _map;
    }
}
