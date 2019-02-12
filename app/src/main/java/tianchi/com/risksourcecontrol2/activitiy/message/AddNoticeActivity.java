package tianchi.com.risksourcecontrol2.activitiy.message;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.log.SafetyLogInfoActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.bean.notice.SendNotice;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyDatePicker;
import tianchi.com.risksourcecontrol2.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.work.UpDownLoadFileWork;

/**
 * Created by hairun.tian on 2018/3/7 0007.
 */

public class AddNoticeActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, MyTakePicDialog.OnItemClickListener {
    private final int GET_RELATIONSHIP = 0;
    private TextView m_tvClose; //返回按钮
    private EditText m_edtTitle;//标题
    private EditText m_edtType;//类型
    private TextView m_edtTime;//时间
    private EditText m_edtReMan;//接收人
    private EditText m_edtContent;//内容
    private Button m_btnSendMsg;//发送按钮
    private AlertDialog m_dialog;//拍照选择弹窗
    private String pictureName = "";//存放照片字符串
    private Bitmap picBitmap;//存放照片位图
    private File resultImgFile;//存放照片位图
    private Uri fileUri;
    private Uri uri;
    private String m_time;
    private GridView m_gridView;
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private List<String> picNames;           //临时图片文件名数组
    private List<File> picFiles;             //临时图片文件数组
    private SimpleAdapter simpleAdapter;     //适配器
    private boolean canUpload = true;
    private ProgressDialog m_progressDialog;
    private String endFile;
    private TextView m_edtAddFile; //附件名称
    private OkHttpClient client;
    private String fileType; //文件类型
    private String m_fileName; //文件名字


    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    fileType = m_fileName.substring(m_fileName.lastIndexOf("."), m_fileName.length());
                    m_edtAddFile.setText(m_fileName);
                    break;
                case 2:
                    m_progressDialog.dismiss();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_news);
        initView();
        initValue();
        initOkHttp();
    }


    private void initValue() {

//        initTakePicArea();

        //        m_safetyLogPresenter.loadSectionList();//加载标段
    }


    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }


    private void initView() {
        m_tvClose = (TextView) findViewById(R.id.tvClose);
        m_tvClose.setOnClickListener(this);
        m_edtTitle = (EditText) findViewById(R.id.edtTitle);
        m_edtType = (EditText) findViewById(R.id.edtType);
        m_edtTime = (TextView) findViewById(R.id.edtTime);
        m_edtReMan = (EditText) findViewById(R.id.edtReceiveMan);
        m_edtContent = (EditText) findViewById(R.id.edtContent);
        m_btnSendMsg = (Button) findViewById(R.id.btnSend);
        m_edtAddFile = (TextView) findViewById(R.id.tvFileName);
        m_edtAddFile.setOnClickListener(this);
        m_btnSendMsg.setOnClickListener(this);
        m_edtTime.setOnClickListener(this);
        m_edtReMan.setOnClickListener(this);
        m_edtReMan.setOnLongClickListener(this);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("通知正在发送中...");
        m_progressDialog.setCancelable(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                finish();
                break;
            //发送通知
            case R.id.btnSend:
                //有附件上传的情况
                if (isCheck()) {
                    if (m_edtAddFile.getText().toString().length() != 0) {
                        uploadFile(m_fileName, UserSingleton.getUserInfo().getUserId() + UserSingleton.getUserInfo().getLoginName());
                    } else {
                        sendNotice();
                    }
                }
//                uploadFirstPicture();
//                sendNotice();
                break;
            //选择文件
            case R.id.tvFileName:
                startActivityForResult(new Intent(AddNoticeActivity.this, CheckFileActivity.class), GET_RELATIONSHIP);
                break;
            case R.id.edtTime:
                MyDatePicker.ShowDatePicker(this, m_edtTime);
                break;

            case R.id.edtReceiveMan:
                startActivityForResult(new Intent(this, RelationshipListActivity.class), GET_RELATIONSHIP);
            default:
                break;
        }

    }

    //上传文件
    private void uploadFile(String fileName, String idLoginName) {
        File file = new File(FoldersConfig.SD_FILE, fileName);
        m_progressDialog.show();
        UpDownLoadFileWork.upLoadFile2Server(file, ServerConfig.URL_UPLOAD_FILE, "notify", idLoginName, new OnUploadFileListener() {
            @Override
            public void uploadSucceed(String msg) {
//                m_progressDialog.setMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.showMyToast(AddNoticeActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
                    }
                });
                sendNotice();
                m_progressDialog.dismiss();
                Message _message = new Message();
                _message.what = 2;
                m_handler.sendMessageDelayed(_message, 500);
            }

            @Override
            public void uploadFailed(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.showMyToast(AddNoticeActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
                        m_progressDialog.setMessage(msg);
                    }
                });

                Message _message = new Message();
                _message.what = 2;
                m_handler.sendMessageDelayed(_message, 500);

            }
        });
    }


    public String getTitleName() {
        return m_edtTitle.getText().toString();
    }

    public String getNoticeTitle() {
        return m_edtTitle.getText().toString();
    }


    public String getTime() {

        return m_edtTime.getText().toString() + " 00:00:00";
    }


    public String getReceiveMan() {
        return m_edtReMan.getText().toString();
    }


    public String getMsgContent() {
        return m_edtContent.getText().toString();
    }


    public void setAccount() {

    }


    public String getAccount() {
        return null;
    }


    public String getReformAccount() {
        return null;
    }


    public void setSaveTime() {

    }


    public Date getSaveTime() {
        return null;
    }
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }

    public File getUploadFile(int position) {
        //        if (resultImgFile != null)
        //            return resultImgFile;
        //        return null;
        if (picFiles.size() > 0) {
            return picFiles.get(position);
        }
        return null;
    }


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


    public void showSubmitSucceed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        finish();
    }


    public void showSubmitFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        finish();
    }


    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        resetParams();
    }

    private void sendNotice() {
        SendNotice _sendNotice = getString();
        String _jsonObject = GsonUtils.objectToJson(_sendNotice);
        OkHttpUtils.postAsync(ServerConfig.URL_SEND_NOTICE, _jsonObject, new OkHttpUtils.InsertDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
//                LogUtils.i("发送通知", request.body().toString());
                MyToast.showMyToast(AddNoticeActivity.this, "通知发送失败", Toast.LENGTH_SHORT);

            }

            @Override
            public void requestSuccess(String result) throws Exception {
//                LogUtils.i("发送通知", result);
                int status = GsonUtils.getIntNoteJsonString(result, "status");
                String msg = GsonUtils.getStringNodeJsonString(result, "msg");
                if (status == 1) {
                    MyToast.showMyToast(AddNoticeActivity.this, msg, Toast.LENGTH_SHORT);
                    finish();
                } else if (status == 0) {
                    MyToast.showMyToast(AddNoticeActivity.this, msg, Toast.LENGTH_SHORT);

                } else {
                    MyToast.showMyToast(AddNoticeActivity.this, msg, Toast.LENGTH_SHORT);
                }
            }
        });


    }


    //dialog里选择拍照、相册、取消
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GET_RELATIONSHIP:
                if (resultCode == RESULT_OK) {
                    String allNameList = "";
                    for (String name : UsersList.getList()) {
                        allNameList += name + "#";
                    }
                    m_edtReMan.setText(allNameList.substring(0, allNameList.length() - 1));
                    UsersList.clearList();
                } else if (resultCode == 2) {
                    m_fileName = data.getStringExtra("file");
                    Message _message = new Message();
                    _message.what = 1;
                    m_handler.sendMessageDelayed(_message, 500);
                }
                break;
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
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.edtReceiveMan) {
            MyAlertDialog.showAlertDialog(this, "已选人员列表", getReceiveMan());
        }
        return false;
    }

    private SendNotice getString() {


        SendNotice _sendNotice = new SendNotice();
        _sendNotice.setTitleName(m_edtTitle.getText().toString());
        _sendNotice.setDataTime(m_edtTime.getText().toString() + " 00:00:00");
        _sendNotice.setReceiveMans(m_edtReMan.getText().toString());
        _sendNotice.setTextContent(m_edtContent.getText().toString());
        _sendNotice.setSendName(UserSingleton.getUserInfo().getRealName());
        if (m_edtAddFile.getText().toString().length() != 0) {
            _sendNotice.setFileType(fileType);
            _sendNotice.setFileUrl(m_fileName);
        } else {
            _sendNotice.setFileType(" ");
            _sendNotice.setFileUrl(" ");
        }


        return _sendNotice;
    }


    private boolean isCheck() {
        boolean isOk = true;
        if (getTitleName().isEmpty() || m_edtTime.getText().toString().isEmpty() ||
                getReceiveMan().isEmpty() || getMsgContent().isEmpty()) {
            MyToast.showMyToast(AddNoticeActivity.this, "内容不能为空，请填写了再发送通知！", Toast.LENGTH_SHORT);
            isOk = false;
        }

        return isOk;

    }


}
