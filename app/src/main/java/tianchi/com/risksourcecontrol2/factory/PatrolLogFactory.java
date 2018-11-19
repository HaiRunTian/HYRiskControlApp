package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.PatrolLogInfo;

//安全巡查日志工厂类
public class PatrolLogFactory implements LogFactory {
    @Override
    public BaseLogInfo logMaker() {
        return new PatrolLogInfo();
    }
}
