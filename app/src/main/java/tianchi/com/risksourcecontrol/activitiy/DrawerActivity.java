package tianchi.com.risksourcecontrol.activitiy;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.supermap.data.CoordSysTranslator;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Environment;
import com.supermap.data.FieldInfos;
import com.supermap.data.LicenseManager;
import com.supermap.data.LicenseStatus;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Rectangle2D;
import com.supermap.data.Size2D;
import com.supermap.data.Workspace;
import com.supermap.mapping.Action;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.GeometrySelectedEvent;
import com.supermap.mapping.GeometrySelectedListener;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapParameterChangedListener;
import com.supermap.mapping.MapView;
import com.supermap.mapping.MeasureListener;
import com.supermap.mapping.RefreshListener;
import com.supermap.services.FeatureSet;
import com.supermap.services.QueryMode;
import com.supermap.services.QueryOption;
import com.supermap.services.QueryService;
import com.supermap.services.ResponseCallback;
import com.supermap.services.ServiceQueryParameter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.message.ReceiveNoticeListActivity;
import tianchi.com.risksourcecontrol.activitiy.risksource.RiskResultListActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.BridgeTypeformMapActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.HighLowTypeRiskForMapActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.SoilTypeFormActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.TakingSoilFieldTypeFromActivity;
import tianchi.com.risksourcecontrol.activitiy.risksourceformap.TunnelTypeFromActivity;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.newnotice.NotifyMessagesInfo;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.config.SuperMapConfig;
import tianchi.com.risksourcecontrol.custom.MyAlertDialog;
import tianchi.com.risksourcecontrol.custom.MyPopupWindow;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.fragment.LogFragment;
import tianchi.com.risksourcecontrol.fragment.MessageFragment;
import tianchi.com.risksourcecontrol.fragment.OtherFuntionFragment;
import tianchi.com.risksourcecontrol.location.BaiDuGPS;
import tianchi.com.risksourcecontrol.location.BaseGPS;
import tianchi.com.risksourcecontrol.location.NavigationPanelView;
import tianchi.com.risksourcecontrol.model.OnOpenWorkSpaceListener;
import tianchi.com.risksourcecontrol.notification.MyNotification;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.ActivityCollector;
import tianchi.com.risksourcecontrol.util.GetNewNotifyUtils;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.view.IHomeView;

/**
 * @描述 底层侧滑活动
 * @作者 kevin蔡跃.
 * @创建日期 2017/11/4  12:07.
 */
