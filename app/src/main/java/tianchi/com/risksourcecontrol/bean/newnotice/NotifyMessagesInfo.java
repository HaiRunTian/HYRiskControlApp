package tianchi.com.risksourcecontrol.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-15 14:38.
 * 未读消息
 */

public class NotifyMessagesInfo {
    public int id; //通知单消息ID 主键
    public String receiverName; //接收者名字
    public int notifyID; //整改通知单ID
    public int replyNotifyID; //整改回复单ID
    public boolean isReaded; //是否已阅读（0未读，1已阅读）
    public String submitTime; //提交时间
    public String remark; //备注

    public NotifyMessagesInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(int notifyID) {
        this.notifyID = notifyID;
    }

    public int getReplyNotifyID() {
        return replyNotifyID;
    }

    public void setReplyNotifyID(int replyNotifyID) {
        this.replyNotifyID = replyNotifyID;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        isReaded = readed;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
