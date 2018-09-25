package tianchi.com.risksourcecontrol.activitiy.notice;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.login.UserInfo;
import tianchi.com.risksourcecontrol.bean.login.UsersList;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol.config.FoldersConfig;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyDatePicker;
import tianchi.com.risksourcecontrol.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.model.OnUserListListener;
import tianchi.com.risksourcecontrol.presenter.LoadingNotifyInfoPresenter;
import tianchi.com.risksourcecontrol.presenter.RectifyNotifyInfoPresenter;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.CameraUtils;
import tianchi.com.risksourcecontrol.util.DateTimeUtils;
import tianchi.com.risksourcecontrol.util.FileUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.view.ILoadingNotifyView;
import tianchi.com.risksourcecontrol.view.IRectifyNotifyView;
import tianchi.com.risksourcecontrol.work.QueryUserListWork;

/**
 * Created by hairun.tian on 2018/6/13 0013.
 * 新建整改通知单
 * 权限:业主、监理、施工方都可以创建
 */

public class ReadDraftNotifyInfoActivity2 extends BaseActivity implements View.OnClickListener, IRectifyNotifyView, MyTakePicDialog.OnItemClickListener, ILoadingNotifyView {
    private static final int GET_SUPERVISOR = 0;
    private static final int GET_COPYER = 8;
    private static final int GET_CONSTRUCTION = 9;
    private int m_logState; //日志状态
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
    private Spinner m_spSection; //标段
    private Button m_btnSubmit; //提交
    private Button m_btnDraft; //草稿
    //    private EditText m_receiveMans; //接收人  @弃用
    private TextView m_tvBack; //返回
    private EditText m_edtSupervisor; //监理
    private EditText m_edtCopyer; //抄送着
    private EditText m_edtConstruction;//施工方


    private List<File> picFilesUp;             //临时图片文件数组
    private List<String> picNamesUp;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItemUp;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private File takPicFileUp;//用户头像拍照文件
    private File resultImgFileUp;//最终生成的img文件
    private Bitmap picBitmapUp;//存储用户修改头像

    private Uri fileUriUp;//生成拍照文件uri
    private Uri uriUp;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureNameUp = "";//照片全名xx.jpg
    private int m_picIndexUp = 0;
    private boolean canUploadUp = true;

    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private List<String> _list;              //图片文件名数组
    private int dbID;
    private String m_checkUnit;
    private String m_beCheckUnit;
    private String userRealName;
    private EditText m_edtSection;
    private int userId;
    private String userLoginName;
    private String pictureName;
    private String itemPicName = "";//子图片文件名
    private boolean canDownLoad;
    private Bitmap picBitmap;


