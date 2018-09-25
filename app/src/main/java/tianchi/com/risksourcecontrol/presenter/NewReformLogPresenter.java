package tianchi.com.risksourcecontrol.presenter;

import tianchi.com.risksourcecontrol.bean.log.ReformLogInfo;
import tianchi.com.risksourcecontrol.factory.LogFactory;
import tianchi.com.risksourcecontrol.factory.ReformLogFactory;
import tianchi.com.risksourcecontrol.model.INewLogBiz;
import tianchi.com.risksourcecontrol.model.NewReformLogBiz;
import tianchi.com.risksourcecontrol.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol.view.INewLogView;

/**
 * 新建整改日志控制器
 * Created by Kevin on 2017/12/20.
 */

public class NewReformLogPresenter implements INewLogPresenter{
    INewLogView m_newReformLogView;//绑定activity的view
    INewLogBiz m_newReformLogBiz;//新建日志接口

    public  NewReformLogPresenter(INewLogView newLogView) {
        this.m_newReformLogView = newLogView;
        m_newReformLogBiz = new NewReformLogBiz();//←←←业务逻辑入口
    }

    @Override
    public void submit() {
        m_newReformLogView.showInSubmiting("提交日志中...");
        ReformLogInfo _reformLogInfo = getReformLog();
        m_newReformLogBiz.submit(_reformLogInfo, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
//                m_newReformLogView.hideInSubmiting();
                m_newReformLogView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
//                m_newReformLogView.hideInSubmiting();
                m_newReformLogView.showSubmitFailed(msg);
            }
        });
    }

    @Override
    public void saveToDraft() {

    }

    public void uploadFile(int position) {//上传图片
        m_newReformLogView.showInSubmiting("提交图片中...");
        m_newReformLogBiz.uploadFile(m_newReformLogView.getUploadFile(position),
                m_newReformLogView.getIdLoginName(), new OnUploadFileListener() {
                    @Override
                    public void uploadSucceed(String msg) {
//                        m_newReformLogView.hideInSubmiting();
                        m_newReformLogView.uploadFileSucceed(msg);
                    }

                    @Override
                    public void uploadFailed(String msg) {
//                        m_newReformLogView.hideInSubmiting();
                        m_newReformLogView.uploadFileFailed(msg);
                    }
                });
    }

    //获取录入的整改日志实体信息
    private ReformLogInfo getReformLog() {
        LogFactory _logFactory = new ReformLogFactory();
        ReformLogInfo _reformLogInfo = (ReformLogInfo) _logFactory.logMaker();
        _reformLogInfo.setLogId(m_newReformLogView.getLogID());
        _reformLogInfo.setRiskId(m_newReformLogView.getRiskID());
        _reformLogInfo.setRiskType(m_newReformLogView.getRiskType());
        _reformLogInfo.setStakeNum(m_newReformLogView.getStakeNum());
        _reformLogInfo.setSaveTime(m_newReformLogView.getSaveTime());
        _reformLogInfo.setReformAccount(m_newReformLogView.getReformAccount());
        _reformLogInfo.setTitle(m_newReformLogView.getTitles());
        _reformLogInfo.setRecorder(m_newReformLogView.getRecorder());
        _reformLogInfo.setDetails(m_newReformLogView.getDetails());
        _reformLogInfo.setSection(m_newReformLogView.getSection());
        _reformLogInfo.setLoginName(m_newReformLogView.getRecorder());
        _reformLogInfo.setChargeBuilder(m_newReformLogView.getLeader());
        _reformLogInfo.setPicture(m_newReformLogView.getPicture());
        return _reformLogInfo;
    }
}
