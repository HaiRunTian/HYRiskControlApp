package tianchi.com.risksourcecontrol.bean;

import java.util.Date;

/**
 *
 * 通知
 * Created by Kevin on 2017/12/22.
 */

public class Notification {
    int id;//id
    String notificationID;//通知编号
    String sendAccount;//发送账号
    String recvAccount;//接收账号
    String title;//标题
    String type;//类型
    Date pushTime;//推送时间
    String content;//通知内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getRecvAccount() {
        return recvAccount;
    }

    public void setRecvAccount(String recvAccount) {
        this.recvAccount = recvAccount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
