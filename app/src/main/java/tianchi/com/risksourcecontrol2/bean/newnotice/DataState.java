package tianchi.com.risksourcecontrol2.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-11 17:18.
 * 数据刷新显示信息数量条数
 */

public class DataState {
    public int status;
    public String msg;
    public int draft; //草稿
    public int recify; // 个人收到
    public int UnChecked; //未审核
    public int checked; //已审核
    public int unReadMsg; //未读消息
    public int rectify1; //个人下达
    public int Replys; //个人回复




    public DataState() {
    }

    public int getRecify1() {
        return rectify1;
    }

    public void setRecify1(int recify1) {
        this.rectify1 = recify1;
    }

    public int getReplys() {
        return Replys;
    }

    public void setReplys(int replys) {
        Replys = replys;
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

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    public int getRecify() {
        return recify;
    }

    public void setRecify(int recify) {
        this.recify = recify;
    }

    public int getUnChecked() {
        return UnChecked;
    }

    public void setUnChecked(int unChecked) {
        this.UnChecked  = unChecked;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getUnReadMsg() {
        return unReadMsg;
    }

    public void setUnReadMsg(int unReadMsg) {
        this.unReadMsg = unReadMsg;
    }
}
