package tianchi.com.risksourcecontrol2.bean;

import java.util.List;

/**
 * Created by hairun.tian on 2018/2/24 0024.
 */

public class DisclosureInfo {

    private List<DisclosureInfoData> m_dataList;
    private int status;
    private String msg;

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

    public List<DisclosureInfoData> getDataList() {
        return m_dataList;
    }

    public void setDataList(List<DisclosureInfoData> dataList) {
        m_dataList = dataList;
    }
}