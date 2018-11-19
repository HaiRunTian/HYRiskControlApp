package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol2.bean.risksource.TakingSoilField;

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
