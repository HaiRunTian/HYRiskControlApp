package tianchi.com.risksourcecontrol.activitiy.risksourceformap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.log.PatrolLogInfoActivity;
import tianchi.com.risksourcecontrol.activitiy.log.SafetyLogInfoActivity;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol.custom.MyPopupWindowMap;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.model.OnQueryRiskListener;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.QueryLogForMapUtils;


/*
* 详情页
* 隧道
*/
public class TunnelTypeFromActivity extends BaseActivity implements View.OnClickListener {

    private EditText m_edtSmid;  //smid
    private EditText m_edtServerNum;  //序号
    private EditText m_edtSection;  //  标段
    private EditText m_edtStakeNum; //(起讫)桩号
    private EditText m_edtTunnelName;//隧道名
    private EditText m_edtForm; //形式
    private EditText m_edtTunnelLength; //隧道长度
    private EditText m_edtDesignSpeed; //设计速度
    private EditText m_edtClearHeight; //净高
    private EditText m_edtClearWidth; //净宽
    private EditText m_edtRiskIdentity;  //风险源标识
    private EditText m_edtGrade;  //评分
    private EditText m_edtColorIdenity;  //颜色标示
    private EditText m_edtPosibilityLevel;  //发生可能性等级
    private EditText m_edtSeriousesLevel;  //后果严重性等级
    private Bundle m_bundle;
    private String m_pipeNo;  // 桩号
    private String m_type;  //类型
    private String colorName;
    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源
    private Button m_btnQuerySafePatLog;//查询最新安全巡查日志
    private Button m_btnQUeryProSafeLog; //查询最新生产安全日志
    private TextView m_tvSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunnel_type2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setWindowsSize();
        m_bundle = getIntent().getExtras();
        initView();
        initDatas();

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDatas() {
        String riskId = m_bundle.getString("_riskId");
        String riskName = riskId.substring(2, 3);

        m_edtServerNum.setText(riskId);
        m_edtSection.setText("TJ" + riskId.substring(0, 2)); //标段

        m_edtStakeNum.setText(m_bundle.getString("H_pileNo"));//起讫桩号
        m_edtTunnelName.setText(m_bundle.getString("H_tunnelName"));//隧道名称
        m_edtForm.setText(m_bundle.getString("H_form"));//形式
        m_edtTunnelLength.setText(m_bundle.getString("H_length"));//隧道长度
        m_edtDesignSpeed.setText(m_bundle.getString("H_speed"));//设计速度
        m_edtClearHeight.setText(m_bundle.getString("H_high"));//净高
        m_edtClearWidth.setText(m_bundle.getString("H_width"));//净宽


        m_edtGrade.setText(m_bundle.getString("H_scope"));//评分

        String color = m_bundle.getString("H_colorNote");

        switch (color) {
            case "1.0":
                colorName = "蓝色";
                break;

            case "2.0":
                colorName = "黄色";
                break;
            case "3.0":
                colorName = "橙色";
                break;
            case "4.0":
                colorName = "红色";
                break;
            default:
                break;
        }
        m_edtColorIdenity.setText(colorName);//颜色标识

        m_edtPosibilityLevel.setText(m_bundle.getString("H_occurPro"));//发生可能性等级
        m_edtSeriousesLevel.setText(m_bundle.getString("H_resultGrade"));//发生严重性等级


    }

    private void initView() {


        m_edtSmid = (EditText) findViewById(R.id.edtSmID);
        m_edtServerNum = (EditText) findViewById(R.id.edtSeriesNum);
        m_edtSection = (EditText) findViewById(R.id.edtSection);
        m_edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);

        m_edtTunnelName = (EditText) findViewById(R.id.edtTunnelName);
        m_edtForm = (EditText) findViewById(R.id.edtForm);
        m_edtTunnelLength = (EditText) findViewById(R.id.edtTunnelLength);
        m_edtDesignSpeed = (EditText) findViewById(R.id.edtDesignSpeed);
        m_edtClearHeight = (EditText) findViewById(R.id.edtClearHeight);
        m_edtClearWidth = (EditText) findViewById(R.id.edtClearWidth);

