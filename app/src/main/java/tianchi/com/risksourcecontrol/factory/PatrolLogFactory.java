package tianchi.com.risksourcecontrol.factory;


import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;

//安全巡查日志工厂类
public class PatrolLogFactory implements LogFactory {
    @Override
    public BaseLogInfo logMaker() {
        return new PatrolLogInfo();
    }
}
