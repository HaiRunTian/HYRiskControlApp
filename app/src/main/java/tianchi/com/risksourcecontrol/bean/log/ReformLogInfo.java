package tianchi.com.risksourcecontrol.bean.log;

import java.util.Date;

/**
 * 风险源整改日志
 * Created by Kevin on 2017/12/20.
 */

public class ReformLogInfo extends BaseLogInfo {

//    public int id;//id
//    public String logID;//日志编号
//    public String stakeNum;//桩号
//    public String section;//标段
//    public Date saveTime;//保存时间
//    public String recorder;//记录员
//    private String riskType;//风险源类型
//    private byte[] picture;//照片
    public int id;//id
    public String logId;//日志编号
    public String stakeNum;//桩号
    public String section;//标段
    public Date saveTime;//保存时间
    public String recorder;//记录员
    public String riskType;//风险源类型
    //    private byte[] picture;//照片
    public String picture;//照片
    public String reformAccount;//整改账户
    public String details;//整改内容
    public String title;//标题
    public String riskId;//风险源ID
    public String loginName;//编写人
    public String chargeBuilder;//标段负责人

    public ReformLogInfo() {

    }

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

    public String getStakeNum() {
        return stakeNum;
    }

    public void setStakeNum(String stakeNum) {
        this.stakeNum = stakeNum;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getReformAccount() {
        return reformAccount;
    }

    public void setReformAccount(String reformAccount) {
        this.reformAccount = reformAccount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRiskId() {
        return riskId;
    }

    public void setRiskId(String riskId) {
        this.riskId = riskId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getChargeBuilder() {
        return chargeBuilder;
    }

    public void setChargeBuilder(String chargeBuilder) {
        this.chargeBuilder = chargeBuilder;
    }
}
