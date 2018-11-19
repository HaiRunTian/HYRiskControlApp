package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.presenter.LoadingNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.presenter.RectifyNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.view.ILoadingNotifyView;
import tianchi.com.risksourcecontrol2.view.IRectifyNotifyView;
import tianchi.com.risksourcecontrol2.work.QueryUserListWork;

/**
 * Created by hairun.tian on 2018/6/21 0021.
 * 查看草稿类
 */

public class ReadDraftNotifyInfoActivity extends BaseActivity implements View.OnClickListener, ILoadingNotifyView, IRectifyNotifyView {

    private static final int GET_SUPERVISOR = 0;
    private static final int GET_COPYER = 8;
    private static final int GET_CONSTRUCTION = 9;
    private EditText m_edtLogId; //日志编号
    private EditText m_edtCheckUnit; //检查单位
    private EditText m_edtBecheckUnit; //受检单位
    private EditText m_edtCheckDate; //检查时间
    private EditText m_edtCheckMan; //检查人
    private EditText m_edtLogRectifyDate; //整改期限日期
    private Button m_btnAddPic; //添加照片
    private GridView m_gdvPic; //
    private EditText m_edtContent; //检查内容
    private EditText m_edtFindPro; //发现问题
    private EditText m_edtReformMethod; // 整改措施与要求
    private EditText m_btnPushlog;  //提交
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//提交进度
    private EditText m_edtSection; //标段
    private Button m_btnSubmit; //提交
    private TextView m_tvBack; //返回
    private EditText m_receiveMans; //接收人
//    private EditText m_edtSupervisor; //监理
    private EditText m_edtCopyer; //抄送着
    private EditText m_edtConstruction;//施工方
    private List<String> _list;              //图片文件名数组
    private String pictureName = "";//多图片拼接文件名
    private String itemPicName = "";//子图片文件名
    private String userRealName = "";//用于取日志图片的用户真实姓名
    private Bitmap picBitmap;
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private int userId;
    private int dbID;//日志唯一索引id
    private String userLoginName = "";
    private TextView m_tvReply; //回复按钮
    private String m_logID;
    private String m_checkUnit;
    private String m_beCheckUnit;
    private int m_picIndex = 0;
    private boolean canUpload = true;
    private File resultImgFile;//最终生成的img文件
    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    private boolean canDownLoad;

