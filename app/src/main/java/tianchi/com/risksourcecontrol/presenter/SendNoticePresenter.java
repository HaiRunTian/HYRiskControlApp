package tianchi.com.risksourcecontrol.presenter;

import tianchi.com.risksourcecontrol.bean.notice.SendNotice;
import tianchi.com.risksourcecontrol.model.ISendNotice;
import tianchi.com.risksourcecontrol.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol.model.SendNoticeBiz;
import tianchi.com.risksourcecontrol.view.INoticeView;

/**
 * Created by hairun.tian on 2018/3/24 0024.
 */

public class SendNoticePresenter {

    INoticeView m_noticeView;//与activity绑定的view接口
    ISendNotice m_sendNotice;//新建日志接口

    public SendNoticePresenter(INoticeView noticeView) {
        this.m_noticeView = noticeView;
         m_sendNotice = new SendNoticeBiz();//←←←业务逻辑入口
    }


    public void submit() {//提交日志
        m_noticeView.showInSubmiting("通知发送中...");
        SendNotice _safetyLog = getNotice();
        m_sendNotice.submit(_safetyLog, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_noticeView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_noticeView.showSubmitFailed(msg);
            }
        });
    }

    private SendNotice getNotice() {
        SendNotice _notice = new SendNotice();
            _notice.setTitleName(m_noticeView.getNoticeTitle());
            _notice.setDataTime(m_noticeView.getTime());
            _notice.setReceiveMans(m_noticeView.getReceiveMan());
            _notice.setTextContent(m_noticeView.getMsgContent());
        return _notice;

    }

    public void uploadFile(int amount, int i) {//上传图片
        m_noticeView.showInSubmiting("上传第" + (i + 1) + "张图片，共" + amount + "张");
        m_sendNotice.uploadFile(m_noticeView.getUploadFile(i),
                m_noticeView.getIdLoginName(), new OnUploadFileListener() {
                    @Override
                    public void uploadSucceed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_noticeView.uploadFileSucceed(msg + ",第" + (i + 1) + "张图片");
                    }

                    @Override
                    public void uploadFailed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_noticeView.uploadFileFailed(msg + ",第" + (i + 1) + "张图片");
                    }
                });
    }
}
