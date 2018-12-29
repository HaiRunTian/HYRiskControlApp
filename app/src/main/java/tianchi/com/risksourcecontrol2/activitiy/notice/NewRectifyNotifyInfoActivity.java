package tianchi.com.risksourcecontrol2.activitiy.notice;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import tianchi.com.risksourcecontrol2.adapter.BeAdapter;
import tianchi.com.risksourcecontrol2.adapter.InputAdapter;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.login.UsersList;
import tianchi.com.risksourcecontrol2.config.FoldersConfig;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol2.custom.MyDatePicker;
import tianchi.com.risksourcecontrol2.custom.MyTakePicDialog;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.presenter.RectifyNotifyInfoPresenter;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.CameraUtils;
import tianchi.com.risksourcecontrol2.util.DateTimeUtils;
import tianchi.com.risksourcecontrol2.util.FileUtils;
import tianchi.com.risksourcecontrol2.view.IRectifyNotifyView;

/**
 * Created by hairun.tian on 2018/6/13 0013.
 * 新建整改通知单
 * 权限:业主、监理、施工方都可以创建
 */

public class NewRectifyNotifyInfoActivity extends BaseActivity implements View.OnClickListener, IRectifyNotifyView, MyTakePicDialog.OnItemClickListener,InputAdapter.OnItemClickListener,BeAdapter.OnItemClickListener{
    private static final int GET_SUPERVISOR = 0;
    private static final int GET_COPYER = 8;
    private static final int GET_CONSTRUCTION = 9;
    private static final int GET_CHECKMAN = 7;
    private int m_logState; //日志状态
    private EditText m_edtLogId; //日志编号
//    private SpinnerEditText m_edtCheckUnit; //检查单位
//    private SpinnerEditText m_edtBecheckUnit; //受检单位
    private EditText m_edtCheckUnit;//检查单位
    private EditText m_edtBecheckUnit; //受检单位
    private EditText m_edtCheckMan; //检查人
    private EditText m_edtCheckMans; //副检查人
    private EditText m_edtCheckDate; //检查时间
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
    private String m_section;
    private String[] m_arrSection;
    private String[] m_spSction;

    private Button m_btnSubmit; //提交
    private Button m_btnDraft; //草稿

    //    private EditText m_receiveMans; //接收人  @弃用
    private TextView m_tvBack; //返回
//    private EditText m_edtSupervisor; //监理
    private EditText m_edtCopyer; //抄送着
    private EditText m_edtConstruction;//施工方
    private List<File> picFiles;             //临时图片文件数组
    private List<String> picNames;           //临时图片文件名数组
    private ArrayList<HashMap<String, Object>> imageItem;//适配器数据
    private SimpleAdapter simpleAdapter;     //适配器
    private File takPicFile;//用户头像拍照文件
    private File resultImgFile;//最终生成的img文件
//    private Bitmap picBitmap;//存储拍照的照片
    private Uri fileUri;//生成拍照文件uri
    private Uri uri;//系统拍照或相册选取返回的uri
    //    private String downloadURL;//下载文件url
    private String pictureName = "";//照片全名xx.jpg
    private int m_picIndex = 0;
    private boolean canUpload = true;
    private StringBuffer m_imgInfo = null;
    private List<String> m_listRemark = null; //照片备注list
    public String m_remark;
    private int uploadImgIndex = 0;//上传照片时的数量
    private int m_remarkIndex = 0; //照片备注序号
    private LinearLayout be_ll;
    private LinearLayout inspect_ll;
    private InputAdapter m_inputAdapter;
    private BeAdapter m_beAdapter;
    private PopupWindow mSelectWindow;
    private ImageButton be_arrow;
    private ImageButton input_arrow;

    private String[] inspect_Title = {
            "中铁十四局集团第二工程有限公司",
            "中铁十二局集团第一工程有限公司",
            "中交二公局第三工程有限公司",
            "中铁太桥局集团有限公司",
            "龙建路桥股份有限公司",
            "中铁二十局集团有限公司",
            "广东省长大公路工程有限公司",
            "中交路桥建设有限公司"
    };