public class DrawerActivity extends BaseActivity implements IHomeView, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, GeometrySelectedListener, MeasureListener, DrawerLayout.DrawerListener,
        AdapterView.OnItemSelectedListener {
    /*界面可见的控件*/
    private DrawerLayout m_drawerLayout;//侧滑布局
    //    private TextView m_tvUserProfile;//个人信息
    //    private TextView m_tvUserHead;//头像缩略图
    private TextView m_tvSignIn;//签到

    //    private EditText edtRiskQuery;//风险源查询输入框
    private RadioButton m_rdbtnLogAdmin;//日志管理
    //    private RadioButton m_rdbtnDisclosure;//技术交底
    private RadioButton m_rdbtnMessage;//消息管理
    //    private RadioButton m_rdbtnMine;//我的面板
    private RadioButton m_rdbtnNone;//空置radiobutton
    private RadioGroup m_radioGroup;
    /*3个主要的Fragment*/
    private FragmentManager m_fragmentManager;//fg管理器
    private FragmentTransaction m_fragmentTransaction;//fg事务
    // private UserAdminFragment m_userAdminFragment;//账号管理
    private LogFragment m_logFragment;//日志管理
    private OtherFuntionFragment m_funtionFragment;//
    private MessageFragment m_messageFragment;//日志管理
    //    private DisclosureFragment m_disclosureFragment;//技术交底
    /*超图相关*/
    private Map m_map; //地图
    private Workspace m_workspace;//工作空间
    private MapView m_mapView; //地图view
    private static MapControl m_MapControl; //地图控件类
    private Button m_btnZoomOut;//放大按钮
    private Button m_btnZoomIn;//缩小按钮
    private Button m_btnFull;//全图按钮
    private Button m_btnPan;//漫游按钮
    private ProgressDialog m_loadingDialog;//加载进度条
    private ProgressDialog m_queryingDialog;//查询进度条
    private Button m_btnMeaLength;//测量长度
    private Button m_btnMeaArea; //测量面积
    private RadioGroup m_radioGroup2; //测量容器
    //    private RadioButton m_rbtnMeaLeng;
    //    private RadioButton m_rbtnMeaArea;
    private Button m_btnLocation; //定位
    private TextView m_tvMeasure; //测量容器
    private Spinner m_spSection;//标段
    /*系统成员变量*/
    //    private String currentLoginName;// 当前账号
    private long intervalTime;//返回键时间间隔
    private List<String> m_listStakeNum;//存储具体风险源的桩号列表
    private List<String> m_riskTypeList;//存储风险源类型列表
    //    private RiskSourceListViewAdapter m_adapter;//风险源listview列表适配器
    //    private HomePresenter m_homePresenter = new HomePresenter(this);//主界面控制器
    private Datasource m_ds;
    private DatasourceConnectionInfo m_dsInfo;
    public static Point2D pointPixel;//全局位置点对象
    //.......导航...........................
    /*private LocationManager m_LocationManager;
    private String m_CurrentProvider;
    private CoordSysTranslator m_CoordSysTranslator;
    private Point2Ds m_Point2ds;
    private LocationService locationService;*/
    public static PrjCoordSys m_PrjCoordSys;
    private SensorManager m_SensorManager;
    private NavigationPanelView m_NavigationPanelView;
    //private Location m_currentLocation;
    //    private boolean m_IsGPSOpen = true;
    private boolean m_IsViewShow = false;
    private String[] m_arrSection;
    private boolean m_IsFirstOpen = true;
    private String m_section;
    private AlertDialog m_dialog;
    private Criteria m_criteria;
    //  private int m_tempsectionPosition = -1; //Activity 在onPause()下记录Spinner的位置
    //  private int m_tempStatuePosition = -1;
    private TextView m_tvStatue; //状态
    private final static int REQUESTCODE = 1; // 返回的结果码
    private CallOut m_callOut;
    private ImageView m_imageView;
    //    private QBadgeView m_qBadgeView;
    //    private Button m_btton;
    //    private Button m_btnOpenView;// 打开查询的View
    //    private Button m_btnStartQuery;//开始查询风险源
    //    private View m_viewQuery;//查询框
    //    private Button m_btnSelectSection;//选择标段
    //    private EditText m_edtPileOrName;//桩号或者名称
    private EditText m_edtSection; //标段
    private String[] m_spSction;
    private String m_realName;
    private BaseGPS m_GPS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Environment.setLicensePath(SuperMapConfig.LIC_PATH);
        Environment.initialization(this);
        setContentView(R.layout.activity_drawer);
        LogUtils.i("DrawerActivity", "-------------------onCreate");
        initView();
        init();
        setDefaultSelected();
        prepareWorkSpace();
        initNotification();
        if (!licenseStatus())
            Toast.makeText(DrawerActivity.this, "许可证不可使用，请联系技术员", Toast.LENGTH_LONG).show();

    }

    //请求网络、查看信息 通知发送
    private void initNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        GetNewNotifyUtils.getNoticeForNotify(ServerConfig.URL_QUERY_MSG, m_realName, new ReceiveNoticeListActivity.CallBack() {
            @Override
            public void getData(String string) {
                String msg = GsonUtils.getNodeJsonString(string, "msg");
                int status = GsonUtils.getIntNoteJsonString(string, "status");
                if (status == 0) {
                    MyToast.showMyToast(DrawerActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == -1) {
                    MyToast.showMyToast(DrawerActivity.this, msg, Toast.LENGTH_SHORT);
                } else if (status == 1) {
//                    GsonUtils.jsonToArrayBeans(string, "data", NotifyMessagesInfo.class);
                    List<NotifyMessagesInfo> _unReplyDatas = GsonUtils.jsonToArrayBeans(string, "data", NotifyMessagesInfo.class);
                    if (_unReplyDatas.size() != 0) {
                        MyNotification.sendNotification(DrawerActivity.this, notificationManager, _unReplyDatas.size());
                    }
                    LogUtils.i("_unReplyDatas.size = ", String.valueOf(_unReplyDatas.size()));
                }
            }
        });


//        GetNewNotifyUtils.getNoticeForNotify(ServerConfig.URL_QUERY_MYSELF__REPLY_NOTIFY, m_realName, new ReceiveNoticeListActivity.CallBack() {
//            @Override
//            public void getData(String string) {
//                String msg = GsonUtils.getNodeJsonString(string, "msg");
//                int status = GsonUtils.getIntNoteJsonString(string, "status");
//                if (status == 0) {
//                    MyToast.showMyToast(DrawerActivity.this, msg, Toast.LENGTH_SHORT);
//                } else if (status == -1) {
//                    MyToast.showMyToast(DrawerActivity.this, msg, Toast.LENGTH_SHORT);
//                } else if (status == 1) {
//                    String beanListUnReply = GsonUtils.getNodeJsonString(string, "UnReply");//解析数据
////                    String beanListStrReply = GsonUtils.getNodeJsonString(string, "Replys");//解析数据
//                    List<RectifyReplyInfo> _unReadDatas = GsonUtils.jsonToArrayBeans(beanListUnReply, "data", RectifyReplyInfo.class);
////                    List<RectifyReplyInfo> _ReadDatas = GsonUtils.jsonToArrayBeans(beanListStrReply, "data", RectifyReplyInfo.class);
//                    if (_unReadDatas.size() != 0) {
//                        MyNotification.sendNotification2(DrawerActivity.this, notificationManager, _unReadDatas.size());
//                    }
//                    LogUtils.i("_unReadDatas.size = ", String.valueOf(_unReadDatas.size()));
//                }
//            }
//        });


    }

    /**
     * 检测许可是否可用
     *
     * @auther HaiRun
     * created at 2018/8/9 14:03
     */
    boolean licenseStatus() {
        LicenseManager _licenseManager = LicenseManager.getInstance();
        LicenseStatus _licenseStatus = _licenseManager.getLicenseStatus();
        boolean isUse = _licenseStatus.isLicenseValid(); //是否有效
        return isUse;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("DrawerActivity----------", "onPause()");
    }

    private void prepareWorkSpace() {
        openWorkSpace(new OnOpenWorkSpaceListener() {
            @Override
            public void onOpenSucceed() {
                MyToast.showMyToast(DrawerActivity.this, "加载成功！", Toast.LENGTH_SHORT);
            }

            @Override
            public void onOpenFailed(String msg) {
                MyToast.showMyToast(DrawerActivity.this, msg, Toast.LENGTH_SHORT);
                LogUtils.i("错误信息 = ", msg);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("DrawerActivity", "-----------------------------------onStart()");

        //        m_rdbtnNone.setChecked(true);
        //        SelectFragment(3);
        //        m_drawerLayout.closeDrawer(Gravity.LEFT);
        //
        //        m_rdbtnNone.setChecked(true);
        //        SelectFragment(3);
        //        m_drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // m_LocationManager.removeUpdates(locationListener);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - intervalTime >= 2000) {
            MyToast.showMyToast(this, "再按一次返回键退出", Toast.LENGTH_SHORT);
            intervalTime = System.currentTimeMillis();
            return;
        } else {
            intervalTime = System.currentTimeMillis();
            ActivityCollector.finishAllActivity();
            System.exit(0);
        }
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
            m_spSection.setAdapter(new ArrayAdapter<String>(DrawerActivity.this, android.R.layout.simple_spinner_dropdown_item, _spData));
            //m_spSection.setAdapter(new SectionAdapter(DrawerActivity.this, m_arrSection));
        }
        // m_riskTypeList = Arrays.asList(getResources().getStringArray(R.array.riskType));//取到风险源类型列表
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (m_GPS == null) {
            Log.e("m_GPS", "initial gps faild...");
            return;
        }
        m_SensorManager.registerListener(m_GPS.getSensorListener(),
                SensorManager.SENSOR_ORIENTATION,
                SensorManager.SENSOR_DELAY_GAME);


        SharedPreferences _preferences = getSharedPreferences("riskSource_Location", MODE_PRIVATE);
        float location_X = _preferences.getFloat("X", 0);
        float location_Y = _preferences.getFloat("Y", 0);
        if (location_X != 0 && location_Y != 0) {
            locate2RiskSource(location_X, location_Y);
            _preferences.edit().putFloat("X", 0).putFloat("Y", 0).commit();
        }
    }

    @Override
    protected void onStop() {
        m_SensorManager.unregisterListener(m_GPS.getSensorListener());
        super.onStop();
    }

    /*
    * 打开工作空间
    * */
    private void openWorkSpace(OnOpenWorkSpaceListener openWorkSpaceListener) {
        showLoading();
        try {
            m_MapControl.getMap().setWorkspace(m_workspace);
            m_dsInfo = new DatasourceConnectionInfo();
            String _section = null;
            if (m_section.isEmpty()) {
                m_dialog = MyAlertDialog.showAlertDialog(this, "温馨提示", "您好，您没有所掌控的标段，将为您默认加载全标段，请事后联系管理员"
                        , R.mipmap.ic_smile, "确定", "返回", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_dialog.dismiss();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (m_dialog != null)
                                    m_dialog.dismiss();
                            }
                        });

                _section = "RiskSource0";
            } else {
                _section = ServerConfig.getMapMap().get(m_spSection.getSelectedItem().toString());
            }

            m_dsInfo.setServer(ServerConfig.URL_CHECK_SECTION + _section);
            m_dsInfo.setEngineType(EngineType.Rest);
            m_dsInfo.setAlias(_section);
            m_ds = m_workspace.getDatasources().open(m_dsInfo);
            if (m_ds != null) {
                m_MapControl.getMap().getLayers().add(m_ds.getDatasets().get(0), true);
                m_MapControl.getMap().refresh();
                hideLoading();
                openWorkSpaceListener.onOpenSucceed();
                m_MapControl.setRefreshListener(new RefreshListener() {
                    @Override
                    // TODO 自动生成的方法存根
                    public void mapRefresh() {
                        //                        if (!m_IsGPSOpen) return;
                        /*if (m_Point2ds != null && m_Point2ds.getCount() > 0) {
                            getmapToPixel(m_Point2ds.getItem(0));
                        }*/
                    }
                });

                //重力感应
                m_SensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                m_NavigationPanelView = new NavigationPanelView(this);
                m_mapView.addView(m_NavigationPanelView);
                m_MapControl.getMap().refresh();
                m_PrjCoordSys = m_ds.getDatasets().get(0).getPrjCoordSys();
                //百度地图定位初始化
                m_GPS = new BaiDuGPS();
                m_GPS.setApplication(this.getApplication());
                m_GPS.setNavigationPanel(m_NavigationPanelView);
                m_GPS.setPrjCoordSys(m_PrjCoordSys);
                m_GPS.setMap(m_MapControl);
                double[] scales = m_MapControl.getMap().getVisibleScales();
                for (int i = 0; i < scales.length; ++i) {
                    LogUtils.i("i: scale " + Double.valueOf(scales[i]));
                }
                // m_CoordSysTranslator = new CoordSysTranslator();
                LogUtils.i("投影名字", m_PrjCoordSys.getName());
                PrjCoordSysType _prjCoordSysType = m_PrjCoordSys.getType();
                LogUtils.i("访问服务成功.....");
                m_GPS.onStart();
            } else {
                hideLoading();
                openWorkSpaceListener.onOpenFailed("访问服务失败");
                LogUtils.i("访问失败", "访问服务失败.....");
            }
        } catch (Exception e) {
            if (openWorkSpaceListener != null) {
                openWorkSpaceListener.onOpenFailed(e.getClass().getSimpleName() + " error detail:" + e.getMessage());
                hideLoading();
            }
            e.printStackTrace();
        }
    }

    /*
    * 初始化布局
    * */
    private void initView() {


        m_realName = UserSingleton.getUserInfo().getRealName();
        m_mapView = (MapView) findViewById(R.id.mvMap);
        //地图标注
        m_callOut = new CallOut(this);
        m_callOut.setCustomize(true);
        m_callOut.setStyle(CalloutAlignment.CENTER);
        m_imageView = new ImageView(DrawerActivity.this);
        m_imageView.setBackgroundResource(R.drawable.ic_location_32px);
        m_callOut.setContentView(m_imageView);
        //        m_mapView.addCallout(m_callOut);

        m_drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        m_tvUserProfile = (TextView) findViewById(R.id.tvActionbarLeft);
//        m_tvUserHead = (TextView) findViewById(R.id.tvActionbarLeft);
        m_tvSignIn = (TextView) findViewById(R.id.tvActionbarRight);
        m_rdbtnLogAdmin = (RadioButton) findViewById(R.id.rdbtnLogAdmin);
//        m_rdbtnDisclosure = (RadioButton) findViewById(R.id.rdbtnDisclosure);
        m_rdbtnMessage = (RadioButton) findViewById(R.id.rdbtnMessage);
//        m_rdbtnMine = (RadioButton) findViewById(R.id.rdbtnMine);
        m_rdbtnNone = (RadioButton) findViewById(R.id.rdbtnNone);
        m_radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        //通知未读显示
        // m_btton = (Button) findViewById(R.id.btn_number);
//        m_qBadgeView = new QBadgeView(this);
//        m_qBadgeView.bindTarget(m_btton).setBadgeNumber(100).setBadgeTextSize(7, true).setBadgeGravity(Gravity.TOP | Gravity.CENTER);


        m_btnZoomOut = (Button) findViewById(R.id.btnZoomOut);
        m_btnZoomIn = (Button) findViewById(R.id.btnZoomIn);
        m_btnFull = (Button) findViewById(R.id.btnViewEntire);
        m_btnPan = (Button) findViewById(R.id.btnPan);
        //状态
        m_tvStatue = (TextView) findViewById(R.id.tvStatue);
        m_btnMeaArea = (Button) findViewById(R.id.btnArea);
        m_btnMeaLength = (Button) findViewById(R.id.btnLength);
        m_btnLocation = (Button) findViewById(R.id.btnLocal);
        m_tvMeasure = (TextView) findViewById(R.id.tvMeasure);
        //        m_btnRiskQuery = (Button) findViewById(R.id.btnQueryRisk);
        //        m_autoRiskQuery = (AutoCompleteTextView) findViewById(R.id.autoRiskQuery);
        //        edtRiskQuery = (EditText) findViewById(R.id.edtRiskQuery);
//        m_spRiskType = (Spinner) findViewById(R.id.spRiskType);
        m_spSection = (Spinner) findViewById(R.id.spSection);
        //        m_btnStartQuery = (Button) findViewById(R.id.btnStartQuery);
        //        m_btnOpenView = (Button) findViewById(R.id.btnOpenView);
        //        m_viewQuery = findViewById(R.id.view_query);
//        m_btnSelectSection = (Button) findViewById(R.id.btnSelectSection);
        //        m_edtPileOrName = (EditText) findViewById(R.id.edtPileOrName);
        m_edtSection = (EditText) findViewById(R.id.edtSection);
        //测量容器
        m_radioGroup2 = (RadioGroup) findViewById(R.id.rdg_btn_measure);
//        m_rbtnMeaArea = (RadioButton) findViewById(R.id.rdg_btn_mea_area);
//        m_rbtnMeaLeng = (RadioButton) findViewById(R.id.rdg_btn_mea_length);
        m_radioGroup2.setOnCheckedChangeListener(this);
        //初始化加载进度条
        m_loadingDialog = new ProgressDialog(this);
        m_loadingDialog.setMessage("工作空间加载中...");
        m_loadingDialog.setCancelable(false);
        //初始化查询进度条
        m_queryingDialog = new ProgressDialog(this);
        m_queryingDialog.setMessage("查询中...");
        m_queryingDialog.setCancelable(false);
        //        m_tvUserProfile.setOnClickListener(this);
        m_tvSignIn.setOnClickListener(this);
        m_radioGroup.setOnCheckedChangeListener(this);
        m_btnZoomOut.setOnClickListener(this);
        m_btnZoomIn.setOnClickListener(this);
        m_btnFull.setOnClickListener(this);
        m_btnPan.setOnClickListener(this);
        m_btnMeaArea.setOnClickListener(this);
        m_btnMeaLength.setOnClickListener(this);
        m_btnLocation.setOnClickListener(this);
        m_drawerLayout.setDrawerListener(this);
//        m_btnRiskQuery.setOnClickListener(this);
//        m_btnOpenView.setOnClickListener(this);
//        m_btnStartQuery.setOnClickListener(this);
//        m_btnSelectSection.setOnClickListener(this);
//        m_spRiskType.setOnItemSelectedListener(this);
        m_workspace = new Workspace();
        m_MapControl = m_mapView.getMapControl();
        m_map = m_MapControl.getMap();
        m_MapControl.addGeometrySelectedListener(this);
        m_spSection.setOnItemSelectedListener(this);

        TextView tvScale = (TextView) findViewById(R.id.tvScale);


        m_MapControl.setMapParamChangedListener(new MapParameterChangedListener() {
            @Override
            public void scaleChanged(double v) {
                if (m_MapControl.getMap() != null) {
                    float _m = (float) (1.0 / v);
                    LogUtils.i(String.valueOf(_m));
                    tvScale.setText("比例尺 1 : " + String.format("%.2f", _m));
                }
            }
            @Override
            public void boundsChanged(Point2D point2D) {

            }

            @Override
            public void angleChanged(double v) {

            }

            @Override
            public void sizeChanged(int i, int i1) {

            }
        });
//        m_spRiskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (m_spRiskType.getSelectedItem().toString().equals("桥梁名称")) {
////                    m_edtPileOrName.setHint("请输入桥梁名称");
//                } else if (m_spRiskType.getSelectedItem().toString().equals("隧道名称")) {
////                    m_edtPileOrName.setHint("请输入隧道名称");
//                } else {
////                    m_edtPileOrName.setHint("请输入风险源桩号");
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    /*
    * 设置默认选中的fragment
    * */
    private void setDefaultSelected() {
        commitFragmentTransaction(3);
    }

    /*
    * 提交fragment的切换
    * */
    private void commitFragmentTransaction(int i) {
        if (m_fragmentManager == null) {
            m_fragmentManager = getSupportFragmentManager();
        }
        m_fragmentTransaction = m_fragmentManager.beginTransaction();
        HideFragment(m_fragmentTransaction);
        SelectFragment(i);
        LogUtils.i("DrawerActivity", "commitFragmentTransaction+ i ==" + i);
        m_fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人信息
            //            case R.id.tvActionbarLeft:
            //                setAllSelectedFalse();
            //                commitFragmentTransaction(0);
            //                m_drawerLayout.openDrawer(Gravity.LEFT);
            //                startActivity(new Intent(this, UserProfileActivity.class));
            //                showMeasureTv(0);
            //                break;
            //签到
            case R.id.tvActionbarRight:
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();
                // m_homePresenter.signIn();
                MyPopupWindow _popupWindow = new MyPopupWindow(this);
                _popupWindow.showPopupWindow(findViewById(R.id.tvActionbarRight));
                setActionPan();
                showMeasureTv(0);
                break;
            //放大
            case R.id.btnZoomOut:
                double _scale = m_MapControl.getMap().getScale();
//                LogUtils.i("比例尺=", String.valueOf(_scale));
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();
                m_MapControl.getMap().zoom(2);
                double scale = m_MapControl.getMap().getScale();
//                LogUtils.i("地图比例尺----------", String.valueOf(1/scale));
                m_MapControl.getMap().refresh();
                setActionPan();
                showMeasureTv(0);
                break;
            //缩小
            case R.id.btnZoomIn:
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();
                m_MapControl.getMap().zoom(0.5);
                m_MapControl.getMap().refresh();
                setActionPan();
                showMeasureTv(0);
                break;
            //点击查询
            case R.id.btnViewEntire:
                setAllBtnBgNor();
                if (m_tvStatue.getText().toString() != "点击查询") {
                    m_tvStatue.setText("点击查询");
                    m_btnFull.setBackgroundResource(R.mipmap.ic_btn_open_query_press);
                    m_MapControl.setGestureDetector(new GestureDetector(onGestureListener));
                } else {
                    m_btnFull.setBackgroundResource(R.mipmap.ic_btn_open_query_nor);
                    m_MapControl.deleteGestureDetector();
                    setActionPan();
                    setBtnPanBg();
                }
                //                m_MapControl.cancelAnimation();
                //                double _scale = 1.0;
                //                m_MapControl.getMap().viewEntire();
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.zoomTo(_scale, 1);
                //                m_MapControl.panTo(new Point2D(12828382.902995, 2740752.028358), 1);

                m_MapControl.getMap().refresh();
                showMeasureTv(0);
                break;
            //漫游
            case R.id.btnPan:
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();
                setActionPan();
                setBtnPanBg();
                showMeasureTv(0);

                break;
            //            //查询风险源
            //            case R.id.btnQueryRisk:
            //                showMeasureTv(0);
            //                setAllBtnBgNor();
            //                setBtnPanBg();
            //                m_MapControl.deleteGestureDetector();
            //                setActionPan();
            //                if (getRiskStakeNum().isEmpty()) {
            //                    MyToast.showMyToast(DrawerActivity.this, "请输入风险源桩号再查询", Toast.LENGTH_SHORT);
            //                    return;
            //                }
            //                m_homePresenter.riskQuery();
            //
            //                break;

            //测量长度
            case R.id.btnLength:
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();
                if (m_MapControl.getAction().equals(Action.MEASURELENGTH)) {
                    setActionPan();
                    showMeasureTv(0);
                    m_btnMeaLength.setBackgroundResource(R.mipmap.ic_btn_mea_length_nor);
                    setBtnPanBg();
                } else {
                    showMeasureTv(1);
                    m_MapControl.addMeasureListener(this);
                    m_MapControl.setAction(Action.MEASURELENGTH);
                    m_tvStatue.setText("测量长度");
                    m_tvMeasure.setText("长度：");
                    m_btnMeaLength.setBackgroundResource(R.mipmap.ic_btn_mea_length_press);
                }

                break;
            //            测量面积
            case R.id.btnArea:
                setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();

                if (m_MapControl.getAction().equals(Action.MEASUREAREA)) {
                    m_btnMeaArea.setBackgroundResource(R.mipmap.ic_btn_mea_area_nor);
                    showMeasureTv(0);
                    setActionPan();
                    setBtnPanBg();

                } else {
                    showMeasureTv(1);
                    m_MapControl.addMeasureListener(this);
                    m_MapControl.setAction(Action.MEASUREAREA);
                    m_tvStatue.setText("测量面积");
                    m_tvMeasure.setText("面积：");
                    m_btnMeaArea.setBackgroundResource(R.mipmap.ic_btn_mea_area_press);
                }

                break;
            //定位
            case R.id.btnLocal:
             /*   setAllBtnBgNor();
                m_MapControl.deleteGestureDetector();*/
                setActionPan();
                // m_LocationView.setVisibility(View.VISIBLE);
                Log.i("click", "local");
             /*   //是否启动GPS
                if (!isOPen(this)) {
                    openGPS(this);
                }
                //根据设置的Criteria对象，获取最符合此标准的provider对象
                m_CurrentProvider = m_LocationManager.getBestProvider(m_criteria, true);
                //根据当前provider对象获取最后一次位置信息
                Location currentLocation = m_LocationManager.getLastKnownLocation(m_CurrentProvider);
                //如果位置信息为null，则请求更新位置信息
                if (currentLocation != null) {
                    updateWithNewLocation(currentLocation);
                    m_MapControl.getMap().setCenter(m_Point2ds.getItem(0));
                    m_MapControl.getMap().setScale(1 / 57373.046875);
                    m_MapControl.getMap().refresh();
                }
                Handler handler = new Handler();
                // handler.
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        //要做的事情
                        m_LocationManager.requestLocationUpdates(m_CurrentProvider, 0, 0, locationListener);
                    }
                };
                handler.postDelayed(runnable, 200);*/
                //m_IsViewShow = !m_IsViewShow;
               /* if (m_IsViewShow) {
                    m_LocationView.setVisibility(View.GONE);
                    m_IsViewShow = !m_IsViewShow;
                } else {
                    m_LocationView.setVisibility(View.VISIBLE);
                    m_IsViewShow = !m_IsViewShow;
                }*/
                // setBtnPanBg();
                // showMeasureTv(0);
                break;
            //            //显示查询框
            //            case R.id.btnOpenView:
            //                    m_viewQuery.setVisibility(View.VISIBLE);
            //                    m_tvStatue.setVisibility(View.GONE);
            //                    m_btnOpenView.setVisibility(View.GONE);
            //
            //                break;
            //            //开始查询风险源
            //            case R.id.btnStartQuery:
            //
            //                if(getRiskStakeNum().isEmpty()||getSection().isEmpty()){
            //                    MyToast.showMyToast(DrawerActivity.this, "请把查询条件都输入后再查询", Toast.LENGTH_SHORT);
            //                }else {
            //                    m_viewQuery.setVisibility(View.GONE);
            //                    m_tvStatue.setVisibility(View.VISIBLE);
            //                    m_btnOpenView.setVisibility(View.VISIBLE);
            //                    m_homePresenter.riskQuery();
            //                }
            //                break;
            //选择标段
            //            case R.id.btnSelectSection:
            //
            //               showDialog(m_spSction,m_edtSection,"标段");
            //
            //                break;
            default:
                break;
        }
    }

    private void setActionPan() {
        m_tvStatue.setText("漫游");
        m_MapControl.setAction(Action.PAN);
    }

    private void setBtnPanBg() {
        if (m_MapControl.getAction().equals(Action.PAN)) {
            m_btnPan.setBackgroundResource(R.mipmap.ic_btn_pan_press);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        showMeasureTv(0);
        m_fragmentManager = getSupportFragmentManager();
        m_fragmentTransaction = m_fragmentManager.beginTransaction();
        HideFragment(m_fragmentTransaction);
        switch (checkedId) {
            case R.id.rdbtnLogAdmin:
                SelectFragment(1);
                m_drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.rdbtnOther:
                SelectFragment(2);
                m_drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.rdbtnMessage:
                SelectFragment(3);
                m_drawerLayout.openDrawer(Gravity.LEFT);
                break;
//            case R.id.rdbtnMine:
//                LogUtils.i("DrawerActivity--------onCheckedChanged", "跳转 tiaozMinePageActivity");
//                SelectFragment(4);
//                break;
            default:
                break;
        }
        m_fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0://如果是从“我的”面板返回必须重置底部按钮状态，左侧抽屉初始为消息模块
                m_rdbtnNone.setChecked(true);
                SelectFragment(3);
                break;
            default:
                break;
        }
    }

    private void locate2RiskSource(float X, float Y) {
        Point2Ds _point2Ds = new Point2Ds();
        _point2Ds.add(new Point2D(X, Y));
        boolean isOK = CoordSysTranslator.forward(_point2Ds, m_PrjCoordSys);
        if (isOK) {

            //                 Point _point =m_mapView.getMapControl().getMap().mapToPixel(_point2Ds.getItem(0));

            //创建一个Criteria对象
            m_MapControl.getMap().setCenter(_point2Ds.getItem(0));


            double x = _point2Ds.getItem(0).getX();
            double y = _point2Ds.getItem(0).getY();

            //                    LogUtils.i("x=======", String.valueOf(x));
            //                    LogUtils.i("y=======", String.valueOf(y));
            //                    //标注类
            //                    if (m_callOut == null) {
            //                        m_callOut = new CallOut(this);
            //                    }
            //                    m_callOut.setCustomize(true);
            //                    m_callOut.setStyle(CalloutAlignment.CENTER);

            //                    if (m_imageView == null) {
            //                        m_imageView = new ImageView(DrawerActivity.this);
            //                    }
            //                    m_imageView.setBackgroundResource(R.drawable.ic_location_32px);
            //                    m_callOut.setContentView(m_imageView);
            m_callOut.setLocation(x, y);
            m_mapView.removeAllCallOut();
            m_mapView.addCallout(m_callOut);
            //地图放大到一定的比例尺
            m_MapControl.getMap().setScale(2.2153311965710296E-4);
            m_MapControl.setGestureDetector(new GestureDetector(onGestureListener));
            m_btnFull.setBackgroundResource(R.mipmap.ic_btn_open_query_press);
            m_tvStatue.setText("点击查询");
            m_MapControl.getMap().refresh();
        }
    }

    //选项卡置false
    private void setAllSelectedFalse() {
        m_rdbtnMessage.setChecked(false);
        m_rdbtnLogAdmin.setChecked(false);
//        m_rdbtnDisclosure.setChecked(false);
//        m_rdbtnMine.setChecked(false);
        m_rdbtnNone.setChecked(false);
    }

    //显示对应framgnet
    private void SelectFragment(int index) {
        switch (index) {
            //            case 0:
            //                if (m_userAdminFragment == null) {
            //                    m_userAdminFragment = new UserAdminFragment();
            //                    m_fragmentTransaction.add(R.id.content, m_userAdminFragment);
            //                } else
            //                    m_fragmentTransaction.show(m_userAdminFragment);
            //                break;
            case 1:
                if (m_logFragment == null) {
                    m_logFragment = new LogFragment();
                    m_fragmentTransaction.add(R.id.content, m_logFragment);
                } else
                    m_fragmentTransaction.show(m_logFragment);
                break;
            case 2:
                if (m_funtionFragment == null) {
                    m_funtionFragment = new OtherFuntionFragment();
                    m_fragmentTransaction.add(R.id.content, m_funtionFragment);
                } else
                    m_fragmentTransaction.show(m_funtionFragment);
                break;
            case 3:
                LogUtils.i("DrawerActivity--------", "跳转 tiaozMinePageActivity33333");
                if (m_messageFragment == null) {
                    m_messageFragment = new MessageFragment();
                    m_fragmentTransaction.add(R.id.content, m_messageFragment);
                } else
                    m_fragmentTransaction.show(m_messageFragment);
                break;
//            case 4:
//                LogUtils.i("DrawerActivity--------", "跳转 tiaozMinePageActivity4444444");
//                startActivityForResult(new Intent(this, MinePageActivity.class), 0);
//                //                overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
//                break;
            default:
                break;
        }
    }

    /*
    * 隐藏所有fragment
    * */
    private void HideFragment(FragmentTransaction fragmentTransaction) {
        if (m_logFragment != null) {
            fragmentTransaction.hide(m_logFragment);
        }
        //        if (m_userAdminFragment != null) {
        //            fragmentTransaction.hide(m_userAdminFragment);
        //        }
        if (m_funtionFragment != null) {
            fragmentTransaction.hide(m_funtionFragment);
        }
        if (m_messageFragment != null) {
            fragmentTransaction.hide(m_messageFragment);
        }
    }

    @Override
    public void showLoading() {
        if (m_loadingDialog != null)
            m_loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (m_loadingDialog != null)
            m_loadingDialog.dismiss();
    }

    @Override
    public void showQuerying() {
        if (m_queryingDialog != null)
            m_queryingDialog.show();
    }

    @Override
    public void hideQuerying() {
        if (m_queryingDialog != null)
            m_queryingDialog.dismiss();
    }


    @Override
    public void showLoadingError() {
        hideLoading();
        MyToast.showMyToast(this, "加载工作空间失败", Toast.LENGTH_SHORT);
    }

    @Override
    public String getRiskType() {
        //        String riskType = m_spRiskType.getSelectedItem().toString();

        //        if (riskType != null) {
        //            if (riskType.equals("隧道名称") || riskType.equals("桥梁名称")) {
        //                return riskType;
        //            } else {
        //                return riskType.substring(2, riskType.length());
        //            }
        //        }
        return "";
    }

    @Override
    public String getRiskStakeNum() {
        //        return m_autoRiskQuery.getText().toString();
        if (m_edtSection.getText().toString().isEmpty()) {
            MyToast.showMyToast(this, "请输入桩号或者名称", Toast.LENGTH_SHORT);
        }
        //        return m_edtPileOrName.getText().toString();
        return "";
    }

    @Override
    public String getSection() {
        if (m_edtSection.getText().toString().isEmpty()) {
            MyToast.showMyToast(this, "请选择标段", Toast.LENGTH_SHORT);
        }
        return m_edtSection.getText().toString();
    }

    @Override
    public void showRiskQuerySucceed(List<String> list) {//展示风险源查询成功
        Intent _intent = new Intent(DrawerActivity.this, RiskResultListActivity.class);
        Bundle _bundle = new Bundle();
        _bundle.putStringArrayList("pipeNo", (ArrayList<String>) list);
        _bundle.putString("riskType", getRiskType());
        _bundle.putString("section", getSection());
        _intent.putExtras(_bundle);
        startActivityForResult(_intent, REQUESTCODE);
    }

    @Override
    public void showRiskQueryFailed() {
        MyToast.showMyToast(this, "未找到该桩号相应风险源信息", Toast.LENGTH_SHORT);
    }

    @Override
    public void toSignInWindow() {
        startActivity(new Intent(DrawerActivity.this, PatrolSignInActivity.class));
    }

    @Override
    public MapControl getMapControl() {
        if (m_MapControl != null)
            return m_MapControl;
        return null;
    }

    @Override
    public Map getMap() {
        if (m_map != null)
            return m_map;
        return null;
    }

    @Override
    public Workspace getWorkSpace() {
        return m_workspace;
    }

    @Override
    public void returnRiskResultList() {

    }

    //单选
    @Override
    public void geometrySelected(GeometrySelectedEvent geometrySelectedEvent) {
        //        Selection _selection = geometrySelectedEvent.getLayer().getSelection(); //获取到选择的对象
        //        Recordset _reSet = _selection.toRecordset();
        //        if (!_reSet.edit()){
        //            return;
        //        }
        //
        //       String riskId = (String) _reSet.getFieldValue("riskId");
        //        switch (riskId.substring(2).toUpperCase()){
        //            //高边坡  高填深挖  低挖浅埋  一般陡坡
        //            case "A":
        //            case "B":
        //            case "C":
        //            case "D":
        //                break;
        //            //软土  高液限土
        //            case "E":
        //            case "F":
        //                break;
        //
        //        }
    }

    //多选
    @Override
    public void geometryMultiSelected(ArrayList<GeometrySelectedEvent> arrayList) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (m_section.isEmpty()) return;
        if (m_IsFirstOpen) {
            m_IsFirstOpen = false;
            return;
        }
        try {
            if (m_dsInfo != null) {
                m_dsInfo.dispose();
            }

            m_MapControl.getMap().close();
            m_dsInfo = new DatasourceConnectionInfo();
            String section = ServerConfig.getMapMap().get(m_spSection.getSelectedItem().toString());
            if (section == null) return;
            m_dsInfo.setServer(ServerConfig.URL_CHECK_SECTION + section);
            LogUtils.i("MapUrl=-------------", ServerConfig.URL_CHECK_SECTION + section);
            m_dsInfo.setEngineType(EngineType.Rest);
            m_dsInfo.setAlias(section);  //别名  每个标段，要设置不一样的别名
            //用别名查找，如果找不到，则用数据源连接信息类打开数据源
            m_ds = m_workspace.getDatasources().get(section);
            if (m_ds == null) {
                m_ds = m_workspace.getDatasources().open(m_dsInfo);
            }
            if (m_ds != null) {
                m_MapControl.getMap().getLayers().add(m_ds.getDatasets().get(0), true);
                String mapName = m_MapControl.getMap().getName();
                LogUtils.i("MapName=-------------", mapName);
                double _scale = 1.0;
                m_MapControl.getMap().viewEntire();
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.zoomTo(_scale, 1);
                m_MapControl.panTo(new Point2D(12828382.902995, 2740752.028358), 1);
                m_MapControl.getMap().refresh();
                hideLoading();
                MyToast.showMyToast(DrawerActivity.this, "选择了" + m_spSection.getSelectedItem().toString(), Toast.LENGTH_SHORT);
                LogUtils.i("访问服务成功.....");
                return;
            }
            hideLoading();
//            openWorkSpaceListener.onOpenFailed();
            LogUtils.i("访问服务失败.....");
        } catch (Exception e) {
//            if (openWorkSpaceListener != null) {
//                openWorkSpaceListener.onOpenFailed();
//                hideLoading();
//            }
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (m_radioGroup2.getCheckedRadioButtonId() != R.id.rdg_btn_mea_area || m_radioGroup2.getCheckedRadioButtonId() != R.id.rdg_btn_mea_length) {
            m_rdbtnNone.setChecked(true);
            SelectFragment(3);
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    /**
     * 测量成度
     *
     * @param v     长度
     * @param point 对象
     */
    @Override
    public void lengthMeasured(double v, Point point) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (v < 1000) {
            m_tvMeasure.setText("长度：" + df.format(v) + "米");
        } else {
            m_tvMeasure.setText("长度：" + df.format(v / 1000) + "公里");
        }
    }

    /**
     * 测量面积
     *
     * @param v     面积
     * @param point 对象
     */
    @Override
    public void areaMeasured(double v, Point point) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (v < 1000000) {
            m_tvMeasure.setText("面积：" + df.format(v) + "平方米");
        } else {
            m_tvMeasure.setText("面积：" + df.format(v / 1000000) + "平方公里");
        }
    }

    /**
     * 测量角度
     *
     * @param v     面积
     * @param point 对象
     */
    @Override
    public void angleMeasured(double v, Point point) {

    }

    /**
     * 是否显示或者隐藏测量容器
     */
    public void showMeasureTv(int i) {
        if (i == 1) {
            m_tvMeasure.setVisibility(View.VISIBLE);
        } else
            m_tvMeasure.setVisibility(View.GONE);
    }

    /**
     * android 手势事件，用于地图点选或长按点选
     */
    private boolean isQuerying = true;
    private long firstTime;
    protected GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onSingleTapUp(MotionEvent e) {
            if (isQuerying) {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    firstTime = secondTime;
                    isQuerying = false;
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    LogUtils.i("XXXXX =", String.valueOf(e.getX()));
                    LogUtils.i("YYYYY =", String.valueOf(e.getY()));
                    Point2D cachePoint = m_MapControl.getMap().pixelToMap(new Point(x, y)); // getMap().pixelToMap(new Point(x, y)将地图中指定点的像素坐标转换为地图坐标,
                    // Recordset recordset = null;
                    // GeoPoint gp = new GeoPoint(cachePoint);
                    LogUtils.i("选中的点 " + cachePoint.toString());
                    try {

                        /*Dataset datasetVector = (Dataset)m_workspace.getDatasources().get(0).getDatasets().get(0);
                        Logs.i("dataset type "+ datasetVector.getType());*/

                        Query(cachePoint);
                      /*  recordset = datasetVector.query(gp, 0.0001, CursorType.STATIC);
                        if (recordset ==null || recordset.getRecordCount() < 1) {
                            Toast.makeText(DrawerActivityTest.this, "未搜索到对象", Toast.LENGTH_SHORT)
                                    .show();
                            Logs.i("is ture to select the name: ");
                            return false;
                        }*/
                        //  Logs.i("name: "+recordset.getID());
                        // showGridInfo(recordset);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block

                    }
                    isQuerying = true;
                } else {
                    Toast.makeText(DrawerActivity.this, "点击太频繁", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(DrawerActivity.this, "正在执行查询...", Toast.LENGTH_SHORT)
                        .show();
            }
            return true;
        }

        public void onLongPress(MotionEvent e) {
        }
    };

    // 查询
    private void Query(Point2D pt) {
        m_mapView.removeAllCallOut();
        final ProgressDialog progress = new ProgressDialog(DrawerActivity.this);
        //http://localhost:8090/iserver/services/map-RiskSource/rest/maps/RiskSource0
        QueryService service = new QueryService(ServerConfig.URLMAP);  //外网地图访问
//        QueryService service = new QueryService("http://192.168.0.21:8090");  //内网地图访问
        ServiceQueryParameter parameter = new ServiceQueryParameter();
        parameter.setQueryMapName("RiskSource0");
        parameter.setQueryServiceName("map-RiskSource/rest");
        parameter.setQueryLayerName("AllRisk@HHB_RISK_XIAN80");    //f16@HHB_RISK_XIAN80
        //设置查询参数
        parameter.setExpectRecordCount(1);
        //parameter.setQueryRecordStart(0);
        parameter.setQueryOption(QueryOption.ATTRIBUTE);
        // parameter.setQueryGeomety(new GeoPoint(pt));
        parameter.setQueryBounds(new Rectangle2D(pt, new Size2D(70, 70)));
        // parameter.setQueryDistance(100.0);
        //parameter.setAttributeFilter("SMID>0");
        service.setResponseCallback(new ResponseCallback() {
            @Override
            public void requestSuccess() {
                //销毁进度条显示框
                progress.dismiss();

                //                Toast.makeText(DrawerActivity.this, "查询成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void requestFailed(String arg0) {
                //销毁进度条显示框
                progress.dismiss();
                Toast.makeText(DrawerActivity.this, "查询失败", Toast.LENGTH_LONG).show();
                System.out.println("错误信息 " + arg0);
            }

            @Override
            public void receiveResponse(FeatureSet arg0) {
                if (arg0 instanceof FeatureSet) {
                    FeatureSet featureSet = (FeatureSet) arg0;
                    System.out.println("has query some data");
                    int nCount = 0;
                    featureSet.moveFirst();
                    while (!featureSet.isEOF()) {

                      /*  Geometry geo = featureSet.getGeometry();
                        if (geo == null) {
                            featureSet.moveNext();
                            continue;
                        }*/
                        FieldInfos infos = featureSet.getFieldInfos();
                        for (int i = 0; i < infos.getCount(); ++i) {
                            LogUtils.i("i = " + i + ", value = " + featureSet.getFieldValue(infos.get(i).getName()));
                            LogUtils.i("字段名称 = " + i + ", value = " + infos.get(i).getName());
                        }


                        Bundle _bundle = new Bundle();
                        Intent _intent;
                        String _riskId = (String) featureSet.getFieldValue("序号");
                        String type = _riskId.substring(2, 3);
                        LogUtils.i("type_________________", type);
                        switch (type) {
                            //高边坡  高填深挖  低挖浅埋  一般陡坡
                            case "A":
                            case "B":
                            case "C":
                            case "D":
                                _intent = new Intent();
                                _intent.setClass(DrawerActivity.this, HighLowTypeRiskForMapActivity.class);
                                String _pileNo = (String) featureSet.getFieldValue("起讫桩号");
                                String _slopeType = (String) featureSet.getFieldValue("边坡类型");
                                double _slopeHighMax1 = (double) featureSet.getFieldValue("边坡最大高");
                                String _slopeHighMax = String.valueOf(_slopeHighMax1);
                                double _slopeLength1 = (double) featureSet.getFieldValue("边坡长度");
                                String _slopeLength = String.valueOf(_slopeLength1);
                                double _step1 = (double) featureSet.getFieldValue("级数");
                                String _step = String.valueOf(_step1);
                                String _protectWay = (String) featureSet.getFieldValue("防护方式");
                                double _scope1 = (double) featureSet.getFieldValue("评分");
                                String _scope = String.valueOf(_scope1);
                                double _colorNote1 = (double) featureSet.getFieldValue("颜色标识");
                                String _colorNote = String.valueOf(_colorNote1);
                                String _occurPro = (String) featureSet.getFieldValue("发生可能性");
                                String _resultGrade = (String) featureSet.getFieldValue("后果严重性");
                                _bundle.putString("_riskId", _riskId);
                                _bundle.putString("_pileNo", _pileNo);
                                _bundle.putString("_slopeType", _slopeType);
                                _bundle.putString("_slopeHighMax", _slopeHighMax);
                                _bundle.putString("_slopeLength", _slopeLength);
                                _bundle.putString("_step", _step);
                                _bundle.putString("_protectWay", _protectWay);
                                _bundle.putString("_scope", _scope);
                                _bundle.putString("_colorNote", _colorNote);
                                _bundle.putString("_occurPro", _occurPro);
                                _bundle.putString("_resultGrade", _resultGrade);
                                _intent.putExtras(_bundle);
                                startActivity(_intent);
                                break;
                            //软土  高液限土
                            case "E":
                            case "F":

                                _intent = new Intent();
                                _intent.setClass(DrawerActivity.this, SoilTypeFormActivity.class);
                                String E_pileNo = (String) featureSet.getFieldValue("起讫桩号");
                                String E_slopeType = (String) featureSet.getFieldValue("边坡类型");
                                String E_roadName = (String) featureSet.getFieldValue("路段");
                                double E_length = (double) featureSet.getFieldValue("处理长度");
                                String E_Length1 = String.valueOf(E_length);
                                double E_width = (double) featureSet.getFieldValue("处治宽度");
                                String E_width1 = String.valueOf(E_width);
                                String E_protectWay = (String) featureSet.getFieldValue("处理方案");
                                double E_scope1 = (double) featureSet.getFieldValue("评分");
                                String E_scope = String.valueOf(E_scope1);
                                double E_colorNote1 = (double) featureSet.getFieldValue("颜色标识");
                                String E_colorNote = String.valueOf(E_colorNote1);
                                String E_occurPro = (String) featureSet.getFieldValue("发生可能性");
                                String E_resultGrade = (String) featureSet.getFieldValue("后果严重性");
                                _bundle.putString("_riskId", _riskId);
                                _bundle.putString("_pileNo", E_pileNo);
                                _bundle.putString("_slopeType", E_slopeType);
                                _bundle.putString("_roadName", E_roadName);
                                _bundle.putString("_slopeLength", E_Length1);
                                _bundle.putString("_width", E_width1);
                                _bundle.putString("_protectWay", E_protectWay);
                                _bundle.putString("_scope", E_scope);
                                _bundle.putString("_colorNote", E_colorNote);
                                _bundle.putString("_occurPro", E_occurPro);
                                _bundle.putString("_resultGrade", E_resultGrade);
                                _intent.putExtras(_bundle);
                                startActivity(_intent);
                                break;
                            //桥梁
                            case "G":
                                _intent = new Intent();
                                _intent.setClass(DrawerActivity.this, BridgeTypeformMapActivity.class);
                                String G_pileNo = (String) featureSet.getFieldValue("中心桩号");
                                String G_holeNum = (String) featureSet.getFieldValue("孔数孔径");
                                String G_bridgeLength = (String) featureSet.getFieldValue("桥梁全长");
                                String G_bridgeHigh = (String) featureSet.getFieldValue("最大墩高");
                                String G_topStruct = (String) featureSet.getFieldValue("上部结构");
                                String G_bridgeName = (String) featureSet.getFieldValue("桥梁名称");
                                String downStruct = (String) featureSet.getFieldValue("下部节构");
                                double G_scope1 = (double) featureSet.getFieldValue("评分");
                                String G_scope = String.valueOf(G_scope1);
                                double G_colorNote1 = (double) featureSet.getFieldValue("颜色标识");
                                String G_colorNote = String.valueOf(G_colorNote1);
                                String G_occurPro = (String) featureSet.getFieldValue("发生可能性");
                                String G_resultGrade = (String) featureSet.getFieldValue("后果严重性");
                                _bundle.putString("_riskId", _riskId);
                                _bundle.putString("G_pileNo", G_pileNo);
                                _bundle.putString("G_holeNum", G_holeNum);
                                _bundle.putString("G_bridgeLength", G_bridgeLength);
                                _bundle.putString("G_bridgeHigh", G_bridgeHigh);
                                _bundle.putString("G_topStruct", G_topStruct);
                                _bundle.putString("G_bridgeName", G_bridgeName);
                                _bundle.putString("downStruct", downStruct);
                                _bundle.putString("G_scope", G_scope);
                                _bundle.putString("G_colorNote", G_colorNote);
                                _bundle.putString("G_occurPro", G_occurPro);
                                _bundle.putString("G_resultGrade", G_resultGrade);
                                _intent.putExtras(_bundle);
                                startActivity(_intent);
                                break;
                            //隧道
                            case "H":
                                _intent = new Intent();
                                _intent.setClass(DrawerActivity.this, TunnelTypeFromActivity.class);
                                String H_pileNo = (String) featureSet.getFieldValue("起讫桩号");
                                String H_tunnelName = (String) featureSet.getFieldValue("隧道名称");
                                String H_form = (String) featureSet.getFieldValue("形式");
                                String H_length = (String) featureSet.getFieldValue("隧道长度");
                                String H_speed = (String) featureSet.getFieldValue("设计速度");
                                String H_high = (String) featureSet.getFieldValue("净高");
                                String H_width = (String) featureSet.getFieldValue("净宽");
                                double H_scope1 = (double) featureSet.getFieldValue("评分");
                                String H_scope = String.valueOf(H_scope1);
                                double H_colorNote1 = (double) featureSet.getFieldValue("颜色标识");
                                String H_colorNote = String.valueOf(H_colorNote1);
                                String H_occurPro = (String) featureSet.getFieldValue("发生可能性");
                                String H_resultGrade = (String) featureSet.getFieldValue("后果严重性");
                                _bundle.putString("_riskId", _riskId);
                                _bundle.putString("H_pileNo", H_pileNo);
                                _bundle.putString("H_tunnelName", H_tunnelName);
                                _bundle.putString("H_form", H_form);
                                _bundle.putString("H_length", H_length);
                                _bundle.putString("H_speed", H_speed);
                                _bundle.putString("H_high", H_high);
                                _bundle.putString("H_width", H_width);
                                _bundle.putString("H_scope", H_scope);
                                _bundle.putString("H_colorNote", H_colorNote);
                                _bundle.putString("H_occurPro", H_occurPro);
                                _bundle.putString("H_resultGrade", H_resultGrade);
                                _intent.putExtras(_bundle);
                                startActivity(_intent);
                                break;

                            //弃土场
                            case "I":
                                _intent = new Intent();
                                _intent.setClass(DrawerActivity.this, TakingSoilFieldTypeFromActivity.class);
                                String I_pileNo = (String) featureSet.getFieldValue("桩号");
                                String I_placeName = (String) featureSet.getFieldValue("地名");
                                String I_soilQuantity = (String) featureSet.getFieldValue("取土数量");
                                String I_soilType = (String) featureSet.getFieldValue("类型");
                                double I_colorNote1 = (double) featureSet.getFieldValue("颜色标识");
                                String I_colorNote = String.valueOf(I_colorNote1);
                                _bundle.putString("_riskId", _riskId);
                                _bundle.putString("I_pileNo", I_pileNo);
                                _bundle.putString("I_placeName", I_placeName);
                                _bundle.putString("I_soilQuantity", I_soilQuantity);
                                _bundle.putString("I_soilType", I_soilType);
                                _bundle.putString("I_colorNote", I_colorNote);
                                _intent.putExtras(_bundle);
                                startActivity(_intent);
                                break;
                            default:
                                break;
                        }


                        LogUtils.i("********************" + "output the " + nCount + " field value complete!" + "*****************************");
                        nCount++;
                       /* Point2D pt = featureSet.getGeometry().getInnerPoint();
                        LayoutInflater lfCallOut = getLayoutInflater();
                        View calloutLayout = lfCallOut.inflate(R.layout.callout, null);
                        CallOut callout = new CallOut(MainFrame.this);
                        callout.setContentView(calloutLayout);				// 设置显示内容
                        callout.setCustomize(true);							// 设置自定义背景图片
                        callout.setLocation(pt.getX(), pt.getY());			// 设置显示位置
                        m_mapView.addCallout(callout);*/
                        featureSet.moveNext();
                    }
                    LogUtils.i("选中的总数量=", String.valueOf(nCount));
                    LogUtils.i("featureSet 数目=", String.valueOf(featureSet.getFeatureCount()));
                    //                    System.out.println("count is " + nCount);
                    //                    System.out.println("featureSet count is " + featureSet.getFeatureCount());
                }
            }
            //

            @Override
            public void dataServiceFinished(String arg0) {
                // TODO Auto-generated method stub
            }
        });

        //显示服务查询进度条，回调里面销毁
        progress.setMessage("服务查询中...");
        progress.show();
        // 查询
        service.query(parameter, QueryMode.BoundsQuery);
    }

    private void setAllBtnBgNor() {
        m_btnMeaLength.setBackgroundResource(R.mipmap.ic_btn_mea_length_nor);
        m_btnMeaArea.setBackgroundResource(R.mipmap.ic_btn_mea_area_nor);
        m_btnPan.setBackgroundResource(R.mipmap.ic_btn_pan_nor);
        m_btnFull.setBackgroundResource(R.mipmap.ic_btn_open_query_nor);
    }

    private void ShowSingleSelectDialog(final String[] data, final TextView textView, String title) {
    }

    /*private void getmapToPixel(Point2D point) {
        Map map = m_mapView.getMapControl().getMap();
        Point _point = map.mapToPixel(point);
        m_NavigationPanelView.setPoint(_point);
    }*/

}