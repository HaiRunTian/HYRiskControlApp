package tianchi.com.risksourcecontrol.model;

import java.util.List;

import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;

/**
 * 查询日志的通用接口
 * Created by Kevin on 2018/1/15.
 */

public interface OnLogQueryListener {

    public void onQuerySucceed(List<? extends BaseLogInfo> logList);

    public void onQueryFailed(String msg);
}