    private SimpleAdapter simpleAdapter;     //适配器
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
                    uploadPictures();
                    break;
//                case 2:
//                    /*提交修改代码片*/
//                    m_logInfoPresenter.submitModifyLog(ServerConfig.URL_SUBMIT_MODIFY_PRO_SAFETY_LOG,getModifyLogMap());
//                    break;
                default:
                    break;
            }
        }
    };

    private void uploadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(i).getName().equals(picNames.get(i))) {
                if (canUpload) {
                    canUpload = false;
                    mReNoticePresenter.uploadFile(picFiles.size(), i);
                    m_picIndex = i;
//                    picNames.set(i, "");
                    break;
                }
            } else {
                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
                break;
            }
        }
    }

    LoadingNotifyInfoPresenter m_presenter = new LoadingNotifyInfoPresenter(this);
    RectifyNotifyInfoPresenter mReNoticePresenter = new RectifyNotifyInfoPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_draft_notify_read);
        initView();
        initEvent();
        initValue();
    }

    //初始化数据
    private void initValue() {
        initShowPicArea();

        RectifyNotifyDraftInfo _draftInfo = (RectifyNotifyDraftInfo) getIntent().getSerializableExtra("data");
        if (_draftInfo != null) {
            try {
                dbID = _draftInfo.getId();
//                m_logID = _draftInfo.getLogId();
                m_checkUnit = _draftInfo.getInspectUnit();
                m_beCheckUnit = _draftInfo.getBeCheckedUnit();
//                m_edtLogId.setText(m_logID);
                m_edtCheckUnit.setText(_draftInfo.getInspectUnit());
                m_edtBecheckUnit.setText(_draftInfo.getBeCheckedUnit());
                m_edtSection.setText(_draftInfo.getSection());
                m_edtCheckMan.setText(_draftInfo.getInspectorSign());
                m_edtContent.setText(_draftInfo.getInspectContent());
                m_edtFindPro.setText(_draftInfo.getQuestion());
                userRealName = _draftInfo.getInspectorSign(); //检查人
                m_edtReformMethod.setText(_draftInfo.getRequest());
                m_edtCheckDate.setText(_draftInfo.getCheckTime().substring(0, 10));
                m_edtLogRectifyDate.setText(_draftInfo.getRectifyPeriod().substring(0, 10));

//                userRealName = UserSingleton.getUserInfo().getRealName(); //检查人
            } catch (Exception e) {
                String s = e.getMessage();
            }

            QueryUserListWork.queryUserInfo(userRealName, new OnUserListListener() {
                @Override
                public void onQuerySucceed(UserInfo userInfo) {
                    userId = userInfo.getUserId();
                    userLoginName = userInfo.getLoginName();
                    pictureName = _draftInfo.getImages();
                    if (pictureName != null && pictureName.length() > 0) {
                        _list = new ArrayList<>();
                        _list.addAll(Arrays.asList(pictureName.split("#")));
                        picNames.addAll(_list);
                        if (picNames.size() > 0) {//第一次下载第一张照片
                            downloadFirstPicture();
                        }
                    } else {
                        MyToast.showMyToast(ReadDraftNotifyInfoActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                        //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(ReadDraftNotifyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void downloadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            File picFile = new File(FoldersConfig.NOTICEFY, picNames.get(i));
            if (!picFile.exists()) {
                if (canDownLoad) {
                    itemPicName = picNames.get(i);
                    canDownLoad = false;
                    m_presenter.downloadLogPicture(picNames.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(i));
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

    private void downloadFirstPicture() {
        File picFile = new File(FoldersConfig.NOTICEFY, picNames.get(0));
        //本地不存在，从网上下载
        if (!picFile.exists()) {
            itemPicName = picNames.get(0);
            canDownLoad = false;
            m_presenter.downloadLogPicture(picNames.size(), 0);
            picNames.set(0, "");
        } else {
            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(0));
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
                m_gdvPic.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initShowPicArea() {

        //初始化拍照区域
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
//        imageItem = new ArrayList<>();
        m_gdvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    File _file = new File(FoldersConfig.NOTICEFY, _list.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    private void initEvent() {

        m_tvReply.setOnClickListener(this);
        m_tvBack.setOnClickListener(this);

    }

    private void initView() {
        m_edtLogId = $(R.id.edtlogid);
        m_edtCheckUnit = $(R.id.edtLogCheckUnit);
        m_edtBecheckUnit = $(R.id.edtLogBeCheckUnit);
        m_edtCheckDate = $(R.id.edtLogCheckDate);
        m_edtCheckMan = $(R.id.edtLogCheckMan);
        m_edtLogRectifyDate = $(R.id.edtLogRectifyDate);
//        m_btnAddPic = $(R.id.btnAddPic);
        m_gdvPic = $(R.id.gridView1);
        m_edtContent = $(R.id.edtCheckContent);
        m_edtFindPro = $(R.id.edtFindProblem);
        m_edtReformMethod = $(R.id.edtReformMethod);
//        m_btnPushlog = $(R.id.btnPushLog);
        m_edtSection = $(R.id.edtSection);
        m_tvBack = $(R.id.tvBack);

        m_btnSubmit = $(R.id.btnSubmit);
//        m_receiveMans = $(R.id.edtRecorder);
//        m_edtSupervisor = $(R.id.edtSupervisor);
        m_edtCopyer = $(R.id.edtCopy);
        m_edtConstruction = $(R.id.edtConstruction);
        m_tvReply = $(R.id.tvReply);
        m_btnSubmit.setOnClickListener(this);
//        m_receiveMans.setOnClickListener(this);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
//        m_edtSupervisor.setOnClickListener(this);
        m_edtCopyer.setOnClickListener(this);
        m_edtConstruction.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        int m_roid = UserSingleton.getUserInfo().getRoleId();
        switch (v.getId()) {
            case R.id.tvReply:
                Bundle _bundle = new Bundle();
                _bundle.putInt("dbID", dbID);
                _bundle.putString("logId", m_logID);
                _bundle.putString("checkUnit", m_checkUnit);
                _bundle.putString("beCheckUnit", m_beCheckUnit);
                _bundle.putString("checkMan", m_edtCheckMan.getText().toString());
                Intent _intent = new Intent(ReadDraftNotifyInfoActivity.this, NewRectifyReplyInfoActivity.class);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                break;

            case R.id.tvBack:

                finish();

                break;

            case R.id.btnSubmit:
//                uploadFirstPicture();
                if (checkInfo()) {
                    mReNoticePresenter.submit();
                }
                break;

            case R.id.edtSupervisor:

                if (m_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_THREE), GET_SUPERVISOR);
                }
                break;


            case R.id.edtCopy:
                if (m_roid == 17 ) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_COPYER);
                } else
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_CONSTRUCTION);

                break;

            case R.id.edtConstruction:

                startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_CONSTRUCTION);
                break;


            default:
                break;
        }
    }


    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
        int i = picFiles.size();
        int j = picNames.size();

        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            if (canUpload) {
                mReNoticePresenter.uploadFile(picFiles.size(), 0);
//                picNames.set(0, "");
                m_picIndex = 0;
                canUpload = false;
            }
        } else {
            MyToast.showMyToast(this, "请检查要上传的图片是否已被手动删除！", Toast.LENGTH_SHORT);
        }
    }


    /**
     * @return 获取下载URL
     */
    @Override
    public String getDownLoadURL() {

        return ServerConfig.URL_NOTICEFY_FILE_UPLOAD +
                getIdLoginName() + "/" + itemPicName;
    }

    @Override
    public String getSection() {
        return null;
    }

    @Override
    public String getLogID() {
        return m_edtLogId.getText().toString().trim();
    }

    @Override
    public String getCheckUnit() {
        return m_edtCheckUnit.getText().toString().trim();
    }

    @Override
    public String getBeCheckUnit() {
        return m_edtBecheckUnit.getText().toString().trim();
    }

    @Override
    public String getcheckDate() {
        try {
            return DateTimeUtils.longToDate2(System.currentTimeMillis(), DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getCheckContent() {
        return m_edtContent.getText().toString().trim();
    }

    @Override
    public String getQuestion() {
        return m_edtFindPro.getText().toString().trim();
    }

    @Override
    public String getRectifyRequest() {
        return m_edtReformMethod.getText().toString().trim();
    }

    @Override
    public String getCheckMan() {
        return m_edtCheckMan.getText().toString().trim();
    }

    @Override
    public String rectifyDate() {
        return m_edtLogRectifyDate.getText().toString() + " 00:00:00";
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

    /**
     * @return 获取登录名 id
     */
    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }

    @Override
    public String getReceiveMans() {
        StringBuffer _stringBuffer = new StringBuffer();
        _stringBuffer.append(m_edtConstruction.getText().toString());
        if (!m_edtCopyer.getText().toString().isEmpty())
            _stringBuffer.append("#" + m_edtCopyer.getText().toString());
//        if (!m_edtSupervisor.getText().toString().isEmpty())
//            _stringBuffer.append("#" + m_edtSupervisor.getText().toString());

//        LogUtils.i("接收人名单目录 = ", _stringBuffer.toString());
        return _stringBuffer.toString();
    }

    @Override
    public int getLogState() {
        return 0;
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

    @Override
    public void uploadFileSucceed(String msg) {
        picNames.set(m_picIndex, "");
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
            _message.what = 2;
            m_handler.sendMessageDelayed(_message, 500);
        } else {//图片上传成功，提交日志或者存为草稿
            mReNoticePresenter.submit();
        }
        //        resetParams();
    }

    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        resetParams();
    }

    @Override
    public void setLogId() {

    }

    @Override
    public String getReformDate() {
        return null;
    }

    @Override
    public String getReformMan() {
        return null;
    }

    @Override
    public String getReformCon() {
        return null;
    }

    @Override
    public String getReCheckCon() {
        return null;
    }

    @Override
    public String getReCheckMan() {
        return null;
    }

    @Override
    public int getDbID() {
        return 0;
    }

    @Override
    public int getDraftStatus() {
        return 0;
    }

    @Override
    public String getSupervisor() {
        return null;
    }

    @Override
    public String getOwner() {
        return null;
    }

    @Override
    public String getImgInfo() {
        return null;
    }

    /**
     * @return 获取照片名字
     */
    @Override
    public String getPictureName() {
        if (itemPicName != null && itemPicName.length() > 0)
            return itemPicName;
        return "";

    }

    /**
     * @param msg 展示下载照片
     * @Override
     */
    public void showLoadingPiture(String msg) {
        if (m_progressDialog != null) {
            m_progressDialog.setMessage(msg);
            m_progressDialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hideLoadingPicture() {
        if (m_progressDialog != null) {
            Message msg = new Message();
            msg.what = 1;
            m_handler.sendMessage(msg);
        }
    }

    /**
     * @param bitmap 下载成功
     */
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

    /**
     * @param msg 下载失败
     */
    @Override
    public void showLoadingFailed(String msg) {
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        Message _message = new Message();
        _message.what = 1;
        m_handler.sendMessage(_message);
    }

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

//                case GET_SUPERVISOR:
//                    if (resultCode == RESULT_OK) {
//                        String allNameList = "";
//                        m_edtSupervisor.setText("");
//                        for (String name : UsersList.getList()) {
//                            allNameList += name + "#";
//                        }
//                        m_edtSupervisor.setText(allNameList.substring(0, allNameList.length() - 1));
//                        UsersList.clearList();
//                    }
//                    break;

                case GET_COPYER:
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }
                        m_edtCopyer.setText("");
                        m_edtCopyer.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;

                case GET_CONSTRUCTION:
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }
                        m_edtConstruction.setText("");
                        m_edtConstruction.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
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

    private boolean checkInfo() {
        boolean isOk = true;

//
//        if (getLogID().length() == 0) {
//            m_edtLogId.setError("日志编号不能为空");
//            isOk = false;
//        }
        if (getCheckUnit().length() == 0) {
            m_edtCheckUnit.setError("检查单位不能为空");
            isOk = false;
        }
        if (getBeCheckUnit().length() == 0) {
            m_edtBecheckUnit.setError("受检单位不能为空");
            isOk = false;
        }
        if (getCheckContent().length() == 0) {
            m_edtContent.setError("检查内容不能为空");
            isOk = false;
        }
        if (getQuestion().length() == 0) {
            m_edtFindPro.setError("发现问题不能为空");
            isOk = false;
        }
        if (getRectifyRequest().length() == 0) {
            m_edtReformMethod.setError("措施与要求不能为空");
            isOk = false;
        }
        if (rectifyDate().length() < 10) {
            m_edtLogRectifyDate.setError("整改期限不能为空");
            isOk = false;
        }

//        if (getCheckMan().length() == 0) {
//            m_edtCheckMan.setError("检查人不能为空");
//            isOk = false;
//        }
        if (getReceiveMans().length() == 0) {
            m_edtCheckMan.setError("接收人不能为空");
            isOk = false;
        }
//
//        if (getSection().length() == 0) {
//
//            isOk = false;
//        }

//        if (getPicture().length() == 0) {
//            Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
//            isOk = false;
//        }
        return isOk;
    }
}
