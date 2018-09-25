package tianchi.com.risksourcecontrol.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-09 14:53.
 */

public class MsgForSupervisor {
    public int id; //唯一标识符
    public String pid;//改通知单是否已读
    public String  remark;//备注信息
    public String  receiver;//接收者信息 限定为监理
    public boolean  hasReaded;//是否已读
    public boolean  hasVerify;//是否审批
    public boolean  update;//是否更新

    public MsgForSupervisor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isHasReaded() {
        return hasReaded;
    }

    public void setHasReaded(boolean hasReaded) {
        this.hasReaded = hasReaded;
    }

    public boolean isHasVerify() {
        return hasVerify;
    }

    public void setHasVerify(boolean hasVerify) {
        this.hasVerify = hasVerify;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
