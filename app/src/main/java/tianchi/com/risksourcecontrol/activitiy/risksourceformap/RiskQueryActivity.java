package tianchi.com.risksourcecontrol.activitiy.risksourceformap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol.R;
import tianchi.com.risksourcecontrol.activitiy.risksource.RiskResultListActivity;
import tianchi.com.risksourcecontrol.base.AppInitialization;
import tianchi.com.risksourcecontrol.base.BaseActivity;
import tianchi.com.risksourcecontrol.bean.risksource.BridgeRiskData;
import tianchi.com.risksourcecontrol.bean.risksource.HighLowTypeRiskData;
import tianchi.com.risksourcecontrol.bean.risksource.SoilTypeRiskData;
import tianchi.com.risksourcecontrol.bean.risksource.TakingRiskData;
import tianchi.com.risksourcecontrol.bean.risksource.TunnelRiskData;
import tianchi.com.risksourcecontrol.config.ServerConfig;
import tianchi.com.risksourcecontrol.custom.MyToast;
import tianchi.com.risksourcecontrol.singleton.UserSingleton;
import tianchi.com.risksourcecontrol.util.GsonUtils;
import tianchi.com.risksourcecontrol.util.LogUtils;
import tianchi.com.risksourcecontrol.util.OkHttpUtils;

