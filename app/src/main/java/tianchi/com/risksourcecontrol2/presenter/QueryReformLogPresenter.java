package tianchi.com.risksourcecontrol2.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tianchi.com.risksourcecontrol2.bean.log.BaseLogInfo;
import tianchi.com.risksourcecontrol2.model.IQueryLogBiz;
import tianchi.com.risksourcecontrol2.model.OnLogQueryListener;
import tianchi.com.risksourcecontrol2.model.QueryReformLogBiz;
import tianchi.com.risksourcecontrol2.view.IQueryLogView;

/**
 * 查询整改日志控制器
 * Created by Kevin on 2017/12/22.
 */

public class QueryReformLogPresenter {
    IQueryLogBiz m_queryReformLogBiz;
    IQueryLogView m_queryLogView;

    public QueryReformLogPresenter(IQueryLogView queryLogView) {
        this.m_queryLogView = queryLogView;
        m_queryReformLogBiz = new QueryReformLogBiz();//←←←业务逻辑入口
    }

    public void query() {
        m_queryLogView.showQuerying("查询中...");
        m_queryReformLogBiz.queryLog(getQueryCondition(), new OnLogQueryListener() {
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
        m_queryReformLogBiz.queryAllLog(getQueryAll(), new OnLogQueryListener() {
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
        _map.put("riskId", m_queryLogView.getRiskId());
        _map.put("section", m_queryLogView.getSection());
        _map.put("riskType", m_queryLogView.getRiskType());
        _map.put("stakeNum", m_queryLogView.getStakeNum());
        _map.put("startDate", m_queryLogView.getStartDate());
        _map.put("endDate", m_queryLogView.getEndDate());
        _map.put("recorder", m_queryLogView.getRecorder());
        return _map;
    }

    public Map<String, Object> getQueryAll() {
        Map<String, Object> _map = new HashMap<String, Object>();
        _map.put("recorder", m_queryLogView.getRecorder());
        _map.put("logId", "");
        _map.put("riskId", "");
        _map.put("section", "");
        _map.put("riskType", "");
        _map.put("stakeNum", "");
        _map.put("startDate", "");
        _map.put("endDate", "");
        return _map;
    }

}
