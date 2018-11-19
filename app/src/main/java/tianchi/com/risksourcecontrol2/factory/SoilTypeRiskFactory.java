package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol2.bean.risksource.SoilTypeRisk;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 * 软土，高液限土
 */

public class SoilTypeRiskFactory implements  RiskFactory{
    @Override
    public BaseRiskInfo riskMaker() {
        return new SoilTypeRisk();
    }
}
