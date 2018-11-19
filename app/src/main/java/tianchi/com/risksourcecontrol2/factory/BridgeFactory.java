package tianchi.com.risksourcecontrol2.factory;

import tianchi.com.risksourcecontrol2.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol2.bean.risksource.Bridge;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 * 桥梁
 */

public class BridgeFactory implements RiskFactory {
    @Override
    public BaseRiskInfo riskMaker() {
        return new Bridge();
    }
}
