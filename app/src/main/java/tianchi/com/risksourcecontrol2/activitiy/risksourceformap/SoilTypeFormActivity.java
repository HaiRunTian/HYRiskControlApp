package tianchi.com.risksourcecontrol2.activitiy.risksourceformap;

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

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.log.PatrolLogInfoActivity;
import tianchi.com.risksourcecontrol2.activitiy.log.SafetyLogInfoActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.custom.MyPopupWindowMap;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnQueryRiskListener;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.QueryLogForMapUtils;

/*
* 土类型详情页
* 软土，高液限土
* */
public class SoilTypeFormActivity extends BaseActivity implements View.OnClickListener{
    private TextView title;
    private EditText m_edtSmid;  //smid
    private EditText m_edtServerNum;  //序号
    private EditText m_edtSection;  //  标段
    private EditText m_edtStakeNum; //(起讫)桩号
    private EditText m_edtSlopeType;//边坡类型
    private EditText m_edtRoadSection; //路段
    private EditText m_edtHandleLength; //处理长度
    private EditText m_edtHandleWidth; //处理宽度
    private EditText m_edtHandleProgram; //处理方案
    private EditText m_edtRiskIdentity;  //风险源标示
    private EditText m_edtGrade;  //评分
    private EditText m_edtColorIdenity;  //颜色标示
    private EditText m_edtPosibilityLevel;  //发生可能性等级
    private EditText m_edtSeriousesLevel;  //后果严重性等级
    private Bundle m_bundle;
    private String m_pipeNo;
    private String m_title;
    private String colorName;

    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源

    private Button m_btnQuerySafePatLog;//查询最新安全巡查日志
    private Button m_btnQUeryProSafeLog; //查询最新生产安全日志
    private TextView m_tvSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_type_risk2);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setWindowsSize();
        m_bundle = getIntent().getExtras();
        initView();
        initData();
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        String riskId = m_bundle.getString("_riskId");
        String riskName = riskId.substring(2, 3);
        String titleStr = "";
        switch (riskName) {
            case "E":
                titleStr = "软土";
                break;
            case "F":
                titleStr = "高液限土";
                break;

            default:
                break;
        }
        title.setText(titleStr);
        m_edtServerNum.setText(riskId);
        m_edtSection.setText("TJ" + riskId.substring(0, 2)); //标段

        m_edtStakeNum.setText(m_bundle.getString("_pileNo"));//起讫桩号
        m_edtSlopeType.setText(m_bundle.getString("_slopeType"));//边坡类型
        m_edtRoadSection.setText(m_bundle.getString("_roadName"));//路段
        m_edtHandleLength.setText(m_bundle.getString("_slopeLength"));//处理长度
        m_edtHandleWidth.setText(m_bundle.getString("_width"));//处置宽度
        m_edtHandleProgram.setText(m_bundle.getString("_protectWay"));//处理方案
        m_edtGrade.setText(m_bundle.getString("_scope"));//评分

        String color = m_bundle.getString("_colorNote");

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
        m_edtPosibilityLevel.setText(m_bundle.getString("_occurPro"));//发生可能性等级
        m_edtSeriousesLevel.setText(m_bundle.getString("_resultGrade"));//发生严重性等级
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

    private void initView() {

        title = (TextView) findViewById(R.id.tvTitle);
        m_edtSmid = (EditText) findViewById(R.id.edtSmID);
        m_edtServerNum = (EditText) findViewById(R.id.edtSeriesNum);
        m_edtSection = (EditText) findViewById(R.id.edtSection);
        m_edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        m_edtSlopeType = (EditText) findViewById(R.id.edtSlopeType);
        m_edtRoadSection = (EditText) findViewById(R.id.edtRoadSection);
        m_edtHandleLength = (EditText) findViewById(R.id.edtHandleLength);
        m_edtHandleWidth = (EditText) findViewById(R.id.edtHandleWidth);
        m_edtHandleProgram = (EditText) findViewById(R.id.edtHandleProgram);

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

                MyPopupWindowMap _popupWindowMap = new MyPopupWindowMap(this,riskIndex);
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

                            MyToast.showMyToast(SoilTypeFormActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 0) {

                            MyToast.showMyToast(SoilTypeFormActivity.this, _msg, Toast.LENGTH_SHORT);

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

                                            _intent = new Intent(SoilTypeFormActivity.this, SafetyLogInfoActivity.class);
                                            _intent.putExtra("logInfo", _safetyLogInfo);
                                            startActivity(_intent);

                                        } else {
                                            MyToast.showMyToast(SoilTypeFormActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
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

                            MyToast.showMyToast(SoilTypeFormActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 0) {

                            MyToast.showMyToast(SoilTypeFormActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 1) {

                            String json = GsonUtils.getNodeJsonString(string, "data");
                            if (json.length() != 0) {


                                try {
                                    JSONObject _jsonObject = new JSONObject(json);

                                    PatrolLogInfo _patrolLogInfo = GsonUtils.parseJson2Bean(_jsonObject, PatrolLogInfo.class);

                                    if (_patrolLogInfo != null) {

                                        _intent = new Intent(SoilTypeFormActivity.this, PatrolLogInfoActivity.class);
                                        _intent.putExtra("logInfo", _patrolLogInfo);
                                        startActivity(_intent);

                                    } else {
                                        MyToast.showMyToast(SoilTypeFormActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                MyToast.showMyToast(SoilTypeFormActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
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


}
