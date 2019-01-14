package tianchi.com.risksourcecontrol2.activitiy.notice;

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
import java.util.HashMap;
import java.util.List;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.user.RelationshipListActivity;
import tianchi.com.risksourcecontrol2.activitiy.user.UserPermission;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UserInfo;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnUserListListener;
import tianchi.com.risksourcecontrol2.presenter.RectifyReplyInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.view.IRectifyNotifyView;
import tianchi.com.risksourcecontrol2.work.QueryUserListWork;

/**
 * Created by hairun.tian on 2018/6/15 0015.
 * 通知整改回复类
 * 对象：施工方
 * 提交的对象：监理、业主
 */
public class NewRectifyReplyInfoActivity extends BaseActivity implements IRectifyNotifyView, View.OnClickListener, MyTakePicDialog.OnItemClickListener {
    private static final int GET_RELATIONSHIP = 0;
    private static final int GET_SUPERISOR = 3;
    private static final int GET_OWNER = 4;
    private String m_logId;
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
    private EditText m_edtLogId1;
    private Button m_btnSubmit;
    private TextView m_btnBack;
    private EditText m_edtSupervisor;
    private TextView m_tvSupervisor;
    //    private EditText m_edtOwner;
    private int dbId;
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private AlertDialog m_dialog;//拍照选择弹窗
    private ProgressDialog m_progressDialog;//提交进度
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件

    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName = "";//照片全名xx.jpg
    private int m_picIndex = 0;
    private int m_checkManRoid; //检查人的角色id 17  19  20;

