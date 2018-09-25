package tianchi.com.risksourcecontrol.bean.newnotice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HaiRun on 2018-09-11 15:49.
 * 草稿
 */

public class RectifyNotifyDraftInfo implements Serializable {

    public static final long serialVersionUID = -7620435178023928291L;
    public int id; //id
    public String title;//通知单编号
    public String inspectUnit; //检查单位
    public String beCheckedUnit; //受检单位
    public String checkedTime;  //检查时间
    public String inspectContent; //检查内容
    public String question; //问题
    public String request; //整改措施与方法
    public String inspectorSign;  //检查人签名
    public String rectifyPeriod;  //整改期限
    public String beCheckedUnitSign; // 受检单位签名
    public String images; //照片名字
    public int logState;  //日志状态
    public String section; //标段
    public String receiverMans; //接收人
    public int status; //草稿箱状态


    public RectifyNotifyDraftInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCheckTime() {
        return checkedTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkedTime = checkTime;
    }

    public String getInspectContent() {
        return inspectContent;
    }

    public void setInspectContent(String inspectContent) {
        this.inspectContent = inspectContent;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getInspectorSign() {
        return inspectorSign;
    }

    public void setInspectorSign(String inspectorSign) {
        this.inspectorSign = inspectorSign;
    }

    public String getRectifyPeriod() {
        return rectifyPeriod;
    }

    public void setRectifyPeriod(String rectifyPeriod) {
        this.rectifyPeriod = rectifyPeriod;
    }

    public String getBeCheckedUnitSign() {
        return beCheckedUnitSign;
    }

    public void setBeCheckedUnitSign(String beCheckedUnitSign) {
        this.beCheckedUnitSign = beCheckedUnitSign;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getReceiverMans() {
        return receiverMans;
    }

    public void setReceiverMans(String receiverMans) {
        this.receiverMans = receiverMans;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
