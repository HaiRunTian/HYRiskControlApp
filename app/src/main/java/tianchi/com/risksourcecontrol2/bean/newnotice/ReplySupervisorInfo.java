package tianchi.com.risksourcecontrol2.bean.newnotice;

import java.io.Serializable;

/**
 * Created by HaiRun on 2018-09-17 19:41.
 * 回复单接收者类
 * 业主和监理接收类一样
 */

public class ReplySupervisorInfo implements Serializable{
    public static final long serialVersionUID = -7620435178099928251L;
    public int id; //整改通知单ID
    public boolean hasReaded; //通知单编号
    public int hasVerify; //检查单位
    public String receiver; //首检单位
    public String result;//审批内容
    public String remark; //批注内容
    public String submitTime; //提交时间
    public String info; //未知


    public ReplySupervisorInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHasReaded() {
        return hasReaded;
    }

    public void setHasReaded(boolean hasReaded) {
        this.hasReaded = hasReaded;
    }

    public int getHasVerify() {
        return hasVerify;
    }

    public void setHasVerify(int hasVerify) {
        this.hasVerify = hasVerify;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }
}
