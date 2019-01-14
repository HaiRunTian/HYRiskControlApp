package tianchi.com.risksourcecontrol2.view;

import java.io.File;

/**
 * Created by hairun.tian on 2018/6/13 0013.
 */

public interface IRectifyNotifyView {

    /**
     * @return 标段
     */
    String getSection();

    /**
     * @return 日志id
     */
    String getLogID();

    /**
     * 检查单位
     *
     * @return
     */
    String getCheckUnit();

    /**
     * 受检查单位
     *
     * @return
     */
    String getBeCheckUnit();

    /**
     * 检查日期
     *
     * @return
     */
    String getcheckDate();


    /**
     * 检查内容
     *
     * @return 检查内容
     */
    String getCheckContent();

    /**
     * 检查发现的问题
     *
     * @return
     */
    String getQuestion();

    /**
     * 整改措施与方法
     *
     * @return
     */
    String getRectifyRequest();

    /**
     * 检查人
     *
     * @return
     */
    String getCheckMan();
    /**
     * 副检查人
     *
     * @returns
     */
    String getCheckMans();

    /**
     * 整改期限
     *
     * @return
     */
    String rectifyDate();

    /**
     * 照片名字
     *
     * @return
     */
    String getPicture();

    /**
     * 登录人id和名字
     *
     * @return
     */
    String getIdLoginName();

    /**
     * 获取接收人信息
     *
     * @return
     */
    String getReceiveMans();


    /**
     * 获取日志状态
     *
     * @return
     */
    int getLogState();


    /**
     * 获取上传的照片
     *
     * @return
     */
    File getUploadFile(int position);//获取上传的照片


    /**
     * 显示提交过程
     *
     * @return
     */
    void showInSubmiting(String msg);//显示提交过程

    /**
     * 隐藏提交过程
     *
     * @return
     */
    void hideInSubmiting();//隐藏提交过程

    /**
     * 提交成功
     *
     * @return
     */
    void showSubmitSucceed(String msg);//提交成功

    /**
     * 提交失败
     *
     * @return
     */
    void showSubmitFailed(String msg);//提交失败

    /**
     * 提交图片成功
     *
     * @return
     */
    void uploadFileSucceed(String msg);//提交图片成功

    /**
     * 提交图片失败
     *
     * @return
     */
    void uploadFileFailed(String msg);//提交图片失败

    void setLogId();

    /**
     * @return 获取整改日期
     */
    String getReformDate();

    /**
     * @return 获取整改人
     */
    String getReformMan();

    /**
     * @retrun 获取整改情况
     */
    String getReformCon();

    /**
     * @return 获取复核情况
     */
    String getReCheckCon();

    /**
     * @return 获取复核人签名
     */
    String getReCheckMan();

    /**
     * @return 获取数据库id
     */
    int getDbID();

    /**
     *
     * @return 获取草稿状态
     */
    int getDraftStatus();

    /**
     *
     * @return 监理员名单
     */
    String getSupervisor();

    /**
     *
     * @return 业主方名单
     */
    String getOwner();

    /**
     *
     * @return 照片备注
     */
    String getImgInfo();

}
