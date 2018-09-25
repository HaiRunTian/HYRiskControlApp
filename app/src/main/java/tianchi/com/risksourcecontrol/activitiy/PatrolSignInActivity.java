package tianchi.com.risksourcecontrol.activitiy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.supermap.data.Point2D;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.presenter.PatrolSignInPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.CameraUtils;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.util.FileUtils;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;
import tianchi.com.risksourcecontrol.view.IPatrolSignInView;

public class PatrolSignInActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, MyTakePicDialog.OnItemClickListener, IPatrolSignInView {
    private EditText m_edtLoginName; //用户账号
    private EditText m_edtRealName; //用户姓名
    //    private EditText edtXCoord; //X坐标
    //    private EditText edtYCoord; //Y坐标
    private Button btnSubmit;
    private Spinner spSection;//标段
    private Spinner spRiskType;//风险源类型
    private Spinner spRisk;//风险源
    private CheckBox cbIsLocate;

    //    private MapView m_mapView;
    private TextView tvTime;
    private TextView tvBack;
    private ScrollView m_scrollView;
    //拍照区域代码
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//上传进度窗口
    private GridView m_gridView;
    private Button btnAddPic;//添加照片
    private Bitmap bmp;                      //导入临时图片
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private PatrolSignInPresenter m_patrolSignInPresenter = new PatrolSignInPresenter(this);//新建安全日志控制器
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件
    private Bitmap picBitmap;//存储用户修改头像
    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName = "";//照片全名xx.jpg
    private String section = "";
    private String riskType = "";
    private boolean canUpload = true;
    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://继续上传
                    uploadPictures();
                    break;
                case 1://提交签到
                    m_patrolSignInPresenter.submit();
                    break;
                case 2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (m_progressDialog != null) {
                                m_progressDialog.setMessage((CharSequence) msg.getData().get("msg"));
                                m_progressDialog.show();
                            }
                        }
                    });
                    break;
                case 3:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (m_progressDialog != null) {
                                m_progressDialog.dismiss();
                            }
                            btnSubmit.setBackgroundResource(R.drawable.shape_circle_green);
                            btnSubmit.setText("签到失败");
                        }
                    });
                    break;
                case 5:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_progressDialog.setMessage("更新风险源列表...");
                            m_progressDialog.show();
                        }
                    });
                    queryRiskList(section, riskType.substring(2, riskType.length()), new QueryPatrolSignRiskListener() {
                        @Override
                        public void failed(String msg) {
                            MyToast.showMyToast(PatrolSignInActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
                            sendMsg2Handler(6);
                        }

                        @Override
                        public void succeed(List<String> list) {
                            spRisk.setAdapter(new ArrayAdapter<String>(PatrolSignInActivity.this, android.R.layout.simple_list_item_1, list));
                            MyToast.showMyToast(PatrolSignInActivity.this, "已更新风险源列表", Toast.LENGTH_SHORT);
                            sendMsg2Handler(6);
                        }
                    });
                    break;
                case 6:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_progressDialog.dismiss();
                        }
                    });
                    break;
                case 7:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnSubmit.setBackgroundResource(R.drawable.shape_circle_green);
                            btnSubmit.setText("签到成功");
                            m_progressDialog.dismiss();
                        }
                    });
                    break;
            }
        }
    };

    private void sendMsg2Handler(int what) {
        Message _message = new Message();
        _message.what = what;
        m_handler.sendMessage(_message);
    }
    //    TimerTask _timerTask2 = new TimerTask() {
    //        @Override
    //        public void run() {
    //            if (DrawerActivity.pointPixel != null) {
    //                runOnUiThread(new Runnable() {
    //                    @Override
    //                    public void run() {
    //                        edtXCoord.setText(String.valueOf(DrawerActivity.pointPixel.getX()));
    //                        edtYCoord.setText(String.valueOf(DrawerActivity.pointPixel.getY()));
    //                    }
    //                });
    //            }
    //        }
    //    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_patrol_sign_in);
        initView();
        initValue();
        initTimer();
        //        initBaidu();
        //        startLocate();
    }

    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            if (canUpload) {
                m_patrolSignInPresenter.uploadFile(picFiles.size(), 0);
                picNames.set(0, "");
                canUpload = false;
            }
        } else {
            MyToast.showMyToast(this, "请检查要上传的图片是否已被手动删除！", Toast.LENGTH_SHORT);
        }
    }

    private void uploadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(i).getName().equals(picNames.get(i))) {
                if (canUpload) {
                    canUpload = false;
                    m_patrolSignInPresenter.uploadFile(picFiles.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
                break;
            }
        }
    }

    //时钟
    TimerTask _timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(DateTimeUtils.dateToString(
                            new Date(System.currentTimeMillis()), "HH:mm:ss"));
                }
            });
        }
    };

    private void initValue() {
        m_edtLoginName.setText(UserSingleton.getUserInfo().getLoginName());
        m_edtRealName.setText(UserSingleton.getUserInfo().getRealName());
        List<String> section = UserSingleton.getSectionList();
        if (section != null) {
            spSection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, section));
        }
        getXY();
        initTakePicArea();
    }

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
                MyAlertDialog.showAlertDialog(PatrolSignInActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
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

    private void requestOpenGPS() {
        AlertDialog.Builder _builder = new AlertDialog.Builder(this)
                .setMessage("系统请求打开GPS开关，若已打开GPS，请等待系统获取您当前的坐标值")
                .setCancelable(true)
                .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("我已打开GPS，返回", null);
        AlertDialog _dialog = _builder.create();
        _dialog.show();
    }

    private void initView() {
        m_edtLoginName = (EditText) findViewById(R.id.edtLoginName);
        m_edtRealName = (EditText) findViewById(R.id.edtRealName);
        //        edtXCoord = (EditText) findViewById(R.id.edtXCoord);
        //        edtYCoord = (EditText) findViewById(R.id.edtYCoord);
        spSection = (Spinner) findViewById(R.id.spSection);
        spSection.setOnItemSelectedListener(this);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        spRiskType.setOnItemSelectedListener(this);
        spRisk = (Spinner) findViewById(R.id.spRisk);
        cbIsLocate = (CheckBox) findViewById(R.id.cbIsLocate);
        //        spRisk.setOnItemSelectedListener(this);
        m_scrollView = (ScrollView) findViewById(R.id.scrlvPatrolSignIn);
        tvTime = (TextView) findViewById(R.id.tvTime);

        m_progressDialog = new ProgressDialog(this);//上传及签到进度条
        m_progressDialog.setCancelable(true);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        btnAddPic = (Button) findViewById(R.id.btnAddPic);
        btnAddPic.setOnClickListener(this);
        m_gridView = (GridView) findViewById(R.id.gridView1);

        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
    }


    private String getX() {
        //        return edtXCoord.getText().toString();
        if (DrawerActivity.pointPixel != null) {
            return String.valueOf(DrawerActivity.pointPixel.getX());
        } else {
            return "";
        }
    }

    private String getY() {
        //        return edtYCoord.getText().toString();
        if (DrawerActivity.pointPixel != null) {
            return String.valueOf(DrawerActivity.pointPixel.getY());
        } else {
            return "";
        }
    }

    private void getXY() {
        DrawerActivity.pointPixel =new Point2D();
        if (DrawerActivity.pointPixel == null) {
            requestOpenGPS();
        }
    }

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
        return UserSingleton.getUserInfo().getUserId() +
                UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public File getUploadFile(int position) {
        if (picFiles.size() > 0) {
            return picFiles.get(position);
        }
        return null;
    }

    @Override
    public Map<String, Object> getMap() {
        HashMap<String, Object> _map = new HashMap<>();
        _map.put("account", getLoginName());
        _map.put("realName", getRealName());
        _map.put("x", getX());
        _map.put("y", getY());
        _map.put("section", getSection());
        _map.put("risk", getRisk());
        _map.put("section", getSection());
        _map.put("location", cbIsLocate.isChecked());
        String date = DateTimeUtils.dateToString(new Date(System.currentTimeMillis()), DateTimeUtils.FULL_DATE_TIME_FORMAT);
        _map.put("date", date);
        _map.put("images", getPicture());
        return _map;
    }

    @Override
    public void showInSubmiting(String msg) {
        Message _message = new Message();
        Bundle _bundle = new Bundle();
        _bundle.putString("msg", msg.replace("\"", ""));
        _message.what = 2;
        _message.setData(_bundle);
        m_handler.sendMessage(_message);
    }

    @Override
    public void hideInSubmiting() {
        sendMsg2Handler(3);
    }

    @Override
    public void showSubmitSucceed(String msg) {
        Message _message = new Message();
        Bundle _bundle = new Bundle();
        _bundle.putString("msg", msg.replace("\"", ""));
        _message.what = 7;
        _message.setData(_bundle);
        m_handler.sendMessage(_message);
    }

    @Override
    public void showSubmitFailed(String msg) {
        sendMsg2Handler(2);
    }

    @Override
    public void uploadFileSucceed(String msg) {
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
            _message.what = 0;
            m_handler.sendMessageDelayed(_message, 500);
        } else {//下载完成，提交日志
            Message _message = new Message();
            _message.what = 1;
            m_handler.sendMessageDelayed(_message, 500);
        }
    }

    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
    }

    private String getLoginName() {
        return m_edtLoginName.getText().toString();
    }

    private String getRealName() {
        return m_edtRealName.getText().toString();
    }

    public String getSection() {
        return spSection.getSelectedItem().toString();
    }

    private String getRisk() {
        return spRisk.getSelectedItem().toString();
    }

    @Override
    protected void onDestroy() {
        //        m_mapView = null;
        //        if (m_mapView != null)
        //            m_mapView.onDestroy();
        _timerTask.cancel();
        super.onDestroy();
    }


    void initTimer() {
        Timer _timer = new Timer();
        _timer.schedule(_timerTask, new Date(System.currentTimeMillis()), 1000);
        //        Timer _timer1 = new Timer();
        //        _timer1.schedule(_timerTask2, new Date(System.currentTimeMillis()), 2000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spSection:
                //            case R.id.spRiskType:
                section = parent.getItemAtPosition(position).toString();
                riskType = spRiskType.getSelectedItem().toString();
                if (section.equals("")) {
                    return;
                }
                sendMsg2Handler(5);

                break;
            //            case R.id.spRisk:
            //                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void queryRiskList(String section, String riskType, QueryPatrolSignRiskListener listener) {
        Map<String, String> _map = new HashMap<>();
        _map.put("section", section);
        _map.put("riskType", riskType);
        String jsonString = GsonUtils.objectToJson(_map);
        OkHttpUtils.postAsync(ServerConfig.URL_GET_PATROL_SIGNIN_RISK, jsonString, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                listener.failed(e.getMessage());
            }

            @Override
            public void requestSuccess(String jsonString) throws Exception {
                if (!jsonString.contains("status") || !jsonString.contains("msg")) {
                    if (listener != null) {
                        listener.failed("服务器解析出错_error:" + jsonString);
                    }
                }
                int resultCode = GsonUtils.getIntNoteJsonString(jsonString, "status");//状态码
                String msg = GsonUtils.getStringNodeJsonString(jsonString, "msg");//附带信息
                switch (resultCode) {
                    case 0:
                        if (listener != null) {
                            listener.failed(msg);
                        }
                        break;
                    case 1:
                        String data = GsonUtils.getNodeJsonString(jsonString, "data");//解析数据
                        List<String> _list = GsonUtils.jsonToList(data);
                        if (listener != null) {
                            listener.succeed(_list);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnAddPic:
                if (imageItem.size() == 3) {
                    MyToast.showMyToast(PatrolSignInActivity.this, "最多支持上传3张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }

                break;
            case R.id.btnSubmit:
                if (btnSubmit.getText().equals("签到成功")) {
                    MyToast.showMyToast(this, "请勿重复签到", Toast.LENGTH_SHORT);
                    return;
                }
                if (getX().length() == 0 || getY().length() == 0) {
                    getXY();
                } else if (getPicture().length() > 0) {
                    uploadFirstPicture();
                } else {
                    sendMsg2Handler(1);
                }
                break;
        }
    }

    @Override
    public void setOnItemClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnTakePicture:
                takPicFile = new File(FoldersConfig.PATROL_SIGNIN_PIC_PATH, System.currentTimeMillis() + ".jpg");
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
            case R.id.btnCancel:
                if (m_dialog != null)
                    m_dialog.dismiss();
                break;
            default:
                break;
        }
    }

    /*拍照、选取相册结果返回*/
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
                                    //  m_safetyLogPresenter.uploadFile();
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

    public interface QueryPatrolSignRiskListener {
        void failed(String msg);

        void succeed(List<String> list);
    }
}
