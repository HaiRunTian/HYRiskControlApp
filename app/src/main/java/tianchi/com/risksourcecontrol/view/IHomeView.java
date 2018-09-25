package tianchi.com.risksourcecontrol.view;

import com.supermap.data.Workspace;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

import java.util.List;

/**
 * 主界面view接口
 * Created by Kevin on 2017/12/19.
 */

public interface IHomeView {

    void showLoading();//显示地图进度

    void hideLoading();//隐藏地图进度

    void showQuerying();//显示查询进度

    void hideQuerying();//隐藏查询进度

    void showLoadingError();//显示地图错误

    String getRiskType();//获取风险源类型

    String getSection();//获取标段

    String getRiskStakeNum();//获取风险源桩号

    void showRiskQuerySucceed(List<String> list);//显示查询风险源成功的列表

    void showRiskQueryFailed();//显示查询风险源失败

//    void showSignInSucceed();

    void toSignInWindow();//跳转到签到窗口

    MapControl getMapControl();//获取mapcontrol

    Map getMap();//获取map

    Workspace getWorkSpace();//获取工作空间

//    List<String> getStakeNumByRiskType();

    void returnRiskResultList();


}
