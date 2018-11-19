package tianchi.com.risksourcecontrol2.activitiy.risksource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.adapter.RiskListviewAdapter;
import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.base.BaseActivity;
import tianchi.com.risksourcecontrol2.bean.risksource.HighLowTypRisk;
import tianchi.com.risksourcecontrol2.bean.risksource.HighLowTypeRiskData;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * 桩号列表
 */
public class RiskResultListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lvRiskResult;
    private RiskListviewAdapter m_adapter;
    private List<String> m_list;
    private String riskType;
    private Button m_btnClose;
    private String section;
    private String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_risk_result);
        lvRiskResult = (ListView) findViewById(R.id.lvRiskResult);
        lvRiskResult.setOnItemClickListener(this);
        m_btnClose = (Button) findViewById(R.id.btnBack);
        setWindowsSize();
        initData();
    }

    private void setWindowsSize() {
        //固定窗口大小
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.85);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;//设置黑暗度
        getWindow().setAttributes(p);
    }

    private void initData() {

        Bundle _bundle = getIntent().getExtras();
        m_list = _bundle.getStringArrayList("pipeNo");  //桩号
        riskType = _bundle.getString("riskType"); //类型
        if (riskType.equals("桥梁名称") || riskType.equals("隧道名称")) {
            type=riskType.substring(0,2);
        }else {
            type = riskType;
        }
        section = _bundle.getString("section"); //标段
        m_adapter = new RiskListviewAdapter(m_list);
        lvRiskResult.setAdapter(m_adapter);
        m_adapter.notifyDataSetChanged();
        MyToast.showMyToast(RiskResultListActivity.this, "共查询到" + m_list.size() + "条记录", 2);
        m_btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent _intent;
        Bundle _bundle;
        _bundle = new Bundle();
        String pipeNo = m_list.get(position); // 获取到对应位置的item值


        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("type", type);
            _jsonObject.put("pileId", pipeNo);
            _jsonObject.put("section", section);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        String str = _jsonObject.toString().replace("\\","");
        String str = _jsonObject.toString().replace("\\", "");
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {
                    //解析json
                    HighLowTypRisk _highLowTypRisk = GsonUtils.jsonToBean(_response, HighLowTypRisk.class);
                    int statue = _highLowTypRisk.getStatus();
                    String msg = _highLowTypRisk.getMsg();
                    if (statue != 1) {
                        MyToast.showMyToast( AppInitialization.getInstance(), msg, 2);
                        return;
                    }
                    //
                    List<HighLowTypeRiskData> _list = _highLowTypRisk.getData();
                    if (_list.size() == 0) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
                    }
                    HighLowTypeRiskData _highLowTypeRiskData = _list.get(0);
                    LogUtils.i("onResponse: HighLowTypRisk " + _response);

                    float X = _highLowTypeRiskData.getX();
                    float Y = _highLowTypeRiskData.getY();
//                    Intent _intent1 = new Intent();
//                    _intent1.putExtra("X",X);
//                    _intent1.putExtra("Y",Y);
//
//                    setResult(2,_intent1);
                    SharedPreferences _preferences = getSharedPreferences("riskSource_Location", MODE_PRIVATE);
                    _preferences.edit()
                            .putFloat("X", X)
                            .putFloat("Y", Y).commit();
                    finish();

//                    LogUtils.i("数据库坐标X=======", String.valueOf(X));
//                    LogUtils.i("数据库坐标Y=======", String.valueOf(Y));
//
//                    Point2Ds _point2Ds = new Point2Ds();
//                    _point2Ds.add(new Point2D(X, Y));
//
//
//                    PrjCoordSys _prjCoordSys = new PrjCoordSys();
//                    _prjCoordSys.setType(PrjCoordSysType.PCS_USER_DEFINED);
//                    boolean isOK = CoordSysTranslator.forward(_point2Ds, DrawerActivity.m_PrjCoordSys);
//                    if (isOK) {
//                        LogUtils.i("Point2dx=======", String.valueOf(_point2Ds.getItem(0).getX()));
//                        LogUtils.i("Point2dY=======", String.valueOf(_point2Ds.getItem(0).getY()));
//
//                        Point _point = DrawerActivity.m_mapView.getMapControl().getMap().mapToPixel(_point2Ds.getItem(0));
//                        LogUtils.i("point x=======", String.valueOf(_point.getX()));
//                        LogUtils.i("point Y=======", String.valueOf(_point.getY()));
//
//                    }

                    LogUtils.i("XXXXX=", String.valueOf(X));
                    LogUtils.i("YYYYY=", String.valueOf(Y));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

/*
        _bundle.putString("PipeNo", pipeNo);
        switch (riskType) {
            case "高边坡":
            case "高填深挖":
            case "低挖浅埋":
            case "一般陡坡":

                _intent = new Intent(RiskResultListActivity.this, HighLowTypeRiskActivity.class);
                _intent.putExtra("title", riskType);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                finish();
                break;

            case "软土":
            case "高液限土":
//                _bundle = new Bundle();
                _intent = new Intent(RiskResultListActivity.this, SoilTypeActivity.class);
                _intent.putExtra("title", riskType);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                finish();
                break;
            case "桥梁":
//                _bundle = new Bundle();
                _intent = new Intent(RiskResultListActivity.this, BridgeTypeActivity.class);
                _intent.putExtra("title", riskType);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                finish();
                break;

            case "隧道":
//                _bundle = new Bundle();
                _intent = new Intent(RiskResultListActivity.this, TunnelTypeActivity.class);
                _intent.putExtra("title", riskType);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                finish();
                break;

            case "弃土场":
//                _bundle = new Bundle();
                _intent = new Intent(RiskResultListActivity.this, TakingSoilFieldTypeActivity.class);
                _intent.putExtra("title", riskType);
                _intent.putExtras(_bundle);
                startActivity(_intent);
                finish();
                break;

            default:
                break;
        }

*/

    }
}