    LoadingNotifyInfoPresenter m_presenter = new LoadingNotifyInfoPresenter(this);
    RectifyNotifyInfoPresenter mReNoticePresenter = new RectifyNotifyInfoPresenter(this);

    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
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

            }
            return false;
        }
    });


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
                msg.what = 0;
                m_handler.sendMessage(msg);
                break;
                //                                m_lock.unlock();
            }
        }
    }

    private void uploadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNamesUp.get(i));
            if (picFiles.get(i).getName().equals(picNames.get(i))) {
                if (canUploadUp) {
                    canUploadUp = false;
                    mReNoticePresenter.uploadFile(picFiles.size(), i);
                    m_picIndexUp = i;
//                    picNamesUp.set(i, "");
                    break;
                }
            } else {
                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
                break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_draft_notify_read);
        initView();
        initEvent();
        initValue();


    }

    private void initValue() {
//        m_edtLogId.setText("1");
//        m_edtCheckUnit.setText("检查单位");
//        m_edtBecheckUnit.setText("受检查单位");
////        m_edtLogRectifyDate.setText("整改期限");
//        m_edtContent.setText("检查内容");
//        m_edtFindPro.setText("发现问题");
//        m_edtReformMethod.setText("整改要求与方法");
        initTakePicArea();

        RectifyNotifyDraftInfo _draftInfo = (RectifyNotifyDraftInfo) getIntent().getSerializableExtra("data");
        if (_draftInfo != null) {
            try {
                dbID = _draftInfo.getId();
//                m_logID = _draftInfo.getLogId();
                m_checkUnit = _draftInfo.getInspectUnit();
                m_beCheckUnit = _draftInfo.getBeCheckedUnit();
//                m_edtLogId.setText(m_logID);
                m_edtCheckUnit.setText(_draftInfo.getInspectUnit() + "");
                m_edtBecheckUnit.setText(_draftInfo.getBeCheckedUnit() + "");
//                m_edtSection.setText(_draftInfo.getSection()+"");
                m_edtCheckMan.setText(_draftInfo.getInspectorSign() + "");
                m_edtContent.setText(_draftInfo.getInspectContent() + "");
                m_edtFindPro.setText(_draftInfo.getQuestion() + "");
                userRealName = _draftInfo.getInspectorSign(); //检查人
                m_edtReformMethod.setText(_draftInfo.getRequest() + "");
                m_edtCheckDate.setText(_draftInfo.getCheckTime().substring(0, 10) + "");
                m_edtLogRectifyDate.setText(_draftInfo.getRectifyPeriod().substring(0, 10) + "");
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
                        MyToast.showMyToast(ReadDraftNotifyInfoActivity2.this, "该日志无照片", Toast.LENGTH_SHORT);
                        //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(ReadDraftNotifyInfoActivity2.this, msg, Toast.LENGTH_SHORT);
                }
            });
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
            picFiles.add(picFile);
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

    private void initEvent() {
        m_btnAddPic.setOnClickListener(this);
        m_btnSubmit.setOnClickListener(this);
//        m_btnDraft.setOnClickListener(this);
//        m_receiveMans.setOnClickListener(this);
        m_edtLogRectifyDate.setOnClickListener(this);
        m_tvBack.setOnClickListener(this);
        m_edtSupervisor.setOnClickListener(this);
        m_edtCopyer.setOnClickListener(this);
        m_edtConstruction.setOnClickListener(this);
    }

    private void initView() {
//        View _view = $(R.id.layout);
//        _view.setVisibility(View.GONE);
        m_edtLogId = $(R.id.edtLogID);
        m_edtCheckUnit = $(R.id.edtLogCheckUnit);
        m_edtBecheckUnit = $(R.id.edtLogBeCheckUnit);
        m_edtCheckDate = $(R.id.edtLogCheckDate);
        m_edtCheckMan = $(R.id.edtLogCheckMan);
        m_edtLogRectifyDate = $(R.id.edtLogRectifyDate);
        m_btnAddPic = $(R.id.btnAddPic);
        m_gdvPic = $(R.id.gridView1);
        m_edtContent = $(R.id.edtCheckContent);
        m_edtFindPro = $(R.id.edtFindProblem);
        m_edtReformMethod = $(R.id.edtReformMethod);
        m_btnPushlog = $(R.id.btnPushLog);
        m_spSection = $(R.id.spSection);
        m_btnSubmit = $(R.id.btnSubmit);
//        m_btnDraft = $(R.id.btnDraft);
//        m_receiveMans = $(R.id.edtRecorder);
        m_tvBack = $(R.id.tvBack);
        m_edtSupervisor = $(R.id.edtSupervisor);
        m_edtConstruction = $(R.id.edtConstruction);
        m_edtCopyer = $(R.id.edtCopy);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("通知发送中...");
        m_progressDialog.setCancelable(true);
        m_spSection.setAdapter(new ArrayAdapter<String>(ReadDraftNotifyInfoActivity2.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.section2)));
