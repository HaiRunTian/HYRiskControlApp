package tianchi.com.risksourcecontrol.bean.risksource;

import java.util.List;

/**
 * Created by hairun.tian on 2018/1/19 0019.
 */

public class BridgeRisk {
    private int status;
    private String msg;
    private List<BridgeRiskData> data;

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
    public List<BridgeRiskData> getData() {

        return data;
    }
    public void setData(List<BridgeRiskData> data) {
        this.data = data;
    }
}