        m_edtRiskIdentity = (EditText) findViewById(R.id.edtRiskIdentity);
        m_edtGrade = (EditText) findViewById(R.id.edtGrade);
        m_edtColorIdenity = (EditText) findViewById(R.id.edtColorIdentity);
        m_edtPosibilityLevel = (EditText) findViewById(R.id.edtPosibilityLevel);
        m_edtSeriousesLevel = (EditText) findViewById(R.id.edtSeriousnesLevel);

        m_btnChange = (Button) findViewById(R.id.btnchange);
        m_btnClose = (Button) findViewById(R.id.btnclose);

        m_btnQuerySafePatLog = $(R.id.btn_query_safe_pat);
        m_btnQUeryProSafeLog = $(R.id.btn_query_pro_safe);

        m_btnQUeryProSafeLog.setOnClickListener(this);
        m_btnQuerySafePatLog.setOnClickListener(this);

        m_tvSignIn = (TextView) findViewById(R.id.tvActionbarRight);
        m_tvSignIn.setOnClickListener(this);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String riskIndex = m_edtServerNum.getText().toString();

        switch (v.getId()) {

            case R.id.tvActionbarRight:

                MyPopupWindowMap _popupWindowMap = new MyPopupWindowMap(this, riskIndex);
                _popupWindowMap.showPopupWindow(findViewById(R.id.tvActionbarRight));
                break;


            //生产安全
            case R.id.btn_query_pro_safe:

                QueryLogForMapUtils.getSafetyLog(riskIndex, new OnQueryRiskListener() {
                    @Override
                    public void onQuerySucceed(String string) {
                        Intent _intent;
                        String _msg = GsonUtils.getNodeJsonString(string, "msg");
                        int statue = GsonUtils.getIntNoteJsonString(string, "status");

                        if (statue == -2) {

                            MyToast.showMyToast(TunnelTypeFromActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 0) {

                            MyToast.showMyToast(TunnelTypeFromActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 1) {
                            String json = GsonUtils.getNodeJsonString(string, "data");

                            if (json.length() != 0) {
                                JSONObject _jsonObject = null;
                                try {
                                    _jsonObject = new JSONObject(json);

                                    SafetyLogInfo _safetyLogInfo = null;
                                    try {
                                        _safetyLogInfo = GsonUtils.parseJson2Bean(_jsonObject, SafetyLogInfo.class);


                                        if (_safetyLogInfo != null) {

                                            _intent = new Intent(TunnelTypeFromActivity.this, SafetyLogInfoActivity.class);
                                            _intent.putExtra("logInfo", _safetyLogInfo);
                                            startActivity(_intent);

                                        } else {
                                            MyToast.showMyToast(TunnelTypeFromActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                    @Override
                    public void onQueryFailed() {

                    }
                });


                break;

            //安全巡查
            case R.id.btn_query_safe_pat:

                QueryLogForMapUtils.getPatrlLog(riskIndex, new OnQueryRiskListener() {
                    @Override
                    public void onQuerySucceed(String string) {

                        Intent _intent;
                        String _msg = GsonUtils.getNodeJsonString(string, "msg");
                        int statue = GsonUtils.getIntNoteJsonString(string, "status");

                        if (statue == -2) {

                            MyToast.showMyToast(TunnelTypeFromActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 0) {

                            MyToast.showMyToast(TunnelTypeFromActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 1) {

                            String json = GsonUtils.getNodeJsonString(string, "data");
                            if (json.length() != 0) {


                                try {
                                    JSONObject _jsonObject = new JSONObject(json);

                                    PatrolLogInfo _patrolLogInfo = GsonUtils.parseJson2Bean(_jsonObject, PatrolLogInfo.class);

                                    if (_patrolLogInfo != null) {

                                        _intent = new Intent(TunnelTypeFromActivity.this, PatrolLogInfoActivity.class);
                                        _intent.putExtra("logInfo", _patrolLogInfo);
                                        startActivity(_intent);

                                    } else {
                                        MyToast.showMyToast(TunnelTypeFromActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                MyToast.showMyToast(TunnelTypeFromActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onQueryFailed() {

                    }
                });


                break;

            default:
                break;
        }
    }

    private void setWindowsSize() {
        //固定窗口大小
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.7);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.85);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;
        //设置黑暗度
        getWindow().setAttributes(p);
    }

}
