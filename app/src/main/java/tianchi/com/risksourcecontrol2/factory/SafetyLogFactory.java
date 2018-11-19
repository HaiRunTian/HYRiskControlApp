package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;

//安全日志工厂类
public class SafetyLogFactory implements LogFactory {

    @Override
    public BaseLogInfo logMaker() {
        return new SafetyLogInfo();
    }

}