    private String[] be_Title= {
            "广东翔飞公路工程监理有限公司",
            "江苏交通工程咨询监理有限公司",
            "北京路桥通国际工程咨询有限公司",
            "深圳高速工程检测有限公司",
            "苏交科集团股份有限公司",
            "山西省交通建设工程质量检测中心"
    };

    RectifyNotifyInfoPresenter mReNoticePresenter = new RectifyNotifyInfoPresenter(this);
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
//            if (uploadImgIndex < picNames.size()) continue; //如果照片为上传完成，
            //            File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(i));
            if (picFiles.get(uploadImgIndex).getName().equals(picNames.get(uploadImgIndex))) {
                if (canUpload) {
                    canUpload = false;
                    mReNoticePresenter.uploadFile(picFiles.size(), uploadImgIndex);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_danger_reform_new);
        initView();
        initEvent();
        initValue();
    }



    private void initValue() {
//        m_edtLogId.setText("1");
//        m_edtCheckUnit.setText("检查单位");
//        m_edtBecheckUnit.setText("受检查单位");
//        m_edtLogRectifyDate.setText("整改期限");
//        m_edtContent.setText("检查内容");
//        m_edtFindPro.setText("发现问题");
//        m_edtReformMethod.setText("整改要求与方法");
        initTakePicArea();


    }

    private void initEvent() {
        m_btnAddPic.setOnClickListener(this);
        m_btnSubmit.setOnClickListener(this);
        m_btnDraft.setOnClickListener(this);
//        m_receiveMans.setOnClickListener(this);
        m_edtLogRectifyDate.setOnClickListener(this);
        m_tvBack.setOnClickListener(this);
//        m_edtSupervisor.setOnClickListener(this);
        m_edtCopyer.setOnClickListener(this);
        m_edtConstruction.setOnClickListener(this);
        m_edtCheckMan.setOnClickListener(this);
        m_edtCheckMans.setOnClickListener(this);

        be_arrow.setOnClickListener(this);
        input_arrow.setOnClickListener(this);
    }

    private void initView() {


        m_imgInfo = new StringBuffer();
        View _view = $(R.id.layout);
        _view.setVisibility(View.GONE);
        m_edtLogId = $(R.id.edtLogID);

        m_edtCheckUnit = $(R.id.edtLogCheckUnit);
        m_edtBecheckUnit = $(R.id.edtLogBeCheckUnit);

        m_edtCheckDate = $(R.id.edtLogCheckDate);
        m_edtCheckMan = $(R.id.edtLogCheckMan);
        m_edtCheckMans = $(R.id.edtLogCheckMans);
        m_edtLogRectifyDate = $(R.id.edtLogRectifyDate);
        m_btnAddPic = $(R.id.btnAddPic);
        m_gdvPic = $(R.id.gridView1);
        m_edtContent = $(R.id.edtCheckContent);
        m_edtFindPro = $(R.id.edtFindProblem);
        m_edtReformMethod = $(R.id.edtReformMethod);
        m_btnPushlog = $(R.id.btnPushLog);
        m_spSection = $(R.id.spSection);
        m_btnSubmit = $(R.id.btnSubmit);
        m_btnDraft = $(R.id.btnDraft);
//        m_receiveMans = $(R.id.edtRecorder);
        m_tvBack = $(R.id.tvBack);
//        m_edtSupervisor = $(R.id.edtSupervisor);
        m_edtConstruction = $(R.id.edtConstruction);
        m_edtCopyer = $(R.id.edtCopy);

        be_arrow = $(R.id.be);
        input_arrow = $(R.id.inspect);
        be_ll = $(R.id.be_ll);
        inspect_ll = $(R.id.inspect_ll);

        m_progressDialog = new ProgressDialog(this);
        m_progressDialog.setMessage("通知发送中...");
        m_progressDialog.setCancelable(true);
//        m_spSection.setAdapter(new ArrayAdapter<String>(NewRectifyNotifyInfoActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.section2)));
//       long time = System.currentTimeMillis();

        init();

        m_edtCheckDate.setText(DateTimeUtils.setCurrentTime());


        m_edtCheckMan.setText(UserSingleton.getUserInfo().getRealName());



    }