    private StringBuffer m_imgInfo = null;
    private List<String> m_listRemark = null;
    public String m_remark;
    private String m_imgInfoN;
    private int uploadImgIndex = 0;//上传照片时的数量
    private int m_remarkIndex = 0; //照片备注序号
    private int remarkIndex = 0;
    private Handler m_handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            uploadPictures();
            return false;
        }
    });


    private void uploadPictures() {
//        for (int i = 0; i < picNames.size(); i++) {
//            if (picNames.get(i).equals("")) {
//                continue;
//            }
        //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
        if (picFiles.get(uploadImgIndex).getName().equals(picNames.get(uploadImgIndex))) {
            if (canUpload) {
                canUpload = false;
                m_presenter.uploadFile(picFiles.size(), uploadImgIndex);
                m_picIndex = uploadImgIndex;
//                    picNames.set(i, "");
//                    break;
            }
//            } else {
//                MyToast.showMyToast(this, "请检查上传的图片是否被手动删除！", Toast.LENGTH_SHORT);
//                break;
//            }
        }
    }

    RectifyReplyInfoPresenter m_presenter = new RectifyReplyInfoPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_reform_reply);
        initView();
        getCheckManRoid();
        initEvent();
        initValue();
        //施工方打开视为已经阅读，回复后就修改已经回复

    }

    private void getCheckManRoid() {
        QueryUserListWork.queryUserInfo(m_checkMan, new OnUserListListener() {
            @Override
            public void onQuerySucceed(UserInfo userInfoList) {
                m_checkManRoid = userInfoList.getRoleId();
//                LogUtils.i("Roid","---"+m_checkManRoid);
            }

            @Override
            public void onQueryFailed(String msg) {


            }
        });
    }

    private void initValue() {
        initTakePicArea();

    }


    private boolean canUpload = true;

    private void initEvent() {
        m_btnSubmit.setOnClickListener(this);
        m_btnBack.setOnClickListener(this);
        m_btnAddPic.setOnClickListener(this);
    }

    private void initView() {
        m_imgInfo = new StringBuffer();
        Bundle _bundle = getIntent().getExtras();
        m_logId = _bundle.getString("logId");
        m_checkUnit = _bundle.getString("checkUnit");
        m_beCheckUnit = _bundle.getString("beCheckUnit");
        m_checkMan = _bundle.getString("checkMan");
        dbId = _bundle.getInt("dbID");
        m_imgInfoN = _bundle.getString("imgInfo");

        m_edtLogId1 = $(R.id.edtLogID);
        m_edtCheckUnit = $(R.id.edtLogCheckUnit);
        m_edtBeCheckUnit = $(R.id.edtLogBeCheckUnit);
        m_edtReformDate = $(R.id.edtLogCheckDate);
        m_edtReformMan = $(R.id.edtLogReformMan);
        m_btnAddPic = $(R.id.btnAddPic);
        m_gridView = $(R.id.gridView1);
        //整改情况
        m_edtReformCon = $(R.id.edtReformCondition);
        //复核
        m_edtReCheckCon = $(R.id.edtReCheck);
        //复核人签名
        m_edtReCheckMan = $(R.id.edtLogReCheckMan);
        m_edtLogId1.setText(m_logId);
        m_edtCheckUnit.setText(m_checkUnit);
        m_edtBeCheckUnit.setText(m_beCheckUnit);
        m_btnSubmit = $(R.id.btnSubmit);
        m_btnBack = $(R.id.tvBack);
        m_edtSupervisor = $(R.id.edtSupervisor);
        m_tvSupervisor = $(R.id.tvSupervisor);
        m_tvSupervisor.setText(getResources().getString(R.string.string_recive_man));
//        m_edtOwner = $(R.id.edtOwner);
        m_edtReformMan.setText(UserSingleton.getUserInfo().getRealName());
        m_edtSupervisor.setOnClickListener(this);
//        m_edtOwner.setOnClickListener(this);

//        m_edtReformCon.setText("整改内容");
//        m_edtReCheckCon.setText("复核内容");
//        m_edtReCheckMan.setText("复核人");

        m_edtReformDate.setText(DateTimeUtils.setCurrentTime());
        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("通知回复发送中...");
        m_progressDialog.setCancelable(true);

    }


    //初始化拍照区域
    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
        m_listRemark = new ArrayList<>();
        m_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPicture(position);
            }
        });

        m_gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyAlertDialog.showAlertDialog(NewRectifyReplyInfoActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageItem.remove(position);
                                picNames.remove(position);
                                picFiles.remove(position);
                                m_listRemark.remove(position);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmit:
                if (checkInfo()) {
                    uploadFirstPicture();
                }

                break;
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnAddPic:
                if (imageItem.size() == 5) {
                    MyToast.showMyToast(NewRectifyReplyInfoActivity.this, "最多支持上传5张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }
                break;

            case R.id.edtSupervisor:
                startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_FIRST), GET_SUPERISOR);
                break;

            case R.id.edtOwner:
                startActivityForResult(new Intent(this, RelationshipListActivity.class), GET_OWNER);
                break;

            default:
                break;
        }

    }


    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
