package tianchi.com.risksourcecontrol2.bean.notice;

import java.util.List;

/**
 * Created by hairun.tian on 2018/3/22 0022.
 * 通知
 */

public class Notice {

    private int  status;
    private String msg;
    private List<ReadData> m_readDataList;
    private List<UnreadData> m_unreadDatas;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ReadData> getReadDataList() {
        return m_readDataList;
    }

    public void setReadDataList(List<ReadData> readDataList) {
        m_readDataList = readDataList;
    }

    public List<UnreadData> getUnreadDatas() {
        return m_unreadDatas;
    }

    public void setUnreadDatas(List<UnreadData> unreadDatas) {
        m_unreadDatas = unreadDatas;
    }
}
