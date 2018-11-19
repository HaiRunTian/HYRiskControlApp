package tianchi.com.risksourcecontrol2.activitiy.risksource;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.risksource.BridgeRisk;
import tianchi.com.risksourcecontrol2.bean.risksource.BridgeRiskData;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/*
* 详情页
* 桥梁
* */
public class BridgeTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private TextView stakeNum;
    private EditText m_edtSmid;  //smid
    private EditText m_edtServerNum;  //序号
    private EditText m_edtSection;  //  标段
    private EditText m_edtStakeNum; //中心桩号
    private EditText m_edtBridgeName; //桥梁名字
    private EditText m_edtHoleCount;//孔径孔数
    private EditText m_edtBridgeLength; //桥梁全长
    private EditText m_edtMaxPierHeight; //最大墩高
    private EditText m_edtTopStru; //上部结构
    private EditText m_edtBottomStru; //下部结构
    private EditText m_edtRiskIdentity;  //风险源标识
    private EditText m_edtGrade;  //评分
    private EditText m_edtColorIdenity;  //颜色标识
    private EditText m_edtPosibilityLevel;  //发生可能性等级
    private EditText m_edtSeriousesLevel;  //后果严重性等级
    private String m_title;  //标题
    private Bundle m_bundle;
    private String m_pipeNo;
    //    private Button m_btnClose;//关闭界面
    private Button m_btnChange;//核销风险源
    private String colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_type);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        m_bundle = getIntent().getExtras();
        m_title = m_bundle.getString("title");
        m_pipeNo = m_bundle.getString("PipeNo");

        tvTitle.setText(m_title);
        m_edtSmid = (EditText) findViewById(R.id.edtSmID);
        m_edtServerNum = (EditText) findViewById(R.id.edtSeriesNum);
        m_edtSection = (EditText) findViewById(R.id.edtSection);
        m_edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);
        stakeNum = (TextView) findViewById(R.id.tvStakeNum);  //桩号
        stakeNum.setText(getResources().getString(R.string.string_centerStakeNum));

        m_edtBridgeName = (EditText) findViewById(R.id.edtBridgeName);
        m_edtHoleCount = (EditText) findViewById(R.id.edtHoleCount);
        m_edtBridgeLength = (EditText) findViewById(R.id.edtBridgeLength);
        m_edtMaxPierHeight = (EditText) findViewById(R.id.edtMaxPierHeight);
        m_edtTopStru = (EditText) findViewById(R.id.edtTopStru);
        m_edtBottomStru = (EditText) findViewById(R.id.edtBottomStru);
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

    private void getRiskAsynByOkHttp3(String title, String pipeNo) {
        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("type", title);
            _jsonObject.put("pileId", pipeNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = _jsonObject.toString();
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);

            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {

                    //解析json
                    BridgeRisk _bridgeRisk = GsonUtils.jsonToBean(_response, BridgeRisk.class);

                    int statue = _bridgeRisk.getStatus();
                    if (statue != 1) {
                        MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                        return;
                    }
                    //获取全部风险源桩号，存进list中
                    List<BridgeRiskData> _list = _bridgeRisk.getData();
                    if (_list.size() == 0) {
                        MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                    }
                    BridgeRiskData _bridgeRiskData = _list.get(0);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_edtSmid.setText(String.valueOf(_bridgeRiskData.getSmId())); //smid
                            m_edtServerNum.setText(_bridgeRiskData.getRiskId());  //序号
                            m_edtSection.setText(_bridgeRiskData.getSection());  //标段
                            m_edtStakeNum.setText(_bridgeRiskData.getPileNo()); //桩号
                            m_edtBridgeName.setText(_bridgeRiskData.getBridgeName());  // 桥梁名字
                            m_edtHoleCount.setText(_bridgeRiskData.getHoleNum()); // 孔数孔径
                            m_edtBridgeLength.setText(_bridgeRiskData.getBridgeLength()); // 桥梁长度
                            m_edtMaxPierHeight.setText(_bridgeRiskData.getBridgeHigh()); //最大墩高
                            m_edtTopStru.setText(_bridgeRiskData.getTopStruct());  //上部结构
                            m_edtBottomStru.setText(_bridgeRiskData.getDownStruct());  // 下部结构
                            m_edtGrade.setText(String.valueOf(_bridgeRiskData.getScope()));  //评分
                            m_edtRiskIdentity.setText(_bridgeRiskData.getRiskNote()); //风险远标示
                            m_edtPosibilityLevel.setText(_bridgeRiskData.getOccurPro()); //可能性等级
                            String color = String.valueOf(_bridgeRiskData.getColorNote());

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

                            m_edtSeriousesLevel.setText(_bridgeRiskData.getResultGrade()); //后果严重性等级
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
}
