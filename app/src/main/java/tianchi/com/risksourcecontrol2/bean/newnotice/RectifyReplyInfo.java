package tianchi.com.risksourcecontrol2.bean.newnotice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hairun.tian on 2018/6/21 0021
 * 整改通知回复类
 */

public class RectifyReplyInfo implements Serializable {

    private static final long serialVersionUID = -7620435178023928259L;
    public int id; //整改通知单ID
    public String logId;  //回复通知单编号
    public String inspectUnit; //检查单位

    public String beCheckedUnit; //受检查单位
    public String checkedTime; //检查时间

    public String rectifySituation; //整改情况
    public String reviewSituation; //复核情况
    public String rectifyManSign; //整改人签名
    public String reviewerSign; //复核人签名
    public String images;  //现场照片
    public int logState;  //日志状态
    public String receiverMans;  //整改单接收人 集合
    public String rectifyLogID;  //整改通知单logID
    public String SubmitTime;  //提交时间
    public int dbID;
    public String imageInfos;//照片备注
    public int isRead; //是否阅读

    public String supervisorMans;
    public String ownerMans;

    public int notifyType;
    public List<ReplySupervisorInfo> supervisionInfos;
    public List<ReplySupervisorInfo> reciversOwnerInfos;


    public List<ReplySupervisorInfo> getSupervisionInfos() {
        return supervisionInfos;
    }

    public void setSupervisionInfos(List<ReplySupervisorInfo> supervisionInfos) {
        this.supervisionInfos = supervisionInfos;
    }

    public List<ReplySupervisorInfo> getReciversOwnerInfos() {
        return reciversOwnerInfos;
    }

    public void setReciversOwnerInfos(List<ReplySupervisorInfo> reciversOwnerInfos) {
        this.reciversOwnerInfos = reciversOwnerInfos;
    }

    public String getSupervisorMans() {
        return supervisorMans;
    }

    public void setSupervisorMans(String supervisorMans) {
        this.supervisorMans = supervisorMans;
    }

    public String getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(String imageInfos) {
        this.imageInfos = imageInfos;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSubmitTime() {
        return SubmitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.SubmitTime = submitTime;
    }

    public RectifyReplyInfo() {
    }


    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getSupervisisonMans() {
        return supervisorMans;
    }

    public void setSupervisisonMans(String supervisisonMans) {
        this.supervisorMans = supervisisonMans;
    }

    public String getOwnerMans() {
        return ownerMans;
    }

    public void setOwnerMans(String ownerMans) {
        this.ownerMans = ownerMans;
    }

    public int isRead() {
        return isRead;
    }

    public void setRead(int read) {
        isRead = read;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCheckTime() {
        return checkedTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkedTime = checkTime;
    }

    public String getReviewSituation() {
        return reviewSituation;
    }

    public void setReviewSituation(String reviewSituation) {
        this.reviewSituation = reviewSituation;
    }


    public String getInspectUnit() {
        return inspectUnit;
    }

    public void setInspectUnit(String inspectUnit) {
        this.inspectUnit = inspectUnit;
    }

    public String getBeCheckedUnit() {
        return beCheckedUnit;
    }

    public void setBeCheckedUnit(String beCheckedUnit) {
        this.beCheckedUnit = beCheckedUnit;
    }

    public String getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(String checkedTime) {
        this.checkedTime = checkedTime;
    }

    public String getRectifySituation() {
        return rectifySituation;
    }

    public void setRectifySituation(String rectifySituation) {
        this.rectifySituation = rectifySituation;
    }


    public String getRectifyManSign() {
        return rectifyManSign;
    }

    public void setRectifyManSign(String rectifyManSign) {
        this.rectifyManSign = rectifyManSign;
    }

    public String getReviewerSign() {
        return reviewerSign;
    }

    public void setReviewerSign(String reviewerSign) {
        this.reviewerSign = reviewerSign;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getLogState() {
        return logState;
    }

    public void setLogState(int logState) {
        this.logState = logState;
    }

    public String getReceiverMans() {
        return receiverMans;
    }

    public void setReceiverMans(String receiverMans) {
        this.receiverMans = receiverMans;
    }

    public String getRectifyLogID() {
        return rectifyLogID;
    }

    public void setRectifyLogID(String rectifyLogID) {
        this.rectifyLogID = rectifyLogID;
    }
//
//    public Date getSubmitTime() {
//        return submitTime;
//    }
//
//    public void setSubmitTime(Date submitTime) {
//        this.submitTime = submitTime;
//    }
}
