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
import tianchi.com.risksourcecontrol.bean.risksource.TakingRisk;
import tianchi.com.risksourcecontrol.bean.risksource.TakingRiskData;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;

/**
 * 弃土场
 */
public class TakingSoilFieldTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView stakeNum;

    private EditText m_edtSmid;  //smid
    private EditText m_edtServerNum;  //序号
    private EditText m_edtSection;  //  标段
    private EditText m_edtStakeNum; //(起讫)桩号

    private EditText m_edtRiskIdentity;  //风险源标识
    //    private EditText m_edtGrade;  //评分
    private EditText m_edtColorIdenity;  //颜色标识

    private EditText m_edtPlaceName;  //地名
    private EditText m_edtType;  //类型
    private EditText m_edtSoilCount;//取土数

    private Bundle m_bundle;
    private String m_pipeNo;  // 桩号
    private String m_type;


    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源
    private String colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_soil_field_type);

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
        m_type = m_bundle.getString("title");
        m_pipeNo = m_bundle.getString("PipeNo");

        stakeNum = (TextView) findViewById(R.id.tvStakeNum);
        stakeNum.setText(getResources().getString(R.string.string_stakeNum));

        m_edtSmid = (EditText) findViewById(R.id.edtSmID);
        m_edtServerNum = (EditText) findViewById(R.id.edtSeriesNum);
        m_edtSection = (EditText) findViewById(R.id.edtSection);
        m_edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);

        m_edtRiskIdentity = (EditText) findViewById(R.id.edtRiskIdentity);
//        m_edtGrade = (EditText) findViewById(R.id.edtGrade);
        m_edtColorIdenity = (EditText) findViewById(R.id.edtColorIdentity);

        m_edtPlaceName = (EditText) findViewById(R.id.edtPlaceName);
        m_edtType = (EditText) findViewById(R.id.edtType);
        m_edtSoilCount = (EditText) findViewById(R.id.edtSoilCount);

        m_btnChange = (Button) findViewById(R.id.btnchange);
        m_btnClose = (Button) findViewById(R.id.btnclose);

        m_btnClose.setOnClickListener(this);
        m_btnChange.setOnClickListener(this);

        getRiskAsynByOkHttp3(m_type, m_pipeNo);

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


            case R.id.btnclose:
                finish();
                break;
            default:
                break;
        }
    }

    private void getRiskAsynByOkHttp3(String m_type, String pipeNo) {
        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("type", m_type);
            _jsonObject.put("pileId", pipeNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = _jsonObject.toString();
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {

                    //解析json
                    TakingRisk _takingRisk = GsonUtils.jsonToBean(_response, TakingRisk.class);

                    int statue = _takingRisk.getStatus();
                    if (statue != 1) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                        return;
                    }
                    //获取全部风险源桩号，存进list中
                    List<TakingRiskData> _list = _takingRisk.getData();
                    if (_list.size() == 0) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                    }
                    TakingRiskData _data = _list.get(0);
                    LogUtils.i("onResponse: TunnelRiskData " + _response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_edtSmid.setText(String.valueOf(_data.getSmId())); //smid
                            m_edtServerNum.setText(_data.getRiskId());  //序号
                            m_edtSection.setText(_data.getSection());  //标段
                            m_edtStakeNum.setText(_data.getPileNo()); //桩号
                            m_edtRiskIdentity.setText(_data.getRiskNote()); //风险远标示



                            String color = String.valueOf(_data.getColorNote());

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
//                            m_edtColorIdenity.setText(String.valueOf(_data.getColorNote())); //颜色标示
                            m_edtPlaceName.setText(_data.getPlace()); //地名
                            m_edtType.setText(_data.getSoilType()); //类型
                            m_edtSoilCount.setText(_data.getSoilQuantity()); //取土数
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