    private void init() {
        //        currentLoginName = getIntent().getStringExtra("userName");//当前账号
        //        UserSingleton.getUserInfo().setLoginName(currentLoginName);//保存账号
        if (UserSingleton.getUserInfo().getLoginName() != null) {
            //            currentLoginName = UserSingleton.getUserInfo().getLoginName();//当前账号
            //            m_tvUserProfile.setText(currentLoginName);//设置左上方账号名

            //            m_tvUserProfile.setText(currentLoginName);//设置左上方账号名
            //获取用户所拥有标段
            m_section = UserSingleton.getUserInfo().getSectionList();
            if (m_section.isEmpty()) {
                //                MyToast.showMyToast(DrawerActivity.this, "你没有掌控的标段，请联系管理员", 4);
                return;
            }

            m_arrSection = m_section.split("#");  //字符串转成数组
            String[] _spData = new String[m_arrSection.length];
            //多选标段数据
            m_spSction = new String[m_arrSection.length];
            for (int _i = 0; _i < m_arrSection.length; _i++) {
                _spData[_i] = ServerConfig.getMap().get(m_arrSection[_i]);
                m_spSction[_i] = ServerConfig.getMapSection().get(m_arrSection[_i]);
            }
            m_spSection.setSelection(0, false);
            m_spSection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _spData));
            //m_spSection.setAdapter(new SectionAdapter(DrawerActivity.this, m_arrSection));
        }
        // m_riskTypeList = Arrays.asList(getResources().getStringArray(R.array.riskType));//取到风险源类型列表
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

    //初始化拍照区域
    private void initTakePicArea() {
        picFiles = new ArrayList<>();
        picNames = new ArrayList<>();
        imageItem = new ArrayList<HashMap<String, Object>>();
        m_listRemark = new ArrayList<>();
        m_gdvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击查看照片
                viewPicture(position);
            }
        });


        m_gdvPic.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyAlertDialog.showAlertDialog(NewRectifyNotifyInfoActivity.this, "删除提示", "确定删除改照片？", "确定", "取消", true,
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        int m_roid = UserSingleton.getUserInfo().getRoleId();
//        LogUtils.i("m_roid = ",m_roid+"");
        switch (v.getId()) {

            case R.id.btnAddPic:
                int i = imageItem.size();
                if (imageItem.size() == 5) {
                    MyToast.showMyToast(NewRectifyNotifyInfoActivity.this, "最多支持上传5张图片", Toast.LENGTH_SHORT);
                } else {
                    MyTakePicDialog _takePicDialog = new MyTakePicDialog();
                    _takePicDialog.setOnItemClickListener(this);
                    m_dialog = _takePicDialog.showTakePicDialog(this);
                    m_dialog.show();
                }
                break;

            //提交通知单 先提交照片 然后提交日志
            case R.id.btnSubmit:
                m_logState = 1;
                if (checkInfo()) {
                    if (getPicture().length() != 0) {
                        uploadFirstPicture();
                    }else {
                        mReNoticePresenter.submit();
                    }
                }
                break;

            case R.id.btnDraft:
                m_logState = 2;
                if (getPicture().length() != 0) {
                    uploadFirstPicture();
                }else {
                    mReNoticePresenter.saveToDraft();
                }
//                if (checkInfo()) {
//                uploadFirstPicture();
//                RectifyNotifyInfoPresenter _rectifyNotifyInfoPresenter = new RectifyNotifyInfoPresenter(this);
//                _rectifyNotifyInfoPresenter.submit();
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
            //获取全部名单
            case R.id.edtLogCheckMans:
                startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.OWNER_ALL), GET_CHECKMAN);
                break;
            //如果是业主，可以选择监理
            case R.id.edtSupervisor:
                if (m_roid == 17) {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_THREE), GET_SUPERVISOR);
