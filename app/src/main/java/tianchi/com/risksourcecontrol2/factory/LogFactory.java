package tianchi.com.risksourcecontrol2.factory;


import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;

/** 日志抽象工厂
 *  @return  BaseLogInfo 基类对象
 *  @datetime 2017/12/25  9:40.
 */
public interface LogFactory {
    public BaseLogInfo logMaker();
}