//        String s = picNames.get(0);
//        String s2 = picFiles.get(0).getName();
        if (picFiles.get(0).getName().equals(picNames.get(0))) {
            if (canUpload) {
                m_presenter.uploadFile(picFiles.size(), 0);
//                picNames.set(0, "");
                m_picIndex = 0;
                canUpload = false;
            }
        } else {
            MyToast.showMyToast(this, "请检查要上传的图片是否已被手动删除！", Toast.LENGTH_SHORT);
        }
    }

    private boolean checkInfo() {
        boolean isOk = true;

        if (getCheckUnit().length() == 0) {
            m_edtCheckUnit.setError("检查单位不能为空");
            isOk = false;
        }
        if (getBeCheckUnit().length() == 0) {
            m_edtBeCheckUnit.setError("被检查单位不能为空");
            isOk = false;
        }

        if (getReformMan().length() == 0) {
            m_edtReformMan.setError("整改人不能为空");
            isOk = false;
        }
//        if (getSection().length() == 0) {
//
//            isOk = false;
//        }

        if (getPicture().length() == 0) {
            Toast.makeText(this, "请添加图片", Toast.LENGTH_SHORT).show();
            isOk = false;
        }

        if (getReformCon().length() == 0) {
            m_edtReformCon.setError("整改情况不能空");
            isOk = false;
        }

        if (getReCheckMan().length() == 0) {
            m_edtReCheckMan.setError("复核人不能空");
            isOk = false;
        }
        if (getSupervisor().length() == 0) {
            m_edtSupervisor.setError("接收人不能空");
            isOk = false;
        }
        return isOk;
    }


    @Override
    public void setOnItemClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnTakePicture:
                takPicFile = new File(FoldersConfig.NOTICEFY, System.currentTimeMillis() + ".jpg");
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
        Bitmap picBitmap = null;
        try {
            switch (requestCode) {  //拍照
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO: {
                    StringBuffer _imgInfo = new StringBuffer();
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//拍摄返回的bitmap
                        Bitmap _bitmap = CameraUtils.comp(picBitmap);
                        if (_bitmap != null) {
                            resultImgFile = new File(new URI(uri.toString()));//拍摄返回的图片file
                            if (!FileUtils.isFileNameIllegal(resultImgFile.getName())) {
                                boolean _overLimit = FileUtils.fileSizeOverLimit(resultImgFile);
                                if (_overLimit) {
                                    MyToast.showMyToast(this, "请拍摄小于8M的照片", Toast.LENGTH_SHORT);
                                } else {
                                    pictureName = resultImgFile.getName();//拍摄返回的图片name
                                    HashMap<String, Object> _map = new HashMap<>();

                                    final EditText m_tv = new EditText(this);
                                    m_tv.setHint("请输入照片备注，整改情况");
                                    if (m_imgInfoN.isEmpty()) {  //如果整改单没有备注
                                        m_imgInfoN = "整改单备注1#整改单备注2#整改单备注3#整改单备注4#整改单备注5";
                                    }
                                    final String[] array = m_imgInfoN.split("#");
                                    MyAlertDialog.showAlertDialog(this, "请选择对应的整改单存在的问题，在照片上添加整改情况，必填！", "确定", "取消", false, m_tv, array, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            remarkIndex = which;
//                                            String str = array[which];
//                                            _imgInfo.append(str + "/");
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //确认
//                                            LogUtils.i("m_remark", m_tv.getText().toString());
                                            m_remark = m_tv.getText().toString();
                                            if (m_remark.isEmpty()) return;
                                            m_remarkIndex++;
                                            picNames.add(pictureName);
                                            picFiles.add(resultImgFile);
                                            _map.put("itemImage", _bitmap);
                                            _map.put("remark", m_remark);
                                            imageItem.add(_map);
                                            String str = array[remarkIndex];
                                            _imgInfo.append(str + "/");
                                            _imgInfo.append(String.valueOf(m_remarkIndex) + m_remark + "#");
//                                            m_edtReformCon.setText(_imgInfo.toString());
                                            m_listRemark.add(_imgInfo.toString());
                                            refreshGridviewAdapter();
//                                            LogUtils.i("adapter m_imgInfo",m_imgInfo.toString());
//                                            dialog.dismiss();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            _map.put("remark", "无照片描述");
//                                            imageItem.add(_map);
                                            // 取消
//                                            dialog.dismiss();
                                        }
                                    });
//                                    _map.put("remark","remark");
//                                    imageItem.add(_map);

                                    //                                    m_safetyLogPresenter.uploadFile();
                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                }
                break;

                case CameraUtils.PHOTO_REQUEST_GALLERY: {
                    StringBuffer _imgInfo = new StringBuffer();
                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};//取媒体文件路径集合
                        Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);//取选中图片的cursor
                        c.moveToFirst();
                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
                        String imgpath = c.getString(colindex);//取文件相对手机路径
                        c.close();
//                        picBitmap = BitmapFactory.decodeFile(imgpath);//拍摄返回的bitmap
                        Bitmap _picBitmap = CameraUtils.getimage(imgpath);//取相册照片
                        if (_picBitmap != null) {
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
                                    HashMap<String, Object> _map = new HashMap<>();
                                    pictureName = resultImgFile.getName();//拍摄返回的图片name
                                    final EditText m_tv = new EditText(this);
                                    m_tv.setHint("请输入照片备注，整改情况");
                                    if (m_imgInfoN.isEmpty()) {
                                        m_imgInfoN = "整改单备注1#整改单备注2#整改单备注3#整改单备注4#整改单备注5";
                                    }
                                    final String[] array = m_imgInfoN.split("#");
                                    MyAlertDialog.showAlertDialog(this, "请选择对应的整改单存在的问题，在照片上添加整改情况，必填！", "确定", "取消", false, m_tv, array, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            remarkIndex = which;
//                                            String str = array[which];
//                                            _imgInfo.append(str + "/");
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            m_remark = m_tv.getText().toString();
                                            if (m_remark.isEmpty()) return;
                                            m_remarkIndex++;
                                            picNames.add(pictureName);
                                            picFiles.add(resultImgFile);
                                            _map.put("itemImage", _picBitmap);
                                            _map.put("remark", m_remark);
                                            imageItem.add(_map);
                                            String str = array[remarkIndex];
                                            _imgInfo.append(str + "/");
                                            _imgInfo.append(String.valueOf(m_remarkIndex) + m_remark + "#");
                                            m_listRemark.add(_imgInfo.toString());
//                                            m_edtReformCon.setText(m_imgInfo.toString());
                                            refreshGridviewAdapter();
                                            dialog.dismiss();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            _map.put("remark", "无照片描述");
//                                            imageItem.add(_map);
//                                            // 取消
//                                            dialog.dismiss();
                                        }
                                    });

                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                }
                break;

                case GET_SUPERISOR:
                    if (resultCode == RESULT_OK) {
                        if (UsersList.getList().size() > 1) {
                            MyToast.showMyToast(NewRectifyReplyInfoActivity.this, "只能选择一个人审核，请重新选择", Toast.LENGTH_SHORT);
                            return;
                        }
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            allNameList += name + "#";
                        }
                        m_edtSupervisor.setText("");
                        m_edtSupervisor.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;

                case GET_OWNER:
