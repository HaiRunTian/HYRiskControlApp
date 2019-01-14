package tianchi.com.risksourcecontrol2.bean.newnotice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hairun.tian on 2018/6/13 0013.
 * 整改通知发送类
 */

public class RectifyNotifyInfo implements Serializable {

    public static final long serialVersionUID = -7620435178023928251L;
    public String beCheckedUnit; //受检单位ed
    public String beCheckedUnitSign; // 受检单位签名
    public String checkedTime;  //检查时间
    public int id; //id
    public String images; //照片名字
    private List<String> infos; //
    public String inspectContent; //检查内容
    public String inspectUnit; //检查单位
    public String inspectorSign;  //检查人签名
    public String inspectorSigns;  //副检查人
    public String logId;  //日志编号
    public int logState;  //日志状态
    public String question; //问题
    public String receiverMans; //接收人
    public String rectifyPeriod;  //整改期限
    public String request; //整改措施与方法
    public String section; //标段
    public String imageInfos;//照片备注
    public int isReply;  //是否回复
    public String submitTime; //提交时间



    public RectifyNotifyInfo() {
    }

    public String getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(String imageInfos) {
        this.imageInfos = imageInfos;
    }

    public String getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(String checkedTime) {
        this.checkedTime = checkedTime;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public int isReply() {
        return isReply;
    }

    public void setReply(int reply) {
        isReply = reply;
    }

    public String getBeCheckedUnit() {
        return beCheckedUnit;
    }

    public void setBeCheckedUnit(String beCheckedUnit) {
        this.beCheckedUnit = beCheckedUnit;
    }

    public String getBeCheckedUnitSign() {
        return beCheckedUnitSign;
    }

    public void setBeCheckedUnitSign(String beCheckedUnitSign) {
        this.beCheckedUnitSign = beCheckedUnitSign;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

//    public List<String> getInfos() {
//        return infos;
//    }
//
//    public void setInfos(List<String> infos) {
//        this.infos = infos;
//    }

    public String getInspectContent() {
        return inspectContent;
    }

    public void setInspectContent(String inspectContent) {
        this.inspectContent = inspectContent;
    }

    public String getInspectUnit() {
        return inspectUnit;
    }

    public void setInspectUnit(String inspectUnit) {
        this.inspectUnit = inspectUnit;
    }

    public String getInspectorSigns() {
        return inspectorSigns;
    }

    public void setInspectorSigns(String inspectorSigns) {
        this.inspectorSigns = inspectorSigns;
    }


    public String getInspectorSign() {
        return inspectorSign;
    }

    public void setInspectorSign(String inspectorSign) {
        this.inspectorSign = inspectorSign;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public int getLogState() {
        return logState;
    }

    public void setLogState(int logState) {
        this.logState = logState;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReceiverMans() {
        return receiverMans;
    }

    public void setReceiverMans(String receiverMans) {
        this.receiverMans = receiverMans;
    }

    public String getRectifyPeriod() {
        return rectifyPeriod;
    }

    public void setRectifyPeriod(String rectifyPeriod) {
        this.rectifyPeriod = rectifyPeriod;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

}
