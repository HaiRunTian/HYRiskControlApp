package tianchi.com.risksourcecontrol2.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import tianchi.com.risksourcecontrol2.base.AppInitialization;
import tianchi.com.risksourcecontrol2.bean.risksource.BridgeRiskData;
import tianchi.com.risksourcecontrol2.bean.risksource.HighLowTypeRiskData;
import tianchi.com.risksourcecontrol2.bean.risksource.SoilTypeRiskData;
import tianchi.com.risksourcecontrol2.bean.risksource.TakingRiskData;
import tianchi.com.risksourcecontrol2.bean.risksource.TunnelRiskData;
import tianchi.com.risksourcecontrol2.config.ServerConfig;
import tianchi.com.risksourcecontrol2.custom.MyToast;
import tianchi.com.risksourcecontrol2.util.GsonUtils;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.util.OkHttpUtils;

/**
 * 主页具体业务逻辑
 * Created by Kevin on 2017/12/19.
 */

public class HomeBiz implements IHomeBiz {

    @Override
    public void signIn() {

    }

    /*
    * 查询风险源列表
    * 具体网络请求，查询界面在此操作
    * @riskType 风险源类型
    * @stakeNum 风险远桩号
    */
    @Override
    public void riskQuery(String riskType, String pileNo, String section, OnRiskQueryListener riskQueryListener) {

        try {
            if (TextUtils.isEmpty(riskType) || TextUtils.isEmpty(pileNo)) {
                MyToast.showMyToast(AppInitialization.getInstance(), "桩号不能为空，请输入桩号再查询", 2);
                riskQueryListener.onQueryFailed();
            } else {

                getRiskAsynByOkHttp3(riskType, pileNo, section, riskQueryListener);

            }
        } catch (Exception e) {
            e.printStackTrace();
            riskQueryListener.onQueryFailed();
//            LogUtils.i("RiskQueryError", "查询失败" + e.getMessage());
        }

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
        String str = _jsonObject.toString();
        OkHttpUtils.getInstance().postAsync(ServerConfig.URL_GET_RISKINFO, str, new OkHttpUtils.QueryDataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                riskQueryListener.onQueryFailed();
            }

            @Override
            public void requestSuccess(String _response) throws Exception {
                try {
//                    LogUtils.i("getRiskAsynByOkHttp3: " + _response);
                    if (!_response.contains("status")) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "服务器解析错误_jsonString:" + _response, 2);
                        riskQueryListener.onQueryFailed();
                        return;
                    }
                    int status = GsonUtils.getIntNoteJsonString(_response, "status");
//                    LogUtils.i("status =" + status);
                    if (status == 0 || _response == null) {
                        MyToast.showMyToast( AppInitialization.getInstance(), "查询失败", 2);
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

//                    LogUtils.i("onResponse: HomeBiz " + list.size() + "____________________" + _response);
                    riskQueryListener.onQuerySucceed(list);

                } catch (Exception e) {
                    MyToast.showMyToast(AppInitialization.getInstance(), "查询失败", 2);
                    e.printStackTrace();
                    riskQueryListener.onQueryFailed();
                }
            }
        });
    }

    /*
    * 打开风险源详情页
    * */
    @Override
    public void openRiskSourceInfo() {
    }

}
