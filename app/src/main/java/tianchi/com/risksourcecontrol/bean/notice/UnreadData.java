package tianchi.com.risksourcecontrol.bean.notice;

/**
 * Created by hairun.tian on 2018/3/22 0022.
 * 未读小时
 */

public class UnreadData {
    private String title; //通知标题
    private String dataTime; //发送日期
    private String receiveMan; //通知接收人
    private String noticeContent; //通知内容
    private String sendName; //发送人昵称
    private String fileType; //通知接收文件类型
    private String fileUrl; //文件路径


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getReceiveMan() {
        return receiveMan;
    }

    public void setReceiveMan(String receiveMan) {
        this.receiveMan = receiveMan;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent
                = noticeContent;
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
