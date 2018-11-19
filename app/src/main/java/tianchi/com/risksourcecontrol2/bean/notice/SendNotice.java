package tianchi.com.risksourcecontrol2.bean.notice;

import java.io.Serializable;

/**
 * Created by hairun.tian on 2018/3/21 0021.
 * 发送的通知
 */

public class SendNotice  implements Serializable{
    private static final long serialVersionUID = -76205144523928252L;
    private String title; //通知标题
    private String dataTime; //发送日期
    private String receiveMans; //通知接收人
    private String noticeContent; //通知内容
    private String sendName; //发送人昵称
    private String fileType; //通知接收文件类型
    private String fileUrl; //文件路径

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitleName() {
        return title;
    }

    public void setTitleName(String titleName) {
        this.title = titleName;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getReceiveMans() {
        return receiveMans;
    }

    public void setReceiveMans(String receiveMans) {
        this.receiveMans = receiveMans;
    }

    public String getTextContent() {
        return noticeContent;
    }

    public void setTextContent(String textContent) {
        this.noticeContent = textContent;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
