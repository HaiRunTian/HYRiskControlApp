package tianchi.com.risksourcecontrol.activitiy.risksource;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


import okhttp3.Request;

import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.base.AppInitialization;
import tianchi.com.risksourcecontrol.base.BaseActivity;


import tianchi.com.risksourcecontrol.bean.risksource.SoilTypRisk;
import tianchi.com.risksourcecontrol.bean.risksource.SoilTypeRiskData;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.util.GsonUtils;

import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;

/*
* 土类型详情页
* 软土，高液限土
* */
public class SoilTypeActivity extends BaseActivity implements View.OnClickListener {
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

//    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源


    private String colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_type_risk);
        initView();
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        m_bundle = getIntent().getExtras();
        m_pipeNo = m_bundle.getString("PipeNo");
        m_title = m_bundle.getString("title");
//        titleStr = getIntent().getStringExtra("title");
        title = (TextView) findViewById(R.id.tvTitle);
        title.setText(m_title);
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
//        m_btnClose = (Button) findViewById(R.id.btnclose);

//        m_btnClose.setOnClickListener(this);
        m_btnChange.setOnClickListener(this);


        getRiskAsynByOkHttp3(m_title, m_pipeNo);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnchange:
                finish();
                break;
//            case R.id.btnclose:
//                finish();
//                break;
            default:
                break;
        }
    }

    private void getRiskAsynByOkHttp3(String title, String pipeNo) {

        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("type", title);
            _jsonObject.put("pileId", pipeNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
        String str = _jsonObject.toString();

        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {

                    //解析json
                    SoilTypRisk _soilTypRisk = GsonUtils.jsonToBean(_response, SoilTypRisk.class);

                    int statue = _soilTypRisk.getStatus();
                    if (statue != 1) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                        return;
                    }
                    //获取全部风险源桩号，存进list中
                    List<SoilTypeRiskData> _list = _soilTypRisk.getData();
                    if (_list.size() == 0) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                    }
                    SoilTypeRiskData _soilTypeRiskData = _list.get(0);
                    LogUtils.i("onResponse: HighLowTypRisk " + _response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_edtSmid.setText(String.valueOf(_soilTypeRiskData.getSmId())); //smid
                            m_edtServerNum.setText(_soilTypeRiskData.getRiskId());  //序号
                            m_edtSection.setText(_soilTypeRiskData.getSection());  //标段
                            m_edtStakeNum.setText(_soilTypeRiskData.getPileNo()); //桩号
                            m_edtSlopeType.setText(_soilTypeRiskData.getSoilType());  //边坡类型
                            m_edtRoadSection.setText(_soilTypeRiskData.getRoad()); //路段
                            m_edtHandleLength.setText(String.valueOf(_soilTypeRiskData.getLength())); //处理长度
                            m_edtHandleWidth.setText(String.valueOf(_soilTypeRiskData.getWidth())); //处理宽度
                            m_edtHandleProgram.setText(_soilTypeRiskData.getProcessScheme());  //处理方案
                            m_edtRiskIdentity.setText(_soilTypeRiskData.getRiskNote()); //风险远标示
                            m_edtGrade.setText(String.valueOf(_soilTypeRiskData.getScope()));  //评分
                            m_edtPosibilityLevel.setText(_soilTypeRiskData.getOccurPro()); //可能性等级


                            String color = String.valueOf(_soilTypeRiskData.getColorNote());

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
                            m_edtColorIdenity.setText(colorName); //颜色标示
//                            m_edtColorIdenity.setText(String.valueOf(_soilTypeRiskData.getColorNote())); //颜色标示
                            m_edtSeriousesLevel.setText(_soilTypeRiskData.getResultGrade()); //后果严重性等级
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
