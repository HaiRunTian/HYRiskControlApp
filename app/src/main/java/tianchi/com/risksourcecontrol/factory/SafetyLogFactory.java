package tianchi.com.risksourcecontrol.factory;


import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.SafetyLogInfo;

//安全日志工厂类
public class SafetyLogFactory implements LogFactory {

    @Override
    public BaseLogInfo logMaker() {
        return new SafetyLogInfo();
    }

}
