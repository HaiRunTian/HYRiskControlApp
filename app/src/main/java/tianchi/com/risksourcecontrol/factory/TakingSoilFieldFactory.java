package tianchi.com.risksourcecontrol.factory;


import tianchi.com.risksourcecontrol.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol.bean.risksource.TakingSoilField;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 * 取弃土场
 */

public class TakingSoilFieldFactory implements RiskFactory {
    @Override
    public BaseRiskInfo riskMaker() {
        return new TakingSoilField();
    }
}
