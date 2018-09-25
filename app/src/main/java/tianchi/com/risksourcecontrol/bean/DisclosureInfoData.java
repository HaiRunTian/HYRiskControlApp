package tianchi.com.risksourcecontrol.bean;

import com.mysql.fabric.xmlrpc.base.Data;

import java.util.Date;

/**
 * 安全交底
 * Created by Kevin on 2017/12/20.
 */

public class DisclosureInfoData {
    private String author;
    private String titleName;
    private String fileUrl;
    private int id;
    private String textContent;
    private String saveTime;
    private String section;
    private String fileType;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return titleName;
    }

    public void setFileName(String fileName) {
        this.titleName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return textContent;
    }

    public void setRemark(String remark) {
        this.textContent = remark;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getType() {
        return fileType;
    }

    public void setType(String type) {
        this.fileType = type;
    }
}
