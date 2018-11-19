package tianchi.com.risksourcecontrol2.bean.newnotice;

/**
 * Created by HaiRun on 2018-09-11 17:18.
 * 数据刷新显示信息数量条数
 */

public class DataState {
    public int status;
    public String msg;
    public int draft;
    public int recify;
    public int UnChecked;
    public int checked;
    public int unReadMsg;
    public DataState() {
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
