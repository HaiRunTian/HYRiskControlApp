package tianchi.com.risksourcecontrol.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol.model.IQueryLogBiz;
import tianchi.com.risksourcecontrol.model.OnLogQueryListener;
import tianchi.com.risksourcecontrol.model.QuerySafetyLogBiz;
import tianchi.com.risksourcecontrol.view.IQueryLogView;
import tianchi.com.risksourcecontrol.work.UserLoginWork;

/**
 * 查询安全日志控制器
 * Created by Kevin on 2017/12/22.
 */

public class QuerySafetyLogPresenter {
    IQueryLogBiz m_querySafetyLogBiz;
    IQueryLogView m_queryLogView;

    public QuerySafetyLogPresenter(IQueryLogView queryLogView) {
        this.m_queryLogView = queryLogView;
        m_querySafetyLogBiz = new QuerySafetyLogBiz();//←←←业务逻辑入口
    }

    public void query() {
        m_queryLogView.showQuerying("查询中...");
        m_querySafetyLogBiz.queryLog(getQueryCondition(), new OnLogQueryListener() {
            @Override
            public void onQuerySucceed(List<? extends BaseLogInfo> logList) {
//                m_queryLogView.hideQuerying();
                m_queryLogView.showQuerySucceed(logList);
            }

            @Override
            public void onQueryFailed(String msg) {
//                m_queryLogView.hideQuerying();
                m_queryLogView.showQueryFailed(msg);
            }
        });
    }

    public void queryAll() {
        m_queryLogView.showQuerying("查询中...");
        m_querySafetyLogBiz.queryAllLog(getQueryAll(), new OnLogQueryListener() {
            @Override
            public void onQuerySucceed(List<? extends BaseLogInfo> logList) {
//                m_queryLogView.hideQuerying();
                m_queryLogView.showQuerySucceed(logList);
            }

            @Override
            public void onQueryFailed(String msg) {
//                m_queryLogView.hideQuerying();
                m_queryLogView.showQueryFailed(msg);
            }
        });
    }

//    public void loadSection() {
//        NewLogWork.loadSection(new OnLoadingDataListener() {
//            @Override
//            public void loadSuccess(List<String> sectionList) {
//
//            }
//
//            @Override
//            public void loadFailed(String msg) {
//
//            }
//        });
//    }

    public Map<String, Object> getQueryCondition() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("logId", m_queryLogView.getLogId());
        _map.put("section", m_queryLogView.getSection());
        _map.put("riskType", m_queryLogView.getRiskType());
        _map.put("stakeNum", m_queryLogView.getStakeNum());
        _map.put("startDate", m_queryLogView.getStartDate());
        _map.put("endDate", m_queryLogView.getEndDate());
        _map.put("recorders", m_queryLogView.getRecorder());
        _map.put("projectRole", m_queryLogView.getProjectRole());
        return _map;
    }

    public Map<String, Object> getQueryAll() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("recorders", UserLoginWork.resolveRelationshipList());
        _map.put("projectRole", "");
        _map.put("logId", "");
        _map.put("section", "");
        _map.put("riskType", "");
        _map.put("stakeNum", "");
        _map.put("startDate", "");
        _map.put("endDate", "");
        return _map;
    }

}
