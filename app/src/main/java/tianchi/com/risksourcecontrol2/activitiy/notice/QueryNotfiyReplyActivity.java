package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.presenter.LoadingNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;
import tianchi.com.risksourcecontrol2.util.ThreadUtil;
import tianchi.com.risksourcecontrol2.view.ILoadingNotifyView;
import tianchi.com.risksourcecontrol2.work.QueryUserListWork;

import static tianchi.com.risksourcecontrol2.util.GsonUtils.jsonToBean;

public class QueryNotfiyReplyActivity extends BaseActivity implements View.OnClickListener,ILoadingNotifyView {
    private ProgressDialog m_progressDialog;//提交进度
    private EditText m_edtLogId; //日志编号
    private EditText m_edtCheckUnit; //检查单位
    private EditText m_edtBecheckUnit; //受检单位
    private EditText m_edtCheckDate; //检查时间
    private EditText m_edtCheckMan; //检查人
    private EditText m_edtLogRectifyDate; //整改期限日期
    private GridView m_gdvPic; //
    private GridView m_gridViewReply;

    private EditText m_edtContent; //检查内容
    private EditText m_edtFindPro; //发现问题
    private EditText m_edtReformMethod; // 整改措施与要求
    private EditText m_edtSection; //标段
    private TextView m_tvBack; //返回
    private EditText m_receiveMans; //接收人
    private TextView m_tvReply; //回复按钮
    private LinearLayout Notify_Reply;
    private EditText edtlogidReply;
    private EditText edtnotifyreplyidReply;
    private EditText edtLogCheckUnitReply;
    private EditText edtLogBeCheckUnitReply;
    private EditText edtLogCheckDateReply;
    private EditText edtNotifyLogReformManReply;
    private EditText edtNotifyReformConditionReply;
    private EditText edtNotifyReCheckReply;
    private EditText edtNotifyLogReCheckManReply;
    private EditText edtSupervisor;
    private EditText edtOwnerReply;
    private RectifyNotifyInfo rectifyNotifyInfo;

    private ArrayList<String> picNames;//图片的集合
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private ArrayList<String> picNamesReply;//图片的集合
    private ArrayList<HashMap<String, Object>> imageItemReply;//适配器数据
    private Bitmap picBitmap;
    private ArrayList<String> _list;
    private ArrayList<String> _listReply;
    private int m_picIndex; //照片下标
    private String pictureName = "";//多图片拼接文件名
    private String itemPicName = "";//子图片文件名
    private String itemPicNameReply = "";//子图片文件名
    LoadingNotifyInfoPresenter m_presenter = new LoadingNotifyInfoPresenter(this);
    private boolean canDownLoad;
    private String m_imgInfo;
    private String[] m_arrayPicRemark;
    private String m_imgInfoReply;
    private String[] m_arrayPicRemarkReply;
    private SimpleAdapter simpleAdapter;     //适配器
    private SimpleAdapter simpleAdapterReply;     //适配器
    private View m_OwerViwe;

    private String m_reply_loginName;
    private int m_reply_userId;
    private ArrayList<Object> m_reply_list;
    private String m_reply_pictureName;
    private int m_notifyStatus = 0; //判断下载是整改单图片还是回复单图片 0 = 整改单   1 = 回复单

    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    downloadPictures(0);
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
                    downloadPictures(2);
                    break;

