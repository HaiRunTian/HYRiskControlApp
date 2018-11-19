package tianchi.com.risksourcecontrol2.bean.risksource;


import java.util.List;

/**
 * Created by hairun.tian on 2018/1/18 0018.
 */

public class HighLowTypRisk {
    public int status;
    public String msg;
    public List<HighLowTypeRiskData> data;

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

    public void setData(List<HighLowTypeRiskData> data) {
        this.data = data;
    }
    public List<HighLowTypeRiskData> getData() {
        return data;
    }

}