public class RiskQueryActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvBack;//返回
    private Spinner spRiskType;//风险源类型
    private EditText edtStakeNum;//桩号或名称
    private Button btnSection;//标段按钮
    private EditText edtSection;//标段显示
    private Button btnQuery;//查询

    private AlertDialog.Builder builder;//标段多选框

    private String[] section_array;//最终数组
    private String[] tempSectionArray;//临时数组
    private String userSectionList;//用户标段字段

    private ProgressDialog m_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_query);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initValues();
    }

    private void initValues() {
        userSectionList = UserSingleton.getUserInfo().getSectionList();
        if (userSectionList.isEmpty()) {
            return;
        }
        tempSectionArray = userSectionList.split("#");  //字符串转成数组
        //多选标段数据
        section_array = new String[tempSectionArray.length];
        for (int i = 0; i < tempSectionArray.length; i++) {
            section_array[i] = ServerConfig.getMapSection().get(tempSectionArray[i]);
        }
    }


    private void initView() {
        edtSection = (EditText) findViewById(R.id.edtSection);
        spRiskType = (Spinner) findViewById(R.id.spRiskType);
        edtStakeNum = (EditText) findViewById(R.id.edtStakeNum);

        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
        btnSection = (Button) findViewById(R.id.btnSection);
        btnSection.setOnClickListener(this);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(this);

        m_dialog = new ProgressDialog(this);
        m_dialog.setMessage("查询中...");
        m_dialog.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.btnSection:
                showDialog(section_array, edtSection, "标段");
                break;
            case R.id.btnQuery:
                startQuery();
                break;
            default:
                break;
        }
    }

    private void startQuery() {
        try {
            boolean isOk = true;
            if (getRiskType().equals("")) {
                MyToast.showMyToast(this, "请选择风险源类型", Toast.LENGTH_SHORT);
                isOk = false;
            }
            if (getStakeNum().equals("")) {
                edtStakeNum.setError("请输入桩号或风险源名称");
                isOk = false;
            }
            if (isOk) {
                showDialog();
                getRiskAsynByOkHttp3(getRiskType(), getStakeNum(), getSection(), new OnRiskQueryListener() {
                    @Override
                    public void onQuerySucceed(List<String> list) {
                        hideDialog();
                        showRiskQuerySucceed(list);
                    }

                    @Override
                    public void onQueryFailed() {
                        hideDialog();
                        showRiskQueryFailed();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("RiskQueryError", "查询失败" + e.getMessage());
        }
    }

    private void showDialog() {
        if (m_dialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_dialog.show();
                }
            });
        }
    }

    private void hideDialog() {
        if (m_dialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_dialog.dismiss();
                }
            });
        }
    }

    private String getRiskType() {
        if (spRiskType.getSelectedItem() != null) {
            String riskType = spRiskType.getSelectedItem().toString();
            if (riskType.equals("隧道名称") || riskType.equals("桥梁名称")) {
                return riskType;
            } else {
                return riskType.substring(2, riskType.length());
            }
        }
        return "";
    }


    private String getStakeNum() {
        return edtStakeNum.getText().toString();
    }

    private String getSection() {
        return edtSection.getText().toString();
    }


    private void showDialog(final String[] data, final EditText textView, String title) {
        textView.setText("");
        builder = new AlertDialog.Builder(RiskQueryActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        final boolean[] selectItems = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            selectItems[i] = false;
        }
        /**
         * 第一个参数指定我们要显示的一组下拉多选框的数据集合
         * 第二个参数代表哪几个选项被选择，如果是null，则表示一个都不选择，如果希望指定哪一个多选选项框被选择，
         * 需要传递一个boolean[]数组进去，其长度要和第一个参数的长度相同，例如 {true, false, false, true};
         * 第三个参数给每一个多选项绑定一个监听器
         */
        builder.setMultiChoiceItems(data, selectItems, new DialogInterface.OnMultiChoiceClickListener() {
            StringBuffer sb = new StringBuffer(100);


            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    //选择的选项保存到sb中
                    sb.append(data[which] + "/");
                }
                String s = sb.toString();
                String data = s.substring(0, s.length() - 1);
                if (data.contains("全部标段")) {
                    textView.setText("全部标段");
                } else {
                    textView.setText(data);
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText("");
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getRiskAsynByOkHttp3(String riskType, String pileNo, String section, final OnRiskQueryListener riskQueryListener) {

        JSONObject _jsonObject = new JSONObject();
        try {
            _jsonObject.put("type", riskType);
            _jsonObject.put("pileId", pileNo);
            _jsonObject.put("section", section);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = _jsonObject.toString().replace("\\", "");
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                riskQueryListener.onQueryFailed();
            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {
                    LogUtils.i("getRiskAsynByOkHttp3: " + _response);
                    if (!_response.contains("status")) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "服务器解析错误_jsonString:" + _response, 2);
                        hideDialog();
                        //                        riskQueryListener.onQueryFailed();
                        return;
                    }
                    int status = GsonUtils.getIntNoteJsonString(_response, "status");
                    LogUtils.i("status =" + status);
                    if (status == 0 || _response == null) {
                        //                        MyToast.showMyToast(MyApplication.getInstance(), "查询失败", 2);
                        riskQueryListener.onQueryFailed();
                        return;
                    }


                    //根据不同的风险远类型，获取全部风险源桩号，存进list中
                    List<String> list = new ArrayList<String>();
                    switch (riskType) {
                        case "高边坡":
                        case "高填深挖":
                        case "低挖浅埋":
                        case "一般陡坡":
                            list.clear();
                            List<HighLowTypeRiskData> _list = GsonUtils.jsonToArrayBeans(_response, "data", HighLowTypeRiskData.class);
                            for (int i = 0; i < _list.size(); i++) {
                                list.add(_list.get(i).getPileNo());
                            }
                            break;

                        case "软土":
                        case "高液限土":

                            list.clear();
                            List<SoilTypeRiskData> soilList = GsonUtils.jsonToArrayBeans(_response, "data", SoilTypeRiskData.class);
                            for (int i = 0; i < soilList.size(); i++) {
                                list.add(soilList.get(i).getPileNo());
                            }
                            break;

                        case "桥梁":
                        case "桥梁名称":

                            list.clear();
                            List<BridgeRiskData> _bridgeRiskDatas = GsonUtils.jsonToArrayBeans(_response, "data", BridgeRiskData.class);

                            for (int i = 0; i < _bridgeRiskDatas.size(); i++) {
                                list.add(_bridgeRiskDatas.get(i).getPileNo());
                            }
                            break;

                        case "隧道":
                        case "隧道名称":

                            list.clear();
                            List<TunnelRiskData> _datas = GsonUtils.jsonToArrayBeans(_response, "data", TunnelRiskData.class);
                            for (int i = 0; i < _datas.size(); i++) {
                                list.add(_datas.get(i).getPileNo());
                            }
                            break;

                        case "弃土场":

                            list.clear();
                            List<TakingRiskData> _takingRiskDatas = GsonUtils.jsonToArrayBeans(_response, "data", TakingRiskData.class);
                            for (int i = 0; i < _takingRiskDatas.size(); i++) {
                                list.add(_takingRiskDatas.get(i).getPileNo());
                            }
                            break;

                        default:
                            break;
                    }

                    LogUtils.i("onResponse: HomeBiz " + list.size() + "____________________" + _response);
                    riskQueryListener.onQuerySucceed(list);

                } catch (Exception e) {
                    MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                    e.printStackTrace();
                    riskQueryListener.onQueryFailed();
                }
            }
        });
    }

    public void showRiskQuerySucceed(List<String> list) {//展示风险源查询成功
        Intent _intent = new Intent(RiskQueryActivity.this, RiskResultListActivity.class);
        Bundle _bundle = new Bundle();
        _bundle.putStringArrayList("pipeNo", (ArrayList<String>) list);
        _bundle.putString("riskType", getRiskType());
        _bundle.putString("section", getSection());
        _intent.putExtras(_bundle);
        startActivityForResult(_intent, 1);
        finish();
    }

    public void showRiskQueryFailed() {
        MyToast.showMyToast(this, "未查询到相关风险源信息", Toast.LENGTH_SHORT);
    }

    public interface OnRiskQueryListener {

        public void onQuerySucceed(List<String> list);

        public void onQueryFailed();
    }
}
