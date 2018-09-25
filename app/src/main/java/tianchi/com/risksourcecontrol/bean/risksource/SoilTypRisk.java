package tianchi.com.risksourcecontrol.bean.risksource;

import java.util.List;

/**
 * Created by hairun.tian on 2018/1/19 0019.
 * 高液限土
 */

public class SoilTypRisk {
    private int status;
    private String msg;
    private List<SoilTypeRiskData> data;

    public List<SoilTypeRiskData> getData() {
        return data;
    }

    public void setData(List<SoilTypeRiskData> data) {
        this.data = data;
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
}
