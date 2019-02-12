package tianchi.com.risksourcecontrol2.activitiy.risksource;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.risksource.TunnelRisk;
import tianchi.com.risksourcecontrol2.bean.risksource.TunnelRiskData;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/*
* 详情页
* 隧道
*/
public class TunnelTypeActivity extends BaseActivity implements View.OnClickListener {

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
//    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tunnel_type);
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
//        m_btnClose = (Button) findViewById(R.id.btnclose);
//        m_btnClose.setOnClickListener(this);
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
                    TunnelRisk _tunnelRisk = GsonUtils.jsonToBean(_response, TunnelRisk.class);

                    int statue = _tunnelRisk.getStatus();
                    if (statue != 1) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                        return;
                    }
                    //获取全部风险源桩号，存进list中
                    List<TunnelRiskData> _list = _tunnelRisk.getData();
                    if (_list.size() == 0) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                    }
                    TunnelRiskData _data = _list.get(0);
//                    LogUtils.i("onResponse: TunnelRiskData " + _response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_edtSmid.setText(String.valueOf(_data.getSmId())); //smid
                            m_edtServerNum.setText(_data.getRiskId());  //序号
                            m_edtSection.setText(_data.getSection());  //标段
                            m_edtStakeNum.setText(_data.getPileNo()); //桩号
                            m_edtTunnelName.setText(_data.getTunnelName());  //隧道名
                            m_edtForm.setText(String.valueOf(_data.getForm())); //形式
                            m_edtTunnelLength.setText(_data.getLength()); //隧道长度
                            m_edtDesignSpeed.setText(_data.getSpeed()); //设计速度
                            m_edtClearHeight.setText(_data.getHigh());  //高度
                            m_edtClearWidth.setText(_data.getWidth());  //宽度
                            m_edtRiskIdentity.setText(_data.getRiskNote()); //风险远标示
                            m_edtGrade.setText(String.valueOf(_data.getScope()));  //评分
                            m_edtPosibilityLevel.setText(_data.getOccurPro()); //可能性等级


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
                            m_edtSeriousesLevel.setText(_data.getResultGrade()); //后果严重性等级
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
