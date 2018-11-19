package tianchi.com.risksourcecontrol2.activitiy.risksourceformap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.log.SafetyLogInfoActivity;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.custom.MyPopupWindowMap;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.model.OnQueryRiskListener;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.QueryLogForMapUtils;


/**
 * Created by HaiRun on 2018-09-29 17:19.
 * 特殊土路基
 */

public class SpecialSoilFromActivity extends BaseActivity implements View.OnClickListener {
    private EditText m_edtSmid;  //smid
    private EditText m_edtServerNum;  //序号
    private EditText m_edtSection;  //  标段
    private EditText m_edtStakeNum; //(起讫)桩号
    private EditText m_edtGrade;  //评分
    private EditText m_edtColorIdenity;  //颜色标示
    private EditText m_edtPosibilityLevel;  //发生可能性等级
    private EditText m_edtSeriousesLevel;  //后果严重性等级
    private EditText m_edtType; //类型
    private EditText m_edtRoad; //路段
    private EditText edtLength; //处理长度
    private EditText edtThickness; //厚度
    private EditText edtWay; //处理方案
    private Bundle m_bundle;
    private String colorName;
    private TextView m_tvSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_soil);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setWindowsSize();
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
        m_bundle = getIntent().getExtras();
        String riskId = m_bundle.getString("_riskId");
        String riskName = riskId.substring(2, 3);
        m_edtServerNum.setText(riskId+"");
        m_edtSection.setText("TJ" + riskId.substring(0, 2)+"");
        m_edtStakeNum.setText(m_bundle.getString("D_pileNo")+"");
        m_edtGrade.setText(m_bundle.getString("D_scope")+"");
        String color = m_bundle.getString("D_colorNote");
        switch (color) {
            case "1":
                colorName = "蓝色";
                break;
            case "2":
                colorName = "黄色";
                break;
            case "3":
                colorName = "橙色";
                break;
            case "4":
                colorName = "红色";
                break;
            default:
                break;
        }
        m_edtColorIdenity.setText(colorName);//颜色标识
//        m_edtColorIdenity.setText(m_bundle.getString("D_colorNote")+"");
        m_edtPosibilityLevel.setText(m_bundle.getString("D_occurPro")+"");
        m_edtSeriousesLevel.setText(m_bundle.getString("D_resultGrade")+"");
        m_edtType.setText(m_bundle.getString("D_type")+"");
        m_edtRoad.setText(m_bundle.getString("D_road")+"");
        edtLength.setText(m_bundle.getString("D_length")+"");
        edtThickness.setText(m_bundle.getString("D_thicknesses")+"");
        edtWay.setText(m_bundle.getString("D_way")+"");

    }

    private void initView() {
        m_edtServerNum = $(R.id.edtSeriesNum);
        m_edtSection = $(R.id.edtSection);
        m_edtStakeNum = $(R.id.edtStakeNum);
        m_edtGrade = $(R.id.edtGrade);
        m_edtColorIdenity = $(R.id.edtColorIdentity);
        m_edtPosibilityLevel = $(R.id.edtPosibilityLevel);
        m_edtSeriousesLevel = $(R.id.edtSeriousnesLevel);
        m_edtType = $(R.id.edtType);
        m_edtRoad = $(R.id.edtroad);
        edtLength = $(R.id.edtLength);
        edtThickness = $(R.id.edtthickness);
        edtWay = $(R.id.edtway);

        m_tvSignIn = $(R.id.tvActionbarRight);
        m_tvSignIn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

                            MyToast.showMyToast(SpecialSoilFromActivity.this, _msg, Toast.LENGTH_SHORT);

                        } else if (statue == 0) {

                            MyToast.showMyToast(SpecialSoilFromActivity.this, "此风险源没有日志", Toast.LENGTH_SHORT);

                        } else if (statue == 1) {
                            String json = GsonUtils.getNodeJsonString(string, "data");

                            if (json.length() != 0) {
                                JSONObject _jsonObject = null;

                                try {
                                    _jsonObject = new JSONObject(json);
//

                                    SafetyLogInfo _safetyLogInfo = null;
                                    try {
                                        _safetyLogInfo = GsonUtils.parseJson2Bean(_jsonObject, SafetyLogInfo.class);


                                        if (_safetyLogInfo != null) {

                                            _intent = new Intent(SpecialSoilFromActivity.this, SafetyLogInfoActivity.class);
                                            _intent.putExtra("logInfo", _safetyLogInfo);
                                            startActivity(_intent);

                                        } else {
                                            MyToast.showMyToast(SpecialSoilFromActivity.this, "没有查询到日志", Toast.LENGTH_SHORT);
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
        }
    }
}
