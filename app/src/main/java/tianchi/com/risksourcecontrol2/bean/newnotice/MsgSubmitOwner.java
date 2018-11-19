package tianchi.com.risksourcecontrol2.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-10 9:26.
 */

public class MsgSubmitOwner {
    public int replyNotifyid; //整改回复单的ID
    public String realName; //提交审核人的真实名字
    public int result; //监理审核通过、监理驳回
    public String remark; //审核者
    public String OwnerMans; //业主名单集合


    public MsgSubmitOwner() {
    }

    public int getReplyNotifyid() {
        return replyNotifyid;
    }

    public void setReplyNotifyid(int replyNotifyid) {
        this.replyNotifyid = replyNotifyid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerMans() {
        return OwnerMans;
    }

    public void setOwnerMans(String ownerMans) {
        OwnerMans = ownerMans;
    }

}
