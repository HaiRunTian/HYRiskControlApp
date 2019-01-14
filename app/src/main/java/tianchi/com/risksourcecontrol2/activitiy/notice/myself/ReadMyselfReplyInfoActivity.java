package tianchi.com.risksourcecontrol2.activitiy.notice.myself;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.VerityfReplyNotify;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.presenter.LoadingNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.view.ILoadingNotifyView;
import tianchi.com.risksourcecontrol2.work.QueryUserListWork;

/**
 * Created by hairun.tian on 2018/6/15 0015.
 * 查看自己提交的回复通知单
 *
 */

public class ReadMyselfReplyInfoActivity extends BaseActivity implements View.OnClickListener, ILoadingNotifyView {
    private static final int GET_RELATIONSHIP = 0;
    private static final int GET_OWNER = 3;
    private String m_logId;  //回复编号
    private EditText m_edtRectifyId;  //下达编号
    private String m_checkUnit;
    private String m_beCheckUnit;
    private String m_checkMan;
    private EditText m_edtCheckUnit;
    private EditText m_edtBeCheckUnit;
    private EditText m_edtReformDate;
    private EditText m_edtReformMan;
    private Button m_btnAddPic;
    private GridView m_gridView;
    private EditText m_edtReformCon;
    private EditText m_edtReCheckCon;
    private EditText m_edtReCheckMan;
    private EditText m_edtLogId;
    private Button m_btnSubmit;
    private TextView m_btnBack;
    private List<String> _list;              //图片文件名数组
    private String itemPicName = "";//子图片文件名
    private String userRealName = "";//用于取日志图片的用户真实姓名
    private int userId;
    private int id;//日志唯一索引id
    private String userLoginName = "";
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//提交进度
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件
    private Bitmap picBitmap;//存储用户修改头像
    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName = "";//照片全名xx.jpg
    private boolean canDownLoad;
    //    private ImageView m_imgSupervisor,m_imgOwner;
//    private Button m_btnPass, m_btnReject;
    private int m_LogState; //日志状态  1:草稿(未发送)，2:已删除，3,监理审核通过，4，监理驳回，5，业主审核通过，6，业主驳回
//    private EditText m_edtOwner;
    private EditText m_edtSup;
    private TextView m_tvSuper;
    private EditText m_edtOpinion;
    private RectifyReplyInfo m_notifyInfo;
    private View m_conOwner;
    private String[] m_arrayPicRemark;
    private int m_picIndex; //照片下标
    private String m_imgInfo;

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
//                case 2:
//                    /*提交修改代码片*/
//                    m_logInfoPresenter.submitModifyLog(ServerConfig.URL_SUBMIT_MODIFY_PRO_SAFETY_LOG,getModifyLogMap());
//                    break;
                default:
                    break;
            }
        }
    };
    private void downloadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            File picFile = new File(FoldersConfig.NOTICEFY, picNames.get(i));
            if (!picFile.exists()) {
                if (canDownLoad) {
                    m_picIndex = i;
                    itemPicName = picNames.get(i);
                    canDownLoad = false;
                    m_presenter.downloadLogPicture(picNames.size(), i);
                    picNames.set(i, "");
                    break;
                }
            } else {
//                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(i));
                Bitmap _bitmap = CameraUtils.getimage(FoldersConfig.NOTICEFY + picNames.get(i));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                if (!m_imgInfo.isEmpty()){
                    _map.put("remark",m_arrayPicRemark[i]);
                }else  _map.put("remark","");
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


    LoadingNotifyInfoPresenter m_presenter = new LoadingNotifyInfoPresenter(this);

    private void uploadPictures() {
        for (int i = 0; i < picNames.size(); i++) {
            if (picNames.get(i).equals("")) {
                continue;
            }
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(i).getName().equals(picNames.get(i))) {
                if (canUpload) {
                    canUpload = false;
                    picNames.set(i, "");
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
        setContentView(R.layout.activity_read_myself_reform);
        initView();
        initEvent();
        initValue();
    }

    private void initValue() {
        initShowPicArea();
        m_notifyInfo = (RectifyReplyInfo) getIntent().getSerializableExtra("data");
        m_imgInfo = m_notifyInfo.getImageInfos();
        if (m_imgInfo.contains("#")){
            m_arrayPicRemark = m_imgInfo.split("#");
        }else m_arrayPicRemark = new String[]{m_imgInfo};

        if (m_notifyInfo != null) {
            m_edtLogId.setText(m_notifyInfo.getLogId()+"");
            m_edtRectifyId.setText(m_notifyInfo.getRectifyLogID()+"");
            m_edtCheckUnit.setText(m_notifyInfo.getInspectUnit()+"");
            m_edtBeCheckUnit.setText(m_notifyInfo.getBeCheckedUnit()+"");
            m_edtReformDate.setText(m_notifyInfo.getCheckTime().substring(0, 10)+"");
            m_edtReformCon.setText(m_notifyInfo.getRectifySituation()+"");
            m_edtReCheckCon.setText(m_notifyInfo.getReviewSituation()+"");
            m_edtReformMan.setText(m_notifyInfo.getRectifyManSign()+"");
            m_edtReCheckMan.setText(m_notifyInfo.getReviewerSign()+"");
            m_edtSup.setText(m_notifyInfo.getSupervisisonMans()+"");
            userRealName = m_notifyInfo.getRectifyManSign();
            //如果整改单的发起人是业主并当前用户是监理，就显示下一审核人控件

            QueryUserListWork.queryUserInfo(userRealName, new OnUserListListener() {
                @Override
                public void onQuerySucceed(UserInfo userInfo) {
                    userId = userInfo.getUserId();
                    userLoginName = userInfo.getLoginName();

                    pictureName = m_notifyInfo.getImages();
                    if (pictureName != null && pictureName.length() > 0) {
                        _list = new ArrayList<>();
                        _list.addAll(Arrays.asList(pictureName.split("#")));
                        picNames.addAll(_list);
                        if (picNames.size() > 0) {//第一次下载第一张照片

                            downloadFirstPicture();
                        }
                    } else {
                        MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                        //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                }
            });
        }

        if (m_notifyInfo.isRead() == 1) {  //本消息第一次阅读，返回给服务器标示已经读取过此信息

            int _dbID = m_notifyInfo.getId();
            String readName = UserSingleton.getUserInfo().getRealName();
            boolean read = true;
            JSONObject _jsonObject = new JSONObject();

            try {
                _jsonObject.put("pid", _dbID);
                _jsonObject.put("realName", readName);
                _jsonObject.put("read", read);
                String str = _jsonObject.toString();
                OkHttpUtils.postAsync(ServerConfig.URL_UPDATE_NOTIFY_STATE_REPLY, str, new OkHttpUtils.InsertDataCallBack() {
                    @Override
                    public void requestFailure(Request request, IOException e) {
                        MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, "失败", Toast.LENGTH_SHORT);
                    }
                    @Override
                    public void requestSuccess(String result) throws Exception {
                        int status = GsonUtils.getIntNoteJsonString(result, "status");
                        String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                        if (status == 0) {
                            MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                        } else if (status == -1) {
                            MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                        } else if (status == 1) {
                            MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void downloadFirstPicture() {
        File picFile = new File(FoldersConfig.NOTICEFY, picNames.get(0));
        if (!picFile.exists()) {
            m_picIndex = 0;
            itemPicName = picNames.get(0);
            canDownLoad = false;
            m_presenter.downloadLogPicture(picNames.size(), 0);
            picNames.set(0, "");
        } else {
//            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(0));
            Bitmap _bitmap = CameraUtils.getimage(FoldersConfig.NOTICEFY + picNames.get(0));
            HashMap<String, Object> _map = new HashMap<>();
            _map.put("itemImage", _bitmap);
            if (!m_imgInfo.isEmpty()){
                _map.put("remark",m_arrayPicRemark[0]);
            }else  _map.put("remark","");
            imageItem.add(_map);
            refreshGridviewAdapter();
            picNames.set(0, "");
            canDownLoad = true;
            Message msg = new Message();
            msg.what = 0;
            m_handler.sendMessage(msg);
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
                    File _file = new File(FoldersConfig.NOTICEFY, _list.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });
    }

    private boolean canUpload = true;

    private void initEvent() {

        m_btnBack.setOnClickListener(this);

    }

    private void initView() {
        m_arrayPicRemark = new String[]{};
        TextView tvTitle = (TextView) findViewById(R.id.tvTitles);
        tvTitle.setText("整改回复单");
        m_edtLogId = $(R.id.edtlogid);  //回复通知单编号
        m_edtRectifyId = $(R.id.edtnotifyreplyid);  //下达通知单编号
        m_edtCheckUnit = $(R.id.edtLogCheckUnit);
        m_edtBeCheckUnit = $(R.id.edtLogBeCheckUnit);
        m_edtReformDate = $(R.id.edtLogCheckDate);
        m_edtReformMan = $(R.id.edtLogReformMan);
        m_gridView = $(R.id.gridView1);
        //整改情况
        m_edtReformCon = $(R.id.edtReformCondition);
        //复核
        m_edtReCheckCon = $(R.id.edtReCheck);
        m_tvSuper = $(R.id.tvSupervisor);
        m_tvSuper.setText(getResources().getString(R.string.string_recive_man));

        //复核人签名
        m_edtReCheckMan = $(R.id.edtLogReCheckMan);
        m_btnBack = $(R.id.tvBack);
//        m_imgOwner = $(R.id.imgOwner);
//        m_btnPass = $(R.id.btnPass);
//        m_btnReject = $(R.id.btnReject);
//        m_edtOwner = $(R.id.edtOwner);
        m_edtSup = $(R.id.edtSupervisor);
//        m_edtOpinion = $(R.id.edtCheckOpinion);
//        m_edtOwner.setOnClickListener(this);
//        m_btnReject.setOnClickListener(this);
//        m_btnPass.setOnClickListener(this);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
        m_conOwner = $(R.id.layoutCon);

        //如果审批的用户是施工方和业主，就不会显示下一步审核人
        if (UserSingleton.getUserRoid() == 17 || UserSingleton.getUserRoid() == 20){
            m_conOwner.setVisibility(View.GONE);
        }

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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:
                break;
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnAddPic:

                break;

            case R.id.edtOwner:
                int _roid = UserSingleton.getUserInfo().getRoleId();
                if (_roid == 19) {  //监理
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_SECOND), GET_OWNER);
                }
                break;

            /*
            *角色是业主或者监理 修改日志状态 logState
            * 1:草稿(未发送)，2:已删除，3,监理审核通过，4，监理驳回，5，业主审核通过，6，业主驳回；
            *通过发送到上级，转告给下级，不通过，驳回给下级
             */
            //通过
            case R.id.btnPass:
//                int result = 0;

                if (!UserSingleton.getUserInfo().getManagerSection().equals("J0")) {
//                    m_imgSupervisor.setVisibility(View.VISIBLE);
//                    m_imgSupervisor.setImageResource(R.mipmap.owner_pass);
                    //监理审核通过
                    m_LogState = 3;

                } else {
                    //业主审核通过
//                    m_imgOwner.setVisibility(View.VISIBLE);
//                    m_imgOwner.setImageResource(R.mipmap.supervisor_pass);
                    m_LogState = 5;
                }
//                submit(m_LogState);
                break;

            //驳回
            case R.id.btnReject:
                if (!UserSingleton.getUserInfo().getManagerSection().equals("J0")) {
//                    m_imgSupervisor.setImageResource(R.mipmap.owner_reject);
//                    m_imgSupervisor.setVisibility(View.VISIBLE);
                    //监理审核驳回
                    m_LogState = 4;
                } else {
                    //业主审核驳回
//                    m_imgOwner.setVisibility(View.VISIBLE);
//                    m_imgOwner.setImageResource(R.mipmap.supervisor_reject);
                    m_LogState = 6;
                }
//                submit(m_LogState);
                break;
            default:
                break;
        }

    }

    private void submit(int reuslt) {
        VerityfReplyNotify _info = new VerityfReplyNotify();
        _info.setRectifyReplyId(m_notifyInfo.getId());
//        _info.setSendNames(m_edtOwner.getText().toString());
        _info.setResult(reuslt);
        _info.setContext(m_edtOpinion.getText().toString());
        int _roid = UserSingleton.getUserRoid();
        _info.setRoletype(_roid);

        String json = GsonUtils.objectToJson(_info);
//        LogUtils.i("审批传数据", json);
        OkHttpUtils.postAsync(ServerConfig.URL_SUBMIT_SUPERVISOR, json, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, "提交失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                int status = GsonUtils.getIntNoteJsonString(result, "status");
                String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                if (status == -2) {
//                    LogUtils.i("msg", msg);
                    MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
//                    LogUtils.i("msg", msg);
                    MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 0) {
//                    LogUtils.i("msg", msg);
                } else {
//                    LogUtils.i("msg", msg);
                    MyToast.showMyToast(ReadMyselfReplyInfoActivity.this, msg, Toast.LENGTH_SHORT);
                }
            }
        });
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
                                    //m_safetyLogPresenter.uploadFile();
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
                                    //       m_safetyLogPresenter.uploadFile();
                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;

                case GET_OWNER:
                    if (resultCode == RESULT_OK) {
//                        m_edtOwner.setText("");
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }
//                        m_edtOwner.setText(allNameList.substring(0, allNameList.length() - 1));
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

    //刷新图片区域gridview
    private void refreshGridviewAdapter() {
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic2, new String[]{"itemImage","remark"}, new int[]{R.id.imageView1,R.id.tv1});
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
                }else if (view instanceof TextView){
                    TextView _textView = (TextView) view;
                    _textView.setText(textRepresentation);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return 获取下载URL
     */
    @Override
    public String getDownLoadURL() {

        return ServerConfig.URL_NOTICEFY_FILE_UPLOAD +
                getIdLoginName() + "/" + itemPicName;
    }

    /**
     * @return 获取登录名 id
     */
    @Override
    public String getIdLoginName() {
        return userId + userLoginName;
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
     */
    @Override
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
            if (!m_imgInfo.isEmpty()){
                _map.put("remark",m_arrayPicRemark[m_picIndex]);
            }else  _map.put("remark","");
            imageItem.add(_map);
            refreshGridviewAdapter();
            //   MyToast.showMyToast(this, "成功加载" + itemPicName, Toast.LENGTH_SHORT);
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
//        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        Message _message = new Message();
        _message.what = 1;
        m_handler.sendMessage(_message);
    }
}
