package tianchi.com.risksourcecontrol2.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.model.IQueryLogBiz;
import tianchi.com.risksourcecontrol2.model.OnLogQueryListener;
import tianchi.com.risksourcecontrol2.model.QueryPatrolLogBiz;
import tianchi.com.risksourcecontrol2.view.IQueryLogView;
import tianchi.com.risksourcecontrol2.work.UserLoginWork;

/**
 * 查询巡查日志控制器
 * Created by Kevin on 2017/12/22.
 */

public class QueryPatrolLogPresenter {
    IQueryLogBiz m_queryPatrolLogBiz;
    IQueryLogView m_queryLogView;

    public QueryPatrolLogPresenter(IQueryLogView queryLogView) {
        this.m_queryLogView = queryLogView;
        m_queryPatrolLogBiz = new QueryPatrolLogBiz();//←←←业务逻辑入口
    }

    public void query() {
        m_queryLogView.showQuerying("查询中...");
        m_queryPatrolLogBiz.queryLog(getQueryCondition(), new OnLogQueryListener() {
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
        m_queryPatrolLogBiz.queryAllLog(getQueryAll(), new OnLogQueryListener() {
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

    /**
     *
     * @return 返回全部用户人员名单
     */
    public Map<String, Object> getQueryAll() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("logId", "");
        _map.put("section", "");
        _map.put("riskType", "");
        _map.put("stakeNum", "");
        _map.put("startDate", "");
        _map.put("endDate", "");
        _map.put("projectRole","");
        _map.put("recorders", UserLoginWork.resolveRelationshipList());
        return _map;
    }

}
