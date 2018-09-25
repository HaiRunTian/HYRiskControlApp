package tianchi.com.risksourcecontrol.bean.newnotice;

import java.util.List;

/**
 * Created by HaiRun on 2018-09-10 9:30.
 */

public class MsgSetReplyNotifyType {
    public List<String> pids;
    public List<String> realNames; //需要设置为已读的整改回复单的名字的集合，与名字一一匹配
    public int approveType; //1.监理  2.业主

    public MsgSetReplyNotifyType() {
    }

    public List<String> getPids() {
        return pids;
    }

    public void setPids(List<String> pids) {
        this.pids = pids;
    }

    public List<String> getRealNames() {
        return realNames;
    }

    public void setRealNames(List<String> realNames) {
        this.realNames = realNames;
    }

    public int getApproveType() {
        return approveType;
    }

    public void setApproveType(int approveType) {
        this.approveType = approveType;
    }
}
