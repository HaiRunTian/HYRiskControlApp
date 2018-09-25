package tianchi.com.risksourcecontrol.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-13 11:31.
 * 提交审批类
 */

public class VerityfReplyNotify {
    public int rectifyReplyId;  //    整改回复单的ID
    public String sendNames; // 业主审批名单（监理审核人（主审核人在第一位））（如果当前是监理审核且审核通过的话，则需要传递这个参数，告诉服务器下一步业主审核的名单）
    public int result;   //3:监理审核通过；4：监理驳回;5:业主审核通过；6：业主驳回
    public String context;  //审批的批注
    public int roletype;  //1:_info.setReciversOwnerInfos(null);_info.setReciversOwnerInfos(null);施工员提交；2:监理审核，3:业主审核

    public VerityfReplyNotify() {
    }

    public int getRectifyReplyId() {
        return rectifyReplyId;
    }

    public void setRectifyReplyId(int rectifyReplyId) {
        this.rectifyReplyId = rectifyReplyId;
    }

    public String getSendNames() {
        return sendNames;
    }

    public void setSendNames(String sendNames) {
        this.sendNames = sendNames;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getRoletype() {
        return roletype;
    }

    public void setRoletype(int roletype) {
        this.roletype = roletype;
    }
}
