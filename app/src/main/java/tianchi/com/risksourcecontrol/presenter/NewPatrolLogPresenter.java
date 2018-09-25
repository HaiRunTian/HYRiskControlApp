package tianchi.com.risksourcecontrol.presenter;

import tianchi.com.risksourcecontrol.bean.log.PatrolLogInfo;
import tianchi.com.risksourcecontrol.factory.LogFactory;
import tianchi.com.risksourcecontrol.factory.PatrolLogFactory;
import tianchi.com.risksourcecontrol.model.INewLogBiz;
import tianchi.com.risksourcecontrol.model.NewPatrolLogBiz;
import tianchi.com.risksourcecontrol.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol.view.INewLogView;

/**
 * 新建巡查日志控制器
 * Created by Kevin on 2017/12/20.
 */

public class NewPatrolLogPresenter implements INewLogPresenter {

    INewLogView m_newPatrolLogView;//与activity绑定的view接口
    INewLogBiz m_newPatrolLogBiz;//新建日志接口

    public  NewPatrolLogPresenter(INewLogView newLogView) {
        this.m_newPatrolLogView = newLogView;
        m_newPatrolLogBiz = new NewPatrolLogBiz();////←←←业务逻辑入口
    }

    @Override
    public void submit() {
        m_newPatrolLogView.showInSubmiting("提交日志中...");
        PatrolLogInfo _patrolLogInfo = getPatrolLog();
        m_newPatrolLogBiz.submit(_patrolLogInfo, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
//                m_newPatrolLogView.hideInSubmiting();
                m_newPatrolLogView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
//                m_newPatrolLogView.hideInSubmiting();
                m_newPatrolLogView.showSubmitFailed(msg);
            }
        });
    }

    @Override
    public void saveToDraft() {

    }

    public void uploadFile(int amount,int i) {
        m_newPatrolLogView.showInSubmiting("上传第" + (i + 1) + "张图片，共" + amount + "张");
        m_newPatrolLogBiz.uploadFile(m_newPatrolLogView.getUploadFile(i),
                m_newPatrolLogView.getIdLoginName(), new OnUploadFileListener() {
            @Override
            public void uploadSucceed(String msg) {
//                m_newPatrolLogView.hideInSubmiting();
                m_newPatrolLogView.uploadFileSucceed(msg + ",第" + (i + 1) + "张图片");
            }

            @Override
            public void uploadFailed(String msg) {
//                m_newPatrolLogView.hideInSubmiting();
                m_newPatrolLogView.uploadFileFailed(msg + ",第" + (i + 1) + "张图片");
            }
        });
    }

    //获取录入的巡查日志实体类信息
    private PatrolLogInfo getPatrolLog() {
        LogFactory _logFactory = new PatrolLogFactory();
        PatrolLogInfo _patrolLogInfo = (PatrolLogInfo) _logFactory.logMaker();
//        _patrolLogInfo.setLogId(m_newPatrolLogView.getLogID());
        _patrolLogInfo.setSection(m_newPatrolLogView.getSection());
        _patrolLogInfo.setRiskType(m_newPatrolLogView.getRiskType());
        _patrolLogInfo.setStakeNum(m_newPatrolLogView.getStakeNum());
        _patrolLogInfo.setWeather(m_newPatrolLogView.getWeather());
        _patrolLogInfo.setEmergency(m_newPatrolLogView.getEmergency());
        _patrolLogInfo.setRecorder(m_newPatrolLogView.getRecorder());
        _patrolLogInfo.setChargeBuilder(m_newPatrolLogView.getLeader());
        _patrolLogInfo.setPicture(m_newPatrolLogView.getPicture());
        _patrolLogInfo.setBuildDetails(m_newPatrolLogView.getMDetails());
        _patrolLogInfo.setTechnoDetails(m_newPatrolLogView.getTDetails());
        _patrolLogInfo.setSaveTime(m_newPatrolLogView.getSaveTime());
        _patrolLogInfo.setProjectRole(m_newPatrolLogView.getProjectRole());
        _patrolLogInfo.setRiskIndex(m_newPatrolLogView.getRiskIndex()); //风险源编号
        _patrolLogInfo.setExpireTime(m_newPatrolLogView.getExpireTime()); //整改期限
        return _patrolLogInfo;
    }
}