//                    LogUtils.i("UserPermission = ",UserPermission.SUPERVISON_THREE+"");
                }
                break;
            //如果是抄送，可以选择监全部
            case R.id.edtCopy:
//                if (m_roid == 17) {
//                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_COPYER);
//                } else
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.OWNER_ALL), GET_COPYER);
                break;

            //如果是监理或者施工方，可以选择施工方
            case R.id.edtConstruction:
//                if (m_roid == 17 || m_roid == 19) {
//                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.SUPERVISON_FIRST), GET_SUPERVISOR);
////                    LogUtils.i("UserPermission = ",UserPermission.SUPERVISON_THREE+"");
//                }else {
                    startActivityForResult(new Intent(this, RelationshipListActivity.class).putExtra("Type", UserPermission.CONSTRU_SECOND), GET_CONSTRUCTION);
//                }

                break;
            case R.id.inspect:
                if (inspect_Title.length!= 0)
                {
                    View view = LayoutInflater.from(this).inflate(R.layout.down_account, null, false);
                    LinearLayout contentview = (LinearLayout) view.findViewById(R.id.input_select_listlayout);
                    ListView listView = (ListView) view.findViewById(R.id.input_select_list);
                    listView.setDividerHeight(0);
                    m_inputAdapter = new InputAdapter(this, inspect_Title);
                    m_inputAdapter.setOnItemClickListener(this);
                    listView.setAdapter(m_inputAdapter);
                    initSpinnerEditText(contentview,be_ll);

                }
                break;
            case R.id.be:
                if (be_Title.length != 0)
                {
                    View view = LayoutInflater.from(this).inflate(R.layout.down_account, null, false);
                    LinearLayout contentview = (LinearLayout) view.findViewById(R.id.input_select_listlayout);
                    ListView listView = (ListView) view.findViewById(R.id.input_select_list);
                    listView.setDividerHeight(0);

                    m_beAdapter = new BeAdapter(this, be_Title);
                    m_beAdapter.setOnBeClickListener(this);
                    listView.setAdapter(m_beAdapter);
                    initSpinnerEditText(contentview,inspect_ll);
                }
                break;
            default:
                break;

        }


    }
    private void initSpinnerEditText( LinearLayout contentview, LinearLayout mInputLayout) {
        mSelectWindow = new PopupWindow(contentview, mInputLayout.getMeasuredWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mSelectWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mSelectWindow.setOutsideTouchable(true);
        mSelectWindow.showAsDropDown(mInputLayout, 0, 0);
    }
    @Override
    public void onItemClicked(int position) {
        closePopWindow();
        m_edtCheckUnit.setText(inspect_Title[position].toString());
    }
    @Override
    public void onBeClicked(int position) {
        closePopWindow();
        m_edtBecheckUnit.setText(be_Title[position].toString());
    }
    private void closePopWindow(){
        mSelectWindow.dismiss();
        mSelectWindow = null;
    }

    //上传第一张照片
    private void uploadFirstPicture() {
        //        File picFile = new File(FoldersConfig.PRO_SAFETY_PIC_PATH, picNames.get(0));
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

    @Override
    public String getCheckMans() {
        return m_edtCheckMans.getText().toString().trim();
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
        StringBuffer _stringBuffer = new StringBuffer();
        _stringBuffer.append(m_edtConstruction.getText().toString());
        if (!m_edtCopyer.getText().toString().isEmpty())
            _stringBuffer.append("#" + m_edtCopyer.getText().toString());

//        if (!m_edtSupervisor.getText().toString().isEmpty())
//            _stringBuffer.append("#" + m_edtSupervisor.getText().toString());

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


    @Override
    public String getSection() {
        String _trim = m_spSection.getSelectedItem().toString().trim();
        String _s = null;
        switch (_trim){
            case "第1标段":
                _s="TJ01";
                break;
            case "第2标段":
                _s="TJ02";
                break;
            case "第3标段":
                _s="TJ03";
                break;
            case "第4标段":
                _s="TJ04";
                break;
            case "第5标段":
                _s="TJ05";
                break;
            case "第6标段":
                _s="TJ06";
                break;
            case "第7标段":
                _s="TJ07";
                break;
            case "第8标段":
                _s="TJ08";
                break;
            case "第9标段":
                _s="TJ09";
                break;
            case "第10标段":
                _s="TJ10";
                break;
            case "第11标段":
                _s="TJ11";
                break;

        }
        return _s;

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
        uploadImgIndex = 0; //上传失败，初始化重新提交
        MyToast.showMyToast(this, msg.replace("\"", ""), Toast.LENGTH_SHORT);
        //        finish();
    }

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
        if (picNames.size() > uploadImgIndex){  //如果集合中的照片还有未上传的，继续上传
            isFinish = false;
        }else {
            isFinish = true;
        }
        if (!isFinish) {//上传未完成，继续发送下载指令
            Message _message = new Message();
            m_handler.sendMessageDelayed(_message, 500);
        } else {//图片上传成功，提交日志或者存为草稿
            if (m_logState == 2) {
                mReNoticePresenter.saveToDraft();
            } else
                mReNoticePresenter.submit();
        }
        //        resetParams();
    }

    @Override
    public void uploadFileFailed(String msg) {
        hideInSubmiting();
        uploadImgIndex = 0; //如果上传失败，照片上传数量初始化
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
    public String getImgInfo() {
        if (m_imgInfo.length()>0){
//            LogUtils.i("m_imgInfo",m_imgInfo.toString());
            return m_imgInfo.substring(0,m_imgInfo.length()-1);
        }
        return "";
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
                case CameraUtils.PHOTO_REQUEST_TAKEPHOTO:

                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        picBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//拍摄返回的bitmap
                        Bitmap _bitmap = CameraUtils.comp(picBitmap); //图片压缩
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
                                    MyAlertDialog.showAlertDialog(NewRectifyNotifyInfoActivity.this, "请输入此照片存在问题，必填！", "确定", "取消", false, m_tv, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //确认
//                                            LogUtils.i("m_remark", m_tv.getText().toString());
                                            m_remark = m_tv.getText().toString();
                                            if (m_remark.isEmpty()) return;
                                            ++m_remarkIndex;
                                            picNames.add(pictureName);  //照片名字列表
                                            picFiles.add(resultImgFile); //照片文件
                                            _map.put("itemImage", _bitmap);
                                            _map.put("remark",m_remark);
                                            imageItem.add(_map);
//                                            m_imgInfo.append(String.valueOf(m_remarkIndex)+m_remark+"#");
                                            m_listRemark.add(String.valueOf(m_remarkIndex)+m_remark);
//                                            m_edtFindPro.setText(m_imgInfo.toString());
//                                            LogUtils.i("adapter m_imgInfo",m_imgInfo.toString());
                                            refreshGridviewAdapter();
                                            dialog.dismiss();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            _map.put("remark","无照片描述");
//                                            imageItem.add(_map);
                                            // 取消
//                                            dialog.dismiss();
                                        }
                                    });
//                                    initedtFindPro();

                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;
                //从相册选择
                case CameraUtils.PHOTO_REQUEST_GALLERY:

                    uri = CameraUtils.getBitmapUriFromCG(requestCode, resultCode, data, fileUri);
                    if (uri != null) {
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};//取媒体文件路径集合
                        Cursor c = getContentResolver().query(uri, filePathColumns, null, null, null);//取选中图片的cursor
                        c.moveToFirst();
                        int colindex = c.getColumnIndex(filePathColumns[0]);//取索引
                        String imgpath = c.getString(colindex);//取文件相对手机路径
                        c.close();
                       Bitmap _picBitmap = CameraUtils.getimage(imgpath);
//                        picBitmap = BitmapFactory.decodeFile(imgpath);//拍摄返回的bitmap
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
                                    MyAlertDialog.showAlertDialog(NewRectifyNotifyInfoActivity.this, "请输入此照片存在问题，必填！", "确定", "取消", false, m_tv, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //确认
//                                            LogUtils.i("m_remark", m_tv.getText().toString());
                                            m_remark = m_tv.getText().toString();
                                            if (m_remark.isEmpty()) return;
                                            ++m_remarkIndex;
                                            picNames.add(pictureName);
                                            picFiles.add(resultImgFile);
                                            _map.put("itemImage", _picBitmap);
                                            _map.put("remark",m_remark);
                                            imageItem.add(_map);
                                            m_listRemark.add(String.valueOf(m_remarkIndex)+m_remark);
//                                            m_imgInfo.append(String.valueOf(m_remarkIndex)+m_remark+"#");
//                                            m_edtFindPro.setText(m_imgInfo.toString());
//                                            dialog.dismiss();
                                            refreshGridviewAdapter();
                                        }
                                    }, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 取消
//                                            _map.put("remark","无照片描述");
//                                            imageItem.add(_map);
//                                            dialog.dismiss();
                                        }
                                    });
