package tianchi.com.risksourcecontrol2.presenter;

import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyDraftInfo;
import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyNotifyInfo;
import tianchi.com.risksourcecontrol2.model.NewNoticeBiz;
import tianchi.com.risksourcecontrol2.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.model.INewNoticeBiz;
import tianchi.com.risksourcecontrol2.view.IRectifyNotifyView;

/**
 * Created by hairun.tian on 2017/12/20.
 */

public class RectifyNotifyInfoPresenter implements INewLogPresenter {
//
//    INewLogView m_newSafetyLogView;//与activity绑定的view接口
//    INewLogBiz m_newSafetyLogBiz;//新建日志接口

    IRectifyNotifyView m_notifyView; //与Activity绑定的view接口
    INewNoticeBiz m_iNewNoticeBiz; //新建通知接口

    public RectifyNotifyInfoPresenter(IRectifyNotifyView m_notifyView) {
        this.m_notifyView = m_notifyView;
        m_iNewNoticeBiz = new NewNoticeBiz();//←←←业务逻辑入口
    }

    @Override
    public void submit() {//提交日志
        m_notifyView.showInSubmiting("通知发送中...");
        RectifyNotifyInfo _rectifyNotifyInfo = getSafetyLog();
        m_iNewNoticeBiz.submit(_rectifyNotifyInfo, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_notifyView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_notifyView.showSubmitFailed(msg);
            }
        });
    }

    @Override
    public void saveToDraft() {

        m_notifyView.showInSubmiting("草稿保存中...");
        RectifyNotifyDraftInfo _info = getDraftInfo();
        m_iNewNoticeBiz.saveToDraft(_info, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                m_notifyView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                m_notifyView.showSubmitFailed(msg);
            }
        });


    }

    private RectifyNotifyDraftInfo getDraftInfo() {
        RectifyNotifyDraftInfo info = new RectifyNotifyDraftInfo();
        info.setInspectUnit(m_notifyView.getCheckUnit());
        info.setBeCheckedUnit(m_notifyView.getBeCheckUnit());
        info.setCheckTime(m_notifyView.getcheckDate());
        info.setInspectContent(m_notifyView.getCheckContent());
        info.setQuestion(m_notifyView.getQuestion());
        info.setRequest(m_notifyView.getRectifyRequest());
        info.setInspectorSign(m_notifyView.getCheckMan());
        info.setRectifyPeriod(m_notifyView.rectifyDate());
        info.setBeCheckedUnitSign(m_notifyView.getBeCheckUnit());
        info.setImages(m_notifyView.getPicture());
        info.setLogState(m_notifyView.getLogState());
        info.setSection(m_notifyView.getSection());
        info.setReceiverMans(m_notifyView.getReceiveMans());
        info.setStatus(m_notifyView.getDraftStatus());
        info.setImageInfos(m_notifyView.getImgInfo());

        return info;
    }

    public void uploadFile(int amount, int i) {//上传图片
        m_notifyView.showInSubmiting("上传第" + (i + 1) + "张图片，共" + amount + "张");
        m_iNewNoticeBiz.uploadFile(m_notifyView.getUploadFile(i),
                m_notifyView.getIdLoginName(), new OnUploadFileListener() {
                    @Override
                    public void uploadSucceed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_notifyView.uploadFileSucceed(msg + ",第" + (i + 1) + "张图片");
                    }

                    @Override
                    public void uploadFailed(String msg) {
                        //                m_noticeView.hideInSubmiting();
                        m_notifyView.uploadFileFailed(msg + ",第" + (i + 1) + "张图片");
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
    private RectifyNotifyInfo getSafetyLog() {

        RectifyNotifyInfo _rectifyNotifyInfo = new RectifyNotifyInfo();
//      _rectifyNotifyInfo.setTitle(m_notifyView.getLogID());  //日志id
        _rectifyNotifyInfo.setInspectUnit(m_notifyView.getCheckUnit()); //检查单位
        _rectifyNotifyInfo.setBeCheckedUnit(m_notifyView.getBeCheckUnit()); //受检单位
        String s = m_notifyView.getcheckDate();
//        LogUtils.i("检查时间", s);
        _rectifyNotifyInfo.setCheckedTime(m_notifyView.getcheckDate()); //检查日期
        _rectifyNotifyInfo.setInspectContent(m_notifyView.getCheckContent()); //检查内容
        _rectifyNotifyInfo.setQuestion(m_notifyView.getQuestion()); //发现问题
        _rectifyNotifyInfo.setRequest(m_notifyView.getRectifyRequest()); //整改措施与方法

        _rectifyNotifyInfo.setInspectorSign(m_notifyView.getCheckMan()); //检查人
//        String s2 = m_notifyView.rectifyDate();
//        LogUtils.i("整改时间", s2);
        _rectifyNotifyInfo.setRectifyPeriod(m_notifyView.rectifyDate()); //整改期限
        _rectifyNotifyInfo.setReceiverMans(m_notifyView.getReceiveMans()); //接收人
        _rectifyNotifyInfo.setLogState(m_notifyView.getLogState()); //日志状态
        _rectifyNotifyInfo.setSection(m_notifyView.getSection()); //标段
        _rectifyNotifyInfo.setImages(m_notifyView.getPicture());  //照片名字
        _rectifyNotifyInfo.setImageInfos(m_notifyView.getImgInfo());//照片备注
//        LogUtils.i("提交数据 = ",m_notifyView.getImgInfo());

        return _rectifyNotifyInfo;
    }
}
