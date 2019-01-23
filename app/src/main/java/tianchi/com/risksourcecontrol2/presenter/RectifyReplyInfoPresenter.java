package tianchi.com.risksourcecontrol2.presenter;

import tianchi.com.risksourcecontrol2.bean.newnotice.RectifyReplyInfo;
import tianchi.com.risksourcecontrol2.model.INewReplyNoticeBiz;
import tianchi.com.risksourcecontrol2.model.NewReplyNoticeBiz;
import tianchi.com.risksourcecontrol2.model.OnSubmitLogListener;
import tianchi.com.risksourcecontrol2.model.OnUploadFileListener;
import tianchi.com.risksourcecontrol2.singleton.UserSingleton;
import tianchi.com.risksourcecontrol2.util.LogUtils;
import tianchi.com.risksourcecontrol2.view.IRectifyNotifyView;

/**
 * 通知回复控制器
 * Created by  on 2017/12/20.
 */

public class RectifyReplyInfoPresenter implements INewLogPresenter {
//
//    INewLogView m_newSafetyLogView;//与activity绑定的view接口
//    INewLogBiz m_newSafetyLogBiz;//新建日志接口

    IRectifyNotifyView m_notifyView; //与Activity绑定的view接口
    INewReplyNoticeBiz m_iNewNoticeBiz; //新建通知接口

    public RectifyReplyInfoPresenter(IRectifyNotifyView m_notifyView) {
        this.m_notifyView = m_notifyView;
        m_iNewNoticeBiz = new NewReplyNoticeBiz();//←←←业务逻辑入口
    }

    @Override
    public void submit() {//提交日志
        m_notifyView.showInSubmiting("通知发送中...");
        RectifyReplyInfo _rectifyReplyInfo = getSafetyLog();
        m_iNewNoticeBiz.submit(_rectifyReplyInfo, new OnSubmitLogListener() {
            @Override
            public void onSubmitSucceed(String msg) {
                //                m_noticeView.hideInSubmiting();
                m_notifyView.showSubmitSucceed(msg);
            }

            @Override
            public void onSubmitFailed(String msg) {
                //                m_noticeView.hideInSubmiting();
//                LogUtils.i("发送失败",msg);
                m_notifyView.showSubmitFailed(msg);
            }
        });
    }

    @Override
    public void saveToDraft() { //保存到草稿

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
    private RectifyReplyInfo getSafetyLog() {

        try {
            RectifyReplyInfo _rectifyReplyInfo = new RectifyReplyInfo();
            _rectifyReplyInfo.setRectifyLogID(m_notifyView.getLogID()+"");  //整改日志id
            _rectifyReplyInfo.setInspectUnit(m_notifyView.getCheckUnit()+""); //检查单位
            _rectifyReplyInfo.setBeCheckedUnit(m_notifyView.getBeCheckUnit()); //受检单位
//        String s = m_notifyView.getReformDate();
//        LogUtils.i("checkTime" ,s);
            _rectifyReplyInfo.setCheckedTime(m_notifyView.getReformDate()+""); //整改日期
            _rectifyReplyInfo.setRectifyManSign(m_notifyView.getReformMan()+""); //整改人
            _rectifyReplyInfo.setRectifySituation(m_notifyView.getReformCon()+""); //整改情况
            _rectifyReplyInfo.setReviewSituation(m_notifyView.getReCheckCon()+""); //复核情况
            _rectifyReplyInfo.setReviewerSign(m_notifyView.getReCheckMan()+""); //复核人签名
//        _rectifyReplyInfo.setReceiverMans(m_notifyView.getReceiveMans()); //接收人
            _rectifyReplyInfo.setLogState(m_notifyView.getLogState()); //日志状态
            _rectifyReplyInfo.setImages(m_notifyView.getPicture()+""); //获取照片名字
            _rectifyReplyInfo.setDbID(m_notifyView.getDbID()); //获取数据库ID
            _rectifyReplyInfo.setSubmitTime(m_notifyView.getcheckDate()+""); //提交时间
            _rectifyReplyInfo.setSupervisisonMans(m_notifyView.getSupervisor()+""); //监理人员
            _rectifyReplyInfo.setOwnerMans(m_notifyView.getOwner()+""); //业主人员
            _rectifyReplyInfo.setNotifyType(UserSingleton.getUserInfo().getRoleId());
            _rectifyReplyInfo.setImageInfos(m_notifyView.getImgInfo());
//            LogUtils.i("新建回复单=",m_notifyView.getImgInfo());

//            LogUtils.i("提交监理", m_notifyView.getSupervisor());
//            LogUtils.i("提交业主", m_notifyView.getOwner());
            return _rectifyReplyInfo;

        }catch (Exception e){

        }
        return null;
    }
}