//                    if (resultCode == RESULT_OK) {
//                        String allNameList = "";
//                        for (String name : UsersList.getList()) {
//                            allNameList += name + "#";
//                        }
//                        m_edtOwner.setText("");
//                        m_edtOwner.setText(allNameList.substring(0, allNameList.length() - 1));
//                        UsersList.clearList();
//                    }
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
        initedtFindPro();
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.layout_griditem_addpic2, new String[]{"itemImage", "remark"}, new int[]{R.id.imageView1, R.id.tv1});
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
                } else if (view instanceof TextView) {
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

    private void initedtFindPro() {
        if (m_imgInfo.length() > 0) {
            m_imgInfo.delete(0, m_imgInfo.length());
        }
        for (int i = 0; i < m_listRemark.size(); i++) {
            m_imgInfo.append(m_listRemark.get(i));
        }
        m_edtReformCon.setText(m_imgInfo.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return 标段
     */
    @Override
    public String getSection() {
        return null;
    }

    /**
     * @return 整改日志id
     */
    @Override
    public String getLogID() {
        return m_logId;
    }

    /**
     * 检查单位
     *
     * @return
     */
    @Override
    public String getCheckUnit() {
        return m_checkUnit;
    }

    /**
     * 受检查单位
     *
     * @return
     */
    @Override
    public String getBeCheckUnit() {
        return m_beCheckUnit;
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
        return null;
    }

    /**
     * 检查发现的问题
     *
     * @return
     */
    @Override
    public String getQuestion() {
        return null;
    }

    /**
     * 整改措施与方法
     *
     * @return
     */
    @Override
    public String getRectifyRequest() {
        return null;
    }

    /**
     * 检查人
     *
     * @return
     */
    @Override
    public String getCheckMan() {
        return null;
    }

    /**
     * 整改期限
     *
     * @return
     */
    @Override
    public String rectifyDate() {
        return null;
    }

    /**
     * 照片名字
     *
     * @return
     */
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
     * 登录人id和名字
     *
     * @return
     */
    @Override
    public String getIdLoginName() {
        return UserSingleton.getUserInfo().getUserId()
                + UserSingleton.getUserInfo().getLoginName();
    }

    /**
     * 获取接收人信息
     *
     * @return
     */
    @Override
    public String getReceiveMans() {
        return m_edtSupervisor.getText().toString();
    }

    /**
     * 获取日志状态
     *
     * @return
     */
    @Override
    public int getLogState() {
        return 1;
    }

    /**
     * 获取上传的照片
     *
     * @param position
     * @return
     */
    @Override
    public File getUploadFile(int position) {

        if (picFiles.size() > 0) {
            return picFiles.get(position);
        }
        return null;
    }

    /**
     * 显示提交过程
     *
     * @param msg
     * @return
     */
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

    /**
     * 隐藏提交过程
     *
     * @return
     */
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

    /**
     * 提交成功
     *
     * @param msg
     * @return
     */
    @Override
    public void showSubmitSucceed(String msg) {
        hideInSubmiting();
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        finish();
    }

    /**
     * 提交失败
     *
     * @param msg
     * @return
     */
    @Override
    public void showSubmitFailed(String msg) {
        hideInSubmiting();
        uploadImgIndex = 0;
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        finish();
    }

    /**
     * 提交图片成功
     *
     * @param msg
     * @return
     */
    @Override
    public void uploadFileSucceed(String msg) {
        //        MyToast.showMyToast(this, msg, Toast.LENGTH_SHORT);
//        picNames.set(m_picIndex, "");
        ++uploadImgIndex;  //照片上传完一张，自动加1
        canUpload = true;//可上传标记
        boolean isFinish = false;//标记整个上传过程是否完成
//        for (String _picName : picNames) {
//            if (!_picName.equals("")) {
//                isFinish = false;
//            } else {
//                isFinish = true;
//            }
//        }
        if (picNames.size() > uploadImgIndex) {  //如果集合中的照片还有未上传的，继续上传
            isFinish = false;
        } else {
            isFinish = true;
        }

        if (!isFinish) {//上传未完成，继续发送下载指令
            Message _message = new Message();
            m_handler.sendMessageDelayed(_message, 500);
        } else {//上传完成，提交日志
            m_presenter.submit();
        }
        //        resetParams();
    }

    /**
     * 提交图片失败
     *
     * @param msg
     * @return
     */
    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        uploadImgIndex = 0;
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
        try {

            return DateTimeUtils.longToDate2(System.currentTimeMillis(), DateTimeUtils.FULL_DATE_TIME_FORMAT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取整改人
     */
    @Override
    public String getReformMan() {
        return m_edtReformMan.getText().toString().trim();
    }

    /**
     * @retrun 获取整改情况
     */
    @Override
    public String getReformCon() {
        return m_edtReformCon.getText().toString().trim();
    }

    /**
     * @return 获取复核情况
     */
    @Override
    public String getReCheckCon() {
        return m_edtReCheckCon.getText().toString().trim();
    }

    /**
     * @return 获取复核人签名
     */
    @Override
    public String getReCheckMan() {
        return m_edtReCheckMan.getText().toString().trim();
    }

    /**
     * @return 获取数据库id
     */
    @Override
    public int getDbID() {
        return dbId;
    }


    @Override
    public int getDraftStatus() {
        return 0;
    }

    @Override
    public String getSupervisor() {
        return m_edtSupervisor.getText().toString();
    }

    @Override
    public String getOwner() {
        return null;
//        return m_edtOwner.getText().toString();
    }

    @Override
    public String getImgInfo() {
        if (m_imgInfo.length() > 0) {
//            LogUtils.i("m_imgInfo",m_imgInfo.toString());
            return m_imgInfo.substring(0, m_imgInfo.length() - 1);
        }
        return "";
    }

}