//                                    initedtFindPro();

                                }
                            } else {
                                MyToast.showMyToast(this, "图片名不允许带特殊符号", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                    break;
                case GET_SUPERVISOR:
//                    if (resultCode == RESULT_OK) {
//                        String allNameList = "";
//                        m_edtSupervisor.setText("");
//                        for (String name : UsersList.getList()) {
//                            allNameList += name + "#";
//                        }
//                        m_edtSupervisor.setText(allNameList.substring(0, allNameList.length() - 1));
//                        UsersList.clearList();
//                    }
                    break;

                case GET_COPYER:
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            String _s = name.toString();
                            String _s1 = _s.substring(_s.lastIndexOf("#")+1);
                            allNameList += _s1 + "#";
                        }
                        m_edtCopyer.setText("");
                        m_edtCopyer.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;
                case GET_CHECKMAN://7
                    if (resultCode == RESULT_OK) {
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            String _s = name.toString();
                            String _s1 = _s.substring(_s.lastIndexOf("#")+1);
                            allNameList += _s1 + "#";
                        }
                        m_edtCheckMans.setText("");
                        m_edtCheckMans.setText(allNameList.substring(0, allNameList.length() - 1));
//                        m_edtCheckMans.setText(allNameList.substring(0, allNameList.length() - 1));

                        UsersList.clearList();
                    }
                    break;
                case GET_CONSTRUCTION:
                    if (resultCode == RESULT_OK) {
                        if (UsersList.getList().size()>1){
                            MyToast.showMyToast(NewRectifyNotifyInfoActivity.this,"只能选择一人发送，剩下的只能选择抄送，请重新选择",1);
                            return;
                        }
                        String allNameList = "";
                        for (String name : UsersList.getList()) {
                            String _s = name.toString();
                            String _s1 = _s.substring(_s.lastIndexOf("#")+1);
                            allNameList += _s1 + "#";
                        }
                        m_edtConstruction.setText("");
                        m_edtConstruction.setText(allNameList.substring(0, allNameList.length() - 1));
                        UsersList.clearList();
                    }
                    break;
            }
        } catch (Exception e) {
            MyToast.showMyToast(this, "new safety log error e:=" + e.getMessage(), Toast.LENGTH_SHORT);
//            LogUtils.i("异常", e.toString());
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

    private void initedtFindPro() {
        if (m_imgInfo.length()>0){
            m_imgInfo.delete(0,m_imgInfo.length());
        }
        for (int i = 0; i < m_listRemark.size(); i++) {
            m_imgInfo.append(m_listRemark.get(i)+"#");
        }
        m_edtFindPro.setText(m_imgInfo.toString());
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
                }
                if (view instanceof TextView && textRepresentation instanceof String) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {//绑定视图
                            TextView i = (TextView) view;
                            i.setText(textRepresentation);

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

        if (m_edtConstruction.getText().toString().trim().length() > 5) {
            MyToast.showMyToast(NewRectifyNotifyInfoActivity.this, "施工方只能选一个人，请重新选择", 1);
            isOk = false;
        }
        if (getReceiveMans().length() == 0) {

            m_edtConstruction.setError("接收人不能为空");
            isOk = false;

        }
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
