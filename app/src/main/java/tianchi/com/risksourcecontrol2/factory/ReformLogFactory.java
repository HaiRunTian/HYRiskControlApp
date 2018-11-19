package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.bean.log.ReformLogInfo;

//风险源整改日志工厂类
public class ReformLogFactory implements LogFactory {

    @Override
    public BaseLogInfo logMaker() {
        return new ReformLogInfo();
    }
}
