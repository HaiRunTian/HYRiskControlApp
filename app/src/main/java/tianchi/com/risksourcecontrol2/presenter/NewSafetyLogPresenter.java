package tianchi.com.risksourcecontrol2.presenter;

import tianchi.com.risksourcecontrol2.bean.log.SafetyLogInfo;
import tianchi.com.risksourcecontrol2.factory.LogFactory;
import tianchi.com.risksourcecontrol2.factory.SafetyLogFactory;
import tianchi.com.risksourcecontrol2.model.INewLogBiz;
import tianchi.com.risksourcecontrol2.model.NewSafetyLogBiz;
import tianchi.com.risksourcecontrol2.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.view.INewLogView;

/**
 * 新建安全日志控制器
 * Created by Kevin on 2017/12/20.
 */

public class NewSafetyLogPresenter implements INewLogPresenter {

    INewLogView m_newSafetyLogView;//与activity绑定的view接口
    INewLogBiz m_newSafetyLogBiz;//新建日志接口

    public NewSafetyLogPresenter(INewLogView newLogView) {
        this.m_newSafetyLogView = newLogView;
        m_newSafetyLogBiz = new NewSafetyLogBiz();//←←←业务逻辑入口
    }

    @Override
    public void submit() {//提交日志
        m_newSafetyLogView.showInSubmiting("提交日志中...");
        SafetyLogInfo _safetyLog = getSafetyLog();
        m_newSafetyLogBiz.submit(_safetyLog, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_newSafetyLogView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_newSafetyLogView.showSubmitFailed(msg);
            }
        });
    }

    @Override
    public void saveToDraft() {

    }

    public void uploadFile(int amount, int i) {//上传图片
        m_newSafetyLogView.showInSubmiting("上传第" + (i + 1) + "张图片，共" + amount + "张");
        m_newSafetyLogBiz.uploadFile(m_newSafetyLogView.getUploadFile(i),
                m_newSafetyLogView.getIdLoginName(), new OnUploadFileListener() {
                    @Override
                    public void uploadSucceed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_newSafetyLogView.uploadFileSucceed(msg + ",第" + (i + 1) + "张图片");
                    }

                    @Override
                    public void uploadFailed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_newSafetyLogView.uploadFileFailed(msg + ",第" + (i + 1) + "张图片");
                    }
                });
    }
    //    public void loadSectionList() {//加载标段数据
    //        m_sendNotice.loadSection(new OnLoadingDataListener() {
    //            @Override
    //            public void loadSuccess(List<String> sectionList) {
    //                m_noticeView.initLoadingDataSucceed(sectionList);
    //            }
    //
    //            @Override
    //            public void loadFailed(String msg) {
    //                m_noticeView.initLoadingDataFailed(msg);
    //            }
    //        });
    //    }

    //获取录入的实体类信息
    private SafetyLogInfo getSafetyLog() {
        LogFactory _logFactory = new SafetyLogFactory();
        SafetyLogInfo _safetyLog = (SafetyLogInfo) _logFactory.logMaker();
        _safetyLog.setLogId(m_newSafetyLogView.getLogID());
        _safetyLog.setSection(m_newSafetyLogView.getSection());
        _safetyLog.setRiskType(m_newSafetyLogView.getRiskType());
        _safetyLog.setStakeNum(m_newSafetyLogView.getStakeNum());
        _safetyLog.setWeather(m_newSafetyLogView.getWeather());
        _safetyLog.setEmergency(m_newSafetyLogView.getEmergency());
        _safetyLog.setRecorder(m_newSafetyLogView.getRecorder());
        _safetyLog.setChargeBuilder(m_newSafetyLogView.getLeader());
        _safetyLog.setBuildDetails(m_newSafetyLogView.getDetails());
        _safetyLog.setPicture(m_newSafetyLogView.getPicture());
        _safetyLog.setSaveTime(m_newSafetyLogView.getSaveTime());
        _safetyLog.setProjectRole(m_newSafetyLogView.getProjectRole());
        _safetyLog.setExpireTime(m_newSafetyLogView.getExpireTime());

        _safetyLog.setTechnoDetails(m_newSafetyLogView.getTDetails());  //建议
        return _safetyLog;
    }
}
