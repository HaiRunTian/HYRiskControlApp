package tianchi.com.risksourcecontrol2.presenter;

import android.graphics.Bitmap;

import tianchi.com.risksourcecontrol2.model.IModifyUserBiz;
import tianchi.com.risksourcecontrol2.model.ModifyUserBiz;
import tianchi.com.risksourcecontrol2.model.OnDownloadFileListener;
import tianchi.com.risksourcecontrol2.model.OnModifyUserListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.view.IModifyUserView;

/**
 * 修改用户属性控制器
 * Created by Kevin on 2018/1/27.
 */

public class ModifyUserPresenter {
    IModifyUserBiz m_modifyUserBiz;
    IModifyUserView m_modifyUserView;

    public ModifyUserPresenter(IModifyUserView modifyUserView) {
        m_modifyUserView = modifyUserView;
        m_modifyUserBiz = new ModifyUserBiz();
    }

    public void submitModified() {
        if (m_modifyUserView.isNotEmpty()) {
            if (m_modifyUserView.checkValidity()) {
                m_modifyUserView.showProgress("提交资料中...");
                m_modifyUserBiz.modifyUser(m_modifyUserView.getMdfUserInfo(), new OnModifyUserListener() {
                    @Override
                    public void modifySucceed(String msg) {//修改成功
                        m_modifyUserView.hideProgress();
                        m_modifyUserView.showModifiedSucceed(msg);
                    }

                    @Override
                    public void modifyFailed(String msg) {//修改失败
                        m_modifyUserView.hideProgress();
                        m_modifyUserView.showModifiedFailed(msg);
                    }

                    @Override
                    public void serverError(String msg) {//服务器错误
                        m_modifyUserView.showServerError(msg);
                    }
                });
            } else {
                m_modifyUserView.alertMessage("修改密码不符合规则");
            }
        }
    }

    public void submitModifiedUserHead() {
        m_modifyUserView.showProgress("修改头像中...");
        m_modifyUserBiz.modifyUserHead(m_modifyUserView.getMdfUserHeadImgFile(),
                m_modifyUserView.getIdLoginName(), new OnUploadFileListener() {
                    @Override
                    public void uploadSucceed(String msg) {
                        m_modifyUserView.showModifiedUserHeadSucceed(msg);
                    }

                    @Override
                    public void uploadFailed(String msg) {
                        m_modifyUserView.showModifiedUserHeadFailed(msg);
                    }
                });
    }

    public void downloadImgFile() {
        m_modifyUserView.showProgress("初始化头像中...");
        m_modifyUserBiz.downloadUserHead(m_modifyUserView.getDownloadFileUrl(),
                m_modifyUserView.getPicture(), new OnDownloadFileListener() {
                    @Override
                    public void downloadSucceed(Bitmap bitmap) {
                        m_modifyUserView.showDownloadUserHeadSucceed(bitmap);
                    }

                    @Override
                    public void downloadFailed(String msg) {
                        m_modifyUserView.showModifiedUserHeadFailed(msg);
                    }
                });
    }
}