//       long time = System.currentTimeMillis();
//        m_edtCheckDate.setText(DateTimeUtils.setCurrentTime());
//        m_edtCheckMan.setText(UserSingleton.getUserInfo().getRealName());
//        m_btnSubmit.setVisibility(View.GONE);
    }

    //查看图片
    private void viewPicture(int position) {
        if (picFilesUp.get(position) != null) {
            //打开照片查看
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(picFilesUp.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
            startActivity(intent);
        }
    }

    //初始化拍照区域
    private void initTakePicArea() {
        picFilesUp = new ArrayList<>();
        picNamesUp = new ArrayList<>();
        imageItemUp = new ArrayList<HashMap<String, Object>>();
        m_gdvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPicture(position);
            }
        });

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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        int m_roid = UserSingleton.getUserInfo().getRoleId();
//        LogUtils.i("m_roid = ", m_roid + "");
        switch (v.getId()) {
            case R.id.btnAddPic:
                int i = imageItemUp.size();
                if (imageItemUp.size() == 5) {
                    MyToast.showMyToast(ReadDraftNotifyInfoActivity2.this, "最多支持上传5张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }
                break;

            //提交通知单 先提交照片 然后提交日志
            case R.id.btnSubmit:
//                m_logState = 2;
//                if (checkInfo()) {
//                    uploadFirstPicture();
//                }
                if (checkInfo()) {
                    mReNoticePresenter.submit();
                }
                break;

            case R.id.btnDraft:
                m_logState = 1;
//                if (checkInfo()) {
                uploadFirstPicture();
//                }
                break;


            case R.id.edtRecorder:
//                startActivityForResult(new Intent(this, RelationshipListActivity.class), GET_RELATIONSHIP);
                break;

            case R.id.edtLogRectifyDate:
                MyDatePicker.ShowDatePicker(this, m_edtLogRectifyDate);
                break;

            case R.id.tvBack:
                finish();
                break;

            //如果是业主，可以选择监理
            case R.id.edtSupervisor:
                if (m_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_THREE), GET_SUPERVISOR);
//                    LogUtils.i("UserPermission = ", UserPermission.SUPERVISON_THREE + "");
                }
                break;

            case R.id.edtCopy:
                if (m_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_COPYER);
                } else
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_CONSTRUCTION);
                break;

            //如果是监理或者施工方，可以选择施工方
            case R.id.edtConstruction:
                startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_CONSTRUCTION);
                break;

            default:
                break;

        }


    }


    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNamesUp.get(0));
        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            if (canUploadUp) {
                mReNoticePresenter.uploadFile(picFilesUp.size(), 0);
//                picNamesUp.set(0, "");
                m_picIndexUp = 0;
                canUploadUp = false;
            }
        } else {
            MyToast.showMyToast(this, "请检查要上传的图片是否已被手动删除！", Toast.LENGTH_SHORT);
        }
    }

    /**
     * @return 日志id
     */
    @Override
    public String getLogID() {
        return m_edtLogId.getText().toString().trim();
    }

    /**
     * 检查单位
     *
     * @return
     */
    @Override
    public String getCheckUnit() {
        return m_edtCheckUnit.getText().toString().trim();
    }

    /**
     * 受检查单位
     *
     * @return
     */
    @Override
    public String getBeCheckUnit() {
        return m_edtBecheckUnit.getText().toString().trim();
    }

    /**
     * 检查日期
     *
     * @return
     */
    @Override
    public String getcheckDate() {
        try {
            return DateTimeUtils.longToDate2(System.currentTimeMillis(), DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查内容
     *
     * @return
     */
    @Override
    public String getCheckContent() {
        return m_edtContent.getText().toString().trim();
    }

    /**
     * 检查发现的问题
     *
     * @return
     */
    @Override
    public String getQuestion() {
        return m_edtFindPro.getText().toString().trim();
    }

    /**
     * 整改措施与方法
     *
     * @return
     */
    @Override
    public String getRectifyRequest() {
        return m_edtReformMethod.getText().toString().trim();
    }

    /**
     * 检查人
     *
     * @return
     */
    @Override
    public String getCheckMan() {
        return m_edtCheckMan.getText().toString().trim();
    }

    /**
     * 整改期限
     *
     * @return
     */
    @Override
    public String rectifyDate() {

        return m_edtLogRectifyDate.getText().toString() + " 00:00:00";
    }

    /**
     * 照片名字
     *
     * @return
     */
    @Override
    public String getPicture() {
//        String jointPictureName = "";
//        if (picFiles.size() == 0) {
//            jointPictureName = "";
//        } else if (picFiles.size() == 1) {
//            jointPictureName = picFiles.get(0).getName();
//        } else {
//            for (File _picFile : picFiles) {
//                jointPictureName += _picFile.getName() + "#";
//            }
//            jointPictureName = jointPictureName.substring(0, jointPictureName.length() - 1);
//        }
        return pictureName;
    }

    @Override
    public String getDownLoadURL() {

        return ServerConfig.URL_NOTICEFY_FILE_UPLOAD +
                getIdLoginName() + "/" + itemPicName;

    }

    /**
     * 登录人id和名字
     *
     * @return
     */
    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();

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
            picFiles.add(CameraUtils.bitMapToFile(picBitmap,
                    FoldersConfig.NOTICEFY, itemPicName));
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

    /**
     * 获取接收人信息
     *
     * @return
     */
    @Override
    public String getReceiveMans() {
        StringBuffer _stringBuffer = new StringBuffer();
        _stringBuffer.append(m_edtConstruction.getText().toString());
        if (!m_edtCopyer.getText().toString().isEmpty())
            _stringBuffer.append("#" + m_edtCopyer.getText().toString());
        if (!m_edtSupervisor.getText().toString().isEmpty())
            _stringBuffer.append("#" + m_edtSupervisor.getText().toString());

//        LogUtils.i("接收人名单目录 = ", _stringBuffer.toString());
        return _stringBuffer.toString();

    }

    /**
     * 获取日志状态
     *
     * @return
     */
    @Override
    public int getLogState() {

        return m_logState;
    }

    /**
     * 获取上传的照片
     *
     * @param position
     * @return
     */
    @Override
    public File getUploadFile(int position) {
        if (picFilesUp.size() > 0) {
            return picFilesUp.get(position);
        }
        return null;
    }


    @Override
    public String getSection() {
        return m_spSection.getSelectedItem().toString().trim();

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
        //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
        picNamesUp.set(m_picIndexUp, "");
        canUploadUp = true;//可上传标记
        boolean isFinish = false;//标记整个上传过程是否完成
        for (String _picName : picNamesUp) {
            if (!_picName.equals("")) {
                isFinish = false;
            } else {
                isFinish = true;
            }
        }
        if (!isFinish) {//上传未完成，继续发送下载指令
            Message _message = new Message();
            m_handler.sendMessageDelayed(_message, 500);
        } else {//图片上传成功，提交日志或者存为草稿
            if (m_logState == 1) {
                mReNoticePresenter.saveToDraft();
            } else
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

    /**
     * @return 获取整改日期
     */
    @Override
    public String getReformDate() {
        return null;
    }

    /**
     * @return 获取整改人
     */
    @Override
    public String getReformMan() {
        return null;
    }

    /**
     * @retrun 获取整改情况
     */
    @Override
    public String getReformCon() {
        return null;
    }

    /**
     * @return 获取复核情况
     */
    @Override
    public String getReCheckCon() {
        return null;
    }

    /**
     * @return 获取复核人签名
     */
    @Override
    public String getReCheckMan() {
        return null;
    }

    /**
     * @return 获取数据库id
     */
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
    public void setOnItemClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnTakePicture:
                takPicFileUp = new File(FoldersConfig.NOTICEFY, System.currentTimeMillis() + ".jpg");
                fileUriUp = Uri.fromFile(takPicFileUp);
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUriUp);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnPickFromAlbum:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CameraUtils.IMAGE_UNSPECIFIED);
                startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_GALLERY);
                break;
            //            case R.id.btnViewPicture:
            //                // 通过picBytes判断当前是否已经拍摄了照片或选择了照片
            //                if (picBitmapUp == null) {
            //                    MyToast.showMyToast(this, "还未拍摄或未选择!", Toast.LENGTH_SHORT);
            //                    return;
            //                }
            //                if (resultImgFileUp != null && resultImgFileUp.exists()) {
            //                    //打开照片查看
            //                    intent = new Intent();
            //                    intent.setAction(Intent.ACTION_VIEW);
            //                    intent.setDataAndType(Uri.fromFile(resultImgFileUp), CameraUtils.IMAGE_UNSPECIFIED);
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
                    uriUp = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUriUp);
                    if (uriUp != null) {
                        picBitmapUp = MediaStore.Images.Media.getBitmap(getContentResolver(), uriUp);//拍摄返回的bitmap
                        if (picBitmapUp != null) {
                            resultImgFileUp = new File(new URI(uriUp.toString()));//拍摄返回的图片file
                            if (!FileUtils.isFileNameIllegal(resultImgFileUp.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFileUp);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureNameUp = resultImgFileUp.getName();//拍摄返回的图片name
                                    picNamesUp.add(pictureNameUp);
                                    picFilesUp.add(resultImgFileUp);
                                    HashMap<String, Object> _map = new HashMap<>();
                                    _map.put("itemImage", picBitmapUp);
                                    imageItemUp.add(_map);

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
                    uriUp = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUriUp);
                    if (uriUp != null) {
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};//取媒体文件路径集合
                        Cursor c = getContentResolver().query(uriUp, filePathColumns, null, null, null);//取选中图片的cursor
                        c.moveToFirst();
                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
                        String imgpath = c.getString(colindex);//取文件相对手机路径
                        c.close();
                        picBitmapUp = BitmapFactory.decodeFile(imgpath);//拍摄返回的bitmap
                        if (picBitmapUp != null) {
                            resultImgFileUp = new File(imgpath);//拍摄返回的图片file
                            if (picNamesUp.contains(resultImgFileUp.getName())) {
                                MyToast.showMyToast(this, "请勿上传重复图片", Toast.LENGTH_SHORT);
                                return;
                            }
                            if (!FileUtils.isFileNameIllegal(resultImgFileUp.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFileUp);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureNameUp = resultImgFileUp.getName();//拍摄返回的图片name
                                    picNamesUp.add(pictureNameUp);
                                    picFilesUp.add(resultImgFileUp);
                                    HashMap<String, Object> _map = new HashMap<>();
                                    _map.put("itemImage", picBitmapUp);
                                    imageItemUp.add(_map);
                                    refreshGridviewAdapter();
                                    //                                    m_safetyLogPresenter.uploadFile();
                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;

                case GET_SUPERVISOR:
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        m_edtSupervisor.setText("");
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }
                        m_edtSupervisor.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;

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
        //            picBitmapUp = CameraUtils.getBitmapFromCG(this, requestCode, resultCode, data, fileUriUp, 400, 300);
        //            if (picBitmapUp != null) {
        //                picBitmapForUser = picBitmapUp;
        //                picBytes = CameraUtils.Bitmap2Bytes(picBitmapUp);
        //                picBase64String = CameraUtils.bitmap2StrByBase64(picBitmapUp);
        //                btnAddPic.setBackground(new BitmapDrawable(picBitmapUp));
        //                if (m_dialog != null)
        //                    m_dialog.dismiss();
        //            }
        //        } else {
        //            CameraUtils.getBitmapFromCG(this, requestCode, resultCode, data, fileUriUp, 400, 300);
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
                m_gdvPic.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
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