                default:
                    break;
            }
        }
    };
    private ArrayList<String> m_ReplyList;
    private String m_InspectorSign;
    private int m_userId;
    private String m_userName;
    private String rectifyManSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_notfiy_reply);
        //初始化控件
        initView();
        //设置数据
        initData();

    }


    private void initView() {
        m_tvBack = ((TextView) findViewById(R.id.tvBack));
        m_tvBack = $(R.id.tvBack);
        m_tvBack.setOnClickListener(this);
        m_tvReply = ((TextView) findViewById(R.id.tvReply));
        m_tvReply.setOnClickListener(this);
        //通知单
        m_edtLogId =((EditText) findViewById(R.id.edtlogid));
        m_edtCheckUnit = ((EditText) findViewById(R.id.edtLogCheckUnit));
        m_edtBecheckUnit =((EditText) findViewById(R.id.edtLogBeCheckUnit));
        m_edtSection = ((EditText) findViewById(R.id.edtSection));
        m_edtCheckDate = ((EditText) findViewById(R.id.edtLogCheckDate));
        m_edtCheckMan = ((EditText) findViewById(R.id.edtLogCheckMan));
        m_edtLogRectifyDate = ((EditText) findViewById(R.id.edtLogRectifyDate));
        m_gdvPic =((GridView) findViewById(R.id.gridView1));

        m_edtContent = ((EditText) findViewById(R.id.edtCheckContent));
        m_edtFindPro = ((EditText) findViewById(R.id.edtFindProblem));
        m_edtReformMethod = ((EditText) findViewById(R.id.edtReformMethod));
        m_receiveMans =((EditText) findViewById(R.id.edtRecorder));
        //回复单显示
        Notify_Reply = $(R.id.Notify_Reply);
        edtlogidReply = ((EditText) findViewById(R.id.edtlogidReply));
        edtnotifyreplyidReply = ((EditText) findViewById(R.id.edtnotifyreplyidReply));
        edtLogCheckUnitReply = ((EditText) findViewById(R.id.edtLogCheckUnitReply));
        edtLogBeCheckUnitReply = ((EditText) findViewById(R.id.edtLogBeCheckUnitReply));
        edtLogCheckDateReply = ((EditText) findViewById(R.id.edtLogCheckDateReply));
        edtNotifyLogReformManReply = ((EditText) findViewById(R.id.edtNotifyLogReformManReply));
        m_gridViewReply = ((GridView) findViewById(R.id.gridView1Reply));
        edtNotifyReformConditionReply = ((EditText) findViewById(R.id.edtNotifyReformConditionReply));
        edtNotifyReCheckReply = ((EditText) findViewById(R.id.edtNotifyReCheckReply));
        edtNotifyLogReCheckManReply = ((EditText) findViewById(R.id.edtNotifyLogReCheckManReply));
        edtSupervisor = ((EditText) findViewById(R.id.edtSupervisor));
        edtOwnerReply = ((EditText) findViewById(R.id.edtOwner));
        m_OwerViwe = findViewById(R.id.owerView);
        m_OwerViwe.setVisibility(View.GONE);

    }

    private void initData() {
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setCancelable(true);
        initShowPicArea();
        Bundle _bundle = getIntent().getExtras();
        int _id = _bundle.getInt("id");
        getNotice(_id, new QueryNotfiyReplyActivity.CallBack() {
            @Override
            public void getData(String string) {
//                LogUtils.i("string =" +string);
                int status = GsonUtils.getIntNoteJsonString(string, "status");
                String msg = GsonUtils.getStringNodeJsonString(string, "msg");

                m_progressDialog.setMessage(msg);
                //回复
                String rectifyNotify = GsonUtils.getStringNodeJsonString(string, "rectifyNotify");
                rectifyNotifyInfo = GsonUtils.jsonToBean(rectifyNotify, RectifyNotifyInfo.class);

                //可回复不可回复
                int _logState = rectifyNotifyInfo.getLogState();
                if (_logState == 1 || _logState==4) {
                    m_tvReply.setVisibility(View.VISIBLE);
                    //                    Notify_Reply.setVisibility(View.GONE);
                }else{
                    m_tvReply.setVisibility(View.GONE);
                    Toast.makeText(QueryNotfiyReplyActivity.this, "该通知到已回复", Toast.LENGTH_SHORT).show();
                    //                    Notify_Reply.setVisibility(View.VISIBLE);
                }
                m_InspectorSign = rectifyNotifyInfo.getInspectorSign();
//                LogUtils.i("logName ="+m_InspectorSign);

                //照片备注
                m_imgInfo = rectifyNotifyInfo.getImageInfos();
//                 LogUtils.i("_imgInfo", m_imgInfo +"");
                if (m_imgInfo.contains("#")){
                    m_arrayPicRemark = m_imgInfo.split("#");
                }else m_arrayPicRemark = new String[]{m_imgInfo};

                if (status == -1) {
                    MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg, Toast.LENGTH_SHORT);
//                    Notify_Reply.setVisibility(View.GONE);

                } else if (status == 0) {
                    MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg, Toast.LENGTH_SHORT);
                    //隐藏回复单布局，展示通知单
                    rectifyNotify(rectifyNotifyInfo);

                    QueryUserListWork.queryUserInfo(m_InspectorSign, new OnUserListListener() {
                        @Override
                        public void onQuerySucceed(UserInfo userInfoList) {
                            //账号的ID 和名字
                            m_userName = userInfoList.getLoginName();
                            m_userId = userInfoList.getUserId();
                            //图片的名字
                            pictureName = rectifyNotifyInfo.getImages();

                            if (pictureName != null && pictureName.length() > 0) {
                                _list = new ArrayList<>();
                                _list.addAll(Arrays.asList(pictureName.split("#")));
                                picNames.addAll(_list);

                                if (picNames.size() > 0) {//第一次下载第一张照片
                                    downloadFirstPicture(0);
                                }
                            } else {
                                MyToast.showMyToast(QueryNotfiyReplyActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                                //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                            }
                        }
                        @Override
                        public void onQueryFailed(String msg) {
                            MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg, Toast.LENGTH_SHORT);
                        }
                    });


                } else if (status == 1) { //已回复 审核
                    Notify_Reply.setVisibility(View.VISIBLE);
                    rectifyNotify(rectifyNotifyInfo);

                    QueryUserListWork.queryUserInfo(m_InspectorSign, new OnUserListListener() {
                        @Override
                        public void onQuerySucceed(UserInfo userInfoList) {
                            m_userName = userInfoList.getLoginName();
                            m_userId = userInfoList.getUserId();
//                            LogUtils.i("logName = -"+m_userName );
//                            LogUtils.i("logName = -"+m_userId );

                            pictureName = rectifyNotifyInfo.getImages();
//                            LogUtils.i("logName = -"+pictureName );
                            if (pictureName != null && pictureName.length() > 0) {
                                _list = new ArrayList<>();
                                _list.addAll(Arrays.asList(pictureName.split("#")));
                                picNames.addAll(_list);
                                if (picNames.size() > 0) {//第一次下载第一张照片
                                    downloadFirstPicture(0);
                                }
                            } else {
                                MyToast.showMyToast(QueryNotfiyReplyActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                                //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                            }
                        }
                        @Override
                        public void onQueryFailed(String msg) {
                            MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg, Toast.LENGTH_SHORT);
                        }
                    });

                    //回复单
                    rectifyReply(string);

                }
                m_progressDialog.dismiss();
            }

         });
    }

    private void rectifyNotify(RectifyNotifyInfo rectifyNotifyInfo) {
        m_edtLogId.setText(rectifyNotifyInfo.getLogId());
        m_edtCheckUnit.setText(rectifyNotifyInfo.getInspectUnit());
        m_edtBecheckUnit.setText(rectifyNotifyInfo.getBeCheckedUnit());
        m_edtSection.setText(rectifyNotifyInfo.getSection());
        m_edtCheckDate.setText(rectifyNotifyInfo.getCheckedTime());
        if (rectifyNotifyInfo.getInspectorSigns()!=null){
            m_edtCheckMan.setText(rectifyNotifyInfo.getInspectorSign()+"#"+rectifyNotifyInfo.getInspectorSigns());
        }else {
            m_edtCheckMan.setText(rectifyNotifyInfo.getInspectorSign());
        }

        m_edtLogRectifyDate.setText(rectifyNotifyInfo.getRectifyPeriod());
        m_edtContent.setText(rectifyNotifyInfo.getInspectContent());
        m_edtFindPro.setText(rectifyNotifyInfo.getQuestion());
        m_edtReformMethod.setText(rectifyNotifyInfo.getRequest());
        m_receiveMans.setText(rectifyNotifyInfo.getReceiverMans());
    }

    private void rectifyReply(String string) {
        initShowPicAreaReply();
        String _rectifyReply = GsonUtils.getStringNodeJsonString(string, "replyInfo");
        RectifyReplyInfo _rectifyReplyInfo = jsonToBean(_rectifyReply, RectifyReplyInfo.class);
        rectifyManSign = _rectifyReplyInfo.getRectifyManSign();

        edtlogidReply.setText(_rectifyReplyInfo.getLogId());
        edtnotifyreplyidReply.setText(_rectifyReplyInfo.getRectifyLogID());
        edtLogCheckUnitReply.setText(_rectifyReplyInfo.getInspectUnit());
        edtLogBeCheckUnitReply.setText(_rectifyReplyInfo.getBeCheckedUnit());
        edtLogCheckDateReply.setText(_rectifyReplyInfo.getCheckedTime());
        edtNotifyLogReformManReply.setText(_rectifyReplyInfo.getRectifyManSign());
        edtNotifyReformConditionReply.setText(_rectifyReplyInfo.getRectifySituation());
        edtNotifyReCheckReply.setText(_rectifyReplyInfo.getReviewSituation());
        edtNotifyLogReCheckManReply.setText(_rectifyReplyInfo.getReviewerSign());
        edtSupervisor.setText(_rectifyReplyInfo.getSupervisorMans());
        edtOwnerReply.setText(_rectifyReplyInfo.getOwnerMans());

        m_imgInfoReply = _rectifyReplyInfo.getImageInfos();
//        LogUtils.i("_imgInfo", m_imgInfoReply +"");
        if (m_imgInfoReply.contains("#")){
            m_arrayPicRemarkReply = m_imgInfoReply.split("#");
        }else m_arrayPicRemarkReply = new String[]{m_imgInfoReply};

        QueryUserListWork.queryUserInfo(rectifyManSign, new OnUserListListener() {
            @Override
            public void onQuerySucceed(UserInfo userInfo) {
                m_reply_userId = userInfo.getUserId();
                m_reply_loginName = userInfo.getLoginName();
                m_reply_pictureName = _rectifyReplyInfo.getImages();

                if (m_reply_pictureName != null && m_reply_pictureName.length() > 0) {
                    _listReply = new ArrayList<>();
                    _listReply.addAll(Arrays.asList(m_reply_pictureName.split("#")));

                    picNamesReply.addAll(_listReply);
                    if (picNamesReply.size() > 0) {//第一次下载第一张照片
                       downloadFirstPicture(1);
                    }
                } else {
                    MyToast.showMyToast(QueryNotfiyReplyActivity.this, "该日志无照片", Toast.LENGTH_SHORT);
                    //                imgvPicture.setImageResource(R.mipmap.ic_image_disable);
                }
            }
            @Override
            public void onQueryFailed(String msg) {
                MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg, Toast.LENGTH_SHORT);
            }
        });
    }

   /*private void replydownloadFirstPicture() {
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
            imageItemReply.add(_map);
            refreshGridviewAdapterReply();
            picNames.set(0, "");
            canDownLoad = true;
            Message msg = new Message();
            msg.what = 0;
            m_handler.sendMessage(msg);
        }
    }*/


    private void downloadPictures(int status) {
        if (status == 0){ //整改单
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
                        m_notifyStatus = 0;
                        m_presenter.downloadLogPicture(picNames.size(), i);
                        picNames.set(i, "");
                        break;
                    }
                } else {
                    //                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(i));
                    Bitmap _bitmap =CameraUtils.getimage(FoldersConfig.NOTICEFY + picNames.get(i));
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
        }else { //回复单
            for (int i = 0; i < picNamesReply.size(); i++) {
                if (picNamesReply.get(i).equals("")) {
                    continue;
                }
                File picFile = new File(FoldersConfig.NOTICEFY, picNamesReply.get(i));
                if (!picFile.exists()) {
                    if (canDownLoad) {
                        m_picIndex = i;
                        itemPicNameReply = picNamesReply.get(i);
                        canDownLoad = false;
                        m_notifyStatus = 1;
                        m_presenter.downloadLogPicture(picNamesReply.size(), i);
                        picNamesReply.set(i, "");
                        break;
                    }
                } else {
                    //                Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(i));
                    Bitmap _bitmap =CameraUtils.getimage(FoldersConfig.NOTICEFY + picNamesReply.get(i));
                    HashMap<String, Object> _map = new HashMap<>();
                    _map.put("itemImage", _bitmap);
                    if (!m_imgInfoReply.isEmpty()){
                        _map.put("remark",m_arrayPicRemarkReply[i]);
                    }else  _map.put("remark","");
                    imageItemReply.add(_map);
                    //                picFiles.add(picFile);
                    refreshGridviewAdapterReply();
                    picNamesReply.set(i, "");
                    canDownLoad = true;
                    Message msg = new Message();
                    m_handler.sendMessage(msg);
                    break;
                    //                                m_lock.unlock();
                }
            }
        }

    }

    private void downloadFirstPicture(int status) {

        if (status == 0) { //整改单
            File picFile = new File(FoldersConfig.NOTICEFY, picNames.get(0));
            if (!picFile.exists()) {
                m_picIndex = 0;
                itemPicName = picNames.get(0);
                canDownLoad = false;
                m_notifyStatus = 0;
                m_presenter.downloadLogPicture(picNames.size(), 0);
                picNames.set(0, "");
            } else {
                //            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(0));
                Bitmap _bitmap = CameraUtils.getimage(FoldersConfig.NOTICEFY + picNames.get(0));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                if (!m_imgInfo.isEmpty()) {
                    _map.put("remark", m_arrayPicRemark[0]);
                } else _map.put("remark", "");
                imageItem.add(_map);
                refreshGridviewAdapter();
                picNames.set(0, "");
                canDownLoad = true;

                ThreadUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 0;
                        m_handler.sendMessage(msg);
                    }
                });

            }
        }else if (status ==1){ //回复单
            File picFile = new File(FoldersConfig.NOTICEFY, picNamesReply.get(0));
            if (!picFile.exists()) {
                m_picIndex = 0;
                itemPicNameReply = picNamesReply.get(0);
                canDownLoad = false;
                m_notifyStatus = 1;
                m_presenter.downloadLogPicture(picNamesReply.size(), 0);
                picNamesReply.set(0, "");
            } else {
                //            Bitmap _bitmap = BitmapFactory.decodeFile(FoldersConfig.NOTICEFY + picNames.get(0));
                Bitmap _bitmap = CameraUtils.getimage(FoldersConfig.NOTICEFY + picNamesReply.get(0));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", _bitmap);
                if (!m_imgInfoReply.isEmpty()) {
                    _map.put("remark", m_arrayPicRemarkReply[0]);
                } else _map.put("remark", "");
                imageItemReply.add(_map);
                refreshGridviewAdapterReply();
                picNamesReply.set(0, "");
                canDownLoad = true;

                ThreadUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 0;
                        m_handler.sendMessage(msg);
                    }
                });

            }
        }
    }
    //整改通知单刷新图片区域gridview
    private void refreshGridviewAdapter() {
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic2, new String[]{"itemImage","remark"}, new int[]{R.id.imageView1, R.id.tv1});
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
                } else if (view instanceof TextView && textRepresentation instanceof String) {
                    TextView _textView = (TextView) view;
                    _textView.setText(textRepresentation);
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
    //整改回复单刷新图片区域gridview
    private void refreshGridviewAdapterReply() {
        simpleAdapterReply = new SimpleAdapter(this, imageItemReply,
                R.layout.layout_griditem_addpic2, new String[]{"itemImage","remark"}, new int[]{R.id.imageView1, R.id.tv1});
        simpleAdapterReply.setViewBinder(new SimpleAdapter.ViewBinder() {
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
                } else if (view instanceof TextView && textRepresentation instanceof String) {
                    TextView _textView = (TextView) view;
                    _textView.setText(textRepresentation);
                }
                return false;
            }
        });
        runOnUiThread(new Runnable() {//主线程绑定adapter刷新数据
            @Override
            public void run() {
                m_gridViewReply.setAdapter(simpleAdapterReply);
                simpleAdapterReply.notifyDataSetChanged();
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
    private void initShowPicAreaReply() {
        picNamesReply = new ArrayList<>();
        imageItemReply = new ArrayList<>();
        m_gridViewReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                if (picFiles.get(position) != null) {
                //                    //打开照片查看
                //                    Intent intent = new Intent();
                //                    intent.setAction(Intent.ACTION_VIEW);
                //                    intent.setDataAndType(Uri.fromFile(picFiles.get(position)), CameraUtils.IMAGE_UNSPECIFIED);
                //                    startActivity(intent);
                //                }
                if (_listReply.get(position) != null) {
                    //打开照片查看
                    File _file = new File(FoldersConfig.NOTICEFY, _listReply.get(position));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(_file), "image/*");
                    startActivity(intent);
                }
            }
        });

    }


    //网络请求接收过的通知
    private void getNotice(int receiveMan, CallBack callBack) {
        m_progressDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", receiveMan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.postAsync(ServerConfig.URL_QUERY__NOTFIY_REPLY, str, new OkHttpUtils.InsertDataCallBack() {
                    @Override
                    public void requestFailure(Request request, IOException e) {
//                        LogUtils.i("接收通知失败", request.body().toString());
                        m_progressDialog.setMessage("加载失败");
                        m_progressDialog.dismiss();
                    }
                    @Override
                    public void requestSuccess(String result) throws Exception {
//                        LogUtils.i("result =", result);
                        if (result==null){
                            m_progressDialog.setMessage("加载失败");
                            m_progressDialog.dismiss();
                        }else
                        callBack.getData(result);

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvReply:
                //                int _logState = _rectifyNotifyInfo.getLogState();
                //                if (_logState==1 || _logState==4) {
                Bundle _bundle = new Bundle();
                _bundle.putInt("dbID", rectifyNotifyInfo.getId());
                _bundle.putString("logId",rectifyNotifyInfo.getLogId());
                _bundle.putString("checkUnit", rectifyNotifyInfo.getInspectUnit());
                _bundle.putString("beCheckUnit",rectifyNotifyInfo.getBeCheckedUnit());
                _bundle.putString("checkMan", m_edtCheckMan.getText().toString());
                _bundle.putString("imgInfo", m_imgInfo);
                Intent _intent = new Intent(QueryNotfiyReplyActivity.this, NewRectifyReplyInfoActivity.class);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                //               }else {
                //                    Toast.makeText(this, "已回复！", Toast.LENGTH_SHORT).show();
                //                }
                break;

            case R.id.tvBack:
                finish();
                break;
        }

    }
    public interface CallBack {
        void getData(String string);
    }

    /**
     * @return 获取下载URL
     */
    @Override
    public String getDownLoadURL() {

//        LogUtils.i(getIdLoginName());
        if (m_notifyStatus == 0){
            return ServerConfig.URL_NOTICEFY_FILE_UPLOAD +
                    getIdLoginName() + "/" + itemPicName;
        }else {

            return ServerConfig.URL_NOTICEFY_FILE_UPLOAD +
                    getIdLoginName() + "/" + itemPicNameReply;
        }
    }

    /**
     * @return 获取登录名 id
     */
    @Override
    public String getIdLoginName() {
//        return "669test3";
        if (m_notifyStatus == 0){
            return m_userId + m_userName;
        }else {
            return m_reply_userId + m_reply_loginName;
        }
    }

    /**
     * @return 获取照片名字
     */
    @Override
    public String getPictureName() {
        if (m_notifyStatus ==0){
            if (itemPicName != null && itemPicName.length() > 0)

                return itemPicName;
        }else {
            if (itemPicNameReply != null && itemPicNameReply.length() > 0)

                return itemPicNameReply;
        }

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
        if (m_notifyStatus == 0){
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
        }else {
            if (bitmap != null) {
                picBitmap = bitmap;
                //            picFiles.add(CameraUtils.bitMapToFile(picBitmap,
                //                    FoldersConfig.PRO_SAFETY_PIC_PATH, itemPicName));
                HashMap<String, Object> _map = new HashMap<>();
                _map.put("itemImage", picBitmap);
                if (!m_imgInfoReply.isEmpty()){
                    _map.put("remark",m_arrayPicRemarkReply[m_picIndex]);
                }else  _map.put("remark","");
                imageItemReply.add(_map);
                refreshGridviewAdapterReply();
                //            MyToast.showMyToast(this, "成功加载" + itemPicName, Toast.LENGTH_SHORT);
                canDownLoad = true;
            }
            boolean isFinish = false;
            for (String _picName : picNamesReply) {
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

    }

    /**
     * @param msg 下载失败
     */
    @Override
    public void showLoadingFailed(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.showMyToast(QueryNotfiyReplyActivity.this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
            }
        });
        Message _message = new Message();
        _message.what = 1;
        m_handler.sendMessage(_message);
    }
}
