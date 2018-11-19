package tianchi.com.risksourcecontrol2.bean.notice;

/**
 * Created by hairun.tian on 2018/3/25 0025.
 */

public class CheckFile {
    private String fileName;
    private boolean check;


    public CheckFile(String fileName, boolean check) {
        this.fileName = fileName;
        this.check = check;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
