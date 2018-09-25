package tianchi.com.risksourcecontrol.bean.notice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hairun.tian on 2018/3/22 0022.
 *
 */

public class  DataObj implements Serializable {
    private static final long serialVersionUID = -76205578023928252L;
    private int  status;
    private String msg;
    private List<SendNotice> m_readDataList;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public List<SendNotice> getReadDataList() {
        return m_readDataList;
    }

    public void setReadDataList(List<SendNotice> readDataList) {
        m_readDataList = readDataList;
    }
}
