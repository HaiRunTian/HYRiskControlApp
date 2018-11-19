package tianchi.com.risksourcecontrol2.presenter;

import tianchi.com.risksourcecontrol2.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.model.PatrolSignInBiz;
import tianchi.com.risksourcecontrol2.view.IPatrolSignInView;

/**
 * Created by Kevin on 2018-05-08.
 */

public class PatrolSignInPresenter {
    IPatrolSignInView m_patrolSignInView;
    PatrolSignInBiz m_patrolSignInBiz;

    public PatrolSignInPresenter(IPatrolSignInView patrolSignInView) {
        m_patrolSignInView = patrolSignInView;
        m_patrolSignInBiz = new PatrolSignInBiz();
    }

    public void submit() {//提交日志
        m_patrolSignInView.showInSubmiting("努力签到中...");
        m_patrolSignInBiz.submit(m_patrolSignInView.getMap(), new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                m_patrolSignInView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                m_patrolSignInView.showSubmitFailed(msg);
            }
        });
    }

    public void uploadFile(int amount, int i) {//上传图片
        m_patrolSignInView.showInSubmiting("上传第" + (i + 1) + "张图片，共" + amount + "张");
        m_patrolSignInBiz.uploadFile(m_patrolSignInView.getUploadFile(i), m_patrolSignInView.getIdLoginName(), new OnUploadFileListener() {
            @Override
            public void uploadSucceed(String msg) {
                m_patrolSignInView.uploadFileSucceed(msg + ",第" + (i + 1) + "张图片");
            }

            @Override
            public void uploadFailed(String msg) {
                m_patrolSignInView.uploadFileFailed(msg + ",第" + (i + 1) + "张图片");
            }
        });
    }
}
