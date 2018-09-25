package tianchi.com.risksourcecontrol.factory;


import tianchi.com.risksourcecontrol.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol.bean.risksource.Tunnel;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 * 隧道
 */

public class TunnelFactory implements RiskFactory {
    @Override
    public BaseRiskInfo riskMaker() {
        return new Tunnel();
    }
}
