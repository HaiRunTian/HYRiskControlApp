package tianchi.com.risksourcecontrol2.bean.risksource;

import java.util.List;

/**
 * Created by hairun.tian on 2018/1/20 0020.
 * 隧道
 */

public class TunnelRisk {
    public int status;
    public String msg;
    public List<TunnelRiskData> data;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(List<TunnelRiskData> data) {
        this.data = data;
    }
    public List<TunnelRiskData> getData() {
        return data;
    }

}
