package tianchi.com.risksourcecontrol2.bean.log;

import java.util.Date;

/**
 * Created by Kevin on 2018/2/1.
 */

public class LogListTitleItems extends BaseLogInfo{
    public int id;//id
    public String logId;//日志编号
    public Date date;//保存时间
    public String recorder;//记录员

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }
}
