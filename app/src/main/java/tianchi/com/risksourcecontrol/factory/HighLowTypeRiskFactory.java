package tianchi.com.risksourcecontrol.factory;


import tianchi.com.risksourcecontrol.bean.risksource.BaseRiskInfo;
import tianchi.com.risksourcecontrol.bean.risksource.HighLowTypeRisk;

/**
 * Created by hairun.tian on 2018/1/12 0012.
 * 4个属性一致的风险源（高边坡，高填方,一般陡坡,低填浅挖）
 */

public class HighLowTypeRiskFactory implements RiskFactory {
    @Override
    public BaseRiskInfo riskMaker() {
        return new HighLowTypeRisk();
    }
}
