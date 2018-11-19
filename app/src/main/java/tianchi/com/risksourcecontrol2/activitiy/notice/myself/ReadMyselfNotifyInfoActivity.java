package tianchi.com.risksourcecontrol2.activitiy.notice.myself;

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
import android.widget.Button;
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

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.notice.NewRectifyReplyInfoActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.presenter.LoadingNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.view.ILoadingNotifyView;
import tianchi.com.risksourcecontrol2.work.QueryUserListWork;

/**
 * Created by hairun.tian on 2018/6/21 0021.
 * 查看我自己的发起的整改通知单
 */

public class ReadMyselfNotifyInfoActivity extends BaseActivity implements View.OnClickListener, ILoadingNotifyView {

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
    private List<String> _list;              //图片文件名数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private String pictureName = "";//多图片拼接文件名
    private String itemPicName = "";//子图片文件名
    private String userRealName = "";//用于取日志图片的用户真实姓名
    private Bitmap picBitmap;
    private int userId;
    private int dbID;//日志唯一索引id
    private String userLoginName = "";
    private TextView m_tvReply; //回复按钮
    private String m_logID;
    private String m_checkUnit;
    private String m_beCheckUnit;
    private boolean canDownLoad;
    private SimpleAdapter simpleAdapter;     //适配器

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

    LoadingNotifyInfoPresenter m_presenter = new LoadingNotifyInfoPresenter(this);
    private String _firstName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_reform_notify_read);
        initView();
        initEvent();
        initValue();
    }

    //初始化数据
    private void initValue() {
        initShowPicArea();
        RectifyNotifyInfo _rectifyNotifyInfo = (RectifyNotifyInfo) getIntent().getSerializableExtra("data");
        m_imgInfo = _rectifyNotifyInfo.getImageInfos();
        if (m_imgInfo != null && m_imgInfo.contains("#")){
            m_arrayPicRemark = m_imgInfo.split("#");
        }else m_arrayPicRemark = new String[]{m_imgInfo};

        String _firstName;

        //第一个接收人的名字
        String _receiverMans = _rectifyNotifyInfo.getReceiverMans();
        if (_receiverMans.contains("#")) {
             _firstName = _receiverMans.substring(0, _receiverMans.indexOf("#"));
        } else {
            _firstName = _receiverMans;
        }
        if (!_firstName.equals(UserSingleton.getUserInfo().getRealName())) m_tvReply.setVisibility(View.GONE    );

        //如果是业主或者监理查看整改单，不可以回复
        if (UserSingleton.getUserRoid() == 19 || UserSingleton.getUserRoid() == 17) {
            m_tvReply.setVisibility(View.GONE);
        }
        if (_rectifyNotifyInfo != null) {

            try {
                dbID = _rectifyNotifyInfo.getId();
                m_logID = _rectifyNotifyInfo.getLogId();
                m_checkUnit = _rectifyNotifyInfo.getInspectUnit();
                m_beCheckUnit = _rectifyNotifyInfo.getBeCheckedUnit();
                m_edtLogId.setText(m_logID);
                m_edtCheckUnit.setText(_rectifyNotifyInfo.getInspectUnit());
                m_edtBecheckUnit.setText(_rectifyNotifyInfo.getBeCheckedUnit());
                m_edtSection.setText(_rectifyNotifyInfo.getSection());
                m_edtCheckMan.setText(_rectifyNotifyInfo.getInspectorSign());
                m_edtContent.setText(_rectifyNotifyInfo.getInspectContent());
                m_edtFindPro.setText(_rectifyNotifyInfo.getQuestion());
                userRealName = _rectifyNotifyInfo.getInspectorSign(); //检查人
                m_edtReformMethod.setText(_rectifyNotifyInfo.getRequest());
                m_receiveMans.setText(_rectifyNotifyInfo.getReceiverMans());
                m_edtCheckDate.setText(_rectifyNotifyInfo.getCheckedTime().substring(0, 10));
                m_edtLogRectifyDate.setText(_rectifyNotifyInfo.getRectifyPeriod().substring(0, 10));
//                userRealName = UserSingleton.getUserInfo().getRealName(); //检查人
            } catch (Exception e) {
                String s = e.getMessage();
            }
            QueryUserListWork.queryUserInfo(userRealName, new OnUserListListener() {
                @Override
                public void onQuerySucceed(UserInfo userInfo) {
                    userId = userInfo.getUserId();
                    userLoginName = userInfo.getLoginName();
                    pictureName = _rectifyNotifyInfo.getImages();
                    if (pictureName != null && pictureName.length() > 0) {
                        _list = new ArrayList<>();
                        _list.addAll(Arrays.asList(pictureName.split("#")));
                        picNames.addAll(_list);
                        if (picNames.size() > 0) {//第一次下载第一张照片
                            downloadFirstPicture();
                        }
                    } else {
                        MyToast.showMyToast(ReadMyselfNotifyInfoActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                        //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                    }
                }

                @Override
                public void onQueryFailed(String msg) {
                    MyToast.showMyToast(ReadMyselfNotifyInfoActivity.this, msg, Toast.LENGTH_SHORT);
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
                m_gdvPic.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initShowPicArea() {
        picNames = new ArrayList<>();
        imageItem = new ArrayList<>();
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
        m_arrayPicRemark = new String[]{};
        TextView tvTitle = (TextView) findViewById(R.id.tvTitles);
        tvTitle.setText("整改下达单");
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
        m_receiveMans = $(R.id.edtRecorder);
        m_tvReply = $(R.id.tvReply);
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
        m_tvReply.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReply:
                Bundle _bundle = new Bundle();
                _bundle.putInt("dbID", dbID);
                _bundle.putString("logId", m_logID);
                _bundle.putString("checkUnit", m_checkUnit);
                _bundle.putString("beCheckUnit", m_beCheckUnit);
                _bundle.putString("checkMan", m_edtCheckMan.getText().toString());
                Intent _intent = new Intent(ReadMyselfNotifyInfoActivity.this, NewRectifyReplyInfoActivity.class);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                break;

            case R.id.tvBack:
                finish();
                break;

            case R.id.btnSubmit:

                break;

            case R.id.edtReceiveMan:


                break;
            default:
                break;
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
}
